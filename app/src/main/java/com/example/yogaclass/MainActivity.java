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
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

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

        adapter = new Adapter(MainActivity.this, this, classId, dayOfWeek, time, capacity, duration, price, type, description, teacher);
        db = new Database(MainActivity.this);

        //set up adapter
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));

        //show class
        showClass();


        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, Create.class);
                startActivity(intent);

            }
        });

        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText input = findViewById(R.id.searchInput);
                String _input = input.getText().toString();
                classId.clear();
                dayOfWeek.clear();
                time.clear();
                capacity.clear();
                duration.clear();
                price.clear();
                type.clear();
                description.clear();
                teacher.clear();
                showSearchResult(_input);
                adapter.notifyDataSetChanged();
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1) {
            recreate();
        }
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        classId.clear();
        dayOfWeek.clear();
        time.clear();
        capacity.clear();
        duration.clear();
        price.clear();
        type.clear();
        description.clear();
        teacher.clear();

        showClass();
        adapter.notifyDataSetChanged();
    }

    private void showSearchResult(String input) {
        Cursor cursor = db.searchClassByName(input);
        if (cursor.getCount() == 0) {
            Toast.makeText(this, "There is 0 class", Toast.LENGTH_SHORT).show();
        }
        else {
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
        Toast.makeText(this, "xcxcx", Toast.LENGTH_SHORT).show();
    }

    private void showClass() {
        Cursor cursor = db.getClasses();
        if (cursor.getCount() == 0) {
            Toast.makeText(this, "There is 0 class", Toast.LENGTH_SHORT).show();
        }
        else {
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
}