package com.example.urkins.ui.main.user

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import com.example.urkins.data.adapter.HistoryAdapter
import com.example.urkins.data.local.entity.HistoryEntity
import com.example.urkins.data.pref.UserPreference2
import com.example.urkins.data.pref.dataStore
import com.example.urkins.data.repository.UserRepository
import com.example.urkins.databinding.FragmentUserBinding
import com.example.urkins.ui.activity.login.LoginActivity
import com.example.urkins.ui.activity.register.RegisterActivity
import com.example.urkins.ui.activity.setting.SettingActivity

class UserFragment : Fragment() {

    private var _binding: FragmentUserBinding? = null
    private val binding get() = _binding!!
    private lateinit var userViewModel: UserViewModel
    private lateinit var userPreference: UserPreference2

    private val REQUEST_CODE_PICK_IMAGE = 100
    private val REQUEST_CODE_PERMISSIONS = 101

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        userPreference = UserPreference2.getInstance(requireContext().dataStore)
        val userRepository = UserRepository.getInstance(userPreference)
        val factory = UserViewModelFactory(userRepository)
        userViewModel = ViewModelProvider(this, factory).get(UserViewModel::class.java)
        _binding = FragmentUserBinding.inflate(inflater, container, false)
        return binding.root
        // return root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        savedInstanceState?.let {
            val imageUriString = it.getString("image_uri")
            imageUriString?.let { uriString ->
                val uri = Uri.parse(uriString)
                binding.civProfileImage.setImageURI(uri)
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        val imageUri = binding.civProfileImage.tag as? Uri
        imageUri?.let {
            outState.putString("image_uri", it.toString())
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val factory: UserViewModelFactory2 =
            UserViewModelFactory2.getInstance(requireActivity())
        val historyViewModel: UserViewModel2 by viewModels {
            factory
        }

        val adapterHistory = HistoryAdapter{ analizeResult ->
            historyViewModel.deleteHistory(analizeResult)
        }

        binding.tvUserName.text="Hi,\nKinnies!"

        historyViewModel.getHistory().observe(viewLifecycleOwner) { history ->
            binding.progressBar.visibility = View.GONE
            val detail = arrayListOf<HistoryEntity>()
            history.map {
                val detailItem = HistoryEntity(
                    id = it.id,
                    imageUri = it.imageUri,
                    prediction = it.prediction,
                    prediction2 = it.prediction2
                )
                detail.add(detailItem)
            }
            adapterHistory.submitList(detail)
        }

        binding.rvHistory.apply {
            layoutManager = GridLayoutManager(requireContext(), 2)
            adapter = adapterHistory
        }
        observeUserSession()
        setupAction()
        loadImageFromPreferences()
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
            val intent = Intent(requireActivity(), SettingActivity::class.java)
            startActivityForResult(intent, REQUEST_CODE_PICK_IMAGE)
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_PICK_IMAGE && resultCode == Activity.RESULT_OK) {
            val uriString = data?.getStringExtra("image_uri")
            uriString?.let {
                val uri = Uri.parse(it)
                binding.civProfileImage.setImageURI(uri)
                saveImageToPreferences(uri) // Simpan URI ke SharedPreferences jika perlu
                Log.d("UserFragment", "Received URI: $uri")
            }
        }
    }

    private fun loadImageFromPreferences() {
        val sharedPreferences = requireActivity().getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
        val imageUriString = sharedPreferences.getString("profile_image_uri", null)
        imageUriString?.let {
            val uri = Uri.parse(it)
            try {
                requireContext().contentResolver.openInputStream(uri)?.use { stream ->
                    binding.civProfileImage.setImageURI(uri)
                    binding.civProfileImage.tag = uri // Menyimpan URI ke tag untuk digunakan saat menyimpan instance state
                }
            } catch (e: Exception) {
                Log.e("UserFragment", "Error loading image: ${e.message}")
            }
        }
    }

    private fun saveImageToPreferences(uri: Uri) {
        val sharedPreferences = requireActivity().getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
        with(sharedPreferences.edit()) {
            putString("profile_image_uri", uri.toString())
            apply()
        }
    }


    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            } else { }
        }
    }
}