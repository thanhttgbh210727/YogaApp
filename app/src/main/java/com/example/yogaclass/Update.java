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

    Spinner dayOfWeek;
    String classId;
    TextView inputTime, timeBtn;
    EditText inputCapacity, inputDuration, inputPrice, inputType, inputDescription, inputTeacher;
    Button updateBtn, deleteBtn;
    Database db;

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

        db = new Database(Update.this);

        // Initialize UI components
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

        setClassDetail();

        // Update button click listener
        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isInputValid()) {
                    // Retrieve input values
                    String _dayOfWeek = dayOfWeek.getSelectedItem().toString();
                    String _time = inputTime.getText().toString();
                    int _capacity = Integer.parseInt(inputCapacity.getText().toString());
                    int _duration = Integer.parseInt(inputDuration.getText().toString());
                    double _price = Double.parseDouble(inputPrice.getText().toString());
                    String _type = inputType.getText().toString();
                    String _description = inputDescription.getText().toString();
                    String _teacher = inputTeacher.getText().toString();

                    // Update class in the database
                    db.updateClass(Update.this, classId, _dayOfWeek, _time, _capacity, _duration, _price, _type, _description, _teacher);
                    finish();
                }
            }
        });

        // Delete button click listener
        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                db.deleteClass(Update.this, classId);
                finish();
            }
        });

        // Time picker button click listener
        timeBtn.setOnClickListener(val -> showTimePicker(inputTime));
        showDropdownList(dayOfWeek);
    }

    // Display a dropdown list for days of the week
    private void showDropdownList(Spinner spinner) {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.dayofweek, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                adapterView.getItemAtPosition(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
    }

    // Show a time picker dialog to select the time
    private void showTimePicker(TextView inputTime) {
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(this, (TimePicker view, int selectedHour, int selectedMinute) -> {
            String time = String.format("%02d:%02d", selectedHour, selectedMinute);
            inputTime.setText(time);
        }, hour, minute, true);
        timePickerDialog.show();
    }

    // Load class details into the UI
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
            setDayOfWeek(dayOfWeek, getIntent().getStringExtra("day"));
            inputTime.setText(getIntent().getStringExtra("time"));
            inputCapacity.setText(getIntent().getStringExtra("capacity"));
            inputDuration.setText(getIntent().getStringExtra("duration"));
            inputPrice.setText(getIntent().getStringExtra("price"));
            inputType.setText(getIntent().getStringExtra("type"));
            inputDescription.setText(getIntent().getStringExtra("description"));
            inputTeacher.setText(getIntent().getStringExtra("teacher"));
        } else {
            Toast.makeText(this, "Failed to load class data", Toast.LENGTH_SHORT).show();
        }
    }

    // Set day of the week in the spinner
    private void setDayOfWeek(Spinner spinner, String value) {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.dayofweek, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        int index = adapter.getPosition(value);
        if (index >= 0)
            spinner.setSelection(index);
    }

    // Validate user input
    private boolean isInputValid() {
        if (inputTime.getText().toString().isEmpty()) {
            showError("Please pick a time for the class");
            return false;
        }
        if (inputCapacity.getText().toString().isEmpty() || Integer.parseInt(inputCapacity.getText().toString()) <= 0) {
            showError("Please enter a valid capacity");
            return false;
        }
        if (inputDuration.getText().toString().isEmpty() || Integer.parseInt(inputDuration.getText().toString()) <= 0) {
            showError("Please enter a valid duration");
            return false;
        }
        if (inputPrice.getText().toString().isEmpty() || Double.parseDouble(inputPrice.getText().toString()) < 0) {
            showError("Please enter a valid price");
            return false;
        }
        if (inputType.getText().toString().isEmpty()) {
            showError("Please enter a type of yoga class");
            return false;
        }
        if (inputTeacher.getText().toString().isEmpty()) {
            showError("Please enter the teacher's name");
            return false;
        }
        return true;
    }

    // Display an error message to the user
    private void showError(String message) {
        Toast.makeText(Update.this, message, Toast.LENGTH_SHORT).show();
    }
}
