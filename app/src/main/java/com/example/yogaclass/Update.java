package com.example.yogaclass;

import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.Calendar;

public class Update extends AppCompatActivity {

    private Spinner dayOfWeek;
    private String classId;
    private TextView inputTime, timeBtn;
    private EditText inputCapacity, inputDuration, inputPrice, inputType, inputDescription, inputTeacher;
    private Button updateBtn, deleteBtn;
    private Database db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_update);
        configureInsets();
        initializeDatabase();
        initializeUIComponents();
        loadClassData();

        updateBtn.setOnClickListener(v -> {
            if (validateInputs()) {
                updateClassDetails();
                finish();
            }
        });

        deleteBtn.setOnClickListener(v -> {
            db.deleteClass(Update.this, classId);
            finish();
        });

        timeBtn.setOnClickListener(v -> openTimePickerDialog());
        configureDayOfWeekSpinner();
    }

    private void configureInsets() {
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (view, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            view.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void initializeDatabase() {
        db = new Database(this);
    }

    private void initializeUIComponents() {
        dayOfWeek = findViewById(R.id.inputDay);
        inputTime = findViewById(R.id.inputTime);
        timeBtn = findViewById(R.id.timeBtn);
        inputCapacity = findViewById(R.id.inputCapacity);
        inputDuration = findViewById(R.id.inputDuration);
        inputPrice = findViewById(R.id.inputPrice);
        inputType = findViewById(R.id.inputType);
        inputDescription = findViewById(R.id.inputDescription);
        updateBtn = findViewById(R.id.updateBtn);
        deleteBtn = findViewById(R.id.deleteBtn);
        inputTeacher = findViewById(R.id.inputTeacher);
    }

    private void configureDayOfWeekSpinner() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this, R.array.dayofweek, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dayOfWeek.setAdapter(adapter);

        dayOfWeek.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                parent.getItemAtPosition(position); // Placeholder for selected item
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // No action needed
            }
        });
    }

    private void openTimePickerDialog() {
        Calendar calendar = Calendar.getInstance();
        int currentHour = calendar.get(Calendar.HOUR_OF_DAY);
        int currentMinute = calendar.get(Calendar.MINUTE);

        TimePickerDialog timePicker = new TimePickerDialog(
                this,
                (TimePicker view, int hourOfDay, int minute) -> {
                    String formattedTime = String.format("%02d:%02d", hourOfDay, minute);
                    inputTime.setText(formattedTime);
                },
                currentHour,
                currentMinute,
                true
        );
        timePicker.show();
    }

    private void loadClassData() {
        if (getIntent().hasExtra("class") &&
                getIntent().hasExtra("day") &&
                getIntent().hasExtra("time") &&
                getIntent().hasExtra("capacity") &&
                getIntent().hasExtra("duration") &&
                getIntent().hasExtra("price") &&
                getIntent().hasExtra("type") &&
                getIntent().hasExtra("description") &&
                getIntent().hasExtra("teacher")) {
            classId = getIntent().getStringExtra("class");
            setSpinnerValue(dayOfWeek, getIntent().getStringExtra("day"));
            inputTime.setText(getIntent().getStringExtra("time"));
            inputCapacity.setText(getIntent().getStringExtra("capacity"));
            inputDuration.setText(getIntent().getStringExtra("duration"));
            inputPrice.setText(getIntent().getStringExtra("price"));
            inputType.setText(getIntent().getStringExtra("type"));
            inputDescription.setText(getIntent().getStringExtra("description"));
            inputTeacher.setText(getIntent().getStringExtra("teacher"));
        } else {
            Toast.makeText(this, "Failed to load class details.", Toast.LENGTH_SHORT).show();
        }
    }

    private void setSpinnerValue(Spinner spinner, String value) {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this, R.array.dayofweek, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        int index = adapter.getPosition(value);
        if (index >= 0) {
            spinner.setSelection(index);
        }
    }

    private boolean validateInputs() {
        if (inputTime.getText().toString().isEmpty()) {
            showErrorToast("Time is required.");
            return false;
        }
        if (inputCapacity.getText().toString().isEmpty() || Integer.parseInt(inputCapacity.getText().toString()) <= 0) {
            showErrorToast("Capacity must be a positive number.");
            return false;
        }
        if (inputDuration.getText().toString().isEmpty() || Integer.parseInt(inputDuration.getText().toString()) <= 0) {
            showErrorToast("Duration must be a positive number.");
            return false;
        }
        if (inputPrice.getText().toString().isEmpty() || Double.parseDouble(inputPrice.getText().toString()) < 0) {
            showErrorToast("Price must be a non-negative number.");
            return false;
        }
        if (inputType.getText().toString().trim().isEmpty()) {
            showErrorToast("Yoga class type is required.");
            return false;
        }
        if (inputTeacher.getText().toString().trim().isEmpty()) {
            showErrorToast("Teacher name is required.");
            return false;
        }
        return true;
    }

    private void showErrorToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    private void updateClassDetails() {
        String selectedDay = dayOfWeek.getSelectedItem().toString();
        String selectedTime = inputTime.getText().toString();
        int capacity = Integer.parseInt(inputCapacity.getText().toString());
        int duration = Integer.parseInt(inputDuration.getText().toString());
        double price = Double.parseDouble(inputPrice.getText().toString());
        String type = inputType.getText().toString();
        String description = inputDescription.getText().toString();
        String teacher = inputTeacher.getText().toString();

        db.updateClass(this, classId, selectedDay, selectedTime, capacity, duration, price, type, description, teacher);
    }
}
