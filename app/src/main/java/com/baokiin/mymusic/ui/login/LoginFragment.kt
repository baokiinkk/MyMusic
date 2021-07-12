package com.baokiin.mymusic.ui.login

import android.util.Log
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.baokiin.mymusic.R
import com.baokiin.mymusic.databinding.FragmentLoginBinding
import com.baokiin.mymusic.utils.BaseFragment
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class LoginFragment : BaseFragment<FragmentLoginBinding>() {
    override fun getLayoutRes(): Int {
        return R.layout.fragment_login
    }


    //-------------------------------- Variable ----------------------------------------
    val viewModel by viewModels<LoginViewModel>()


    //-------------------------------- createView ----------------------------------------
    override fun onCreateViews() {
        setup()
        clickView()
    }

    //-------------------------------- Func ----------------------------------------
    private fun setup() {
        baseBinding.apply {
            viewmodel = viewModel
        }

        viewModel.user.observe(viewLifecycleOwner, Observer {
            it?.let {
                Log.d("quocbao",it.toString())

            }
        })

    }

    private fun clickView() {
        baseBinding.btnLogin.apply {
            setOnClickListener {
                viewModel.login()
            }
        }
    }

}