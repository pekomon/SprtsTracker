package com.example.pekomon.sprtstracker.ui.fragment

import android.Manifest
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.pekomon.sprtstracker.R
import com.example.pekomon.sprtstracker.databinding.FragmentRunBinding
import com.example.pekomon.sprtstracker.internal.Constants.REQUEST_CODE_LOCATION_PERMISSION
import com.example.pekomon.sprtstracker.ui.viewmodel.MainViewModel
import com.example.pekomon.sprtstracker.utils.PermissionHelperImpl
import dagger.hilt.android.AndroidEntryPoint
import pub.devrel.easypermissions.AppSettingsDialog
import pub.devrel.easypermissions.EasyPermissions

@AndroidEntryPoint
class RunFragment : Fragment(R.layout.fragment_run), EasyPermissions.PermissionCallbacks {

    private val viewModel: MainViewModel by viewModels()
    private lateinit var binding: FragmentRunBinding

    private val neededPermissions = if(Build.VERSION.SDK_INT < Build.VERSION_CODES.Q)
        arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )
    else
        arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_BACKGROUND_LOCATION,
        )

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentRunBinding.bind(view)
        checkAndRequestPermissions()
        setupClickListeners()
    }

    private fun setupClickListeners() {
        binding.fab.setOnClickListener {
            findNavController().navigate(R.id.action_runFragment_to_trackingFragment)
        }
    }

    private fun checkAndRequestPermissions() {
        if (PermissionHelperImpl.hasPermissions(requireContext(), *neededPermissions)) {
            return
        }
        requestPermissions()
    }

    private fun requestPermissions(){
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
            EasyPermissions.requestPermissions(
                this,
                getString(R.string.allow_location_permissions),
                REQUEST_CODE_LOCATION_PERMISSION,
                *neededPermissions
            )
        }
    }

    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>) {
        // Great! Everything is fine
    }

    override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>) {
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            AppSettingsDialog.Builder(this).build().show()
        } else {
            requestPermissions()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(
            requestCode, permissions, grantResults, this
        )
    }
}