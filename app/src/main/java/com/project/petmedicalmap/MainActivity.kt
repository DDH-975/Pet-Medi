package com.project.petmedicalmap

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.project.petmedicalmap.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(), OnMapReadyCallback {
    lateinit var binding: ActivityMainBinding
    private lateinit var mMap: GoogleMap
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<ConstraintLayout>

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
    }


    // 버튼 등을 눌러 프로그래밍 방식으로 상태 변경 시
    private fun expandSheet() {
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED

    }


    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        //특정 위치(예: 119동물병원) 좌표 설정
        val hos119 = LatLng(35.85716248, 128.4665375)

        //마커 추가 및 가메라 이동
        mMap.addMarker(MarkerOptions().position(hos119).title("119동물병원"))
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(hos119, 15f))

    }
}