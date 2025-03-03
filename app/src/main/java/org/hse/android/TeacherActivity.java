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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class TeacherActivity extends AppCompatActivity {
    private static final String TAG = "TeacherActivity";
    private static final String PATTERN = "HH:mm";
    private TextView time, status, subject, cabinet, corp, teacher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher);

        final Spinner spinner = findViewById(R.id.groupList);

        List<StudentActivity.Group> groups = new ArrayList<>();
        initGroupList(groups);

        ArrayAdapter<StudentActivity.Group> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                groups
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                StudentActivity.Group item = (StudentActivity.Group) parent.getItemAtPosition(position);
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

    private void initGroupList(List<StudentActivity.Group> groups) {
        groups.add(new StudentActivity.Group(1, "Преподаватель 1"));
        groups.add(new StudentActivity.Group(2, "Преподаватель 2"));
    }

    private void initTime() {
        var currentTime = new Date();
        Locale russianLocale = new Locale("ru", "RU");
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(PATTERN, russianLocale);
        time.setText(simpleDateFormat.format(currentTime));
    }

    private void initData() {
        status.setText(getString(R.string.lessonStatus));

        subject.setText(getString(R.string.discipline));
        cabinet.setText(getString(R.string.cabinet));
        corp.setText(getString(R.string.building));
        teacher.setText(getString(R.string.teacher));
    }
}
