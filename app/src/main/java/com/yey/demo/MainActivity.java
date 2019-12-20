package com.yey.demo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ScrollView;

import com.yey.qindexy.QuickIndexY;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getName();
    private Button mBtn;
    private QuickIndexY quickIndexY;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        quickIndexY = (QuickIndexY) findViewById(R.id.qiy);
        mBtn = (Button) findViewById(R.id.btn);
        quickIndexY.setWords(new String[]{"1","2","3","4","5","6","7","8","9","10"});
        quickIndexY.setOnQySelectIndex(new QuickIndexY.OnQySelectIndex() {
            @Override
            public void index(int index, String word) {
                Log.e(TAG, "index " + index + " word " + word);
            }
        });
        mBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                quickIndexY.setWords(new String[]{"12","14","89","20","98","65","73","85","98","110"});
            }
        });

    }
}
