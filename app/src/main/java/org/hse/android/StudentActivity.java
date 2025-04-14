package org.hse.android;

import android.os.Bundle;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

public class StudentActivity extends BaseActivity {
    private static final String TAG = "StudentActivity";
    private static final List<String> PROGRAMS = List.of("РИС", "МБ", "Ю", "ИЯ");
    private static final List<String> YEARS = List.of("22", "23", "24");
    private static final Integer NUM_GROUPS = 4;

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_student;
    }

    @Override
    protected List<Group> initGroupList() {
        List<Group> groups = new ArrayList<>();
        var id = 1;
        for (int i = 0; i < PROGRAMS.size(); i++) {
            for (int j = 0; j < YEARS.size(); j++) {
                for (int k = 1; k <= NUM_GROUPS; k++) {
                    groups.add(new Group(id++, PROGRAMS.get(i) + "-" + YEARS.get(j) + "-" + k));
                }
            }
        }
        return groups;
    }
}