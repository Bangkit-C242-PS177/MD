package com.example.urkins.ui.main.analyze

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.example.urkins.R
import com.example.urkins.databinding.FragmentAnalyzeBinding
import com.example.urkins.ui.activity.camera.CameraActivity
import com.example.urkins.ui.activity.login.LoginActivity

class AnalyzeFragment : Fragment() {

    private var _binding: FragmentAnalyzeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private val addStoryViewModel: AnalyzeViewModel by viewModels()

    private val launchCameraActivity = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val uri = result.data?.getStringExtra("image_uri")?.let { Uri.parse(it) }
            uri?.let {
                addStoryViewModel.setSelectImageUri(it)
                showImage()
            }
        } else {
            Log.d(getString(R.string.camera_title), getString(R.string.failed_take_photo))
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

//        val textView: TextView = binding.textDashboard
//        analyzeViewModel.text.observe(viewLifecycleOwner) {
//            textView.text = it
//        }
        return root


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnTakePicture.setOnClickListener { cekToken() }
    }

    private fun cekToken() {
            val userToken = true
            if (userToken) {
                val intent = Intent(requireContext(), CameraActivity::class.java)
                startActivity(intent)
            } else {
                val intent = Intent(requireContext(), LoginActivity::class.java)
                startActivity(intent)
            }
    }

    private fun showImage() {
        addStoryViewModel.selectUriImage.value?.let {
            binding.previewImage.setImageURI(it)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}