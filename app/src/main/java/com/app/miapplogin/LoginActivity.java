package com.app.miapplogin;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.widget.Button;

import com.app.miapplogin.MenuActivity;
import com.app.miapplogin.R;

public class LoginActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Button btnLogin = findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(v -> {
            // Navegar al menú principal
            Intent intent = new Intent(this, MenuActivity.class);
            startActivity(intent);
        });
    }
}
