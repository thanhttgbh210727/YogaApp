package com.example.yogaclass;

import android.app.TimePickerDialog;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.Calendar;

public class Create extends AppCompatActivity {
    private Spinner dayOfWeekSpinner;
    private TextView inputTime, timeBtn;
    private EditText inputCapacity, inputDuration, inputPrice, inputType, inputDescription, inputTeacher;
    private Button addBtn;
    private Database db;
    private boolean isTimePicked;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_class);

        setupInsets();
        initializeFields();
        setupDropdown(dayOfWeekSpinner);
        setupListeners();
    }

    private void setupInsets() {
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void initializeFields() {
        db = new Database(this);
        dayOfWeekSpinner = findViewById(R.id.inputDay);
        inputTime = findViewById(R.id.inputTime);
        timeBtn = findViewById(R.id.timeBtn);
        inputCapacity = findViewById(R.id.inputCapacity);
        inputDuration = findViewById(R.id.inputDuration);
        inputPrice = findViewById(R.id.inputPrice);
        inputType = findViewById(R.id.inputType);
        inputDescription = findViewById(R.id.inputDescription);
        addBtn = findViewById(R.id.addBtn);
        inputTeacher = findViewById(R.id.inputTeacher);
        isTimePicked = false;
    }

    private void setupDropdown(Spinner spinner) {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.dayofweek, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }

    private void setupListeners() {
        timeBtn.setOnClickListener(view -> showTimePicker());

        addBtn.setOnClickListener(view -> {
            String selectedDayOfWeek = dayOfWeekSpinner.getSelectedItem().toString();
            String selectedTime = inputTime.getText().toString();
            int capacity = Integer.parseInt(inputCapacity.getText().toString());
            int duration = Integer.parseInt(inputDuration.getText().toString());
            double price = Double.parseDouble(inputPrice.getText().toString());
            String type = inputType.getText().toString();
            String description = inputDescription.getText().toString();
            String teacher = inputTeacher.getText().toString();

            db.addClass(this, selectedDayOfWeek, selectedTime, capacity, duration, price, type, description, teacher);
            finish();
        });
    }

    private void showTimePicker() {
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        new TimePickerDialog(this, (view, selectedHour, selectedMinute) -> {
            inputTime.setText(String.format("%02d:%02d", selectedHour, selectedMinute));
            isTimePicked = true;
        }, hour, minute, true).show();
    }
}
