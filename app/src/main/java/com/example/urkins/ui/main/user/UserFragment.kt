package com.example.urkins.ui.main.user

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import com.example.urkins.data.pref.UserPreference2
import com.example.urkins.data.pref.dataStore
import com.example.urkins.data.repository.UserRepository
import com.example.urkins.databinding.FragmentUserBinding
import com.example.urkins.ui.activity.login.LoginActivity
import com.example.urkins.ui.activity.register.RegisterActivity
import com.example.urkins.ui.activity.setting.SettingActivity

class UserFragment : Fragment() {

    private var _binding: FragmentUserBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private lateinit var userViewModel: UserViewModel
    private lateinit var userPreference: UserPreference2

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // val userViewModel =
        //     ViewModelProvider(this).get(UserViewModel::class.java)

        // _binding = FragmentUserBinding.inflate(inflater, container, false)
        // val root: View = binding.root

        userPreference = UserPreference2.getInstance(requireContext().dataStore)
        val userRepository = UserRepository.getInstance(userPreference)
        val factory = UserViewModelFactory(userRepository)
        userViewModel = ViewModelProvider(this, factory).get(UserViewModel::class.java)

//        val textView: TextView = binding.textNotifications
//        userViewModel.text.observe(viewLifecycleOwner) {
//            textView.text = it
//        }
        _binding = FragmentUserBinding.inflate(inflater, container, false)
        return binding.root
        // return root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeUserSession()


    // override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    //     super.onViewCreated(view, savedInstanceState)
         setupAction()



//        val analyzeResultAdapter = ResultAdapter { analyzeResult ->
//            historyViewModel.removeAnalyzeResult(analyzeResult)
//        }
//
//        binding.rvHistory.apply {
//            layoutManager = GridLayoutManager(requireActivity(), 2)
//            adapter = analyzeResultAdapter
//            addItemDecoration(
//                DividerItemDecoration(
//                    requireActivity(),
//                    GridLayoutManager(requireActivity(), 2).orientation
//                )
//            )
//            setPadding(0, 0, 0, 200)
//            clipToPadding = false
//        }
    }

    private fun observeUserSession() {
        userViewModel.getSession().observe(viewLifecycleOwner) { user ->
            if (!user.isLogin) {
                binding.clUserPage.visibility = View.GONE
                binding.clGuestUserPage.visibility = View.VISIBLE
            } else {
                binding.clUserPage.visibility = View.VISIBLE
                binding.clGuestUserPage.visibility = View.GONE
            }
        }
    }

    private fun setupAction() {
        binding.btnGoingToSetting.setOnClickListener {
            val intentMaps = Intent(requireActivity(), SettingActivity::class.java)
            startActivity(intentMaps)
        }
        binding.btnRegisterWelcomeUser.setOnClickListener {
            val intent = Intent(requireActivity(), RegisterActivity::class.java)
            startActivity(intent)
        }
        binding.btnLogInWelcomeUser.setOnClickListener {
            val intent = Intent(requireActivity(), LoginActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}