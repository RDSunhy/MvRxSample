package com.shy.mvrxsample.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.IdRes
import androidx.navigation.fragment.findNavController
import com.airbnb.mvrx.BaseMvRxFragment
import com.airbnb.mvrx.MvRx
import java.io.Serializable

abstract class BaseFragment : BaseMvRxFragment(){


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    protected fun navigateTo(@IdRes actionId: Int, arg: Serializable? = null) {
        /**
         * If we put a parcelable arg in [MvRx.KEY_ARG] then MvRx will attempt to call a secondary
         * constructor on any MvRxState objects and pass in this arg directly.
         * @see [com.yrickwong.tech.mvrx.feature.wechat.KnowledgeArgs]
         */
        val bundle = arg?.let { Bundle().apply { putSerializable(MvRx.KEY_ARG, it) } }
        findNavController().navigate(actionId, bundle)
    }

}