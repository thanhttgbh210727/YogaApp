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

        // Handle system insets for edge-to-edge layout
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (view, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            view.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize views and data structures
        inputSearch = findViewById(R.id.searchInput);
        searchBtn = findViewById(R.id.searchBtn);
        addBtn = findViewById(R.id.addBtn);
        recyclerView = findViewById(R.id.recyleView);

        classId = new ArrayList<>();
        dayOfWeek = new ArrayList<>();
        time = new ArrayList<>();
        capacity = new ArrayList<>();
        duration = new ArrayList<>();
        price = new ArrayList<>();
        type = new ArrayList<>();
        description = new ArrayList<>();
        teacher = new ArrayList<>();
        adapter = new Adapter(this, this, classId, dayOfWeek, time, capacity, duration, price, type, description, teacher);
        db = new Database(this);

        // Configure RecyclerView
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        loadClasses();

        // Add class button action
        addBtn.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, Create.class);
            startActivity(intent);
        });

        // Search button action
        searchBtn.setOnClickListener(view -> {
            String query = inputSearch.getText().toString();
            clearClassLists();
            loadSearchResults(query);
            adapter.notifyDataSetChanged();
        });
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
        clearClassLists();
        loadClasses();
        adapter.notifyDataSetChanged();
    }

    // Fetch classes matching the search query
    private void loadSearchResults(String query) {
        Cursor cursor = db.searchClassByName(query);
        if (cursor.getCount() == 0) {
            Toast.makeText(this, "Try Again", Toast.LENGTH_SHORT).show();
        } else {
            while (cursor.moveToNext()) {
                populateClassLists(cursor);
            }
        }
        Toast.makeText(this, "Fill teacher name for searching", Toast.LENGTH_SHORT).show();
    }

    // Fetch and display all classes
    private void loadClasses() {
        Cursor cursor = db.getClasses();
        if (cursor.getCount() == 0) {
            Toast.makeText(this, "No records available", Toast.LENGTH_SHORT).show();
        } else {
            while (cursor.moveToNext()) {
                populateClassLists(cursor);
            }
        }
    }

    // Clear class lists for fresh data
    private void clearClassLists() {
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

    // Add data from cursor to lists
    private void populateClassLists(Cursor cursor) {
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
