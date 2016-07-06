package com.example.calendar;

import android.app.Activity;
import android.os.Bundle;

import java.util.*;


/**
 * 日历显示activity
 * 
 * @author Vincent Lee
 * 
 */
public class CalendarActivity extends Activity {
    private HashMap<String,List<String>> map = new HashMap<String, List<String>>();
    private Random random = new Random();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.calendar_activity);

        //初始化map，map中的key是当前年的月份，value是当前月请假的日子的day
        for(int i=1;i<13;i++){
            if(map.get(String.valueOf(i)) != null){
                for(int j=0;j<5;j++){
                    map.get(String.valueOf(i)).add(String.valueOf(random.nextInt(20)));
                }
            }else{
                List<String> result = new ArrayList<String>();
                for(int j=0;j<5;j++){
                    result.add(String.valueOf(random.nextInt(20)));
                }
                map.put(String.valueOf(i),result);
            }
        }

        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH)+1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        CalendarViewFlipper calendarViewFlipper = (CalendarViewFlipper) findViewById(R.id.calendarViewFlipper);
        calendarViewFlipper.initCurrentDate(year,month,day,map);// 当期日期
	}
}