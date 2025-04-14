package org.hse.android;

import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

public class TeacherActivity extends BaseActivity {
    private static final String TAG = "TeacherActivity";

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_teacher;
    }

    @Override
    protected List<Group> initGroupList() {
        List<Group> groups = new ArrayList<>();
        groups.add(new Group(1, "Преподаватель 1"));
        groups.add(new Group(2, "Преподаватель 2"));
        return groups;
    }
}