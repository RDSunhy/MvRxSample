package com.shy.mvrxsample

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.airbnb.mvrx.BaseMvRxFragment
import kotlinx.android.synthetic.main.fragment_main.*

class MainFragment : BaseMvRxFragment(){


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_main,container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bnSample.setOnClickListener { findNavController().navigate(R.id.action_mainFragment_to_sampleFragment) }
        bnTransmitData.setOnClickListener { findNavController().navigate(R.id.action_mainFragment_to_firstFragment) }
        bnSubscribe.setOnClickListener { findNavController().navigate(R.id.action_mainFragment_to_subscribeFragment) }
    }

    override fun invalidate() {

    }

}