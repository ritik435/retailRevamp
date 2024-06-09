package com.example.retailrevamp.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Build;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.retailrevamp.Activity.HomepageActivity;
import com.example.retailrevamp.Constant.Tags;
import com.example.retailrevamp.Model.StatsticsInfoModel;
import com.example.retailrevamp.R;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class StatsticsInfoAdapter extends RecyclerView.Adapter<StatsticsInfoAdapter.StatsticsInfoViewHolder> {

    private static final String TAG = "StatsticsInfoAdapter";
    Context mContext;
    public HashMap<String, List<Integer>> hmStatsInfo;
    Timestamp dailyPickerDate;
    String dailyPickerDateString;
    Timestamp monthlyStartPickerDate, monthlyEndPickerDate;
    String monthlyStartPickerDateString, monthlyEndPickerDateString;
    SimpleDateFormat dateFormat = new SimpleDateFormat(Tags.DATE_FORMAT);
    public StatsticsInfoAdapter(HashMap<String, List<Integer>> hmStatsInfo, Context mContext) {
        this.mContext = mContext;
        this.hmStatsInfo = hmStatsInfo;
    }

    @NonNull
    @Override
    public StatsticsInfoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_statstics_info, parent, false);
        return new StatsticsInfoViewHolder(view);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onBindViewHolder(@NonNull StatsticsInfoViewHolder holder, int position) {
        Log.d(TAG, "onBindViewHolder: " + position);
        Log.d(TAG, "onBindViewHolder: check the hashmap hmStatsInfo.get(\"CurrentDate\"): ");
//        StatsticsInfoModel statsticsInfoModel=statsticsInfoModelList.get(position);
        if (position % 4 == 0) {
            holder.txtStatsTitle.setText(Tags.STRING_DAILY_STATS);
            //find the current date
            //send the start date and end date
            Log.d(TAG, "onBindViewHolder: DAILY STATS: ");
            dailyPickerDate = new Timestamp(System.currentTimeMillis());
            dailyPickerDateString = dateFormat.format(dailyPickerDate);
            Log.d(TAG, "onBindViewHolder: check the hashmap hmStatsInfo.get(\"CurrentDate\"): "+dailyPickerDateString+" : "+hmStatsInfo.get(dailyPickerDateString));
            List<String> stats=calculateSales(dailyPickerDate, dailyPickerDate);
            String totalSales=stats.get(0);
            String profitLoss=stats.get(1);
            Log.d(TAG, "onBindViewHolder: totalSalessss : "+totalSales);
            holder.txtTotalSales.setText(totalSales);
            holder.txtBottomline.setText(profitLoss);
            holder.txtRange.setText(dailyPickerDateString);

            Timestamp currentDate=new Timestamp(System.currentTimeMillis());
            String currentDateString = dateFormat.format(currentDate);
            Log.d(TAG, "onTouch1: "+dailyPickerDate + " :: "+currentDate);
            if (dailyPickerDateString.equals(currentDateString)){
                holder.ivNextDate.setVisibility(View.GONE);
            }else {
                holder.ivNextDate.setVisibility(View.VISIBLE);
            }

            holder.ivPreviousDate.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                        holder.ivPreviousDate.setAlpha(0.5f);
                    } else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                        holder.ivPreviousDate.setAlpha(1f);
                        // Create a Calendar instance and set it to the timestamp
                        Calendar calendar = Calendar.getInstance();
                        calendar.setTimeInMillis(dailyPickerDate.getTime());

                        // Subtract one day
                        calendar.add(Calendar.DAY_OF_MONTH, -1);
                        dailyPickerDate = new Timestamp(calendar.getTimeInMillis());
                        dailyPickerDateString = dateFormat.format(dailyPickerDate);

                        List<String> stats=calculateSales(dailyPickerDate, dailyPickerDate);
                        String totalSales=stats.get(0);
                        String profitLoss=stats.get(1);
                        Log.d(TAG, "onBindViewHolder: totalSalessss : "+totalSales);
                        holder.txtTotalSales.setText(totalSales);
                        holder.txtBottomline.setText(profitLoss);
                        holder.txtRange.setText(dailyPickerDateString);

                        Log.d(TAG, "onTouch2: "+dailyPickerDate + " :: "+currentDate);
                        if (dailyPickerDateString.equals(currentDateString)){
                            holder.ivNextDate.setVisibility(View.GONE);
                        }else {
                            holder.ivNextDate.setVisibility(View.VISIBLE);
                        }
                    }
                    return true;
                }
            });
            holder.ivNextDate.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                        holder.ivNextDate.setAlpha(0.5f);
                    } else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                        holder.ivNextDate.setAlpha(1f);

                        // Create a Calendar instance and set it to the timestamp
                        Calendar calendar = Calendar.getInstance();
                        calendar.setTimeInMillis(dailyPickerDate.getTime());

                        // Subtract one day
                        calendar.add(Calendar.DAY_OF_MONTH, +1);
                        dailyPickerDate = new Timestamp(calendar.getTimeInMillis());
                        dailyPickerDateString = dateFormat.format(dailyPickerDate);

                        List<String> stats=calculateSales(dailyPickerDate, dailyPickerDate);
                        String totalSales=stats.get(0);
                        String profitLoss=stats.get(1);
                        Log.d(TAG, "onBindViewHolder: totalSalessss : "+totalSales);
                        holder.txtTotalSales.setText(totalSales);
                        holder.txtBottomline.setText(profitLoss);
                        holder.txtRange.setText(dailyPickerDateString);

                        Log.d(TAG, "onTouch3: "+dailyPickerDate + " :: "+currentDate);
                        if (dailyPickerDateString.equals(currentDateString)){
                            holder.ivNextDate.setVisibility(View.GONE);
                        }else {
                            holder.ivNextDate.setVisibility(View.VISIBLE);
                        }
                    }
                    return true;
                }
            });
        }


        else if (position % 4 == 1) {
            Log.d(TAG, "onBindViewHolder: MONTHLY STATS: ");
            holder.txtStatsTitle.setText(Tags.STRING_MONTHLY_STATS);
//            holder.txtTotalSales.setText("1000");
//            holder.txtBottomline.setText("100");

            // Example timestamp
            monthlyEndPickerDate = new Timestamp(System.currentTimeMillis());
            monthlyEndPickerDateString = dateFormat.format(monthlyEndPickerDate);
            // Create a Calendar instance
            Calendar calendar = Calendar.getInstance();

            // Set the timestamp to the Calendar instance
            calendar.setTime(monthlyEndPickerDate);

            // Set the day of the month to 1
            calendar.set(Calendar.DAY_OF_MONTH, 1);

            // Get the first day of the month
            monthlyStartPickerDate = new Timestamp(calendar.getTimeInMillis());
            monthlyStartPickerDateString = dateFormat.format(monthlyStartPickerDate);

            Timestamp currentDate=new Timestamp(System.currentTimeMillis());
            String currentDateString = dateFormat.format(currentDate);

            if (monthlyEndPickerDateString.equals(currentDateString)){
                holder.ivNextDate.setVisibility(View.GONE);
            }else{
                holder.ivNextDate.setVisibility(View.VISIBLE);
            }

            List<String> stats=calculateSales(monthlyStartPickerDate, monthlyEndPickerDate);
            String totalSales=stats.get(0);
            String profitLoss=stats.get(1);
            Log.d(TAG, "onBindViewHolder: totalSalessss : "+totalSales);
            holder.txtTotalSales.setText(totalSales);
            holder.txtBottomline.setText(profitLoss);

            SpannableStringBuilder sbPickerDate = new SpannableStringBuilder();
            sbPickerDate.append(monthlyStartPickerDateString,new StyleSpan(Typeface.NORMAL),Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            sbPickerDate.append(" - ",new StyleSpan(Typeface.BOLD), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            sbPickerDate.append(monthlyEndPickerDateString,new StyleSpan(Typeface.NORMAL),Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

            holder.txtRange.setText(sbPickerDate);

            holder.ivPreviousDate.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                        holder.ivPreviousDate.setAlpha(0.5f);
                    } else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                        holder.ivPreviousDate.setAlpha(1f);

                        // Create a Calendar instance and set it to the timestamp
                        Calendar calendar = Calendar.getInstance();
                        calendar.setTimeInMillis(monthlyStartPickerDate.getTime());

                        // Subtract one day
                        calendar.add(Calendar.DAY_OF_MONTH, -1);
                        monthlyEndPickerDate = new Timestamp(calendar.getTimeInMillis());
                        monthlyEndPickerDateString = dateFormat.format(monthlyEndPickerDate);

                        // Create a Calendar instance
                        calendar = Calendar.getInstance();

                        // Set the timestamp to the Calendar instance
                        calendar.setTime(monthlyEndPickerDate);

                        // Set the day of the month to 1
                        calendar.set(Calendar.DAY_OF_MONTH, 1);

                        // Get the first day of the month
                        monthlyStartPickerDate = new Timestamp(calendar.getTimeInMillis());
                        monthlyStartPickerDateString = dateFormat.format(monthlyStartPickerDate);

                        Timestamp currentDate=new Timestamp(System.currentTimeMillis());
                        String currentDateString = dateFormat.format(currentDate);

                        if (monthlyEndPickerDateString.equals(currentDateString)){
                            holder.ivNextDate.setVisibility(View.GONE);
                        }else{
                            holder.ivNextDate.setVisibility(View.VISIBLE);
                        }


                        List<String> stats=calculateSales(monthlyStartPickerDate, monthlyEndPickerDate);
                        String totalSales=stats.get(0);
                        String profitLoss=stats.get(1);
                        Log.d(TAG, "onBindViewHolder: totalSalessss : "+totalSales);
                        holder.txtTotalSales.setText(totalSales);
                        holder.txtBottomline.setText(profitLoss);


                        SpannableStringBuilder sbPickerDate = new SpannableStringBuilder();
                        sbPickerDate.append(monthlyStartPickerDateString,new StyleSpan(Typeface.NORMAL),Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                        sbPickerDate.append(" - ",new StyleSpan(Typeface.BOLD), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                        sbPickerDate.append(monthlyEndPickerDateString,new StyleSpan(Typeface.NORMAL),Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

                        holder.txtRange.setText(sbPickerDate);

                    }
                    return true;
                }
            });
            holder.ivNextDate.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                        holder.ivNextDate.setAlpha(0.5f);
                    } else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                        holder.ivNextDate.setAlpha(1f);


                        // Create a Calendar instance and set it to the timestamp
                        Calendar calendar = Calendar.getInstance();
                        calendar.setTimeInMillis(monthlyEndPickerDate.getTime());

                        // Subtract one day
                        calendar.add(Calendar.DAY_OF_MONTH, +1);
                        monthlyStartPickerDate = new Timestamp(calendar.getTimeInMillis());
                        monthlyStartPickerDateString = dateFormat.format(monthlyStartPickerDate);
// Create a Calendar instance
                        calendar = Calendar.getInstance();

                        // Set the timestamp to the Calendar instance
                        calendar.setTimeInMillis(monthlyStartPickerDate.getTime());

                        // Get the last day of the month
                        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));

                        // Get the last day of the month
                        monthlyEndPickerDate = new Timestamp(calendar.getTimeInMillis());
                        monthlyEndPickerDateString = dateFormat.format(monthlyEndPickerDate);

                        Timestamp currentDate=new Timestamp(System.currentTimeMillis());
                        String currentDateString = dateFormat.format(currentDate);
                        if (monthlyEndPickerDate.getTime()>currentDate.getTime()){
                            monthlyEndPickerDate=currentDate;
                            monthlyEndPickerDateString=currentDateString;
                        }

                        if (monthlyEndPickerDateString.equals(currentDateString)){
                            holder.ivNextDate.setVisibility(View.GONE);
                        }else{
                            holder.ivNextDate.setVisibility(View.VISIBLE);
                        }


                        List<String> stats=calculateSales(monthlyStartPickerDate, monthlyEndPickerDate);
                        String totalSales=stats.get(0);
                        String profitLoss=stats.get(1);
                        Log.d(TAG, "onBindViewHolder: totalSalessss : "+totalSales);
                        holder.txtTotalSales.setText(totalSales);
                        holder.txtBottomline.setText(profitLoss);


                        SpannableStringBuilder sbPickerDate = new SpannableStringBuilder();
                        sbPickerDate.append(monthlyStartPickerDateString,new StyleSpan(Typeface.NORMAL),Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                        sbPickerDate.append(" - ",new StyleSpan(Typeface.BOLD), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                        sbPickerDate.append(monthlyEndPickerDateString,new StyleSpan(Typeface.NORMAL),Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

                        holder.txtRange.setText(sbPickerDate);
                    }
                    return true;
                }
            });

        }


        else if (position % 4 == 2) {
            Log.d(TAG, "onBindViewHolder: CUSTOM STATS: ");
            holder.txtStatsTitle.setText(Tags.STRING_CUSTOM_STATS);
            holder.txtTotalSales.setText("10000");
            holder.txtBottomline.setText("1000");
            holder.ivPreviousDate.setVisibility(View.GONE);
            holder.ivNextDate.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return 3;
    }

    public static class StatsticsInfoViewHolder extends RecyclerView.ViewHolder {
        public TextView txtStatsTitle, txtTotalSales, txtBottomline,txtRange;
        public ImageView ivPreviousDate, ivNextDate;

        public StatsticsInfoViewHolder(@NonNull View itemView) {
            super(itemView);
            txtStatsTitle = itemView.findViewById(R.id.txt_stats_title);
            txtBottomline = itemView.findViewById(R.id.txt_bottomline);
            txtTotalSales = itemView.findViewById(R.id.txt_total_sales);
            ivPreviousDate = itemView.findViewById(R.id.iv_previous_date);
            ivNextDate = itemView.findViewById(R.id.iv_next_date);
            txtRange = itemView.findViewById(R.id.txt_range);

        }
    }

    public List<String> calculateSales(Timestamp start, Timestamp end) {
        Integer totalSales = 0;
        Integer profitLoss = 0;

        Timestamp picker = end;
        while (picker.getTime() >= start.getTime()) {
            String formattedDate = dateFormat.format(picker);
            //do something
            List<Integer> dateStatsInfo = hmStatsInfo.get(formattedDate);
            if (dateStatsInfo != null && dateStatsInfo.get(0) != null && dateStatsInfo.get(1) != null && dateStatsInfo.get(2) != null && dateStatsInfo.get(3) != null && dateStatsInfo.get(4) != null) {
                Log.d(TAG, "calculateSales: " + dateStatsInfo.get(0) + " : " + dateStatsInfo.get(1) + " : " + dateStatsInfo.get(2) + " : " + dateStatsInfo.get(3) + " : " + dateStatsInfo.get(4) + " : ");
                totalSales += dateStatsInfo.get(0) + dateStatsInfo.get(2) - dateStatsInfo.get(1);
                profitLoss += dateStatsInfo.get(0)  - dateStatsInfo.get(1);
                //cipsk
            }
            Log.d(TAG, "calculateSales: totalSales : "+ totalSales + " profitLoss: "+profitLoss);

            // Create a Calendar instance and set it to the timestamp
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(picker.getTime());

            // Subtract one day
            calendar.add(Calendar.DAY_OF_MONTH, -1);
            picker = new Timestamp(calendar.getTimeInMillis());
        }
        Log.d(TAG, "calculateSales: totalSales FINALLLLL :: "+ totalSales+ " profitLoss: "+profitLoss);
        String totalSalesString=String.valueOf(totalSales);
        String profitLossString=String.valueOf(profitLoss);
        return Arrays.asList(totalSalesString, profitLossString);
        // Convert the Timestamp to a string in the specified format

        //pick end key 's list from hashmap then calc. sales & p/l & add to daily stats column

        //pick end-1 key 's list from hashmap then calc. sales & p/l & add to daily stats column
        // ...........
        //....
        //pick start key 's list from hashmap then calc. sales & p/l & add to daily stats column

    }

//    public Integer calculateSales(String start, String  end) {
//        DateTimeFormatter formatter = null;
//        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
//            formatter = DateTimeFormatter.ofPattern("d MMMM yy");
//        }
//
//        // Parse the date strings
//        LocalDate date1 = null;
//        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
//            date1 = LocalDate.parse(end, formatter);
//        }
//        LocalDate date2 = null;
//        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
//            date2 = LocalDate.parse(start, formatter);
//        }
//
//        // Calculate the number of days between the dates
//        long daysBetween = 0;
//        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
//            daysBetween = ChronoUnit.DAYS.between(date1, date2);
//        }
//
//        // Absolute value of daysBetween to handle negative result
//        daysBetween = Math.abs(daysBetween);
//        daysBetween++;
//        Log.d(TAG, "calculateSales: daysBetween : " + daysBetween);
//        return Math.toIntExact(daysBetween);
//
//        // calc. the sales and profit
//
//    }
}
