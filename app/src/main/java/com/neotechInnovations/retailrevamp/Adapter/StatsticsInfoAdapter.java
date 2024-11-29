package com.neotechInnovations.retailrevamp.Adapter;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Context;
import android.graphics.Typeface;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.neotechInnovations.retailrevamp.Constant.Tags;
import com.neotechInnovations.retailrevamp.R;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class StatsticsInfoAdapter extends RecyclerView.Adapter<StatsticsInfoAdapter.StatsticsInfoViewHolder> {

    private static final String TAG = "StatsticsInfoAdapter";
    Context mContext;
    public HashMap<String, List<Integer>> hmStatsInfo;
    Timestamp dailyPickerDate;
    String dailyPickerDateString;
    Timestamp monthlyStartPickerDate, monthlyEndPickerDate,customStartPickerDate,customEndPickerDate;
    String monthlyStartPickerDateString, monthlyEndPickerDateString,customStartPickerDateString, customEndPickerDateString;
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
            holder.llDailyMonthlyStatsRange.setVisibility(View.VISIBLE);
            holder.llCustomStatsRange.setVisibility(View.GONE);
            holder.txtStatsTitle.setText(Tags.STRING_DAILY_STATS);
            //find the current date
            //send the start date and end date
            Log.d(TAG, "onBindViewHolder: DAILY STATS: ");
            dailyPickerDate = new Timestamp(System.currentTimeMillis());
            dailyPickerDateString = dateFormat.format(dailyPickerDate);
            Log.d(TAG, "onBindViewHolder: check the hashmap hmStatsInfo.get(\"CurrentDate\"): " + dailyPickerDateString + " : " + hmStatsInfo.get(dailyPickerDateString));
            List<String> stats = calculateSales(dailyPickerDate, dailyPickerDate);
            String totalSales = stats.get(0);
            String profitLoss = stats.get(1);
            Log.d(TAG, "onBindViewHolder: totalSalessss : " + totalSales);
            holder.txtTotalSales.setText(totalSales);
            holder.txtBottomline.setText(profitLoss);
            holder.txtRange.setText(dailyPickerDateString);

            Timestamp currentDate = new Timestamp(System.currentTimeMillis());
            String currentDateString = dateFormat.format(currentDate);
            Log.d(TAG, "onTouch1: " + dailyPickerDate + " :: " + currentDate);
            if (dailyPickerDateString.equals(currentDateString)) {
                holder.llNextDate.setVisibility(View.GONE);
            } else {
                holder.llNextDate.setVisibility(View.VISIBLE);
            }

            holder.llPreviousDate.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                        holder.llPreviousDate.setAlpha(0.5f);
                    } else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                        holder.llPreviousDate.setAlpha(1f);
                        // Create a Calendar instance and set it to the timestamp
                        Calendar calendar = Calendar.getInstance();
                        calendar.setTimeInMillis(dailyPickerDate.getTime());

                        // Subtract one day
                        calendar.add(Calendar.DAY_OF_MONTH, -1);
                        dailyPickerDate = new Timestamp(calendar.getTimeInMillis());
                        dailyPickerDateString = dateFormat.format(dailyPickerDate);

                        List<String> stats = calculateSales(dailyPickerDate, dailyPickerDate);
                        String totalSales = stats.get(0);
                        String profitLoss = stats.get(1);
                        Log.d(TAG, "onBindViewHolder: totalSalessss : " + totalSales);
                        holder.txtTotalSales.setText(totalSales);
                        holder.txtBottomline.setText(profitLoss);
                        holder.txtRange.setText(dailyPickerDateString);

                        Log.d(TAG, "onTouch2: " + dailyPickerDate + " :: " + currentDate);
                        if (dailyPickerDateString.equals(currentDateString)) {
                            holder.llNextDate.setVisibility(View.GONE);
                        } else {
                            holder.llNextDate.setVisibility(View.VISIBLE);
                        }
                    }
                    return true;
                }
            });
            holder.llNextDate.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                        holder.llNextDate.setAlpha(0.5f);
                    } else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                        holder.llNextDate.setAlpha(1f);

                        // Create a Calendar instance and set it to the timestamp
                        Calendar calendar = Calendar.getInstance();
                        calendar.setTimeInMillis(dailyPickerDate.getTime());

                        // Subtract one day
                        calendar.add(Calendar.DAY_OF_MONTH, +1);
                        dailyPickerDate = new Timestamp(calendar.getTimeInMillis());
                        dailyPickerDateString = dateFormat.format(dailyPickerDate);

                        List<String> stats = calculateSales(dailyPickerDate, dailyPickerDate);
                        String totalSales = stats.get(0);
                        String profitLoss = stats.get(1);
                        Log.d(TAG, "onBindViewHolder: totalSalessss : " + totalSales);
                        holder.txtTotalSales.setText(totalSales);
                        holder.txtBottomline.setText(profitLoss);
                        holder.txtRange.setText(dailyPickerDateString);

                        Log.d(TAG, "onTouch3: " + dailyPickerDate + " :: " + currentDate);
                        if (dailyPickerDateString.equals(currentDateString)) {
                            holder.llNextDate.setVisibility(View.GONE);
                        } else {
                            holder.llNextDate.setVisibility(View.VISIBLE);
                        }
                    }
                    return true;
                }
            });
        }
        else if (position % 4 == 1) {
            Log.d(TAG, "onBindViewHolder: MONTHLY STATS: ");
            holder.llDailyMonthlyStatsRange.setVisibility(View.VISIBLE);
            holder.llCustomStatsRange.setVisibility(View.GONE);
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

            Timestamp currentDate = new Timestamp(System.currentTimeMillis());
            String currentDateString = dateFormat.format(currentDate);

            if (monthlyEndPickerDateString.equals(currentDateString)) {
                holder.llNextDate.setVisibility(View.GONE);
            } else {
                holder.llNextDate.setVisibility(View.VISIBLE);
            }

            List<String> stats = calculateSales(monthlyStartPickerDate, monthlyEndPickerDate);
            String totalSales = stats.get(0);
            String profitLoss = stats.get(1);
            Log.d(TAG, "onBindViewHolder: totalSalessss : " + totalSales);
            holder.txtTotalSales.setText(totalSales);
            holder.txtBottomline.setText(profitLoss);

            SpannableStringBuilder sbPickerDate = new SpannableStringBuilder();
            sbPickerDate.append(monthlyStartPickerDateString, new StyleSpan(Typeface.NORMAL), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            sbPickerDate.append(" - ", new StyleSpan(Typeface.BOLD), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            sbPickerDate.append(monthlyEndPickerDateString, new StyleSpan(Typeface.NORMAL), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

            holder.txtRange.setText(sbPickerDate);

            holder.llPreviousDate.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                        holder.llPreviousDate.setAlpha(0.5f);
                    } else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                        holder.llPreviousDate.setAlpha(1f);

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

                        Timestamp currentDate = new Timestamp(System.currentTimeMillis());
                        String currentDateString = dateFormat.format(currentDate);

                        if (monthlyEndPickerDateString.equals(currentDateString)) {
                            holder.llNextDate.setVisibility(View.GONE);
                        } else {
                            holder.llNextDate.setVisibility(View.VISIBLE);
                        }


                        List<String> stats = calculateSales(monthlyStartPickerDate, monthlyEndPickerDate);
                        String totalSales = stats.get(0);
                        String profitLoss = stats.get(1);
                        Log.d(TAG, "onBindViewHolder: totalSalessss : " + totalSales);
                        holder.txtTotalSales.setText(totalSales);
                        holder.txtBottomline.setText(profitLoss);


                        SpannableStringBuilder sbPickerDate = new SpannableStringBuilder();
                        sbPickerDate.append(monthlyStartPickerDateString, new StyleSpan(Typeface.NORMAL), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                        sbPickerDate.append(" - ", new StyleSpan(Typeface.BOLD), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                        sbPickerDate.append(monthlyEndPickerDateString, new StyleSpan(Typeface.NORMAL), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

                        holder.txtRange.setText(sbPickerDate);

                    }
                    return true;
                }
            });
            holder.llNextDate.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                        holder.llNextDate.setAlpha(0.5f);
                    } else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                        holder.llNextDate.setAlpha(1f);


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

                        Timestamp currentDate = new Timestamp(System.currentTimeMillis());
                        String currentDateString = dateFormat.format(currentDate);
                        if (monthlyEndPickerDate.getTime() > currentDate.getTime()) {
                            monthlyEndPickerDate = currentDate;
                            monthlyEndPickerDateString = currentDateString;
                        }

                        if (monthlyEndPickerDateString.equals(currentDateString)) {
                            holder.llNextDate.setVisibility(View.GONE);
                        } else {
                            holder.llNextDate.setVisibility(View.VISIBLE);
                        }


                        List<String> stats = calculateSales(monthlyStartPickerDate, monthlyEndPickerDate);
                        String totalSales = stats.get(0);
                        String profitLoss = stats.get(1);
                        Log.d(TAG, "onBindViewHolder: totalSalessss : " + totalSales);
                        holder.txtTotalSales.setText(totalSales);
                        holder.txtBottomline.setText(profitLoss);


                        SpannableStringBuilder sbPickerDate = new SpannableStringBuilder();
                        sbPickerDate.append(monthlyStartPickerDateString, new StyleSpan(Typeface.NORMAL), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                        sbPickerDate.append(" - ", new StyleSpan(Typeface.BOLD), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                        sbPickerDate.append(monthlyEndPickerDateString, new StyleSpan(Typeface.NORMAL), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

                        holder.txtRange.setText(sbPickerDate);
                    }
                    return true;
                }
            });

        }
        else if (position % 4 == 2) {
            Log.d(TAG, "onBindViewHolder: CUSTOM STATS: ");
            holder.llDailyMonthlyStatsRange.setVisibility(View.GONE);
            holder.llCustomStatsRange.setVisibility(View.VISIBLE);
            holder.txtStatsTitle.setText(Tags.STRING_CUSTOM_STATS);

            // Example timestamp
            customEndPickerDate = new Timestamp(System.currentTimeMillis());
            customEndPickerDateString = dateFormat.format(customEndPickerDate);
            // Create a Calendar instance
            Calendar calendar = Calendar.getInstance();

            // Set the timestamp to the Calendar instance
            calendar.setTime(customEndPickerDate);

            // Subtract one day
            calendar.add(Calendar.DAY_OF_MONTH, -35);

            // Set the day of the month to 1
//            calendar.set(Calendar.DAY_OF_MONTH, 1);

            // Get the first day of the month
            customStartPickerDate = new Timestamp(calendar.getTimeInMillis());
            customStartPickerDateString = dateFormat.format(customStartPickerDate);
            holder.selectedStartDate=calendar;
//            Timestamp currentDate = new Timestamp(System.currentTimeMillis());
//            String currentDateString = dateFormat.format(currentDate);
            holder.txtStartDate.setText(customStartPickerDateString);
            holder.txtEndDate.setText(customEndPickerDateString);
//            if (monthlyEndPickerDateString.equals(currentDateString)) {
//                holder.llNextDate.setVisibility(View.GONE);
//            } else {
//                holder.llNextDate.setVisibility(View.VISIBLE);
//            }

            List<String> stats1 = calculateSales(customStartPickerDate, customEndPickerDate);
            String totalSales1 = stats1.get(0);
            String profitLoss1 = stats1.get(1);
            Log.d(TAG, "onBindViewHolder: totalSalessss : " + totalSales1);
            holder.txtTotalSales.setText(totalSales1);
            holder.txtBottomline.setText(profitLoss1);

//            SpannableStringBuilder sbPickerDate = new SpannableStringBuilder();
//            sbPickerDate.append(monthlyStartPickerDateString, new StyleSpan(Typeface.NORMAL), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//            sbPickerDate.append(" - ", new StyleSpan(Typeface.BOLD), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//            sbPickerDate.append(monthlyEndPickerDateString, new StyleSpan(Typeface.NORMAL), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//
//            holder.txtStartDate.setText(sbPickerDate);


            holder.txtStartDate.setOnClickListener(view1 -> {
                Log.d(TAG, "onBindViewHolder: CUSTOM STATS: startDateClicked ... ");
                holder.selectedStartDate=Calendar.getInstance();
                holder.selectedEndDate=Calendar.getInstance();
                //select start Date
                Calendar currentDate = Calendar.getInstance();
                DatePickerDialog datePicker = new DatePickerDialog(mContext,
                        (view, year, month, dayOfMonth) -> {
                            Log.d(TAG, "onBindViewHolder: CUSTOM STATS: startDateClicked date selected... ");
                            holder.selectedStartDate.set(year, month, dayOfMonth);
                            holder.txtStartDate.setText(dayOfMonth + "/" + (month + 1) + "/" + year);
                            // Reset the end date selection and button text
                            holder.txtEndDate.setText("End Date");
                            holder.selectedEndDate.setTimeInMillis(holder.selectedStartDate.getTimeInMillis());
                        },
                        currentDate.get(Calendar.YEAR),
                        currentDate.get(Calendar.MONTH),
                        currentDate.get(Calendar.DAY_OF_MONTH));
                datePicker.getDatePicker().setMaxDate(currentDate.getTimeInMillis());
                // Show the dialog to get references to the buttons
                datePicker.setOnShowListener(dialog -> {
                    // Access the header and change the background color
                    int headerId = mContext.getResources().getIdentifier("android:id/date_picker_header", null, null);
                    View header = datePicker.findViewById(headerId);
                    if (header != null) {
                        header.setBackgroundColor(mContext.getColor(R.color.decreasing_red)); // Set header background color
                    }

                    // Change header text color
                    int headerTitleId = mContext.getResources().getIdentifier("android:id/date_picker_header_title", null, null);
                    TextView headerTitle = datePicker.findViewById(headerTitleId);
                    if (headerTitle != null) {
                        headerTitle.setTextColor(mContext.getColor(R.color.black)); // Set header text color
                    }
                    // Change the color of the "OK" button
                    Button okButton = datePicker.getButton(DatePickerDialog.BUTTON_POSITIVE);
                    okButton.setTextColor(mContext.getColor(R.color.increasing_green));

                    // Change the color of the "Cancel" button
                    Button cancelButton = datePicker.getButton(DatePickerDialog.BUTTON_NEGATIVE);
                    cancelButton.setTextColor(mContext.getColor(R.color.decreasing_red));
                });
                datePicker.show();
            });
            holder.txtEndDate.setOnClickListener(view1 -> {
                Log.d(TAG, "onBindViewHolder: CUSTOM STATS: endDateClicked ... "+holder.selectedStartDate);
                holder.selectedEndDate=Calendar.getInstance();
                //Select End Date
                if (holder.selectedStartDate==null || holder.selectedStartDate.getTimeInMillis() == 0) {
                    Log.d(TAG, "onBindViewHolder: CUSTOM STATS: endDateClicked start is not selected... ");
                    Toast.makeText(mContext, "Please select a start date first", Toast.LENGTH_SHORT).show();
                    return;
                }
                Calendar currentDate1 = Calendar.getInstance();
                DatePickerDialog datePicker1 = new DatePickerDialog(mContext,
                        (view, year, month, dayOfMonth) -> {
                            Log.d(TAG, "onBindViewHolder: CUSTOM STATS: endDateClicked endDate is selected... ");
                            holder.selectedEndDate.set(year, month, dayOfMonth);
                            holder.txtEndDate.setText(dayOfMonth + "/" + (month + 1) + "/" + year);

                            customStartPickerDate = new Timestamp(holder.selectedStartDate.getTimeInMillis());
                            customEndPickerDate = new Timestamp(holder.selectedEndDate.getTimeInMillis());
                            //set the creteria
                            List<String> stats = calculateSales(customStartPickerDate, customEndPickerDate);
                            String totalSales = stats.get(0);
                            String profitLoss = stats.get(1);
                            Log.d(TAG, "onBindViewHolder: CUSTOM STATS totalSalessss : " + totalSales);
                            holder.txtTotalSales.setText(totalSales);
                            holder.txtBottomline.setText(profitLoss);
                        },
                        currentDate1.get(Calendar.YEAR),
                        currentDate1.get(Calendar.MONTH),
                        currentDate1.get(Calendar.DAY_OF_MONTH));

                // Set the minimum date for endDate to be the selected startDate
                datePicker1.getDatePicker().setMinDate(holder.selectedStartDate.getTimeInMillis());
                datePicker1.getDatePicker().setMaxDate(currentDate1.getTimeInMillis());
                // Show the dialog to get references to the buttons
                datePicker1.setOnShowListener(dialog -> {
                    // Access the header and change the background color
                    int headerId = mContext.getResources().getIdentifier("android:id/date_picker_header", null, null);
                    View header = datePicker1.findViewById(headerId);
                    if (header != null) {
                        header.setBackgroundColor(mContext.getColor(R.color.decreasing_red)); // Set header background color
                    }

                    // Change header text color
                    int headerTitleId = mContext.getResources().getIdentifier("android:id/date_picker_header_title", null, null);
                    TextView headerTitle = datePicker1.findViewById(headerTitleId);
                    if (headerTitle != null) {
                        headerTitle.setTextColor(mContext.getColor(R.color.black)); // Set header text color
                    }
                    // Change the color of the "OK" button
                    Button okButton = datePicker1.getButton(DatePickerDialog.BUTTON_POSITIVE);
                    okButton.setTextColor(mContext.getColor(R.color.increasing_green));

                    // Change the color of the "Cancel" button
                    Button cancelButton = datePicker1.getButton(DatePickerDialog.BUTTON_NEGATIVE);
                    cancelButton.setTextColor(mContext.getColor(R.color.decreasing_red));
                });
                datePicker1.show();

            });
//            holder.txtTotalSales.setText("10000");
//            holder.txtBottomline.setText("1000");
//            holder.llPreviousDate.setVisibility(View.GONE);
//            holder.llNextDate.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return 3;
    }

    public static class StatsticsInfoViewHolder extends RecyclerView.ViewHolder {
        public TextView txtStatsTitle, txtTotalSales, txtBottomline, txtRange, txtStartDate, txtEndDate;
        public ImageView ivPreviousDate, ivNextDate;
        LinearLayout llPreviousDate, llNextDate, llDailyMonthlyStatsRange, llCustomStatsRange;
        private Calendar selectedStartDate;
        private Calendar selectedEndDate;

        public StatsticsInfoViewHolder(@NonNull View itemView) {
            super(itemView);
            txtStatsTitle = itemView.findViewById(R.id.txt_stats_title);
            txtBottomline = itemView.findViewById(R.id.txt_bottomline);
            txtTotalSales = itemView.findViewById(R.id.txt_total_sales);
            ivPreviousDate = itemView.findViewById(R.id.iv_previous_date);
            llPreviousDate = itemView.findViewById(R.id.ll_previous_date);
            ivNextDate = itemView.findViewById(R.id.iv_next_date);
            llNextDate = itemView.findViewById(R.id.ll_next_date);
            txtRange = itemView.findViewById(R.id.txt_range);
            llDailyMonthlyStatsRange = itemView.findViewById(R.id.ll_daily_monthly_stats_range);
            llCustomStatsRange = itemView.findViewById(R.id.ll_custom_stats_range);
            txtStartDate = itemView.findViewById(R.id.txt_start_date);
            txtEndDate = itemView.findViewById(R.id.txt_end_date);
            // Initialize selected dates
//            selectedStartDate = Calendar.getInstance();
//            selectedEndDate = Calendar.getInstance();
        }
    }

    private void selectStartDate() {
    }

    private void selectEndDate() {
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
                profitLoss += dateStatsInfo.get(0) - dateStatsInfo.get(1);
                //cipsk
            }
            Log.d(TAG, "calculateSales: totalSales : " + totalSales + " profitLoss: " + profitLoss);

            // Create a Calendar instance and set it to the timestamp
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(picker.getTime());

            // Subtract one day
            calendar.add(Calendar.DAY_OF_MONTH, -1);
            picker = new Timestamp(calendar.getTimeInMillis());
        }
        Log.d(TAG, "calculateSales: totalSales FINALLLLL :: " + totalSales + " profitLoss: " + profitLoss);
        String totalSalesString = String.valueOf(totalSales);
        String profitLossString = String.valueOf(profitLoss);
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
