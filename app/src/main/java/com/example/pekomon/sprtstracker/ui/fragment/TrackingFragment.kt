package com.example.pekomon.sprtstracker.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.example.pekomon.sprtstracker.R
import com.example.pekomon.sprtstracker.databinding.FragmentTrackingBinding
import com.example.pekomon.sprtstracker.internal.Constants.ACTION_PAUSE_SERVICE
import com.example.pekomon.sprtstracker.internal.Constants.ACTION_START_RESUME_SERVICE
import com.example.pekomon.sprtstracker.internal.Constants.MAP_ZOOM_LEVEL
import com.example.pekomon.sprtstracker.internal.Constants.POLYLINE_COLOR
import com.example.pekomon.sprtstracker.internal.Constants.POLYLINE_WIDTH
import com.example.pekomon.sprtstracker.service.Polyline
import com.example.pekomon.sprtstracker.service.TrackingService
import com.example.pekomon.sprtstracker.ui.viewmodel.MainViewModel
import com.example.pekomon.sprtstracker.utils.TimeUtils
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.PolylineOptions
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TrackingFragment : Fragment(R.layout.fragment_tracking) {

    private val viewModel: MainViewModel by viewModels()
    private var isTrackingOn = false
    private var points = mutableListOf<Polyline>()

    private lateinit var binding: FragmentTrackingBinding

    // Actual map object which our MapView in Fragment uses to display map
    private var map: GoogleMap? = null

    private var currentRunTimeMillis = 0L

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentTrackingBinding.bind(view)

        binding.mapView.onCreate(savedInstanceState)
        binding.mapView.getMapAsync{
            map = it
            addAllPolylines()
        }

        binding.btnToggleRun

        setupClicklistener()
        setupObservers()

    }

    private fun setupClicklistener() {
        binding.btnToggleRun.setOnClickListener {
            toggleRun()
        }
    }

    private fun sendToService(action: String) {
        Intent(requireContext(), TrackingService::class.java).also {
            it.action = action
            requireContext().startService(it)
        }
    }

    private fun setupObservers() {
        TrackingService.isTrackingOn.observe(viewLifecycleOwner, Observer {
            setTrackingEnabled(it)
        })

        TrackingService.pathPoints.observe(viewLifecycleOwner, Observer {
            points = it
            addLatestPolyline()
            moveCameraToLastLocation()
        })

        TrackingService.timeRunMillis.observe(viewLifecycleOwner, Observer {
            currentRunTimeMillis = it
            val formattedTimeStr = TimeUtils.getFormattedTime(it, true)
            binding.tvTimer.text = formattedTimeStr
        })
    }

    private fun toggleRun() {
        if (isTrackingOn) {
            sendToService(ACTION_PAUSE_SERVICE)
        } else {
            sendToService(ACTION_START_RESUME_SERVICE)
        }
    }

    private fun setTrackingEnabled(trackingOn: Boolean) {
        isTrackingOn = trackingOn
        if (!trackingOn) {
            binding.btnToggleRun.text = resources.getString(R.string.start)
            binding.btnFinishRun.visibility = View.VISIBLE
        } else {
            binding.btnToggleRun.text = resources.getString(R.string.stop)
            binding.btnFinishRun.visibility = View.GONE
        }
    }

    private fun moveCameraToLastLocation() {
        if (points.isNotEmpty() && points.last().isNotEmpty()) {
            map?.animateCamera(
                CameraUpdateFactory.newLatLngZoom(
                    points.last().last(),
                    MAP_ZOOM_LEVEL
                )
            )
        }
    }

    private fun addAllPolylines() {
        for (polyline in points) {
            val polylineOptions = getDefaultPolylineOptions()
                .addAll(polyline)
            map?.addPolyline(polylineOptions)
        }
    }

    private fun addLatestPolyline() {
        if (points.isNotEmpty() && points.last().size > 1) {
            val secondLastPoint = points.last()[points.last().size - 2]
            val lastPoint = points.last().last()
            val polylineOptions = getDefaultPolylineOptions()
                .add(secondLastPoint)
                .add(lastPoint)
            map?.addPolyline(polylineOptions)
        }
    }

    private fun getDefaultPolylineOptions(): PolylineOptions {
        return PolylineOptions()
            .color(POLYLINE_COLOR)
            .width(POLYLINE_WIDTH)
    }

    override fun onResume() {
        super.onResume()
        binding.mapView.onResume()
    }

    override fun onStart() {
        super.onStart()
        binding.mapView.onStart()
    }

    override fun onStop() {
        super.onStop()
        binding.mapView.onStop()
    }

    override fun onPause() {
        super.onPause()
        binding.mapView.onPause()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        binding.mapView.onLowMemory()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        binding.mapView.onSaveInstanceState(outState)
    }
}