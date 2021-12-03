package ua.devserhii.testapp.ui

import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.room.Room
import com.google.android.gms.location.*
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import ua.devserhii.testapp.common.API_KEY
import ua.devserhii.testapp.common.LOCATION_PERMISSION_CODE
import ua.devserhii.testapp.data.AppDatabase
import ua.devserhii.testapp.databinding.ActivityMainBinding
import ua.devserhii.testapp.request.ApiModule


class MainActivity : AppCompatActivity() {

    private var adapter: ArrayAdapter<String>? = null
    private var list = mutableListOf<String>()
    private var availableLocation = false
    private var cityName = "London"
    private lateinit var database: AppDatabase

    private lateinit var binding: ActivityMainBinding

    private val compositeDisposable = CompositeDisposable()

    private lateinit var fusedLocationClient: FusedLocationProviderClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        checkPermission(ACCESS_FINE_LOCATION, LOCATION_PERMISSION_CODE)

        initSpinner()

        database = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java, "populus-database"
        ).build()

        binding.bRequestWeather.setOnClickListener {
            if (database.getWeatherDao()?.getCityByName(cityName) == null) {
                if (cityName == "Current Position")
                    requestWeatherByCoordinates()
                requestWeatherByCityName(cityName)
            } else {
                binding.tvWeather.text =
                    database.getWeatherDao()?.getCityByName(cityName)?.temperature?.temp.toString()
            }
        }
    }

    private fun initSpinner() {

        list.add("London")
        list.add("Paris")
        list.add("Istanbul")
        list.add("Tokyo")
        list.add("Kyiv")
        if (availableLocation)
            list.add("Current Position")

        adapter = ArrayAdapter(
            applicationContext,
            android.R.layout.simple_spinner_item, list
        )
        adapter?.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.sSpinner.adapter = adapter
        binding.sSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                cityName = p0?.getItemAtPosition(p2).toString()
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
            }
        }
    }

    private fun checkPermission(permission: String, requestCode: Int) {
        if (ContextCompat.checkSelfPermission(
                this@MainActivity,
                permission
            ) == PackageManager.PERMISSION_DENIED
        ) {
            ActivityCompat.requestPermissions(
                this@MainActivity,
                arrayOf(permission),
                requestCode
            )
        } else {
            availableLocation = true
            Toast.makeText(this@MainActivity, "Permission already granted", Toast.LENGTH_SHORT)
                .show()

            fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

            fusedLocationClient.lastLocation
                .addOnSuccessListener { location: Location? ->
                    binding.tvLatitude.text = location?.latitude.toString()
                    binding.tvLongitude.text = location?.longitude.toString()
                }

        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(
                this@MainActivity,
                "Location Permission Granted",
                Toast.LENGTH_SHORT
            )
                .show()

            availableLocation = true

            checkPermission(ACCESS_FINE_LOCATION, LOCATION_PERMISSION_CODE)

            fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

            fusedLocationClient.lastLocation
                .addOnSuccessListener { location: Location? ->
                    binding.tvLatitude.text = location?.latitude.toString()
                    binding.tvLongitude.text = location?.longitude.toString()
                }
        } else {
            Toast.makeText(
                this@MainActivity,
                "Location Permission Denied",
                Toast.LENGTH_SHORT
            )
                .show()
            availableLocation = false
        }
    }

    private fun requestWeatherByCityName(cityName: String) {
        val subscribe =
            ApiModule.API_INTERFACE_CLIENT.weatherRequestByCityName(cityName, API_KEY)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { weatherResponse ->
                        database.getWeatherDao()?.insertAll(weatherResponse)
                        binding.tvWeather.text = weatherResponse.temperature.temp.toString()
                    },
                    { error ->
                        Log.e("TAG", error.toString())
                    })
        compositeDisposable.add(subscribe)
    }

    private fun requestWeatherByCoordinates() {
        val latitude = binding.tvLatitude.text.toString()
        val longitude = binding.tvLongitude.text.toString()
        val subscribe = ApiModule.API_INTERFACE_CLIENT.weatherRequestByCoordinates(
            latitude,
            longitude,
            API_KEY
        )
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { weatherResponse ->
                    database.getWeatherDao()?.insertAll(weatherResponse)
                    binding.tvWeather.text = weatherResponse.temperature.temp.toString()
                },
                { error ->
                    Log.e("TAG", error.toString())
                })
        compositeDisposable.add(subscribe)
    }


    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.dispose()
    }
}