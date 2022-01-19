package com.example.analyticskitdemocansu;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.huawei.hms.analytics.HiAnalytics;
import com.huawei.hms.analytics.HiAnalyticsInstance;

public class SettingActivity extends AppCompatActivity {
    private EditText editFavorSport = null;
    private String strFavorSport = null;
    private HiAnalyticsInstance instance;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        instance = HiAnalytics.getInstance(SettingActivity.this);

        Button btnSave = findViewById(R.id.save_setting_button);
        btnSave.setOnClickListener(v -> {
            editFavorSport = findViewById(R.id.edit_favorite_sport);
            strFavorSport = editFavorSport.getText().toString().trim();
            if(!strFavorSport.equals("")) {
                instance.setUserProfile("favor_sport", strFavorSport);
                Toast.makeText(this,
                        "Favorite sport user attribute has been saved: " + strFavorSport, Toast.LENGTH_SHORT).show();
            }
            Intent i = new Intent(SettingActivity.this, MainActivity.class);
            startActivityForResult(i, 0);

        });
    }
}
