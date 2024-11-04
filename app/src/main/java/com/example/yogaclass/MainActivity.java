package com.example.yogaclass;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
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
                Intent intent = new Intent(MainActivity.this, AddClass.class);
                startActivity(intent);

            }
        });

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