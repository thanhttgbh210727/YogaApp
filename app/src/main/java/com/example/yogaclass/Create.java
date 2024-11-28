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
    Spinner dayOfWeek;
    TextView inputTime, timeBtn;
    EditText inputCapacity, inputDuration, inputPrice, inputType, inputDescription, inputTeacher;
    Button addBtn;
    Database db;
    boolean isPickedTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_class);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        db = new Database(Create.this);

        // Initialize UI elements
        dayOfWeek = findViewById(R.id.inputDay);
        inputTime = findViewById(R.id.inputTime);
        timeBtn = findViewById(R.id.timeBtn);
        inputCapacity = findViewById(R.id.inputCapacity);
        inputDuration = findViewById(R.id.inputDuration);
        inputPrice = findViewById(R.id.inputPrice);
        inputType = findViewById(R.id.inputType);
        inputDescription = findViewById(R.id.inputDescription);
        addBtn = findViewById(R.id.addBtn);
        inputTeacher = findViewById(R.id.inputTeacher);

        isPickedTime = false;
        showDropdownList(dayOfWeek);

        // Set up time picker for the time button
        timeBtn.setOnClickListener(x -> showTimePicker(inputTime));


        addBtn.setOnClickListener(new View.OnClickListener() {
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

                    // Add class to the database
                    db.addClass(Create.this, _dayOfWeek, _time, _capacity, _duration, _price, _type, _description, _teacher);
                    finish();
                }
            }
        });
    }


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

    private void showTimePicker(TextView inputTime) {
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(this, (TimePicker view, int selectedHour, int selectedMinute) -> {
            String time = String.format("%02d:%02d", selectedHour, selectedMinute);
            inputTime.setText(time);
            isPickedTime = true;
        }, hour, minute, true);
        timePickerDialog.show();
    }

    // Validate input
    private boolean isInputValid() {
        if (!isPickedTime) {
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

    private void showError(String message) {
        Toast.makeText(Create.this, message, Toast.LENGTH_SHORT).show();
    }
}
