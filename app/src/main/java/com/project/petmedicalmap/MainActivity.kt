package com.project.petmedicalmap

import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
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
    private var hospitalList: List<HospitalEntity> = emptyList()
    private var pharmacyList: List<PharmacyEntity> = emptyList()


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

        if (hospitalList.isNotEmpty()) {
            showHospitalMarkers(hospitalList)

        }
    }

    // 병원 리스트를 받아 지도에 병원 마커를 표시하는 함수
    private fun showHospitalMarkers(list: List<HospitalEntity>) {
        mMap.clear()

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

        list.firstOrNull()?.let {
            if (it.lat != null && it.lng != null) {
                val pos = LatLng(it.lat, it.lng)
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(pos, 14f))
            }
        }
    }

    // 약국 리스트를 받아 지도에 약국 마커를 표시하는 함수
    private fun showParmacyMarkers(list: List<PharmacyEntity>) {
        mMap.clear()

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

        list.firstOrNull()?.let {
            if (it.lat != null && it.lng != null) {
                val pos = LatLng(it.lat, it.lng)
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(pos, 14f))
            }
        }
    }
}
