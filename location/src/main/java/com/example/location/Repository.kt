package com.example.location

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.location.LocationManager
import com.google.android.gms.location.*
import com.google.android.gms.tasks.CancellationTokenSource
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException


class Repository {

    @SuppressLint("MissingPermission")
    suspend fun getLocationWithGooglePlay(context: Context): Location? =
        suspendCancellableCoroutine { cont ->
            val cancellationTokenSource = CancellationTokenSource()
            val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)

            fusedLocationClient.getCurrentLocation(LocationRequest.PRIORITY_HIGH_ACCURACY, cancellationTokenSource.token).apply {
                addOnSuccessListener { cont.resume(it) }
                addOnCanceledListener { cont.resume(null) }
                addOnFailureListener { cont.resumeWithException(it) }
            }

            cont.invokeOnCancellation { cancellationTokenSource.cancel() }
        }

    @SuppressLint("MissingPermission")
    fun getLocationWithAndroid(context: Context) : Location? {
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
        return location
    }
}
