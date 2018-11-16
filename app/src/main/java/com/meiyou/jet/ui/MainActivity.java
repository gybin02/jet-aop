package com.jet.jet.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.jet.jet.annotation.JFindView;
import com.jet.jet.annotation.JFindViewOnClick;
import com.jet.jet.annotation.JLogMethod;
import com.jet.jet.annotation.JLogTime;
import com.jet.jet.annotation.JTryCatch;
import com.jet.jet.process.Jet;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @JFindView(R.id.btn_findView)
    Button btnHello;

    @JFindViewOnClick(R.id.btn_findview_onclick)
    Button btnWorld;

    String temp = "";

    @JFindViewOnClick(R.id.btn_implement)
    Button btn_implement;

    @JFindViewOnClick(R.id.btn_log)
    Button btn_log;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Jet.bind(this);

    }


    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.btn_findView:
                Toast.makeText(this, "Btn Hello1", Toast.LENGTH_SHORT).show();
                break;
            case R.id.btn_findview_onclick:
                Toast.makeText(this, "To Intent", Toast.LENGTH_SHORT).show();
                break;
            case R.id.btn_implement:
                break;
            case R.id.btn_log:
                testLog(10);
//                testAOP();
                break;
        }

    }

    @JTryCatch
    @JLogTime
    @JLogMethod
    public int testLog(int k) {
        int i = k + 100;
        int j = i++;
        j=j/0;
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return j;
    }


    public void testAOP() {
        Log.d("xuyisheng", "testAOP");
    }
}
