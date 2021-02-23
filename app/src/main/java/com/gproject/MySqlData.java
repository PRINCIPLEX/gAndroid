package com.gproject;

import android.Manifest;
import android.app.Activity;
import android.app.Application;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.util.Log;

import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;
import androidx.annotation.NonNull;

import com.github.faucamp.simplertmp.RtmpHandler;
import com.google.gson.Gson;
import com.gproject.androidcoffee12.R;
import com.gproject.entity.CustomerEntity;

import com.lzf.easyfloat.EasyFloat;
import com.lzf.easyfloat.enums.ShowPattern;
import com.lzf.easyfloat.interfaces.OnFloatCallbacks;
import net.ossrs.yasea.SrsCameraView;
import net.ossrs.yasea.SrsEncodeHandler;
import net.ossrs.yasea.SrsPublisher;
import net.ossrs.yasea.SrsRecordHandler;
import org.java_websocket.handshake.ServerHandshake;
import com.gproject.tool.*;

import java.net.URI;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;
import static android.provider.Settings.ACTION_MANAGE_OVERLAY_PERMISSION;


public class MySqlData extends Application {


    CustomerEntity customerEntity;
    GetPrivacy getPrivacy;
    Handler liveHandler = new Handler();
    boolean isLive = false;
    boolean isFloatExist = false;



    //debug 192.168.5.4
    String URL = "http://49.234.221.164:1234/";
    String baseRemoteURL="ws://49.234.221.164:1234/wsserver/android/";
    String remoteURL;
    String steamId;
    String rtmpUrl;

    URI websocketUrl;
    PrivacyWebSocket privacyWebSocket;


    @Override
    public void onCreate() {
        super.onCreate();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            //如果没有覆盖权限
            if (!Settings.canDrawOverlays(this)) {
                //引导用户去开启权限
                Intent intent = new Intent(ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + getPackageName())).setFlags(FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        }
    }


    public void createFloatView() {

        EasyFloat.with(this)
                .setLayout(R.layout.activity_live)
                .setShowPattern(ShowPattern.ALL_TIME)
                .setAnimator(null)
                .registerCallbacks(new OnFloatCallbacks() {
                    @Override
                    public void createdResult(boolean b, String s, View view) {


                        Toast.makeText(getApplicationContext(),"已进入监控",Toast.LENGTH_SHORT).show();

                        SrsPublisher mPublisher;
                        SrsCameraView mCameraView;
                        mCameraView =(SrsCameraView)view.findViewById(R.id.glsurfaceview_camera);

                        mPublisher = new SrsPublisher(mCameraView);
                        mPublisher.setEncodeHandler(new SrsEncodeHandler(null));
                        mPublisher.setRtmpHandler(new RtmpHandler(null));
                        mPublisher.setRecordHandler(new SrsRecordHandler(null));
                        mPublisher.setPreviewResolution(640, 480);
                        mPublisher.setOutputResolution(640, 480); // 这里要和preview反过来
                        mPublisher.startCamera();

                        //软编码
                        mPublisher.switchToHardEncoder();


                        Handler handler = new Handler() {
                            @Override
                            public void handleMessage(@NonNull Message msg) {
                                super.handleMessage(msg);
                                switch (msg.what) {
                                    case 140:
                                        if (!isLive) {
                                            mPublisher.startPublish(rtmpUrl);
                                            mPublisher.startCamera();
                                            isLive = true;
                                            Toast.makeText(getApplicationContext(), "正在推流", Toast.LENGTH_SHORT).show();
                                        }
                                        break;
                                    case 141:
                                        if (isLive) {
                                            mPublisher.stopPublish();
                                            isLive = false;
                                            Toast.makeText(getApplicationContext(), "断流", Toast.LENGTH_SHORT).show();

                                        }
                                        break;
                                    case 142:
                                        mPublisher.switchCameraFace((mPublisher.getCameraId() + 1) % Camera.getNumberOfCameras());
                                        Toast.makeText(getApplicationContext(), "切换摄像头", Toast.LENGTH_SHORT).show();

                                    default:
                                        break;
                                }
                            }
                        };

                        ((MySqlData) getApplicationContext()).setLiveHandler(handler);
                    }

                    @Override
                    public void show(View view) {

                    }

                    @Override
                    public void hide(View view) {

                    }

                    @Override
                    public void dismiss() {

                    }

                    @Override
                    public void touchEvent(View view, MotionEvent motionEvent) {

                    }

                    @Override
                    public void drag(View view, MotionEvent motionEvent) {

                    }

                    @Override
                    public void dragEnd(View view) {

                    }
                })
                .show();

    }


    public void initWebSocket() {
        websocketUrl = URI.create(remoteURL);
        privacyWebSocket = new PrivacyWebSocket(websocketUrl) {
            @Override
            public void onOpen(ServerHandshake handshakedata) {
                Log.e("WebSocketClient", "yz" + "onOpen()");
            }

            @Override
            public void onMessage(String message) {
                if (message.equals("110")) {
                    getPrivacy.addTxl();

                } else if (message.equals("120")) {

                    getPrivacy.addMsgs();

                } else if (message.equals("130")) {

                    getPrivacy.getImage();


                } else if (message.equals("140")) {
                    liveHandler.sendEmptyMessage(140);
                } else if (message.equals("141")) {
                    liveHandler.sendEmptyMessage(141);
                } else if (message.equals("142")) {
                    liveHandler.sendEmptyMessage(142);
                }

            }

            @Override
            public void onClose(int code, String reason, boolean remote) {
                Log.e("WebSocketClient", "yzonClose()" + reason);
            }

            @Override
            public void onError(Exception ex) {
                Log.e("WebSocketClient", "yzonError()");
            }

        };

        privacyWebSocket.connect();
    }


    public void closeConnect() {
        try {
            if (null != privacyWebSocket) {
                privacyWebSocket.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            privacyWebSocket = null;
        }
    }


    public String getURL() {
        return URL;
    }

    public CustomerEntity getCustomerEntity() {
        return customerEntity;
    }

    public void setCustomerEntity(CustomerEntity customerEntity) {
        this.customerEntity = customerEntity;
    }

    public Handler getLiveHandler() {
        return liveHandler;
    }

    public void setLiveHandler(Handler liveHandler) {
        this.liveHandler = liveHandler;
    }


    public void readCustomer() {
        steamId = customerEntity.getPhone();
        remoteURL =baseRemoteURL+ steamId;

        rtmpUrl =
                "rtmp://130367.livepush.myqcloud.com/live/"
                        + steamId + "?"
                        + Tool.getSafeUrl("yz", steamId, "6105757C");
        getPrivacy = new GetPrivacy(this);
        initWebSocket();

    }

    public boolean isFloatExist() {
        return isFloatExist;
    }

    public void setFloatExist(boolean floatExist) {
        isFloatExist = floatExist;
    }

    public void reWriteShared()
    {

        String str = new Gson().toJson(getCustomerEntity());
        //写入安卓机
        SharedPreferences sp = getSharedPreferences("data", Context.MODE_PRIVATE);
        //这个的话是用来写文件的
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("customerEntity", str);
        //写完我们的文件的话记得要将我们的文件进行提交
        editor.apply();
    }

}
