package com.example.pekomon.sprtstracker.ui.fragment

import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.example.pekomon.sprtstracker.R
import com.example.pekomon.sprtstracker.databinding.FragmentSetupBinding
import com.example.pekomon.sprtstracker.internal.Constants.SETTING_VALUE_IS_FIRST_LAUNCH
import com.example.pekomon.sprtstracker.internal.Constants.SETTING_VALUE_NAME
import com.example.pekomon.sprtstracker.internal.Constants.SETTING_VALUE_WEIGHT
import com.example.pekomon.sprtstracker.ui.MainActivity
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SetupFragment : Fragment(R.layout.fragment_setup) {

    @Inject
    lateinit var sharedPrefs: SharedPreferences

    @set:Inject
    var isFirstLaunch = true

    private lateinit var binding: FragmentSetupBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (!isFirstLaunch) {
            val navOptions = NavOptions.Builder()
                .setPopUpTo(R.id.setupFragment, true)
                .build()
            findNavController().navigate(
                R.id.action_setupFragment_to_runFragment,
                savedInstanceState,
                navOptions
            )
        }

        binding = FragmentSetupBinding.bind(view)

        setupListeners()

    }

    private fun setupListeners() {
        binding.tvContinue.setOnClickListener {
            if (storeUserDataToSharesPrefs()) {
                findNavController().navigate(R.id.action_setupFragment_to_runFragment)

            } else {
                Snackbar.make(requireView(), resources.getString(R.string.snackbar_nag_fill_fields), Snackbar.LENGTH_SHORT).show()
            }
        }
    }

    private fun storeUserDataToSharesPrefs(): Boolean {
        val name = binding.etName.text.toString()
        val weight = binding.etWeight.text.toString()
        if (name.isEmpty() || weight.isEmpty()) {
            return false
        }

        sharedPrefs.edit()
            .putString(SETTING_VALUE_NAME, name)
            .putFloat(SETTING_VALUE_WEIGHT, weight.toFloat())
            .putBoolean(SETTING_VALUE_IS_FIRST_LAUNCH, false)
            .apply()
        (activity as MainActivity).setToolbarTitle(resources.getString(
            R.string.let_s_go_username,
            name
        ))
        return true
    }
}