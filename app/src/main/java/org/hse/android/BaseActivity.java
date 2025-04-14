package org.hse.android;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public abstract class BaseActivity extends AppCompatActivity {
    protected static final String PATTERN = "HH:mm, EEEE";
    protected static final String TAG = "BaseActivity";
    protected TextView time, status, subject, cabinet, corp, teacher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResourceId());

        final Spinner spinner = findViewById(R.id.groupList);

        List<?> groups = initGroupList();
        ArrayAdapter<?> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, groups);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Object item = parent.getItemAtPosition(position);
                Log.d(TAG, "selectedItem: " + item.toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                //
            }
        });

        time = findViewById(R.id.timeValue);
        status = findViewById(R.id.status);
        subject = findViewById(R.id.discipline);
        cabinet = findViewById(R.id.cabinet);
        corp = findViewById(R.id.building);
        teacher = findViewById(R.id.teacher);

        initTime();
        initData();
    }

    protected abstract int getLayoutResourceId();

    protected abstract List<?> initGroupList();

    protected void initTime() {
        var currentTime = new Date();
        Locale russianLocale = new Locale("ru", "RU");
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(PATTERN, russianLocale);

        String formattedTime = simpleDateFormat.format(currentTime);
        String[] parts = formattedTime.split(", ");
        if (parts.length == 2) {
            String timePart = parts[0];
            String dayPart = parts[1];

            if (!dayPart.isEmpty()) {
                dayPart = dayPart.substring(0, 1).toUpperCase() + dayPart.substring(1);
            }

            formattedTime = timePart + ", " + dayPart;
        }
        time.setText(formattedTime);
    }

    protected void initData() {
        status.setText(getString(R.string.lessonStatus));
        subject.setText(getString(R.string.discipline));
        cabinet.setText(getString(R.string.cabinet));
        corp.setText(getString(R.string.building));
        teacher.setText(getString(R.string.teacher));
    }
}