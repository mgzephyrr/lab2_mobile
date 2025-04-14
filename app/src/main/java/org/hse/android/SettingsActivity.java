package org.hse.android;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class SettingsActivity extends AppCompatActivity implements SensorEventListener {
    private SensorManager sensorManager;
    private Sensor light;
    private TextView sensorLight;
    private static final int REQUEST_PERMISSION = 100;
    private ImageView avatarImageView;
    private ActivityResultLauncher<Intent> cameraLauncher;
    private EditText nameEditText;
    private File tempPhotoFile;
    private ListView sensorListView; // список для отображения датчиков

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        avatarImageView = findViewById(R.id.avatarPhoto);
        Button takePhotoBtn = findViewById(R.id.btnTakePhoto);
        Button saveButton = findViewById(R.id.btnSave);
        sensorLight = findViewById(R.id.text_lux);
        nameEditText = findViewById(R.id.getName);

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        light = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);

        // загружаю имя из preferences
        SharedPreferences prefs = getSharedPreferences("settings_prefs", MODE_PRIVATE);
        String savedName = prefs.getString("user_name", "");
        nameEditText.setText(savedName);

        sensorListView = findViewById(R.id.sensorListView);

        takePhotoBtn.setOnClickListener(v -> checkPermissionsAndOpenCamera());

        // сохраняю имя и фото при нажатии "Сохранить"
        saveButton.setOnClickListener(v -> {
            String name = nameEditText.getText().toString();
            SharedPreferences.Editor editor = getSharedPreferences("settings_prefs", MODE_PRIVATE).edit();
            editor.putString("user_name", name);
            editor.apply();

            if (tempPhotoFile != null && tempPhotoFile.exists()) {
                File finalPhoto = getAvatarFile();

                if (finalPhoto.exists()) finalPhoto.delete(); // удалить старый
                boolean success = tempPhotoFile.renameTo(finalPhoto);

                if (success) {
                    loadSavedAvatar();
                } else {
                    Toast.makeText(this, "Ошибка при сохранении фото", Toast.LENGTH_SHORT).show();
                }
            }

            Toast.makeText(this, "Данные сохранены", Toast.LENGTH_SHORT).show();
        });

        cameraLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK) {
                        loadTempAvatar(); // показать, но не сохранять
                    }
                });

        // загрузка аватара
        loadSavedAvatar();

        // загрузка датчиков
        displaySensors();
    }

    private void checkPermissionsAndOpenCamera() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, REQUEST_PERMISSION);
        } else {
            openCamera();
        }
    }

    private void openCamera() {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        tempPhotoFile = new File(getFilesDir(), "temp_avatar.jpg"); // временный аватар (до сохранения)
        Uri photoURI = FileProvider.getUriForFile(this, getPackageName() + ".fileprovider", tempPhotoFile);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);

        cameraLauncher.launch(cameraIntent);
    }
    private void loadTempAvatar() {
        if (tempPhotoFile != null && tempPhotoFile.exists()) {
            Bitmap bitmap = BitmapFactory.decodeFile(tempPhotoFile.getAbsolutePath());
            avatarImageView.setImageBitmap(bitmap);
        }
    }
    private void loadSavedAvatar() {
        File avatar = getAvatarFile();
        if (avatar.exists()) {
            Bitmap bitmap = BitmapFactory.decodeFile(avatar.getAbsolutePath());
            avatarImageView.setImageBitmap(bitmap);
        }
    }

    private File getAvatarFile() {
        return new File(getFilesDir(), "user_avatar.jpg");
    }
    private void displaySensors() {
        // получаю список всех датчиков
        List<Sensor> sensors = sensorManager.getSensorList(Sensor.TYPE_ALL);
        List<String> sensorNames = new ArrayList<>();

        for (Sensor sensor : sensors) {
            sensorNames.add(sensor.getName());
        }

        ArrayAdapter<String> sensorAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, sensorNames);
        sensorListView.setAdapter(sensorAdapter);
    }

    @Override
    public final void onAccuracyChanged(Sensor sensor, int accuracy) {}

    @SuppressLint("DefaultLocale")
    @Override
    public final void onSensorChanged(SensorEvent event) {
        float lux = event.values[0];
        sensorLight.setText(String.format("%s %.2f lux", getString(R.string.currentLux), lux));
    }

    @Override
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(this, light, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }
}
