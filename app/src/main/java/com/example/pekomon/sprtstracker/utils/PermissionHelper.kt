package com.example.pekomon.sprtstracker.utils

import android.content.Context
import androidx.fragment.app.Fragment

interface PermissionHelper {
    fun hasPermissions(context: Context, vararg perms: String): Boolean
    fun requestPermissions(host: Fragment, rationale: String, requestCode: Int, vararg perms: String)
}