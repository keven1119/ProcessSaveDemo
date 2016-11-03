package com.keven.joyrun.myplugin.services;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;

import com.keven.joyrun.myplugin.AppUtils;
import com.keven.joyrun.myplugin.receivers.GetuiReceiver;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by keven on 16/10/20.
 */

public class DaemonService extends Service implements Handler.Callback{

    private static Handler mHandler;
    private boolean isServiceDestroyed;
    private long lastTime;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;

    }

    @Override
    public void onCreate() {
        Log.d("keven1119", "DaemonService   onCreate  ==>"+ toString());
        super.onCreate();
        isServiceDestroyed = false;
//        if(null == mHandler){
//            mHandler = new Handler(this);
//
//            mHandler.sendEmptyMessageDelayed(1,5000);
//        }

        new Thread(new TcpServer()).start();
    }

    @Override
    public boolean handleMessage(Message msg) {

//        if (!AppUtils.isWorking(DaemonService.this,"com.keven.joyrun.myplugin.services.WorkingService")){
//            AppUtils.wakeupProgress(DaemonService.this);
//            Log.d("keven1119","wakeup BubbleActivity");
//        }
////                    sendBroadcast(new Intent("android.hardware.usb.action.USB_STATE"));
//        mHandler.sendEmptyMessageDelayed(1,5000);
        return false;
    }


    private class TcpServer implements Runnable {
        @Override
        public void run() {
            ServerSocket serverSocket;
            try {
                //监听8688端口
                serverSocket = new ServerSocket(8688);
            } catch (IOException e) {

                return;
            }
            while (!isServiceDestroyed) {
                try {
                    // 接受客户端请求，并且阻塞直到接收到消息
                    final Socket client = serverSocket.accept();
                    new Thread() {
                        @Override
                        public void run() {
                            try {
                                responseClient(client);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }.start();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void responseClient(Socket client) throws IOException {
        // 用于接收客户端消息
        BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
        // 用于向客户端发送消息
        PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(client.getOutputStream())), true);
        out.println("您好，我是服务端");
        while (!isServiceDestroyed) {
            String str = in.readLine();
            Log.i("moon", "收到客户端发来的信息" + str);
            if (TextUtils.isEmpty(str)) {
                //客户端断开了连接
                Log.i("moon", "客户端断开连接");
                break;
            }
            String message = "收到了客户端的信息为：" + str;
            // 从客户端收到的消息加工再发送给客户端
            out.println("working service  你好!!!");

            Log.d("keven1119"," accept ===> "+ str);

            if((System.currentTimeMillis() - lastTime)/1000 > 20) {
                Intent intent = new Intent(DaemonService.this, GetuiReceiver.class);
                intent.setAction("com.igexin.sdk.action.stiWjuGybX5B7HmPkANd4A");
                sendBroadcast(intent);
            }
            lastTime = System.currentTimeMillis();
        }

        out.close();
        in.close();
        client.close();
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        isServiceDestroyed = true;
    }
}
