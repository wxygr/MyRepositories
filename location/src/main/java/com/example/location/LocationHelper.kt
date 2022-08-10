package com.example.location

import android.content.Context
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.util.Log
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.CancellationTokenSource
import java.io.IOException
import java.util.*

internal class LocationHelper(private val context: Context) {

    private var locationListener: LocationListener? = null
    private val geocoder: Geocoder by lazy { Geocoder(context, Locale.getDefault()) }

    fun setLocationListener(locationListener: LocationListener) {
        this.locationListener = locationListener
    }

    fun getLocationWithGooglePlayService() {
        val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
        fusedLocationClient.lastLocation.addOnSuccessListener{
            if (it != null) locationListener?.onGetLocation(it)
            else getLocationWithAndroid()
        }
    }

    private fun getLocationWithAndroid() {
        val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
        val isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
        var location : Location? = null
        if (isGPSEnabled) {
             location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
        } else if (isNetworkEnabled) {
            // 从网络获取经纬度
            location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
        }
        locationListener?.onGetLocation(location)
    }

    // Geocoder specifically says that this call can use network and that it must not be called
    // from the main thread, so move it to the IO dispatcher.
    fun getAddressFromLocation(
        location: Location,
        maxResults: Int = 1
    ): String?  =
        try {
            val addresses = geocoder.getFromLocation(
                location.latitude,
                location.longitude,
                maxResults
            ).firstOrNull()
            addresses?.getAddressLine(0)
        } catch (e: IOException) {
            Log.w("GeocodingApi", "Error trying to get address from location.", e)
            null
        }


    internal interface LocationListener {
        fun onGetLocation(location: Location?)
    }

}
