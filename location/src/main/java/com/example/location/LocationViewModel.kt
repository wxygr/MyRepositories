package com.example.location

import android.annotation.SuppressLint
import android.location.Location
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LocationViewModel : BaseViewModel() {
  private val repository = Repository()
  private var _location : Location? = null

  val location: MutableLiveData<Location> = MutableLiveData()

  @SuppressLint("MissingPermission")
  fun getLocation() {
    viewModelScope.launch {
      withContext(Dispatchers.Default) {
        var currentLocation = repository.getLocationWithGooglePlay(MyApplication.app)
        if (currentLocation == null) {
          currentLocation = repository.getLocationWithAndroid(MyApplication.app)
        }
        _location = currentLocation
      }
      location.value = _location
    }
  }
}
