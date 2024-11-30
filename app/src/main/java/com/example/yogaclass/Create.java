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

public class Create extends AppCompatActivity {
    private Spinner dayOfWeek;
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
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (view, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            view.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        db = new Database(this);
        initializeViews();

        isTimePicked = false;
        populateDayOfWeekSpinner();

        timeBtn.setOnClickListener(v -> openTimePickerDialog());

        addBtn.setOnClickListener(v -> {
            if (validateInput()) {
                addClassToDatabase();
                finish();
            }
        });
    }

    private void initializeViews() {
        dayOfWeek = findViewById(R.id.inputDay);
        inputTime = findViewById(R.id.inputTime);
        timeBtn = findViewById(R.id.timeBtn);
        inputCapacity = findViewById(R.id.inputCapacity);
        inputDuration = findViewById(R.id.inputDuration);
        inputPrice = findViewById(R.id.inputPrice);
        inputType = findViewById(R.id.inputType);
        inputDescription = findViewById(R.id.inputDescription);
        inputTeacher = findViewById(R.id.inputTeacher);
        addBtn = findViewById(R.id.addBtn);
    }

    private void populateDayOfWeekSpinner() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this, R.array.dayofweek, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dayOfWeek.setAdapter(adapter);

        dayOfWeek.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                parent.getItemAtPosition(position); // Placeholder for future use
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // No action needed
            }
        });
    }

    private void openTimePickerDialog() {
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        new TimePickerDialog(this, (view, selectedHour, selectedMinute) -> {
            String formattedTime = String.format("%02d:%02d", selectedHour, selectedMinute);
            inputTime.setText(formattedTime);
            isTimePicked = true;
        }, hour, minute, true).show();
    }

    private boolean validateInput() {
        if (!isTimePicked) {
            showErrorMessage("Please select a time for the class.");
            return false;
        }
        if (inputCapacity.getText().toString().isEmpty() || Integer.parseInt(inputCapacity.getText().toString()) <= 0) {
            showErrorMessage("Capacity must be a positive number.");
            return false;
        }
        if (inputDuration.getText().toString().isEmpty() || Integer.parseInt(inputDuration.getText().toString()) <= 0) {
            showErrorMessage("Duration must be a positive number.");
            return false;
        }
        if (inputPrice.getText().toString().isEmpty() || Double.parseDouble(inputPrice.getText().toString()) < 0) {
            showErrorMessage("Price cannot be negative.");
            return false;
        }
        if (inputType.getText().toString().trim().isEmpty()) {
            showErrorMessage("Type of yoga class cannot be empty.");
            return false;
        }
        if (inputTeacher.getText().toString().trim().isEmpty()) {
            showErrorMessage("Please provide the teacher's name.");
            return false;
        }
        return true;
    }

    private void addClassToDatabase() {
        String selectedDay = dayOfWeek.getSelectedItem().toString();
        String selectedTime = inputTime.getText().toString();
        int capacity = Integer.parseInt(inputCapacity.getText().toString());
        int duration = Integer.parseInt(inputDuration.getText().toString());
        double price = Double.parseDouble(inputPrice.getText().toString());
        String type = inputType.getText().toString();
        String description = inputDescription.getText().toString();
        String teacher = inputTeacher.getText().toString();

        db.addClass(this, selectedDay, selectedTime, capacity, duration, price, type, description, teacher);
    }

    private void showErrorMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
