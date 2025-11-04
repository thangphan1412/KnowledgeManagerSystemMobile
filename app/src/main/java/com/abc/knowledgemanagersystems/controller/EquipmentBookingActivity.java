package com.abc.knowledgemanagersystems.controller;

import android.app.TimePickerDialog;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.abc.knowledgemanagersystems.R;

import java.util.Calendar;

public class EquipmentBookingActivity extends AppCompatActivity {

    // --- Biến lưu trữ ---
    private long equipmentId = -1;
    private String equipmentName = "";
    private int selectedStartHour = -1;
    private int selectedStartMinute = -1;
    private int selectedEndHour = -1;
    private int selectedEndMinute = -1;

    // --- Views ---
    private TextView textViewTitle;
    private CalendarView calendarView;
    private Button buttonPickStartTime;
    private Button buttonPickEndTime;
    private Button buttonBookNow;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_equipment_booking);

        // 1. Nhận ID và Tên được gửi từ EquipmentDetailActivity
        equipmentId = getIntent().getLongExtra("EQUIPMENT_ID", -1);
        equipmentName = getIntent().getStringExtra("EQUIPMENT_NAME");

        // 2. Ánh xạ Views
        textViewTitle = findViewById(R.id.text_view_booking_title);
        calendarView = findViewById(R.id.calendar_view);
        buttonPickStartTime = findViewById(R.id.button_pick_start_time);
        buttonPickEndTime = findViewById(R.id.button_pick_end_time);
        buttonBookNow = findViewById(R.id.button_book_now);

        // 3. Cập nhật tiêu đề
        if (equipmentName != null && !equipmentName.isEmpty()) {
            textViewTitle.setText("Đặt lịch cho: " + equipmentName);
        }

        // 4. Cài đặt listeners (Đây là code cũ từ EquipmentDetailActivity)
        setupClickListeners();
    }

    private void setupClickListeners() {
        // --- Nút Chọn Giờ Bắt đầu ---
        buttonPickStartTime.setOnClickListener(v -> {
            final Calendar c = Calendar.getInstance();
            int currentHour = c.get(Calendar.HOUR_OF_DAY);
            int currentMinute = c.get(Calendar.MINUTE);
            TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                    (view, hourOfDay, minute) -> {
                        selectedStartHour = hourOfDay;
                        selectedStartMinute = minute;
                        String timeString = String.format(java.util.Locale.getDefault(), "%02d:%02d", hourOfDay, minute);
                        buttonPickStartTime.setText("Giờ bắt đầu: " + timeString);
                    }, currentHour, currentMinute, true);
            timePickerDialog.show();
        });

        // --- Nút Chọn Giờ Kết thúc ---
        buttonPickEndTime.setOnClickListener(v -> {
            final Calendar c = Calendar.getInstance();
            int currentHour = c.get(Calendar.HOUR_OF_DAY);
            int currentMinute = c.get(Calendar.MINUTE);
            TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                    (view, hourOfDay, minute) -> {
                        selectedEndHour = hourOfDay;
                        selectedEndMinute = minute;
                        String timeString = String.format(java.util.Locale.getDefault(), "%02d:%02d", hourOfDay, minute);
                        buttonPickEndTime.setText("Giờ kết thúc: " + timeString);
                    }, currentHour, currentMinute, true);
            timePickerDialog.show();
        });

        // --- Nút Xác nhận Đặt lịch ---
        buttonBookNow.setOnClickListener(v -> {
            if (selectedStartHour == -1 || selectedEndHour == -1) {
                Toast.makeText(this, "Vui lòng chọn giờ bắt đầu và kết thúc", Toast.LENGTH_SHORT).show();
                return;
            }
            String startTime = String.format(java.util.Locale.getDefault(), "%02d:%02d", selectedStartHour, selectedStartMinute);
            String endTime = String.format(java.util.Locale.getDefault(), "%02d:%02d", selectedEndHour, selectedEndMinute);

            Toast.makeText(this, "Đã đặt lịch cho ID " + equipmentId + " từ " + startTime + " đến " + endTime, Toast.LENGTH_LONG).show();

            // TODO: Gửi thông tin này lên server/database

            // Đóng màn hình Đặt lịch và quay lại màn hình Chi tiết
            finish();
        });

        // --- Lịch (CalendarView) ---
        calendarView.setOnDateChangeListener((view, year, month, dayOfMonth) -> {
            String selectedDate = dayOfMonth + "/" + (month + 1) + "/" + year;
            Toast.makeText(this, "Ngày chọn: " + selectedDate, Toast.LENGTH_SHORT).show();
        });
    }
}