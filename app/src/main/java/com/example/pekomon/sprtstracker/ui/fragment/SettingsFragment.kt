package com.example.pekomon.sprtstracker.ui.fragment

import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.example.pekomon.sprtstracker.R
import com.example.pekomon.sprtstracker.databinding.FragmentSettingsBinding
import com.example.pekomon.sprtstracker.internal.Constants
import com.example.pekomon.sprtstracker.internal.Constants.SETTING_VALUE_NAME
import com.example.pekomon.sprtstracker.internal.Constants.SETTING_VALUE_WEIGHT
import com.example.pekomon.sprtstracker.ui.MainActivity
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SettingsFragment : Fragment(R.layout.fragment_settings) {

    private lateinit var binding: FragmentSettingsBinding

    @Inject
    lateinit var prefs: SharedPreferences

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentSettingsBinding.bind(view)

        setupClickListener()
        loadSettingValues()
    }

    private fun setupClickListener() {
        binding.btnApplyChanges.setOnClickListener { view ->
            if (saveSettings()) {
                Snackbar.make(view, R.string.snackbar_changes_saved, Snackbar.LENGTH_SHORT).show()
            } else {
                Snackbar.make(view, R.string.snackbar_nag_fill_fields, Snackbar.LENGTH_SHORT).show()
            }
        }
    }

    private fun loadSettingValues() {
        val name = prefs.getString(SETTING_VALUE_NAME, "")
        val weight = prefs.getFloat(SETTING_VALUE_WEIGHT, 77.7f)
        binding.etName.setText(name)
        binding.etWeight.setText(weight.toString())
    }

    private fun saveSettings(): Boolean {
        val name = binding.etName.text.toString()
        val weight = binding.etWeight.text.toString()

        if (name.isEmpty() || weight.isEmpty()) {
            return false
        }

        prefs.edit()
            .putString(SETTING_VALUE_NAME, name)
            .putFloat(SETTING_VALUE_WEIGHT, weight.toFloat())
            .apply()

        (activity as MainActivity).setToolbarTitle(resources.getString(
            R.string.let_s_go_username,
            name
        ))
        return true
    }
}