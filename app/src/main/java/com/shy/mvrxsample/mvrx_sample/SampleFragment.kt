package com.shy.mvrxsample.mvrx_sample

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.airbnb.mvrx.*
import com.shy.mvrxsample.R
import com.shy.mvrxsample.http.Api
import kotlinx.android.synthetic.main.fragment_sample.*

/**
 *  state 可以理解为 包含所有当前页面需要的数据的一个 data class
 *  state 中的 属性 必须赋予初始值
 *
 *  Async<T> 是MvRx的封装 将网络请求的 Observable<T> 映射到 State 中的 Async<T>上 使网络请求处理变得很简单
 *          分别有：Uninitialized（未初始化）
 *                  Loading （请求中）
 *                  Success （请求成功）
 *                  Fail    （请求失败）
 */
data class SampleState(
    val name: String = "--",
    val age: Int = 0,
    val articleData: Async<ArticleData> = Uninitialized
) : MvRxState


class SampleViewModel(initialState: SampleState) : BaseMvRxViewModel<SampleState>(initialState, debugMode = true) {

    /**
     *  init 方法 在ViewModel 初始化的时候会调用
     */
    init {
        /**
         *  logStateChanges 打印 State 变化日志 debugMode = true 时打印
         */
        logStateChanges()
    }

    /**
     * 改变state的方法必须在viewModel
     *  withState{}
     */
    fun changeName(newName: String){
        withState { state ->
            /**
             * 可以通过 withState 拿到 当前页面State 中的所有属性
             * 如：state.age
             */
            setState { copy(name = newName) }
        }
    }

    fun changeAge(newAge: Int){
        withState {
            setState { copy(age = newAge) }
        }
    }

    /**
     *  网络请求 retrofit
     *
     *  execute 是对 rxjava 的封装 直接返回一个Async<T>对象 里面包含了 请求状态 和 请求的数据
     */
    fun getArticleData(){
        withState {
            /**
             *  Loading 表示 正在请求中 防止重复请求
             */
            if(it.articleData is Loading) return@withState
            Api.api.getArticleList()
                .execute { data ->
                    copy(articleData = data)
                }
        }
    }
}

class SampleFragment : BaseMvRxFragment(){

    /**
     *  获取当前页面的 viewModel 通过viewModel中的方法实现数据交互
     *  fragment中 只负责数据如何呈现 无需管理任何网络请求逻辑
     */

    val sampleViewModel by fragmentViewModel(SampleViewModel::class)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_sample,container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bnChangeName.setOnClickListener { sampleViewModel.changeName("Sunhy") }
        bnChangeAge.setOnClickListener { sampleViewModel.changeAge(21) }
        bnRequest.setOnClickListener { sampleViewModel.getArticleData() }
    }

    /**
     *  invalidate 当 监听的 viewModel 对应的 State 中的属性发生改变时 自动调用
     *
     *  在我个人理解 这个相当于 自定义view中的 重绘方法 不过这个是自动在调用
     *  当你的State发生改变时 就会触发invalidate() 执行里面的逻辑
     *  这一点 和 React-Native 中的 state 是一模一样的 都是一个思想 当数据改变时 UI视图也随之改变
     *
     */

    override fun invalidate() {
        withState(sampleViewModel){state ->
            /**
             * 切记一点思想 在 fragment 中只需要关心 view 是如何呈现数据 不需要关心数据获取的逻辑
             *
             *  如： 一个TextView 需要展示姓名  姓名对应 State 中的一个属性 则 text == state.name
             */
            tvName.text = state.name
            tvAge.text = state.age.toString()

            /**
             *  网络请求 也同上 当 state 中的 Async<T> 对象 改变时 会自动触发 invalidate()
             *  去刷新页面 就相当于 invalidate() 中的代码 重新执行一遍
             */
            when(state.articleData){
                is Success -> {
                    /**
                     *  因为 Async 是在 Observable 上封装了一层 所以 需要 invoke() 之后 获取到的才是 实体类 (也就是响应数据)
                     */
                    tvData.text = state.articleData.invoke().data.toString()
                }
                is Fail -> {
                    Log.e("网络请求失败","网络请求失败")
                }
                is Loading -> {
                    Log.e("网络请求中","网络请求中")
                }
                else -> {}
            }
        }
    }

}