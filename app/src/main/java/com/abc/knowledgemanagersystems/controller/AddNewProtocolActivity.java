package com.abc.knowledgemanagersystems.controller;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.abc.knowledgemanagersystems.R;
import com.abc.knowledgemanagersystems.db.AppDataBase;
import com.abc.knowledgemanagersystems.model.ExperimentLogs;
import com.abc.knowledgemanagersystems.model.Sops;
import com.google.android.material.button.MaterialButton;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
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

    private MaterialButton btnChooseFilePath, btnChooseSafeDataSheet;
    private TextView tvFilePathName, tvSafeDataSheetName;

    private List<ExperimentLogs> experimentList = new ArrayList<>();
    private int selectedExperimentId = -1;

    private String selectedFilePath = "";
    private String selectedSafeDataSheet = "";

    private static final int REQUEST_FILE_PATH = 2001;
    private static final int REQUEST_SAFE_DATA_SHEET = 2002;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_protocol);

        edtSopsName = findViewById(R.id.edtSopsName);
        edtTitle = findViewById(R.id.edtTitle);
        edtDescription = findViewById(R.id.edtDescription);
        spinnerExperiment = findViewById(R.id.spinnerExperiment);
        btnSave = findViewById(R.id.btnSaveProtocol);

        btnChooseFilePath = findViewById(R.id.btnChooseFilePath);
        btnChooseSafeDataSheet = findViewById(R.id.btnChooseSafeDataSheet);
        tvFilePathName = findViewById(R.id.tvFilePathName);
        tvSafeDataSheetName = findViewById(R.id.tvSafeDataSheetName);

        loadExperiments();

        btnChooseFilePath.setOnClickListener(v -> openFilePicker(REQUEST_FILE_PATH));
        btnChooseSafeDataSheet.setOnClickListener(v -> openFilePicker(REQUEST_SAFE_DATA_SHEET));
        btnSave.setOnClickListener(v -> saveSop());
    }

    private void loadExperiments() {
        Executors.newSingleThreadExecutor().execute(() -> {
            experimentList = AppDataBase.getInstance(this)
                    .experimentLogsDao()
                    .getAllExperiments();

            List<String> names = new ArrayList<>();
            for (ExperimentLogs exp : experimentList) {
                names.add("" + exp.getId());
            }

            runOnUiThread(() -> {
                ArrayAdapter<String> adapter = new ArrayAdapter<>(
                        this, android.R.layout.simple_spinner_dropdown_item, names);
                spinnerExperiment.setAdapter(adapter);

                spinnerExperiment.setOnItemSelectedListener(new android.widget.AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(android.widget.AdapterView<?> parent, android.view.View view, int position, long id) {
                        selectedExperimentId = experimentList.get(position).getId();
                    }

                    @Override
                    public void onNothingSelected(android.widget.AdapterView<?> parent) {
                        selectedExperimentId = -1;
                    }
                });
            });
        });
    }

    private void openFilePicker(int requestCode) {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("application/pdf");
        startActivityForResult(intent, requestCode);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && data != null) {
            Uri uri = data.getData();
            if (uri != null) {
                String fileName = "file_" + System.currentTimeMillis() + ".pdf";
                String savedPath = copyPdfToInternalStorage(uri, fileName);
                if (savedPath != null) {
                    if (requestCode == REQUEST_FILE_PATH) {
                        selectedFilePath = savedPath;
                        tvFilePathName.setText(fileName);
                    } else if (requestCode == REQUEST_SAFE_DATA_SHEET) {
                        selectedSafeDataSheet = savedPath;
                        tvSafeDataSheetName.setText(fileName);
                    }
                } else {
                    Toast.makeText(this, "Error saving file", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private String copyPdfToInternalStorage(Uri uri, String fileName) {
        try {
            InputStream inputStream = getContentResolver().openInputStream(uri);
            File file = new File(getFilesDir(), fileName);
            OutputStream outputStream = new FileOutputStream(file);

            byte[] buffer = new byte[1024];
            int length;
            while ((length = inputStream.read(buffer)) > 0) {
                outputStream.write(buffer, 0, length);
            }

            outputStream.close();
            inputStream.close();

            return file.getAbsolutePath();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    private void saveSop() {
        String sopsName = edtSopsName.getText().toString().trim();
        String title = edtTitle.getText().toString().trim();
        String description = edtDescription.getText().toString().trim();

        if (sopsName.isEmpty()) {
            Toast.makeText(this, "Please enter SOP name", Toast.LENGTH_SHORT).show();
            return;
        }

        if (title.isEmpty()) {
            Toast.makeText(this, "Please enter SOP title", Toast.LENGTH_SHORT).show();
            return;
        }

        if (description.isEmpty()) {
            Toast.makeText(this, "Please enter SOP description", Toast.LENGTH_SHORT).show();
            return;
        }

        if (sopsName.isEmpty() || title.isEmpty() || selectedExperimentId == -1) {
            Toast.makeText(this, "Please fill all fields and select an experiment!", Toast.LENGTH_SHORT).show();
            return;
        }

        String currentDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());

        Sops sops = new Sops();
        sops.setSopsName(sopsName);
        sops.setTitle(title);
        sops.setDescription(description);
        sops.setCreateAt(currentDate);
        sops.setFilePath(selectedFilePath);
        sops.setSafeDataSheet(selectedSafeDataSheet);
        sops.setExperimentId(selectedExperimentId);

        Executors.newSingleThreadExecutor().execute(() -> {
            AppDataBase.getInstance(this).sopsDao().insert(sops);
            runOnUiThread(() -> {
                Toast.makeText(this, "SOP added successfully!", Toast.LENGTH_SHORT).show();
                finish();
            });
        });
    }
}