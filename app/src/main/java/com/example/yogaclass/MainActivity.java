package com.example.yogaclass;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    EditText inputSearch;
    TextView searchBtn, addBtn;
    RecyclerView recyclerView;
    Adapter adapter;
    Database db;

    ArrayList<String> classId, dayOfWeek, time, capacity, duration, price, type, description, teacher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        // Apply system insets padding
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize views and data
        inputSearch = findViewById(R.id.searchInput);
        searchBtn = findViewById(R.id.searchBtn);
        addBtn = findViewById(R.id.addBtn);
        recyclerView = findViewById(R.id.recyleView);

        // Initialize lists and database
        initializeLists();
        db = new Database(this);

        // Set up RecyclerView
        adapter = new Adapter(this, this, classId, dayOfWeek, time, capacity, duration, price, type, description, teacher);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Load and display classes
        loadAndShowClasses();

        // Set button listeners
        addBtn.setOnClickListener(view -> startActivity(new Intent(MainActivity.this, Create.class)));
        searchBtn.setOnClickListener(view -> searchClasses(inputSearch.getText().toString()));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            recreate();
        }
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        loadAndShowClasses();
    }

    private void initializeLists() {
        classId = new ArrayList<>();
        dayOfWeek = new ArrayList<>();
        time = new ArrayList<>();
        capacity = new ArrayList<>();
        duration = new ArrayList<>();
        price = new ArrayList<>();
        type = new ArrayList<>();
        description = new ArrayList<>();
        teacher = new ArrayList<>();
    }

    private void clearLists() {
        classId.clear();
        dayOfWeek.clear();
        time.clear();
        capacity.clear();
        duration.clear();
        price.clear();
        type.clear();
        description.clear();
        teacher.clear();
    }

    private void loadAndShowClasses() {
        clearLists();
        Cursor cursor = db.getClasses();
        if (cursor.getCount() == 0) {
            Toast.makeText(this, "No records available", Toast.LENGTH_SHORT).show();
        } else {
            addDataFromCursor(cursor);
        }
        adapter.notifyDataSetChanged();
    }

    private void searchClasses(String query) {
        clearLists();
        Cursor cursor = db.searchClassByName(query);
        if (cursor.getCount() == 0) {
            Toast.makeText(this, "Try Again", Toast.LENGTH_SHORT).show();
        } else {
            addDataFromCursor(cursor);
        }
        adapter.notifyDataSetChanged();
    }

    private void addDataFromCursor(Cursor cursor) {
        while (cursor.moveToNext()) {
            classId.add(cursor.getString(0));
            dayOfWeek.add(cursor.getString(1));
            time.add(cursor.getString(2));
            capacity.add(cursor.getString(3));
            duration.add(cursor.getString(4));
            price.add(cursor.getString(5));
            type.add(cursor.getString(6));
            description.add(cursor.getString(7));
            teacher.add(cursor.getString(8));
        }
    }
}
