package com.example.pekomon.sprtstracker.ui.fragment

import android.Manifest
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pekomon.sprtstracker.R
import com.example.pekomon.sprtstracker.databinding.FragmentRunBinding
import com.example.pekomon.sprtstracker.internal.Constants.REQUEST_CODE_LOCATION_PERMISSION
import com.example.pekomon.sprtstracker.ui.adapters.RunAdapter
import com.example.pekomon.sprtstracker.ui.viewmodel.MainViewModel
import com.example.pekomon.sprtstracker.utils.LocationPermissionHelper
import com.example.pekomon.sprtstracker.utils.PermissionHelperImpl
import dagger.hilt.android.AndroidEntryPoint
import pub.devrel.easypermissions.AppSettingsDialog
import pub.devrel.easypermissions.EasyPermissions

@AndroidEntryPoint
class RunFragment : Fragment(R.layout.fragment_run), EasyPermissions.PermissionCallbacks {

    private val viewModel: MainViewModel by viewModels()
    private lateinit var binding: FragmentRunBinding

    private lateinit var runAdapter: RunAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentRunBinding.bind(view)
        checkAndRequestPermissions()
        setupRecyclerView()
        setupClickListeners()
        setupObservers()
    }

    private fun setupClickListeners() {
        binding.fab.setOnClickListener {
            findNavController().navigate(R.id.action_runFragment_to_trackingFragment)
        }
    }

    private fun setupRecyclerView() {
        binding.rvRuns.apply {
            runAdapter = RunAdapter().also {
                adapter = it
            }
            layoutManager = LinearLayoutManager(requireContext())

        }
    }

    private fun setupObservers() {
        viewModel.runs.observe(viewLifecycleOwner, Observer {
            runAdapter.submitlist(it)
        })
    }

    private fun checkAndRequestPermissions() {
        if (LocationPermissionHelper.hasLocationPermissions(requireContext())) {
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
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
            )
        } else {
            EasyPermissions.requestPermissions(
                this,
                getString(R.string.allow_location_permissions),
                REQUEST_CODE_LOCATION_PERMISSION,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_BACKGROUND_LOCATION
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