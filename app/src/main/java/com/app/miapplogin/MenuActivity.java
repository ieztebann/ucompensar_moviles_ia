package com.app.miapplogin;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;

import androidx.core.view.WindowCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.app.miapplogin.databinding.ActivityMenuBinding;

public class MenuActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        findViewById(R.id.btnMap).setOnClickListener(v -> {
            Intent intent = new Intent(this, MapsActivity.class);
            startActivity(intent);
        });

        findViewById(R.id.btnCamera).setOnClickListener(v -> {
            Intent intent = new Intent(this, CameraActivity.class);
            startActivity(intent);
        });

        findViewById(R.id.btnPCita).setOnClickListener(v -> {
            Intent intent = new Intent(this, PDatesActivity2.class);
            startActivity(intent);
        });
        findViewById(R.id.btnGCitas).setOnClickListener(v -> {
            Intent intent = new Intent(this, GDateActivity.class);
            startActivity(intent);
        });
        findViewById(R.id.btnCita).setOnClickListener(v -> {
            Intent intent = new Intent(this, DatesActivity.class);
            startActivity(intent);
        });
    }
}
