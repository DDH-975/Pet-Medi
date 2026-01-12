package com.project.petmedicalmap

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.app.ActivityCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.project.petmedicalmap.databinding.ActivityMainBinding
import com.project.petmedicalmap.roomDB.hospital.HospitalEntity
import com.project.petmedicalmap.roomDB.pharmacy.PharmacyEntity


class MainActivity : AppCompatActivity(), OnMapReadyCallback {
    private lateinit var binding: ActivityMainBinding
    private lateinit var mMap: GoogleMap
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<ConstraintLayout>
    private lateinit var hospitalviewModel: HospitalViewModel
    private lateinit var pharmacyViewModel: PharmacyViewModel
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var hospitalList: List<HospitalEntity> = emptyList()
    private var pharmacyList: List<PharmacyEntity> = emptyList()
    private var selectedMarker: Marker? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        hospitalviewModel = ViewModelProvider(this).get(HospitalViewModel::class.java)
        pharmacyViewModel = ViewModelProvider(this).get(PharmacyViewModel::class.java)


        hospitalviewModel._hospitalData.observe(this) { hospitalData ->
            hospitalList = hospitalData
            if (::mMap.isInitialized) {
                showHospitalMarkers(hospitalList)
            }
        }


        hospitalviewModel._Hospiatal24thData.observe(this) { hospital24thData ->
            hospitalList = hospital24thData
            if (::mMap.isInitialized) {
                showHospitalMarkers(hospitalList)
            }
        }


        pharmacyViewModel._pharmacyData.observe(this) { pharmacyData ->
            pharmacyList = pharmacyData
            if (::mMap.isInitialized) {
                showParmacyMarkers(pharmacyList)
            }
        }


        val locationPermissionRequest = registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->
            if (permissions.getOrDefault(Manifest.permission.ACCESS_FINE_LOCATION, false)) {
                moveToMyLocation()
            }
        }

        locationPermissionRequest.launch(
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
        )


        // 1. View 연결 및 Behavior 획득
        bottomSheetBehavior = BottomSheetBehavior.from(binding.bottomSheet)

        // 2. 콜백 설정 (상태 변경 감지)
        bottomSheetBehavior.addBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when (newState) {
                    BottomSheetBehavior.STATE_EXPANDED -> { /* 완전히 펼쳐짐 */
                        expandSheet()
                    }

                    BottomSheetBehavior.STATE_COLLAPSED -> { /* 접힘 */
                    }

                    BottomSheetBehavior.STATE_DRAGGING -> { /* 드래그 중 */
                    }
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                // slideOffset: 접힘(0.0) ~ 펼쳐짐(1.0) 사이의 값
            }
        })

        // SupportMapFragment를 찾아서 지도가 준비되면 통지를 받도록 설정
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)


        if (binding.radioBtnHos.isChecked) {
            hospitalviewModel.getHosAllData()
        }

        binding.radioBtnHos.setOnCheckedChangeListener { _, Checked ->
            if (Checked) {
                binding.check24Hos.visibility = View.VISIBLE
                hospitalviewModel.getHosAllData()

            } else {
                binding.check24Hos.apply {
                    visibility = View.GONE
                    isChecked = false
                }
                pharmacyViewModel.getPharmacyData()
            }
        }


        binding.check24Hos.setOnCheckedChangeListener { _, Checked ->
            if (Checked) {
                hospitalviewModel.get24thHosData()
            } else {
                hospitalviewModel.getHosAllData()
            }
        }

    }

    // 버튼 등을 눌러 프로그래밍 방식으로 상태 변경 시
    private fun expandSheet() {
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
    }


    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
            == PackageManager.PERMISSION_GRANTED
        ) {
            mMap.isMyLocationEnabled = true
        }

        //마커 클릭 리스너
        mMap.setOnMarkerClickListener({ marker ->
            val markerTitle = marker.title
            selectedMarker?.setIcon(
                BitmapDescriptorFactory.defaultMarker(
                    BitmapDescriptorFactory.HUE_RED
                )
            )
            Toast.makeText(this, markerTitle, Toast.LENGTH_SHORT).show()
            marker.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))

            selectedMarker = marker
            false
        })

        if (hospitalList.isNotEmpty()) {
            showHospitalMarkers(hospitalList)
        }

    }

    // 병원 리스트를 받아 지도에 병원 마커를 표시하는 함수
    private fun showHospitalMarkers(list: List<HospitalEntity>) {
        mMap.clear()
        selectedMarker = null

        list.forEach { list ->
            val lat = list.lat
            val lng = list.lng

            if (lat != null && lng != null) {
                val position = LatLng(lat, lng)
                mMap.addMarker(
                    MarkerOptions().position(position).title(list.name)
                )
            }
        }
    }

    // 약국 리스트를 받아 지도에 약국 마커를 표시하는 함수
    private fun showParmacyMarkers(list: List<PharmacyEntity>) {
        mMap.clear()
        selectedMarker = null

        list.forEach { list ->
            val lat = list.lat
            val lng = list.lng

            if (lat != null && lng != null) {
                val position = LatLng(lat, lng)
                mMap.addMarker(
                    MarkerOptions().position(position).title(list.name)
                )

            }
        }
    }

    // 내 위치 확인 후 화면 이동 메서드
    private fun moveToMyLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
            == PackageManager.PERMISSION_GRANTED
        ) {

            fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                if (location != null) {
                    val latLng = LatLng(location.latitude, location.longitude)
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 17f))
                }
            }
        }
    }
}
