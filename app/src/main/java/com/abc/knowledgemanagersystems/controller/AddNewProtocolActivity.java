package com.abc.knowledgemanagersystems.controller;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.abc.knowledgemanagersystems.R;
import com.abc.knowledgemanagersystems.db.AppDataBase;
import com.abc.knowledgemanagersystems.model.ExperimentLogs;
import com.abc.knowledgemanagersystems.model.Sops;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.Executors;

public class AddNewProtocolActivity extends AppCompatActivity {

    private EditText edtSopsName, edtTitle, edtDescription;
    private Spinner spinnerExperiment;
    private Button btnSave;

    private List<ExperimentLogs> experimentList = new ArrayList<>();
    private int selectedExperimentId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_protocol);

        edtSopsName = findViewById(R.id.edtSopsName);
        edtTitle = findViewById(R.id.edtTitle);
        edtDescription = findViewById(R.id.edtDescription);
//        spinnerExperiment = findViewById(R.id.spinnerExperiment);
        btnSave = findViewById(R.id.btnSaveProtocol);

        loadExperiments();

        btnSave.setOnClickListener(v -> saveSop());
    }

    private void loadExperiments() {
        Executors.newSingleThreadExecutor().execute(() -> {
            experimentList = AppDataBase.getInstance(this)
                    .experimentLogsDao()
                    .getAllExperiments();

//            List<String> names = new ArrayList<>();
//            for (ExperimentLogs exp : experimentList) {
//                names.add("Log #" + exp.getId() + " | " + exp.getLogDate());
//            }
//
//            runOnUiThread(() -> {
//                ArrayAdapter<String> adapter = new ArrayAdapter<>(
//                        this, android.R.layout.simple_spinner_dropdown_item, names);
//                spinnerExperiment.setAdapter(adapter);
//
//                spinnerExperiment.setOnItemSelectedListener(new android.widget.AdapterView.OnItemSelectedListener() {
//                    @Override
//                    public void onItemSelected(android.widget.AdapterView<?> parent, android.view.View view, int position, long id) {
//                        selectedExperimentId = experimentList.get(position).getId();
//                    }
//
//                    @Override
//                    public void onNothingSelected(android.widget.AdapterView<?> parent) {
//                        selectedExperimentId = -1;
//                    }
//                });
//            });
        });
    }

    private void saveSop() {
        String sopsName = edtSopsName.getText().toString().trim();
        String title = edtTitle.getText().toString().trim();
        String description = edtDescription.getText().toString().trim();

//        if (sopsName.isEmpty() || title.isEmpty() || selectedExperimentId == -1) {
//            Toast.makeText(this, "Please fill all fields and select an experiment!", Toast.LENGTH_SHORT).show();
//            return;
//        }

        String currentDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());

        Sops sops = new Sops();
        sops.setSopsName(sopsName);
        sops.setTitle(title);
        sops.setDescription(description);
        sops.setCreateAt(currentDate);
        sops.setFilePath("");
        sops.setSafeDataSheet("");
//        sops.setExperimentId(selectedExperimentId);

        Executors.newSingleThreadExecutor().execute(() -> {
            AppDataBase.getInstance(this).sopsDao().insert(sops);
            runOnUiThread(() -> {
                Toast.makeText(this, "SOP added successfully!", Toast.LENGTH_SHORT).show();
                finish();
            });
        });
    }
}