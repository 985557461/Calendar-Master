package com.example.calendar;

import android.app.Activity;
import android.os.Bundle;

import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * 日历显示activity
 * 
 * @author Vincent Lee
 * 
 */
public class CalendarActivity extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.calendar_activity);

        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-M-d");
        CalendarViewFlipper calendarViewFlipper = (CalendarViewFlipper) findViewById(R.id.calendarViewFlipper);
        calendarViewFlipper.initCurrentDate(sdf.format(date));// 当期日期
	}
}