package com.baokiin.mymusic.ui.info

import android.content.Intent
import android.view.View
import androidx.fragment.app.viewModels
import com.baokiin.mymusic.R
import com.baokiin.mymusic.adapter.ViewPageAdapter
import com.baokiin.mymusic.databinding.FragmentInfoBinding
import com.baokiin.mymusic.ui.activity.LoginActivity
import com.baokiin.mymusic.ui.info.offline.OfflineFragment
import com.baokiin.mymusic.ui.info.online.OnlineFragment
import com.baokiin.mymusic.utils.BaseFragment
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class InfoFragment : BaseFragment<FragmentInfoBinding>() {
    override fun getLayoutRes(): Int {
        return R.layout.fragment_info
    }

    private val viewModel by viewModels<InfoViewModel>()
    private lateinit var googleSignInClient: GoogleSignInClient

    override fun onResume() {
        super.onResume()
        viewModel.auth.postValue(Firebase.auth)
    }
    override fun onCreateViews() {
        instanceGoogleSignIn()
        baseBinding.apply {
            viewmodel = viewModel
            adapter = ViewPageAdapter(mutableListOf(OnlineFragment(),OfflineFragment()),requireActivity())

            btnLogOut.setOnClickListener {
                viewModel.auth.value?.signOut()
                googleSignInClient.signOut()
                showView()
            }
            viewModel.auth.observe(viewLifecycleOwner,{
                it.currentUser.let {
                    if (it == null) {
                        showView()
                        btnLogin.setOnClickListener {
                            startActivity(Intent(requireContext(), LoginActivity::class.java))
                        }
                    } else
                        btnLogOut.visibility = View.VISIBLE
                }
            })
        }


    }

    private fun showView() {
        baseBinding.apply {
            image.setImageResource(R.drawable.ic_account)
            btnLogOut.visibility = View.GONE
            btnLogin.text = "Đăng Nhập"
        }
    }



    private fun instanceGoogleSignIn() {
        // Configure Google Sign In
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(requireContext(), gso)
    }

}