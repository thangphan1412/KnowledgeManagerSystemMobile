package com.abc.knowledgemanagersystems.controller;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.abc.knowledgemanagersystems.R;
import com.abc.knowledgemanagersystems.db.AppDataBase;
import com.abc.knowledgemanagersystems.model.InventoryItem;
import com.abc.knowledgemanagersystems.model.InventoryLogs;
import com.abc.knowledgemanagersystems.utils.SessionManager;
import com.abc.knowledgemanagersystems.status.StatusInventoryItem;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class AddUpdateInventoryActivity extends AppCompatActivity {

    private TextInputEditText edtName, edtFormula, edtLocation, edtExpiryDate, edtQuantity, edtUnits;
    private AutoCompleteTextView spinnerStatus;
    private TextView tvCurrentQuantity;
    private MaterialButton btnCheckIn, btnCheckOut;

    private AppDataBase db;
    private InventoryItem currentItem;
    private boolean isEditMode = false;
    private int currentUserId;

    private final Calendar calendar = Calendar.getInstance();
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_update_inventory);

        db = AppDataBase.getInstance(this);

        // Lấy user id từ SessionManager
        SessionManager session = new SessionManager(this);
        currentUserId = session.getUserId();
        if (currentUserId == -1) currentUserId = 1; // fallback admin

        // Toolbar setup
        MaterialToolbar toolbar = findViewById(R.id.toolbar_add_item);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Mapping UI components
        edtName = findViewById(R.id.edit_text_item_name);
        edtFormula = findViewById(R.id.edit_text_formula);
        edtLocation = findViewById(R.id.edit_text_location);
        edtExpiryDate = findViewById(R.id.edit_text_expiry_date);
        edtQuantity = findViewById(R.id.edit_text_quantity);
        edtUnits = findViewById(R.id.edit_text_units);
        spinnerStatus = findViewById(R.id.spinner_status);
        tvCurrentQuantity = findViewById(R.id.text_view_current_quantity);
        btnCheckIn = findViewById(R.id.button_check_in);
        btnCheckOut = findViewById(R.id.button_check_out);

        setupStatusDropdown();
        setupDatePicker();

        // Kiểm tra chế độ chỉnh sửa
        int itemId = getIntent().getIntExtra("item_id", -1);
        if (itemId != -1) {
            isEditMode = true;
            toolbar.setTitle("Cập nhật Vật tư");
            loadInventoryItem(itemId);
        } else {
            isEditMode = false;
            toolbar.setTitle("Thêm Vật tư Mới");
            tvCurrentQuantity.setText("Hiện có: 0");
        }

        btnCheckIn.setOnClickListener(v -> handleInventoryChange(true));
        btnCheckOut.setOnClickListener(v -> handleInventoryChange(false));
    }

    private void setupStatusDropdown() {
        ArrayAdapter<StatusInventoryItem> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_dropdown_item_1line,
                StatusInventoryItem.values()
        );
        spinnerStatus.setAdapter(adapter);
    }

    private void setupDatePicker() {
        edtExpiryDate.setOnClickListener(v -> {
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);
            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    AddUpdateInventoryActivity.this,
                    (DatePicker view, int year1, int month1, int dayOfMonth) -> {
                        calendar.set(year1, month1, dayOfMonth);
                        edtExpiryDate.setText(dateFormat.format(calendar.getTime()));
                    },
                    year, month, day);
            datePickerDialog.show();
        });
    }

    private void loadInventoryItem(int itemId) {
        AppDataBase.databaseWriteExecutor.execute(() -> {
            currentItem = db.inventoryItemDao().getItemById(itemId);
            if (currentItem != null) {
                runOnUiThread(() -> {
                    edtName.setText(currentItem.getName());
                    edtFormula.setText(currentItem.getFormula());
                    edtLocation.setText(currentItem.getLocation());
                    edtExpiryDate.setText(currentItem.getExpiredDate());

                    StatusInventoryItem status = currentItem.getStatusInventoryItem() != null
                            ? currentItem.getStatusInventoryItem()
                            : StatusInventoryItem.Available;
                    spinnerStatus.setText(status.toString(), false);

                    edtUnits.setText(currentItem.getUnits());
                    tvCurrentQuantity.setText("Hiện có: " + currentItem.getQuantity() + " " + currentItem.getUnits());
                });
            }
        });
    }

    private void handleInventoryChange(boolean isCheckIn) {
        String name = edtName.getText().toString().trim();
        String formula = edtFormula.getText().toString().trim();
        String location = edtLocation.getText().toString().trim();
        String expiry = edtExpiryDate.getText().toString().trim();
        String quantityStr = edtQuantity.getText().toString().trim();
        String units = edtUnits.getText().toString().trim();
        String statusStr = spinnerStatus.getText().toString().trim();

        if (name.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập tên vật tư", Toast.LENGTH_SHORT).show();
            return;
        }
        if (quantityStr.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập số lượng thay đổi", Toast.LENGTH_SHORT).show();
            return;
        }

        double qtyChange = Double.parseDouble(quantityStr);
        if (qtyChange <= 0) {
            Toast.makeText(this, "Số lượng phải lớn hơn 0", Toast.LENGTH_SHORT).show();
            return;
        }

        AppDataBase.databaseWriteExecutor.execute(() -> {
            if (isEditMode && currentItem != null) {
                double currentQty = currentItem.getQuantity();
                double newQty = isCheckIn ? currentQty + qtyChange : currentQty - qtyChange;
                if (newQty < 0) newQty = 0;

                currentItem.setName(name);
                currentItem.setFormula(formula);
                currentItem.setLocation(location);
                currentItem.setExpiredDate(expiry);
                currentItem.setUnits(units);
                currentItem.setStatusInventoryItem(StatusInventoryItem.valueOf(statusStr));
                currentItem.setQuantity(newQty);

                db.inventoryItemDao().update(currentItem);

                insertLog(currentItem.getId(), (isCheckIn ? "Nhập " : "Xuất ") + qtyChange + " " + units);

                double finalNewQty = newQty;
                runOnUiThread(() -> {
                    tvCurrentQuantity.setText("Hiện có: " + finalNewQty + " " + units);
                    Toast.makeText(this, "Cập nhật thành công!", Toast.LENGTH_SHORT).show();
                });

            } else {
                InventoryItem newItem = new InventoryItem();
                newItem.setName(name);
                newItem.setFormula(formula);
                newItem.setLocation(location);
                newItem.setExpiredDate(expiry);
                newItem.setUnits(units);
                newItem.setStatusInventoryItem(StatusInventoryItem.valueOf(statusStr));
                newItem.setQuantity(qtyChange);
                newItem.setUserId(currentUserId);
                newItem.setSopId(1); // SOP mặc định (nếu có thể thay sau này)

                long id = db.inventoryItemDao().insert(newItem);
                insertLog((int) id, "Thêm mới vật tư với số lượng " + qtyChange + " " + units);

                runOnUiThread(() -> {
                    Toast.makeText(this, "Đã thêm vật tư mới!", Toast.LENGTH_SHORT).show();
                    finish();
                });
            }
        });
    }

    private void insertLog(int itemId, String desc) {
        InventoryLogs log = new InventoryLogs();
        log.setInventoryItemId(itemId);
        log.setDescription(desc);
        log.setDate(dateFormat.format(calendar.getTime()));
        log.setUserID(currentUserId);

        db.inventoryLogDao().insert(log);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
