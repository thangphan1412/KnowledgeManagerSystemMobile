package com.abc.knowledgemanagersystems;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.abc.knowledgemanagersystems.databinding.ActivityCreateExperimentBinding;
import com.abc.knowledgemanagersystems.model.Experiment;
import com.abc.knowledgemanagersystems.model.Sops;
import com.abc.knowledgemanagersystems.repository.ExperimentRepository;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CreateExperimentActivity extends AppCompatActivity {
    private ActivityCreateExperimentBinding binding;
    private Calendar calendar;
    private ExperimentRepository repository;
    private ExecutorService executorService;
    private List<Sops> availableSops;
    private long selectedSopId = -1;


    private static final long CURRENT_USER_ID = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityCreateExperimentBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        repository = new ExperimentRepository(this);
        calendar = Calendar.getInstance();

        executorService = Executors.newSingleThreadExecutor();

        binding.editTextStartDate.setOnClickListener(v -> showDatePickerDialog());
        binding.buttonCreateExperiment.setOnClickListener(v -> createExperimentRecord());

        loadSops();
    }

    private void loadSops() {
        executorService.execute(() -> {
            try {
                availableSops = repository.getAllSops();
                runOnUiThread(() -> setupProtocolAutoComplete(availableSops));
            } catch (Exception e) {
                runOnUiThread(() -> Toast.makeText(this, "Không thể tải danh sách Protocol.", Toast.LENGTH_SHORT).show());
            }
        });
    }

    private void setupProtocolAutoComplete(List<Sops> sopsList) {
        String[] sopNames = new String[sopsList.size()];
        for (int i = 0; i < sopsList.size(); i++) {
            sopNames[i] = sopsList.get(i).getSopsName();
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_dropdown_item_1line,
                sopNames
        );
        binding.autoCompleteProtocol.setAdapter(adapter);

        binding.autoCompleteProtocol.setOnItemClickListener((parent, view, position, id) -> {
            String selectedName = (String) parent.getItemAtPosition(position);
            for (Sops sop : sopsList) {
                if (sop.getSopsName().equals(selectedName)) {
                    selectedSopId = sop.getId();
                    return;
                }
            }
        });
    }

    private void showDatePickerDialog() {
        DatePickerDialog.OnDateSetListener dateSetListener = (view, year, monthOfYear, dayOfMonth) -> {
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, monthOfYear);
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateDateLabel();
        };

        new DatePickerDialog(
                CreateExperimentActivity.this,
                dateSetListener,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        ).show();
    }

    private void updateDateLabel() {
        String myFormat = "dd/MM/yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        binding.editTextStartDate.setText(sdf.format(calendar.getTime()));
    }


    private void createExperimentRecord() {
        String title = binding.editTextExperimentTitle.getText().toString().trim();
        String objective = binding.editTextHypothesisObjective.getText().toString().trim();
        String startDate = binding.editTextStartDate.getText().toString();

        if (TextUtils.isEmpty(title) || TextUtils.isEmpty(objective) || TextUtils.isEmpty(startDate) || selectedSopId == -1) {
            Toast.makeText(this, "Vui lòng điền đầy đủ thông tin và chọn Protocol.", Toast.LENGTH_LONG).show();
            return;
        }


        Experiment newExperiment = new Experiment();


        executorService.execute(() -> {
            try {
                long localId = repository.createAndSyncExperiment(newExperiment);
                runOnUiThread(() -> {
                    Toast.makeText(this, "Tạo bản ghi thành công (ID cục bộ: " + localId + ")", Toast.LENGTH_LONG).show();
                    finish();
                });
            } catch (Exception e) {
                runOnUiThread(() -> {
                    Toast.makeText(this, "Lỗi tạo thí nghiệm: " + e.getMessage(), Toast.LENGTH_LONG).show();
                });
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        executorService.shutdown();
    }
}
