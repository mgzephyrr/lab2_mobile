package org.hse.android;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class StudentActivity extends AppCompatActivity {
    private static final String TAG = "StudentActivity";
    private static final String PATTERN = "HH:mm, EEEE";
    private static final List<String> programs = List.of("РИС", "МБ", "Ю", "ИЯ");
    private static final List<String> years = List.of("22", "23", "24");
    private static final Integer numGroups = 4;
    private TextView time, status, subject, cabinet, corp, teacher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student);

        final Spinner spinner = findViewById(R.id.groupList);

        List<Group> groups = new ArrayList<>();
        initGroupList(groups);

        ArrayAdapter<Group> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, groups);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Group item = (Group) parent.getItemAtPosition(position);
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

    private void initGroupList(List<Group> groups) {
        var id = 1;
        for (int i = 0; i < programs.size(); i++){
            for (int j = 0; j < years.size(); j++){
                for (int k = 1; k <= numGroups; k++){
                    groups.add(new Group(id++, programs.get(i) + "-" + years.get(j) + "-" + k));
                }
            }
        }
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

    static class Group {
        private Integer id;
        private String name;

        public Group(Integer id, String name) {
            this.id = id;
            this.name = name;
        }

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        @NonNull
        @Override
        public String toString() {
            return name;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}


