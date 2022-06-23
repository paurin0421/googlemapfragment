package com.example.googlemapfragment;

import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.*;
import android.widget.Button;
import android.widget.EditText;
import androidx.fragment.app.Fragment;
import com.google.android.gms.maps.*;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MainFragment extends Fragment implements OnMapReadyCallback {

    View rootView;
    MapView mapView;
    GoogleMap mMap;
    private Geocoder geocoder1;
    Button button;
    EditText editText;

    public MainFragment() {
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.content_main, container, false);
        mapView = (MapView) rootView.findViewById(R.id.mapview);
        mapView.onCreate(savedInstanceState);
        editText=(EditText) rootView.findViewById(R.id.editText);
        button= (Button) rootView.findViewById(R.id.button);
        mapView.getMapAsync(this);
        return rootView;
    }

    @Override
    public void onResume() {
        mapView.onResume();
        super.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    public void onMapReady(final GoogleMap googleMap) {
        Geocoder geocoder;
        mMap = googleMap;
        geocoder = new Geocoder(MainFragment.this);
        // 맵 터치 이벤트 구현
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener(){
            @Override
            public void onMapClick(LatLng point) {
                MarkerOptions mOptions = new MarkerOptions();
                // 마커 타이틀
                mOptions.title("마커좌표");
                Double latitude = point.latitude; // 위도
                Double longitude = point.longitude; // 경도
                //마커 세부 내용 타이틀
                mOptions.snippet(latitude.toString() + ", " + longitude.toString());
                // LatLng: 위도 경도 쌍을 나타냄
                mOptions.position(new LatLng(latitude, longitude));
                // 마커(핀) 추가
                googleMap.addMarker(mOptions);
            }
        });
        // 검색 이벤트
        button.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v){
                String str=editText.getText().toString();
                List<Address> addressList = null;

//                String urlStr = defaultUrl + str + "&key=AIzaSyDlUlU-ayvetxUT1SW7Tc4z1IGn-FKz7K0&language=ko";
//                ConnectedThread thread = new ConnectThread(urlStr);
//                thread.start();
                try {
                    addressList = geocoder.getFromLocationName(str, 10);
                } catch (IOException e) {
                    e.printStackTrace();

                }


                if(addressList.size()!=0) {
                    System.out.println(addressList.get(0).toString());
                    //관련 정보 문자열에 따라 위 경도 뽑아내는 작업
                    String[] splitStr = addressList.get(0).toString().split(",");
                    String address = splitStr[0].substring(splitStr[0].indexOf("\"") + 1, splitStr[0].length() - 2); // 주소
                    System.out.println(address);

                    String latitude = splitStr[10].substring(splitStr[10].indexOf("=") + 1); // 위도
                    String longitude = splitStr[12].substring(splitStr[12].indexOf("=") + 1); // 경도
                    System.out.println(latitude);
                    System.out.println(longitude);

                    // 좌표(위도, 경도) 생성
                    LatLng point = new LatLng(Double.parseDouble(latitude), Double.parseDouble(longitude));
                    // 마커 생성 ( 검색 관련 마커 )
                    MarkerOptions mOptions2 = new MarkerOptions();
                    mOptions2.title("search result");
                    mOptions2.snippet(latitude.toString() + ", " + longitude.toString());
                    mOptions2.position(point);
                    // 마커 추가
                    mMap.addMarker(mOptions2);
                    // 해당 좌표로 화면 이동
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(point, 15));
                }
            }
        });

        //기본 위치 설정하기
        LatLng snu = new LatLng(37.4574324, 126.947918);
        mMap.addMarker(new MarkerOptions().position(snu).title("기본위치 : 37.4574324,126.947918"));

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(snu,16));
    }
//    @Override
//    public boolean onMarkerClick(Marker marker){
//        Toast.makeText(this, marker.getTitle()+ marker.getPosition(), Toast.LENGTH_SHORT).show();
//        return true;
//    }

}