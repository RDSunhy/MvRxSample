package com.shy.mvrxsample.mvrx_subscribe

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.airbnb.mvrx.*
import com.shy.mvrxsample.R
import com.shy.mvrxsample.base.BaseFragment
import com.shy.mvrxsample.base.BaseViewModel
import com.shy.mvrxsample.http.Api
import com.shy.mvrxsample.http.ApiService
import com.shy.mvrxsample.http.HttpUtils
import com.shy.mvrxsample.mvrx_sample.ArticleData
import kotlinx.android.synthetic.main.fragment_sample.*
import kotlinx.android.synthetic.main.fragment_subscribe.*
import kotlinx.android.synthetic.main.fragment_subscribe.bnChangeAge
import kotlinx.android.synthetic.main.fragment_subscribe.bnChangeName
import kotlinx.android.synthetic.main.fragment_subscribe.bnRequest
import kotlinx.android.synthetic.main.fragment_subscribe.tvAge
import kotlinx.android.synthetic.main.fragment_subscribe.tvData
import kotlinx.android.synthetic.main.fragment_subscribe.tvName

data class SubscribeState(
    val name: String = "Shy",
    val age: Int = 21,
    val articleData: Async<ArticleData> = Uninitialized
) : MvRxState

class SubscribeViewModel(state: SubscribeState, private val apiService: ApiService) :
    BaseViewModel<SubscribeState>(state) {

    init {
        logStateChanges()
    }

    fun changeName(newName: String){
        withState { state ->
            setState { copy(name = newName) }
        }
    }

    fun changeAge(newAge: Int){
        withState {
            setState { copy(age = newAge) }
        }
    }

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

    /**
     * If you implement MvRxViewModelFactory in your companion object, MvRx will use that to create
     * your ViewModel. You can use this to achieve constructor dependency injection with MvRx.
     *
     * @see MvRxViewModelFactory
     */
    companion object : MvRxViewModelFactory<SubscribeViewModel, SubscribeState> {

        override fun create(
            viewModelContext: ViewModelContext,
            state: SubscribeState
        ):  SubscribeViewModel{
            val service: ApiService by lazy {
                HttpUtils.retrofit.create(ApiService::class.java)
            }
            return SubscribeViewModel(state, service)
        }
    }
}

class SubscribeFragment : BaseFragment(){

    val subscribeViewModel by fragmentViewModel(SubscribeViewModel::class)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_subscribe,container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        /**
         * 通过两个按钮的单击事件 触发 State 的改变
         */
        bnChangeName.setOnClickListener {
            if(tvName.text == "Shy"){
                subscribeViewModel.changeName("Sunhy")
            }else{
                subscribeViewModel.changeName("Shy")
            }
        }

        bnChangeAge.setOnClickListener {
            if(tvAge.text == "21"){
                subscribeViewModel.changeAge(99)
            }else{
                subscribeViewModel.changeAge(21)
            }
        }

        bnRequest.setOnClickListener {
            subscribeViewModel.getArticleData()
        }

        /**
         *  通过两种监听 监听 State"普通"属性 和 State中Async<T>属性
         *
         *  selectSubscribe 监听"普通
         */

        subscribeViewModel.selectSubscribe(SubscribeState::name, SubscribeState::age,
            subscriber = { name, age ->
                Toast.makeText(context,"name：-> ${name}，age：-> ${age}",Toast.LENGTH_SHORT).show()
            })

        /**
         * asyncSubscribe 监听网络请求接受bean的成功或者失败
         */
        subscribeViewModel.asyncSubscribe(SubscribeState::articleData,
            onSuccess = {
                //请求成功
                Toast.makeText(context,"请求成功",Toast.LENGTH_SHORT).show()
            },
            onFail = {
                //请求失败
                Toast.makeText(context,"请求失败->${it}",Toast.LENGTH_SHORT).show()
            })

    }

    override fun invalidate() {
        withState(subscribeViewModel){
            tvName.text = it.name
            tvAge.text = it.age.toString()
            when(it.articleData){
                is Success -> {
                    /**
                     *  因为 Async 是在 Observable 上封装了一层 所以 需要 invoke() 之后 获取到的才是 实体类 (也就是响应数据)
                     */
                    tvData.text = it.articleData.invoke().data.toString()
                }
                is Fail -> {
                    tvData.text = "请求失败"
                    Log.e("网络请求失败","网络请求失败")
                }
                is Loading -> {
                    tvData.text = "请求中..."
                    Log.e("网络请求中","网络请求中")
                }
                else -> {}
            }
        }
    }

}