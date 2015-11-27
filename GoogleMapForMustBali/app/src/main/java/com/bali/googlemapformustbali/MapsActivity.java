package com.bali.googlemapformustbali;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
/**
 * 该类继承了 FragmentActivity，实现了 OnMapReadyCallback、OnMyLocationButtonClickListener、OnRequestPermissionsResultCallback 接口。
 * 实现了用户定位和在 Map 中设置 Mark。
 * */
public class MapsActivity extends FragmentActivity implements OnMapReadyCallback,GoogleMap.OnMyLocationButtonClickListener,ActivityCompat.OnRequestPermissionsResultCallback {
    //定义GoogleMap类型变量mMap，用来存放生成的googleMap
    private GoogleMap mMap;
    //设置一个参数来表示 Permission 请求是否成功
    private boolean mPermissionDenied = false;
    //设置一个参数来检测 GPS 是否开启
    private boolean isGPSOpen;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        /* 以下代码用来初始化地图，确保地图可用 */
        // 初始化 SupportMapFragment
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        //通过异步调用，获取Map可使用通知
        mapFragment.getMapAsync(this);
    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        //设置变量mMap为初始化生成的 googleMap
        mMap = googleMap;
        /* 以下代码用来设置一个 Mark，Mark 生成条件：设置已知的经纬度来定位 */
        //设置一个 LatLng 对象，该类用来存放经纬度：Latitude & Longitude 的缩写
        LatLng sydney = new LatLng(-34, 151);
        //设置一个 MarkerOptions 对象，用来设置标志信息
        MarkerOptions markerOptions = new MarkerOptions();
        //设置点位：采用经纬度定位
        markerOptions.position(sydney);
        //设置 Title 信息，点击 Mark 会显示该信息
        markerOptions.title("Sydney");
        //将 Mark 添加到 Map 中
        mMap.addMarker(markerOptions);
        /*
        简化版代码：
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        */
        /*以下代码用来设置 Map 初始显示的位置，即 Map 显示中心位置*/
        //将定义好的 sydney 设为中心点，可用变量定位也可用经纬度直接定位
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        /* 以下代码通过调用自定义函数来完成定位用户所在位置 */
        //调用函数，实现定位
        findMyLocation();
    }
    /**
     * 该方法通过调实现 MyLocationButton 来获取用户所在的位置
     * */
    private void findMyLocation(){
        //检测权限并设置 Map 中 MyLocationButton 可见
        enableMyLocation();
    }
    /**
     * 该方法用来验证 ACCESS_FINE_LOCATION 权限是否被授予
     * 如果未授予，则提示用户
     * 如果授予，则显示MyLocationButton
     * */
    private void enableMyLocation(){
        //检查是否获得 ACCESS_FINE_LOCATION 权限。
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED){
            mPermissionDenied = true;
            Toast.makeText(this,"缺少权限，无法定位！",Toast.LENGTH_LONG).show();
        }else if(mMap!= null){
            //实现 MyLocationButton 可见
            mMap.setMyLocationEnabled(true);
            //设置 MyLocationButton 监听器
            mMap.setOnMyLocationButtonClickListener(this);
        }
    }
    /**
     * 该方法通过实现 onMyLocationButtonClickListener 接口来获取用户所在位置
     * */
    @Override
    public boolean onMyLocationButtonClick() {
        if(checkGPS())
            Toast.makeText(this,"开始定位!",Toast.LENGTH_LONG).show();
        else
            Toast.makeText(this,"Please open CPS!",Toast.LENGTH_SHORT).show();
        return false;
    }
    @Override
    protected void onResumeFragments() {
        super.onResumeFragments();
        //检测是否已获取权限
        if(mPermissionDenied){
            //permission获取失败，显示erroMessage
            showMissingPermissionError();
            mPermissionDenied = false;
        }
    }
    /**
     * 该方法用来显示permission获取失败时的提示信息
     * */
    private void showMissingPermissionError() {
        Toast.makeText(this,"权限获取失败!",Toast.LENGTH_LONG).show();
    }
    /* 该方法用来检测用户是否打开了GPS　*/
    private boolean checkGPS(){
        //定义 LocationManager 来启用 LOCATION_SERVICE
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        //检查GPS是否打开
        isGPSOpen = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        //返回检测结果
        return isGPSOpen;
    }
}
