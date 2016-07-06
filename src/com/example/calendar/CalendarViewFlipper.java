package com.example.calendar;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.AttributeSet;
import android.view.*;
import android.view.animation.AnimationUtils;
import android.widget.*;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Administrator on 2016/7/5.
 */
public class CalendarViewFlipper extends FrameLayout implements View.OnClickListener{
    private GestureDetector gestureDetector;
    private CalendarAdapter calV;
    private ViewFlipper flipper;
    private GridView gridView;
    private static int jumpMonth = 0; // 每次滑动，增加或减去一个月,默认为0（即显示当前月）
    private int year_c = 0;
    private int month_c = 0;
    private int day_c = 0;
    /** 当前的年月，现在日历顶端 */
    private TextView currentMonth;
    /** 上个月 */
    private ImageView prevMonth;
    /** 下个月 */
    private ImageView nextMonth;
    /**每个月标记的行程,key是月份，value是当前月份中的日期，如key=2（二月），value=list{2,6,9,12,24}**/
    private HashMap<String,List<String>> map;

    public CalendarViewFlipper(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public CalendarViewFlipper(Context context) {
        super(context);
        init(context);
    }

    private void init(Context context){
        LayoutInflater inflater = LayoutInflater.from(context);
        inflater.inflate(R.layout.calendar_viewflipper,this,true);

        currentMonth = (TextView) findViewById(R.id.currentMonth);
        prevMonth = (ImageView) findViewById(R.id.prevMonth);
        nextMonth = (ImageView) findViewById(R.id.nextMonth);
        flipper = (ViewFlipper) findViewById(R.id.flipper);
        flipper.removeAllViews();

        setListener();
    }

    public void initCurrentDate(int curYear,int curMonth,int curDay,HashMap<String,List<String>> sign){
        year_c = curYear;
        month_c = curMonth;
        day_c = curDay;
        map = sign;

        gestureDetector = new GestureDetector(getContext(), new MyGestureListener());
        List<String> signDays = null;
        if(map!= null){
            signDays = map.get(String.valueOf(curMonth));
        }
        calV = new CalendarAdapter(getContext(), jumpMonth, year_c, month_c, day_c,signDays);
        addGridView();
        gridView.setAdapter(calV);
        flipper.addView(gridView, 0);
        addTextToTopTextView(currentMonth);
    }

    private class MyGestureListener extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            if (e1.getX() - e2.getX() > 120) {
                // 像左滑动
                enterNextMonth();
                return true;
            } else if (e1.getX() - e2.getX() < -120) {
                // 向右滑动
                enterPrevMonth();
                return true;
            }
            return false;
        }
    }

    /**
     * 移动到下一个月
     */
    private void enterNextMonth() {
        addGridView(); // 添加一个gridView
        jumpMonth++; // 下一个月
        List<String> signDays = null;
        if(map!= null){
            signDays = map.get(String.valueOf(month_c+jumpMonth));
        }
        calV = new CalendarAdapter(getContext(),jumpMonth, year_c, month_c, day_c,signDays);
        gridView.setAdapter(calV);
        addTextToTopTextView(currentMonth); // 移动到下一月后，将当月显示在头标题中
        flipper.addView(gridView, 1);
        flipper.setInAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.push_left_in));
        flipper.setOutAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.push_left_out));
        flipper.showNext();
        flipper.removeViewAt(0);
    }

    /**
     * 移动到上一个月
     */
    private void enterPrevMonth() {
        addGridView(); // 添加一个gridView
        jumpMonth--; // 上一个月
        List<String> signDays = null;
        if(map!= null){
            signDays = map.get(String.valueOf(month_c+jumpMonth));
        }
        calV = new CalendarAdapter(getContext(),jumpMonth, year_c, month_c, day_c,signDays);
        gridView.setAdapter(calV);
        addTextToTopTextView(currentMonth); // 移动到上一月后，将当月显示在头标题中
        flipper.addView(gridView, 1);

        flipper.setInAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.push_right_in));
        flipper.setOutAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.push_right_out));
        flipper.showPrevious();
        flipper.removeViewAt(0);
    }

    /**
     * 添加头部的年份 闰哪月等信息
     *
     * @param view
     */
    public void addTextToTopTextView(TextView view) {
        StringBuffer textDate = new StringBuffer();
        // draw = getResources().getDrawable(R.drawable.top_day);
        // view.setBackgroundDrawable(draw);
        textDate.append(calV.getShowYear()).append("年").append(calV.getShowMonth()).append("月").append("\t");
        view.setText(textDate);
    }

    private void addGridView() {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT, AbsListView.LayoutParams.MATCH_PARENT);
        // 取得屏幕的宽度和高度
        WindowManager windowManager = ((Activity)getContext()).getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        int Width = display.getWidth();
        int Height = display.getHeight();

        gridView = new GridView(getContext());
        gridView.setNumColumns(7);
        gridView.setColumnWidth(40);
        // gridView.setStretchMode(GridView.STRETCH_COLUMN_WIDTH);
        if (Width == 720 && Height == 1280) {
            gridView.setColumnWidth(40);
        }
        gridView.setGravity(Gravity.CENTER_VERTICAL);
        gridView.setSelector(new ColorDrawable(Color.TRANSPARENT));
        // 去除gridView边框
        gridView.setVerticalSpacing(1);
        gridView.setHorizontalSpacing(1);
        gridView.setOnTouchListener(new OnTouchListener() {
            // 将gridview中的触摸事件回传给gestureDetector

            public boolean onTouch(View v, MotionEvent event) {
                // TODO Auto-generated method stub
                return gestureDetector.onTouchEvent(event);
            }
        });

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                // TODO Auto-generated method stub
                // 点击任何一个item，得到这个item的日期(排除点击的是周日到周六(点击不响应))
                int startPosition = calV.getStartPositon();
                int endPosition = calV.getEndPosition();
                if (startPosition <= position + 7 && position <= endPosition - 7) {
                    String scheduleDay = calV.getDateByClickItem(position).split("\\.")[0]; // 这一天的阳历
                    // String scheduleLunarDay =
                    // calV.getDateByClickItem(position).split("\\.")[1];
                    // //这一天的阴历
                    String scheduleYear = calV.getShowYear();
                    String scheduleMonth = calV.getShowMonth();
                    Toast.makeText(getContext(), scheduleYear + "-" + scheduleMonth + "-" + scheduleDay, Toast.LENGTH_LONG).show();
                    // Toast.makeText(CalendarActivity.this, "点击了该条目",
                    // Toast.LENGTH_SHORT).show();
                }
            }
        });
        gridView.setLayoutParams(params);
    }

    private void setListener() {
        prevMonth.setOnClickListener(this);
        nextMonth.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.nextMonth: // 下一个月
                enterNextMonth();
                break;
            case R.id.prevMonth: // 上一个月
                enterPrevMonth();
                break;
        }
    }
}
