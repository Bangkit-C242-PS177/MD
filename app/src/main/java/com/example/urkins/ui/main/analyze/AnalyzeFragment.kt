package com.example.urkins.ui.main.analyze

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.example.urkins.R
import com.example.urkins.data.pref.UserPreference2
import com.example.urkins.data.pref.dataStore
import com.example.urkins.data.repository.UserRepository
import com.example.urkins.databinding.FragmentAnalyzeBinding
import com.example.urkins.ui.activity.camera.CameraActivity
import com.example.urkins.ui.activity.login.LoginActivity
import com.example.urkins.ui.activity.result.ResultActivity
import com.example.urkins.ui.activity.result.ResultActivity.Companion.EXTRA_IMAGE
import com.google.android.material.snackbar.Snackbar

class AnalyzeFragment : Fragment() {

    private var _binding: FragmentAnalyzeBinding? = null
    private val binding get() = _binding!!
    private lateinit var analyzeViewModel: AnalyzeViewModel

    var resultAnalyzer: String? = null
    var resultAnalyzer2: String? = null

    private val launchCameraActivity = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val uri = result.data?.getStringExtra("image_uri")?.let { Uri.parse(it) }
            uri?.let {
                analyzeViewModel.setSelectImageUri(it)
//                showImage()
                showImagePreview()
            }
        } else {
            Log.d(getString(R.string.camera_title), getString(R.string.failed_take_photo))
        }
    }

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            Log.d(getString(R.string.photo_picker_title), getString(R.string.acces_granted))
            showSnackBar(getString(R.string.acces_granted))
        } else {
            Log.d(getString(R.string.photo_picker_title), getString(R.string.acces_denied))
            showSnackBar(getString(R.string.acces_denied))
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        analyzeViewModel = ViewModelProvider(
            this,
            AnalyzeViewModelFactory(UserPreference2.getInstance(requireContext().dataStore), UserRepository.getInstance(UserPreference2.getInstance(requireContext().dataStore)))
        )[AnalyzeViewModel::class.java]
        _binding = FragmentAnalyzeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val userPreferences = UserPreference2.getInstance(requireContext().dataStore)

//        observeViewModel()
        setupObserver()
        binding.btnAnalyze.setOnClickListener { analyzeImage() }

        binding.btnTakePicture.setOnClickListener { cekToken() }
        if (!allPermissionGranted()) {
            requestPermissionLauncher.launch(REQUIRED_PERMISSION)
        }

//        binding.btnAnalyze.setOnClickListener {
//            analyzeViewModel.selectUriImage.value?.let { uri ->
//                analyzeViewModel.uploadImage(uri, 35)
//            } ?: showSnackBar(getString(R.string.no_image_selected))
//        }
    }


    private fun cekToken() {
        analyzeViewModel.getSession().observe(viewLifecycleOwner) { user ->
            if (!user.isLogin) {
                val intent = Intent(requireContext(), LoginActivity::class.java)
//                launchCameraActivity.launch(intent)
                startActivity(intent)
            } else {
                val intent = Intent(requireContext(), CameraActivity::class.java)
//                startActivity(intent)
                launchCameraActivity.launch(intent)
            }

        }
//        analyzeViewModel.checkUserToken(true)
    }

    private fun observeViewModel() {
//        binding.progressBar.visibility = View.VISIBLE
        analyzeViewModel.skinResponse.observe(viewLifecycleOwner) { response ->
            val skinConditions = response.skinConditions.flatten() as ArrayList<String>
            val skinType = response.skinType.flatten() as ArrayList<String>
            val intent = Intent(requireContext(), ResultActivity::class.java).apply {
                putExtra("skin_conditions", skinConditions)
                putExtra("skin_type", skinType)
                putExtra(EXTRA_IMAGE, analyzeViewModel.selectUriImage.value)
            }
            startActivity(intent)
        }

        analyzeViewModel.errorMessage.observe(viewLifecycleOwner) { errorMessage ->
            showSnackBar(errorMessage)
        }
    }

    private fun analyzeImage() {
        if (analyzeViewModel.selectUriImage.value != null) {
            val resultKondisi= listOf("Acne", "Eye Bag", "Acne + Eye Bag")
            val resultTipe=listOf("Oily", "Dry", "Normal")
            val kondisiKulit  = resultKondisi.random()
            val tipeKulit = resultTipe.random()
            resultAnalyzer = kondisiKulit
            resultAnalyzer2 = tipeKulit
            binding.progressBar.visibility = View.GONE
            moveToResult()
        } else {
            showToast(getString(R.string.empty_image))
        }
    }

    private fun moveToResult() {
        binding.progressBar.visibility = View.VISIBLE
        val intent = Intent(requireContext(), ResultActivity::class.java)
        intent.putExtra(EXTRA_RESULT, resultAnalyzer )
        intent.putExtra(EXTRA_RESULT2, resultAnalyzer2)
        intent.putExtra(EXTRA_IMAGE, analyzeViewModel.selectUriImage.value)
        startActivity(intent)
    }

    private fun setupObserver() {
        analyzeViewModel.selectUriImage.observe(viewLifecycleOwner) {
            showImagePreview()
            if (it != null) {
                binding.btnAnalyze.isEnabled = true
            }
        }
    }

    private fun showImage() {
        analyzeViewModel.selectUriImage.value?.let {
            binding.previewImage.setImageURI(it)
        }
    }

    private fun showImagePreview() {
        analyzeViewModel.selectUriImage.observe(viewLifecycleOwner) { uri ->
            uri?.let {
                binding.previewImage.setImageURI(it)
            }
        }
    }

    private fun showSnackBar(message: String) {
        Snackbar.make(binding.root, message, Snackbar.LENGTH_SHORT).show()
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    private fun allPermissionGranted() = REQUIRED_PERMISSION.all {
        ContextCompat.checkSelfPermission(
            requireContext(),
            REQUIRED_PERMISSION
        ) == PackageManager.PERMISSION_GRANTED
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val REQUIRED_PERMISSION = Manifest.permission.CAMERA
        const val EXTRA_IMAGE = "extra_image"
        const val EXTRA_RESULT = "extra_result"
        const val EXTRA_RESULT2 = "extra_result2"
    }
}