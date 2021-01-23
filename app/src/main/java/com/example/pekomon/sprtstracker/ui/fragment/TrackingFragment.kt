package com.example.pekomon.sprtstracker.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.pekomon.sprtstracker.R
import com.example.pekomon.sprtstracker.data.entity.Run
import com.example.pekomon.sprtstracker.databinding.FragmentTrackingBinding
import com.example.pekomon.sprtstracker.extensions.length
import com.example.pekomon.sprtstracker.internal.Constants.ACTION_PAUSE_SERVICE
import com.example.pekomon.sprtstracker.internal.Constants.ACTION_START_RESUME_SERVICE
import com.example.pekomon.sprtstracker.internal.Constants.ACTION_STOP_SERVICE
import com.example.pekomon.sprtstracker.internal.Constants.MAP_ZOOM_LEVEL
import com.example.pekomon.sprtstracker.internal.Constants.POLYLINE_COLOR
import com.example.pekomon.sprtstracker.internal.Constants.POLYLINE_WIDTH
import com.example.pekomon.sprtstracker.service.Polyline
import com.example.pekomon.sprtstracker.service.TrackingService
import com.example.pekomon.sprtstracker.ui.viewmodel.MainViewModel
import com.example.pekomon.sprtstracker.utils.TimeUtils
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.PolylineOptions
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import java.lang.Math.round
import java.util.*

@AndroidEntryPoint
class TrackingFragment : Fragment(R.layout.fragment_tracking) {

    private val viewModel: MainViewModel by viewModels()
    private var isTrackingOn = false
    private var points = mutableListOf<Polyline>()

    private lateinit var binding: FragmentTrackingBinding

    // Actual map object which our MapView in Fragment uses to display map
    private var map: GoogleMap? = null

    private var currentRunTimeMillis = 0L

    private var menu: Menu? = null

    private var weight = 77.2f

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

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.toolbar_menu, menu)
        this.menu = menu
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        super.onPrepareOptionsMenu(menu)
        if (currentRunTimeMillis > 0L) {
            this.menu?.getItem(0)?.isVisible = true
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menuitemCancel -> {
                showCancelConfirmationDialog()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun showCancelConfirmationDialog() {
        val dialog = MaterialAlertDialogBuilder(requireContext(), R.style.AlertDialogTheme)
            .setTitle(R.string.confirm_dialog_title_cancel)
            .setMessage(R.string.confirm_dialog_message)
            .setIcon(R.drawable.ic_delete)
            .setPositiveButton(R.string.confirm_dialog_yes, { _, _ ->
                stop()
            })
            .setNegativeButton(R.string.confirm_dialog_no) { dialogInterface, _ ->
                dialogInterface.cancel()
            }
            .create()
        dialog.show()
    }

    private fun stop() {
        sendToService(ACTION_STOP_SERVICE)
        findNavController().navigate(R.id.action_trackingFragment_to_runFragment)
    }

    private fun setupClicklistener() {
        binding.btnToggleRun.setOnClickListener {
            toggleRun()
        }

        binding.btnFinishRun.setOnClickListener {
            zoomWholeTrackOnMap()
            endRun()
        }
    }

    private fun sendToService(action: String) {
        Intent(requireContext(), TrackingService::class.java).also {
            it.action = action
            requireContext().startService(it)
        }
    }

    private fun setupObservers() {
        TrackingService.isTrackingOn.observe(viewLifecycleOwner, {
            setTrackingEnabled(it)
        })

        TrackingService.pathPoints.observe(viewLifecycleOwner, {
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
            menu?.getItem(0)?.isVisible = true
            sendToService(ACTION_PAUSE_SERVICE)
        } else {
            sendToService(ACTION_START_RESUME_SERVICE)
        }
    }

    private fun setTrackingEnabled(trackingOn: Boolean) {
        isTrackingOn = trackingOn
        if (!trackingOn && currentRunTimeMillis > 0L) {
            binding.btnToggleRun.text = resources.getString(R.string.start)
            binding.btnFinishRun.visibility = View.VISIBLE
        } else if (trackingOn){
            binding.btnToggleRun.text = resources.getString(R.string.stop)
            binding.btnFinishRun.visibility = View.GONE
            menu?.getItem(0)?.isVisible = true
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

    private fun zoomWholeTrackOnMap() {
        val bounds = LatLngBounds.builder()
        for (polyline in points) {
            for (position in polyline) {
                bounds.include(position)
            }
        }
        map?.moveCamera(
            CameraUpdateFactory.newLatLngBounds(
                bounds.build(),
                binding.mapView.width,
                binding.mapView.height,
                (binding.mapView.height * 0.05f).toInt()
            )
        )
    }

    private fun endRun() {
        map?.snapshot { bmp ->
            var totalDistance = 0
            for (polyline in points) {
                totalDistance += polyline.length().toInt()
            }
            // get km/h  rounded
            val averageSpeed = round(totalDistance / (currentRunTimeMillis/1000f))* 3.6f
            val dateTimeStamp = Calendar.getInstance().timeInMillis
            val caloriesBurned = ((totalDistance/1000) * weight).toInt()
            val run = Run(bmp, dateTimeStamp,
                averageSpeed, totalDistance, currentRunTimeMillis, caloriesBurned)
            viewModel.addRun(run)
            Snackbar.make(
                requireActivity().findViewById(R.id.rootView),
                resources.getString(R.string.snackbar_saved_successfully),
                Snackbar.LENGTH_LONG
            ).show()
            stop()
        }
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