package com.example.asynchtaskjava.service;

import android.app.IntentService;
import android.content.Intent;
import android.net.Uri;
import android.nfc.Tag;
import android.util.Log;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.example.asynchtaskjava.model.DataItem;
import com.example.asynchtaskjava.parser.XMLParser;
import com.example.asynchtaskjava.utils.HttpHelper;
import com.google.gson.Gson;

import java.io.IOException;

public class MyService extends IntentService {

    public static final String TAG= "MyService";
    public static final String MY_SERVICE_MESSAGE= "myServiceMessage";
    public static final String MY_SERVICE_PAYLOAD= "myServicePayload";

    public MyService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        Uri uri = intent.getData();
        Log.i(TAG,"OnHandleIntent : "+ uri.toString());
        String response = null;

        try {
             response = HttpHelper.downloadUrl(uri.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }

//        Gson gson = new Gson();
//        DataItem[] dataItems = gson.fromJson(response, DataItem[].class);

        DataItem[] dataItems = XMLParser.parseFeed(response);

        Intent messageIntent= new Intent(MY_SERVICE_MESSAGE);
        messageIntent.putExtra(MY_SERVICE_PAYLOAD,dataItems);
        LocalBroadcastManager localBroadcastManager = LocalBroadcastManager.getInstance(getApplicationContext());
        localBroadcastManager.sendBroadcast(messageIntent);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(TAG,"OnCreate");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i(TAG,"onDestroy");
    }
}
