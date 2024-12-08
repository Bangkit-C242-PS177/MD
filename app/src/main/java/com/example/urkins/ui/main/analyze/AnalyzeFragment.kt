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
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.example.urkins.R
import com.example.urkins.databinding.FragmentAnalyzeBinding
import com.example.urkins.ui.activity.camera.CameraActivity
import com.example.urkins.ui.activity.login.LoginActivity
import com.google.android.material.snackbar.Snackbar

class AnalyzeFragment : Fragment() {

    private var _binding: FragmentAnalyzeBinding? = null
    private val analyzeViewModel: AnalyzeViewModel by viewModels()

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private val launchCameraActivity = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val uri = result.data?.getStringExtra("image_uri")?.let { Uri.parse(it) }
            uri?.let {
                analyzeViewModel.setSelectImageUri(it)
                showImage()
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
        val analyzeViewModel =
            ViewModelProvider(this).get(AnalyzeViewModel::class.java)

        _binding = FragmentAnalyzeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnTakePicture.setOnClickListener { cekToken() }
        if (!allPermissionGranted()) {
            requestPermissionLauncher.launch(REQUIRED_PERMISSION)
        }
    }

    private fun cekToken() {
            val userToken = false
            if (userToken) {
                val intent = Intent(requireContext(), CameraActivity::class.java)
                launchCameraActivity.launch(intent)
            } else {
                val intent = Intent(requireContext(), LoginActivity::class.java)
                startActivity(intent)
            }
    }

    private fun showImage() {
        analyzeViewModel.selectUriImage.value?.let {
            binding.previewImage.setImageURI(it)
        }
    }

    private fun showSnackBar(message: String) {
        Snackbar.make(binding.root, message, Snackbar.LENGTH_SHORT).show()
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
    }
}