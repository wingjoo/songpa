package com.example.wing.workingsongpa.CourseList;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.example.wing.workingsongpa.R;

public class DetailCourseListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_course_list);

        Intent intent = getIntent();
        String msg = intent.getStringExtra(CourseListFlagment.EXTRA_MESSAGE);

        TextView textView = (TextView)findViewById(R.id.textView);
        textView.setText(msg);
    }
}
