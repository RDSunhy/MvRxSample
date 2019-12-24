package com.shy.mvrxsample.mvrx_transmit_data

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.airbnb.mvrx.MvRxState
import com.airbnb.mvrx.MvRxViewModelFactory
import com.airbnb.mvrx.ViewModelContext
import com.shy.mvrxsample.R
import com.shy.mvrxsample.base.BaseFragment
import com.shy.mvrxsample.base.BaseViewModel
import com.shy.mvrxsample.http.ApiService
import com.shy.mvrxsample.http.HttpUtils
import kotlinx.android.synthetic.main.fragment_first.*

data class FirstState(
    val name: String = "--"
) : MvRxState

class FirstViewModel(firstState: FirstState, private val apiService: ApiService) :
    BaseViewModel<FirstState>(firstState) {

    init {
        logStateChanges()
    }

    /**
     * If you implement MvRxViewModelFactory in your companion object, MvRx will use that to create
     * your ViewModel. You can use this to achieve constructor dependency injection with MvRx.
     *
     * @see MvRxViewModelFactory
     */
    companion object : MvRxViewModelFactory<FirstViewModel, FirstState> {

        override fun create(
            viewModelContext: ViewModelContext,
            state: FirstState
        ): FirstViewModel {
            val service: ApiService by lazy {
                HttpUtils.retrofit.create(ApiService::class.java)
            }
            return FirstViewModel(state, service)
        }
    }
}

class FirstFragment : BaseFragment(){

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_first,container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bnIntent.setOnClickListener{
            if(etName.text.toString().isNotEmpty()
                ||etAge.text.toString().isNotEmpty()
                ||etSex.text.toString().isNotEmpty()){
                var person = Person(
                    etName.text.toString(),
                    etAge.text.toString(),
                    etSex.text.toString()
                )
                //传递数据
                navigateTo(R.id.action_firstFragment_to_secondFragment,person)
            }else{
                Toast.makeText(context,"请输入要传递的数据",Toast.LENGTH_SHORT)
            }

        }
    }

    override fun invalidate() {

    }

}