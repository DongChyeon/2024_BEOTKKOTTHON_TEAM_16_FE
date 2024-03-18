package com.beotkkot.tamhumhajang.ui.kakaomap

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Looper
import androidx.core.app.ActivityCompat
import com.beotkkot.tamhumhajang.common.BaseViewModel
import com.beotkkot.tamhumhajang.ui.map.MovingCameraWrapper
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

@HiltViewModel
class KakaoMapViewModel @Inject constructor(
    @ApplicationContext private val context: Context
) : BaseViewModel<KakaoMapContract.State, KakaoMapContract.Event, KakaoMapContract.Effect>(
    initialState = KakaoMapContract.State()
) {

    companion object {
        var initialMarkerLoadFlag = true
    }

    override fun reduceState(event: KakaoMapContract.Event) {
        TODO("Not yet implemented")
    }

    private val fusedLocationClient: FusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(context)

    private val locationRequest: LocationRequest =
        LocationRequest.Builder(1000) // 초기 1회만 가져오고 Long.MAX_VALUE 만큼 기다림
            .setPriority(Priority.PRIORITY_LOW_POWER)
            .build()
    private val locationCallback: CustomLocationCallback = CustomLocationCallback()

    fun updateMovingCameraPosition(movingCameraPosition: MovingCameraWrapper) {
        updateState(currentState.copy(movingCameraPosition = movingCameraPosition))
    }

    fun updateMovingCameraPositionToDefault() {
        updateState(currentState.copy(movingCameraPosition = MovingCameraWrapper.DEFAULT))
    }

    fun updateIsFixedPerspective(isFixed: Boolean) {
        updateState(currentState.copy(isFixedPerspective = isFixed))
    }

    inner class CustomLocationCallback : LocationCallback() {

        override fun onLocationResult(locationResult: LocationResult) {
            super.onLocationResult(locationResult)
            locationResult.lastLocation?.let {
                // 초기 1회 진입할 때 마커 불러오기
                if (initialMarkerLoadFlag) {
                    initialMarkerLoadFlag = false
                    updateState(
                        currentState.copy(
                            isLoading = false,
                            movingCameraPosition = MovingCameraWrapper.MOVING(it)
                        )
                    )
                }
                updateState(currentState.copy(userPosition = com.kakao.vectormap.LatLng.from(it.latitude, it.longitude)))
            }
        }
    }

    fun addLocationListener() {
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }

        fusedLocationClient.requestLocationUpdates(
            locationRequest,
            locationCallback,
            Looper.getMainLooper()
        )
    }

    fun removeLocationListener() {
        fusedLocationClient.removeLocationUpdates(locationCallback)
    }

}