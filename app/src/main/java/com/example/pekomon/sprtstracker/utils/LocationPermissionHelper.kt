package com.example.pekomon.sprtstracker.utils

import android.Manifest
import android.content.Context
import android.os.Build

object LocationPermissionHelper {

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

    //TODO: inject
    val permissionHelper: PermissionHelper = PermissionHelperImpl

    fun hasLocationPermissions(context: Context): Boolean {
        return (permissionHelper.hasPermissions(context, *neededPermissions))
    }


}