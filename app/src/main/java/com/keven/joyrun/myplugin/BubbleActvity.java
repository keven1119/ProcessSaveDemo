package com.keven.joyrun.myplugin;

import android.Manifest;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.GpsSatellite;
import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.keven.joyrun.myplugin.services.AService;
import com.keven.joyrun.myplugin.services.BService;
import com.keven.joyrun.myplugin.services.DaemonService;
import com.keven.joyrun.myplugin.services.WorkingService;
import com.keven.joyrun.myplugin.widget.bubble.BubbleDrawer;
import com.keven.joyrun.myplugin.widget.bubble.FloatBubbleView;

import java.util.Iterator;

import rx.AsyncEmitter;
import rx.Observable;
import rx.functions.Action1;
import com.igexin.sdk.PushManager;


/**
 * Created by keven on 16/10/8.
 */

public class BubbleActvity extends AppCompatActivity implements View.OnClickListener, LocationListener, GpsStatus.Listener {

    private FloatBubbleView mDWView;
    Button button;
    LocationManager locationManager;
    private final static String TAG = BubbleActvity.class.getSimpleName();
    ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.d("keven1119", "onServiceConnected");
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("keven1119","BubbleActvity   onCreate");
        setContentView(R.layout.activity_buble);
        PushManager.getInstance().initialize(this.getApplicationContext());
//        mDWView = (FloatBubbleView) findViewById(R.id.bubble_surfaceview);
        button = (Button) findViewById(R.id.button_status);
        button.setOnClickListener(this);
//        initData();

//        Observable.fromAsync(new Action1<AsyncEmitter<Object>>() {
//            @Override
//            public void call(AsyncEmitter<Object> asyncEmitter) {
//                Log.d("keven1119", "fromAsync  work ");
//            }
//        }, AsyncEmitter.BackpressureMode.BUFFER)
//                .subscribe();

//        Intent intentA = new Intent(this, AService.class);
//        startService(intentA);
//
//        Intent intentB = new Intent(this, BService.class);
//        startService(intentB);

        startService(new Intent(this, WorkingService.class));
        if(!AppUtils.isWorking(this,"com.keven.joyrun.myplugin.services.DaemonService")) {
            bindService(new Intent(this, DaemonService.class), mServiceConnection , BIND_AUTO_CREATE);
            Log.d("keven1119", " DaemonService  启动");
        }


        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        locationManager.addGpsStatusListener(this);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2000, 8, this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(mServiceConnection);
    }

    /**
     * 初始化Data
     */
    private void initData() {
        //设置气泡绘制者
        BubbleDrawer bubbleDrawer = new BubbleDrawer(this);
        //设置渐变背景 如果不需要渐变 设置相同颜色即可
        bubbleDrawer.setBackgroundGradient(new int[]{0xffffffff, 0xffffffff});
        //给SurfaceView设置一个绘制者
        mDWView.setDrawer(bubbleDrawer);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.button_status) {
            button.setBackgroundColor(Color.RED);
        }
    }

    /**
     * 位置信息变化时触发
     */
    public void onLocationChanged(Location location) {
        updateView(location);
        Log.i(TAG, "时间：" + location.getTime());
        Log.i(TAG, "经度：" + location.getLongitude());
        Log.i(TAG, "纬度：" + location.getLatitude());
        Log.i(TAG, "海拔：" + location.getAltitude());
    }

    private void updateView(Location location) {
    }

    /**
     * GPS状态变化时触发
     */
    public void onStatusChanged(String provider, int status, Bundle extras) {
        switch (status) {
            //GPS状态为可见时
            case LocationProvider.AVAILABLE:
                Log.i(TAG, "当前GPS状态为可见状态");
                break;
            //GPS状态为服务区外时
            case LocationProvider.OUT_OF_SERVICE:
                Log.i(TAG, "当前GPS状态为服务区外状态");
                break;
            //GPS状态为暂停服务时
            case LocationProvider.TEMPORARILY_UNAVAILABLE:
                Log.i(TAG, "当前GPS状态为暂停服务状态");
                break;
        }
    }

    /**
     * GPS开启时触发
     */
    public void onProviderEnabled(String provider) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Location location = locationManager.getLastKnownLocation(provider);
        updateView(location);
    }

    /**
     * GPS禁用时触发
     */
    public void onProviderDisabled(String provider) {
        updateView(null);
    }

    @Override
    public void onGpsStatusChanged(int event) {
        switch (event) {
            //第一次定位
            case GpsStatus.GPS_EVENT_FIRST_FIX:
                Log.i(TAG, "第一次定位");
                break;
            //卫星状态改变
            case GpsStatus.GPS_EVENT_SATELLITE_STATUS:
                Log.i(TAG, "卫星状态改变");
                //获取当前状态
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                GpsStatus gpsStatus = locationManager.getGpsStatus(null);
                //获取卫星颗数的默认最大值
                int maxSatellites = gpsStatus.getMaxSatellites();
                //创建一个迭代器保存所有卫星
                Iterator<GpsSatellite> iters = gpsStatus.getSatellites().iterator();
                int count = 0;
                while (iters.hasNext() && count <= maxSatellites) {
                    GpsSatellite s = iters.next();
                    count++;
                }
                System.out.println("搜索到："+count+"颗卫星");
                break;
            //定位启动
            case GpsStatus.GPS_EVENT_STARTED:
                Log.i(TAG, "定位启动");
                break;
            //定位结束
            case GpsStatus.GPS_EVENT_STOPPED:
                Log.i(TAG, "定位结束");
                break;
        }
    }
}
