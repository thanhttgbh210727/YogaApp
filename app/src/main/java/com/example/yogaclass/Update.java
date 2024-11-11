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
    private TextView inputTime;
    private EditText inputCapacity, inputDuration, inputPrice, inputType, inputDescription, inputTeacher;
    private Database db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_update);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        db = new Database(this);

        // Initialize UI components
        initializeUI();

        // Load class details into fields
        setClassDetail();

        // Set listeners for buttons
        findViewById(R.id.updateBtn).setOnClickListener(v -> updateClassDetails());
        findViewById(R.id.deleteBtn).setOnClickListener(v -> deleteClass());
        findViewById(R.id.timeBtn).setOnClickListener(v -> showTimePicker());
    }

    private void initializeUI() {
        dayOfWeek = findViewById(R.id.inputDay);
        inputTime = findViewById(R.id.inputTime);
        inputCapacity = findViewById(R.id.inputCapacity);
        inputDuration = findViewById(R.id.inputDuration);
        inputPrice = findViewById(R.id.inputPrice);
        inputType = findViewById(R.id.inputType);
        inputDescription = findViewById(R.id.inputDescription);
        inputTeacher = findViewById(R.id.inputTeacher);

        // Initialize and populate dayOfWeek Spinner
        setupDayOfWeekSpinner();
    }

    private void setupDayOfWeekSpinner() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.dayofweek, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dayOfWeek.setAdapter(adapter);
    }

    private void showTimePicker() {
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(this, (view, selectedHour, selectedMinute) -> {
            String time = String.format("%02d:%02d", selectedHour, selectedMinute);
            inputTime.setText(time);
        }, hour, minute, true);
        timePickerDialog.show();
    }

    private void setClassDetail() {
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
            String day = getIntent().getStringExtra("day");
            String time = getIntent().getStringExtra("time");
            String capacity = getIntent().getStringExtra("capacity");
            String duration = getIntent().getStringExtra("duration");
            String price = getIntent().getStringExtra("price");
            String type = getIntent().getStringExtra("type");
            String description = getIntent().getStringExtra("description");
            String teacher = getIntent().getStringExtra("teacher");

            // Populate fields with data
            selectDayOfWeek(day);
            inputTime.setText(time);
            inputCapacity.setText(capacity);
            inputDuration.setText(duration);
            inputPrice.setText(price);
            inputType.setText(type);
            inputDescription.setText(description);
            inputTeacher.setText(teacher);
        } else {
            Toast.makeText(this, "Load data fail", Toast.LENGTH_SHORT).show();
        }
    }

    private void selectDayOfWeek(String day) {
        ArrayAdapter<CharSequence> adapter = (ArrayAdapter<CharSequence>) dayOfWeek.getAdapter();
        int index = adapter.getPosition(day);
        if (index >= 0) dayOfWeek.setSelection(index);
    }

    private void updateClassDetails() {
        try {
            String selectedDay = dayOfWeek.getSelectedItem().toString();
            String time = inputTime.getText().toString();
            int capacity = Integer.parseInt(inputCapacity.getText().toString());
            int duration = Integer.parseInt(inputDuration.getText().toString());
            double price = Double.parseDouble(inputPrice.getText().toString());
            String type = inputType.getText().toString();
            String description = inputDescription.getText().toString();
            String teacher = inputTeacher.getText().toString();

            db.updateClass(this, classId, selectedDay, time, capacity, duration, price, type, description, teacher);
            finish();
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Invalid input", Toast.LENGTH_SHORT).show();
        }
    }

    private void deleteClass() {
        db.deleteClass(this, classId);
        finish();
    }
}
