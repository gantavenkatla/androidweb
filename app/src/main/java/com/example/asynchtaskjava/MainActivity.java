package com.example.asynchtaskjava;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;

import com.example.asynchtaskjava.model.DataItem;
import com.example.asynchtaskjava.service.MyService;
import com.example.asynchtaskjava.utils.NetworkHelper;

import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    TextView output;
    private static final String JSON_URL="http://560057.youcanlearnit.net/services/json/itemsfeed.php";
    private static final String XML_URL="http://560057.youcanlearnit.net/services/xml/itemsfeed.php";

    private boolean isNetworkOk;

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
//            String message = intent.getStringExtra(MyService.MY_SERVICE_PAYLOAD);
//            output.append(message +"\n");

            DataItem[] dataItems = (DataItem[]) intent.getParcelableArrayExtra(MyService.MY_SERVICE_PAYLOAD);

            for(DataItem dataItem : dataItems)
            {
                output.append(dataItem.getItemName() + "\n");
            }


        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        output = (TextView) findViewById(R.id.output);
        LocalBroadcastManager localBroadcastManager = LocalBroadcastManager.getInstance(getApplicationContext());

        localBroadcastManager.registerReceiver(broadcastReceiver, new IntentFilter(MyService.MY_SERVICE_MESSAGE));

        isNetworkOk = NetworkHelper.hasNetworkAccess(getApplicationContext());

        output.append("\n " + "Net Work Ok :  "+ isNetworkOk);

    }

    public void appendValue(View view)
    {
//        MyAsynchClass myAsynchClass = new MyAsynchClass();
//        myAsynchClass.execute("Venkat","Srivani","Rakesh","Hithesh");

        if (isNetworkOk) {
            Intent intent = new Intent(this, MyService.class);
            intent.setData(Uri.parse(XML_URL));
            startService(intent);
        }else {
            Toast.makeText(this,"Network Not available",Toast.LENGTH_SHORT).show();
        }
    }

    public void clearValue(View view)
    {
        output.setText("");
    }


private class MyAsynchClass extends AsyncTask<String,String,Void> {

    @Override
    protected Void doInBackground(String... strings) {

        for(String string: strings)
        {
            publishProgress(string); //This is inbuilt one

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @Override
    protected void onProgressUpdate(String... values) {
        super.onProgressUpdate(values);
        output.append(values[0] +"\n");
    }

    @Override
    protected void onPreExecute() {
        output.append("Ganta ");
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        output.append("\n All are done");
        super.onPostExecute(aVoid);
    }
}

}
