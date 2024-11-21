package com.app.miapplogin;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import android.content.Intent; // Importar Intent
import android.util.Log;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    static final int REQUEST_IMAGE_CAPTURE = 1;
    private ImageView imageView;
    private Uri photoURI;
    private File photoFile;
    private EditText editTextResponse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imageView = findViewById(R.id.imageView);
        editTextResponse = findViewById(R.id.editTextResponse);

        Button takePictureButton = findViewById(R.id.button);
        takePictureButton.setOnClickListener(v -> tomarFoto());
    }

    private void tomarFoto() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            try {
                photoFile = crearArchivoImg();
            } catch (IOException ex) {
                Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
            }

            if (photoFile != null) {
                photoURI = FileProvider.getUriForFile(this, "com.example.mycameraapp.fileprovider", photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        }
    }

    private File crearArchivoImg() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_DCIM);
        return File.createTempFile(
                imageFileName,  /* prefix */
                ".jpeg",         /* suffix */
                storageDir      /* directory */
        );
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            imageView.setImageURI(photoURI);
            // Aquí subes la imagen después de tomar la foto
            subirImagen(photoFile);
        }
    }

    private void subirImagen(File file) {
        OkHttpClient client = new OkHttpClient();

        // Construimos el cuerpo de la petición con la imagen
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("file", file.getName(),
                        RequestBody.create(file, MediaType.parse("image/jpeg")))
                .build();

        // Crear la solicitud POST
        Request request = new Request.Builder()
                .url("http://154.12.227.105:8000/predict") // Cambia esto a tu URL real
                .post(requestBody)
                .build();

        // Hacemos la petición de forma asíncrona
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace(); // Esto imprime el stack trace en la consola
                String errorMessage = "Error al subir la imagen: " + e.getMessage(); // Captura el mensaje de error
                runOnUiThread(() ->
                        Toast.makeText(MainActivity.this, errorMessage, Toast.LENGTH_SHORT).show() // Muestra el error en un Toast
                );
                Log.d("MainActivity", errorMessage);

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    final String responseBody = response.body().string();
                    runOnUiThread(() -> {
                        editTextResponse.setText(responseBody); // Mostrar la respuesta en el EditText
                    });
                } else {
                    runOnUiThread(() ->
                            Toast.makeText(MainActivity.this, "Error en la respuesta", Toast.LENGTH_SHORT).show()
                    );
                }
            }
        });
    }
}
