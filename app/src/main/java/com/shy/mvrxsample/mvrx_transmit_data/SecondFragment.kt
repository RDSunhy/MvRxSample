package com.shy.mvrxsample.mvrx_transmit_data

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.airbnb.mvrx.*
import com.shy.mvrxsample.R
import com.shy.mvrxsample.base.BaseFragment
import com.shy.mvrxsample.base.BaseViewModel
import com.shy.mvrxsample.http.ApiService
import com.shy.mvrxsample.http.HttpUtils
import kotlinx.android.synthetic.main.fragment_second.*


/**
 * 通过State的构造器也可以接受传递的数据
 * 同时，如果你的state中的属性需要初始值（例如：分页加载的页数），都可以在构造器中赋值
 */
data class SecondState(
    val name: String = "",
    val state_person: Person? = null
) : MvRxState {
    constructor(person: Person) : this(
        name = "Sunhy",
        state_person = person
    )
}

class SecondViewModel(secondState: SecondState, private val apiService: ApiService) :
    BaseViewModel<SecondState>(secondState) {

    init {
        logStateChanges()
    }

    /**
     * If you implement MvRxViewModelFactory in your companion object, MvRx will use that to create
     * your ViewModel. You can use this to achieve constructor dependency injection with MvRx.
     *
     * @see MvRxViewModelFactory
     */
    companion object : MvRxViewModelFactory<SecondViewModel, SecondState> {

        override fun create(
            viewModelContext: ViewModelContext,
            state: SecondState
        ): SecondViewModel {
            val service: ApiService by lazy {
                HttpUtils.retrofit.create(ApiService::class.java)
            }
            return SecondViewModel(state, service)
        }
    }
}

class SecondFragment : BaseFragment(){

    /**
     * 接受传递过来的数据  只需要指定类型 从 args()中取出
     *  在BaseFragment中 我们把数据存在了 MvRx.KEY_ARG 里
     *  @see [BaseFragment]
     */
    val person :Person by args()
    val secondViewModel by fragmentViewModel(SecondViewModel::class)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_second,container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun invalidate() {
        /**
         * 通过 args() 取到的 person 数据
         */
        if (person != null){
            tvName.text = person.name
            tvAge.text = person.age
            tvSex.text = person.sex
        }

        /**
         * 通过 State 构造器 取到的 state_person 数据
         */
        withState(secondViewModel){
            if(it.state_person != null){
                tvName2.text = it.state_person.name
                tvAge2.text = it.state_person.age
                tvSex2.text = it.state_person.sex
            }

            /**
             * 通过 State 构造器 赋初始值
             */
            tvName3.text = it.name
        }
    }

}