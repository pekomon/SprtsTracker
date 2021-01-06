package com.example.pekomon.sprtstracker.utils

import android.content.Context
import androidx.fragment.app.Fragment
import pub.devrel.easypermissions.EasyPermissions

object PermissionHelperImpl : PermissionHelper {
    override fun hasPermissions(context: Context, vararg perms: String): Boolean {
        return EasyPermissions.hasPermissions(context.applicationContext, *perms)
    }

    override fun requestPermissions(
        host: Fragment,
        rationale: String,
        requestCode: Int,
        vararg perms: String
    ) {
        EasyPermissions.requestPermissions(host, rationale, requestCode, *perms)
    }
}