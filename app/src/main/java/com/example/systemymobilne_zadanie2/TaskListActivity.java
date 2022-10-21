package com.example.systemymobilne_zadanie2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;

import java.util.UUID;

public class TaskListActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {
//        //FragmentManager fragmentManager = getSupportFragmentManager();
//        Fragment fragment = fragmentManager.findFragmentById(R.id.fragment_container);
//        //UUID taskId = (UUID) getIntent().getSerializableExtra(TaskListFragment.KEY_EXTRA_TASK_ID);
//        return fragment;

        return new TaskListFragment();
    }
}