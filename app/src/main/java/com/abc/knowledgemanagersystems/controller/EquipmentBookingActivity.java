package com.abc.knowledgemanagersystems.controller;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.abc.knowledgemanagersystems.R;
import com.abc.knowledgemanagersystems.model.Booking;
import com.abc.knowledgemanagersystems.viewmodel.BookingViewModel;

import java.util.Calendar;
import java.util.concurrent.Future;

public class EquipmentBookingActivity extends AppCompatActivity {

    private TextView mHeaderText;
    private CalendarView mCalendarView;
    private Button mButtonBookNow, mButtonStartTime, mButtonEndTime;

    private BookingViewModel mBookingViewModel;
    private int mEquipmentId = -1;
    private int mCurrentUserId = 1;

    private Calendar mStartCalendar = Calendar.getInstance();
    private Calendar mEndCalendar = Calendar.getInstance();
    private Calendar mSelectedDay = Calendar.getInstance();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_equipment_booking);

        mHeaderText = findViewById(R.id.text_view_booking_title);
        mCalendarView = findViewById(R.id.calendar_view);
        mButtonStartTime = findViewById(R.id.button_pick_start_time);
        mButtonEndTime = findViewById(R.id.button_pick_end_time);
        mButtonBookNow = findViewById(R.id.button_book_now);

        mBookingViewModel = new ViewModelProvider(this).get(BookingViewModel.class);

        Intent intent = getIntent();
        mEquipmentId = intent.getIntExtra("EQUIPMENT_ID", -1);
        String equipmentName = intent.getStringExtra("EQUIPMENT_NAME");
        mHeaderText.setText("Đặt lịch cho: " + equipmentName);

        setupCalendar();
        setupClickListeners();
    }

    private void setupCalendar() {
        mCalendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                mSelectedDay.set(year, month, dayOfMonth);
            }
        });
    }

    private void setupClickListeners() {
        mButtonStartTime.setOnClickListener(v -> showTimePicker(true));
        mButtonEndTime.setOnClickListener(v -> showTimePicker(false));

        mButtonBookNow.setOnClickListener(v -> {
            long startTime = combineDateAndTime(mSelectedDay, mStartCalendar).getTimeInMillis();
            long endTime = combineDateAndTime(mSelectedDay, mEndCalendar).getTimeInMillis();

            if (startTime >= endTime) {
                Toast.makeText(this, "Giờ kết thúc phải sau giờ bắt đầu", Toast.LENGTH_SHORT).show();
                return;
            }

            Future<Boolean> conflictCheck = mBookingViewModel.hasConflict(mEquipmentId, startTime, endTime);

            try {
                if (conflictCheck.get()) {
                    Toast.makeText(this, "Lỗi: Đã có người đặt trong thời gian này!", Toast.LENGTH_LONG).show();
                    return;
                }
            } catch (Exception e) {
                Toast.makeText(this, "Lỗi khi kiểm tra lịch: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                return;
            }

            Booking newBooking = new Booking();
            newBooking.setEquipmentId(mEquipmentId);
            newBooking.setUserId(mCurrentUserId);
            newBooking.setStartTime(startTime);
            newBooking.setEndTime(endTime);
            newBooking.setStatus("CONFIRMED");

            mBookingViewModel.insert(newBooking);
            Toast.makeText(this, "Đã đặt lịch thành công!", Toast.LENGTH_SHORT).show();
            finish();
        });
    }

    /**
     * SỬA LỖI 1: Reset Giây (Second) và Miligiây (Millisecond)
     */
    private void showTimePicker(boolean isStartTime) {
        Calendar cal = (isStartTime) ? mStartCalendar : mEndCalendar;

        TimePickerDialog dialog = new TimePickerDialog(this,
                (view, hourOfDay, minute) -> {
                    cal.set(Calendar.HOUR_OF_DAY, hourOfDay);
                    cal.set(Calendar.MINUTE, minute);

                    // --- SỬA LỖI: Reset Giây và Miligiây ---
                    cal.set(Calendar.SECOND, 0);
                    cal.set(Calendar.MILLISECOND, 0);

                    String time = String.format("%02d:%02d", hourOfDay, minute);
                    if (isStartTime) {
                        mButtonStartTime.setText("Bắt đầu: " + time);
                    } else {
                        mButtonEndTime.setText("Kết thúc: " + time);
                    }
                },
                cal.get(Calendar.HOUR_OF_DAY),
                cal.get(Calendar.MINUTE),
                true // 24-hour format
        );
        dialog.show();
    }

    /**
     * SỬA LỖI 2: Đảm bảo Miligiây (Millisecond) là 0
     */
    private Calendar combineDateAndTime(Calendar date, Calendar time) {
        Calendar cal = Calendar.getInstance();
        cal.set(date.get(Calendar.YEAR),
                date.get(Calendar.MONTH),
                date.get(Calendar.DAY_OF_MONTH),
                time.get(Calendar.HOUR_OF_DAY),
                time.get(Calendar.MINUTE),
                0); // <-- Set Giây (Second) = 0

        // --- SỬA LỖI: Set Miligiây (Millisecond) = 0 ---
        cal.set(Calendar.MILLISECOND, 0);

        return cal;
    }
}