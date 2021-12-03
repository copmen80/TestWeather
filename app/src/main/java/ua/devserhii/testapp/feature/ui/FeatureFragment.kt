package ua.devserhii.testapp.feature.ui

import android.Manifest.permission.ACCESS_COARSE_LOCATION
import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.R
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import ua.devserhii.testapp.databinding.FeatureFragmentBinding

@AndroidEntryPoint
class FeatureFragment : Fragment() {
    private var _binding: FeatureFragmentBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: FeatureViewModel


    private var cityName = "London"
    private var availableLocation = false
    private lateinit var fusedLocationClient: FusedLocationProviderClient


    private val locationPermissionRequest = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        when {
            permissions[ACCESS_FINE_LOCATION] ?: false ||
                    permissions[ACCESS_COARSE_LOCATION] ?: false -> {
                Toast.makeText(
                    requireContext(),
                    getString(ua.devserhii.testapp.R.string.location_permission_granted),
                    Toast.LENGTH_SHORT
                )
                    .show()

                availableLocation = true

                checkPermissionAndUseLocation(ACCESS_FINE_LOCATION)
            }
            else -> {

                Toast.makeText(
                    requireContext(),
                    getString(ua.devserhii.testapp.R.string.location_permission_denied),
                    Toast.LENGTH_SHORT
                )
                    .show()
                availableLocation = false
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProvider(requireActivity())[FeatureViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FeatureFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        checkPermissionAndUseLocation(ACCESS_FINE_LOCATION)
        initSpinner()
        handleViewModel()
        observeEvents()
        initButton()
    }

    private fun initButton() {
        binding.bRequestWeather.setOnClickListener {
            if (cityName == CURRENT_LOCATION) {
                viewModel.getTemperatureByCoordinates(
                    binding.tvLatitude.text.toString().toDouble(),
                    binding.tvLongitude.text.toString().toDouble()
                )
            } else {
                viewModel.getTemperatureByCity(cityName)
            }
        }
    }

    private fun handleViewModel() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.content.collectLatest { state ->
                binding.tvWeather.text = state.temperature.toString()
            }
        }
    }

    private fun observeEvents() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.eventsFlow.collectLatest { event ->
                Toast.makeText(requireContext(), event, Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    private fun checkPermissionAndUseLocation(permission: String) {
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                permission
            ) == PackageManager.PERMISSION_DENIED
        ) {
            locationPermissionRequest.launch(arrayOf(permission))
        } else {
            availableLocation = true
            Toast.makeText(
                requireContext(),
                getString(ua.devserhii.testapp.R.string.permission_already_granted),
                Toast.LENGTH_SHORT
            )
                .show()

            fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())

            fusedLocationClient.lastLocation
                .addOnSuccessListener { location: Location? ->
                    binding.tvLatitude.text = location?.latitude.toString()
                    binding.tvLongitude.text = location?.longitude.toString()
                    initSpinner()
                }
        }
    }

    private fun initSpinner() {
        val list = mutableListOf("London", "Paris", "Istanbul", "Tokyo", "Kyiv")

        if (availableLocation)
            list.add(CURRENT_LOCATION)

        val adapter: ArrayAdapter<String> = ArrayAdapter(
            requireContext(),
            R.layout.simple_spinner_item, list
        )
        adapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item)
        binding.sSpinner.adapter = adapter
        binding.sSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                cityName = p0?.getItemAtPosition(p2).toString()
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
            }
        }
    }

    companion object {
        private const val CURRENT_LOCATION = "Current Position"
    }

}