package com.example.yogaclass;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class Adapter extends RecyclerView.Adapter<Adapter.MyViewHolder> {
    private Context context;
    private ArrayList class_id, dayOfWeek, time, capacity, duration, price, type, description, teacher;
    private Activity activity;

    public Adapter(Activity activity, Context context, ArrayList class_id, ArrayList dayOfWeek, ArrayList time, ArrayList capacity, ArrayList duration, ArrayList price, ArrayList type, ArrayList description, ArrayList teacher) {
        this.context = context;
        this.class_id = class_id;
        this.dayOfWeek = dayOfWeek;
        this.time = time;
        this.capacity = capacity;
        this.duration = duration;
        this.price = price;
        this.type = type;
        this.description = description;
        this.teacher = teacher;
        this.activity = activity;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.card_view, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder._teacher.setText(String.valueOf(teacher.get(position)));
        holder._type.setText(String.valueOf(type.get(position)));
        holder._time.setText(String.valueOf(time.get(position)));
        holder.classYoga.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int index = holder.getAdapterPosition();
                if (index != RecyclerView.NO_POSITION) {
                    Intent intent = new Intent(context, Update.class);
                    intent.putExtra("class", String.valueOf(class_id.get(index)));
                    intent.putExtra("day", String.valueOf(dayOfWeek.get(index)));
                    intent.putExtra("time", String.valueOf(time.get(index)));
                    intent.putExtra("capacity", String.valueOf(capacity.get(index)));
                    intent.putExtra("duration", String.valueOf(duration.get(index)));
                    intent.putExtra("price", String.valueOf(price.get(index)));
                    intent.putExtra("type", String.valueOf(type.get(index)));
                    intent.putExtra("description", String.valueOf(description.get(index)));
                    intent.putExtra("teacher", String.valueOf(teacher.get(index)));
                    activity.startActivityForResult(intent, 1);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return class_id.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView _teacher, _type, _time;
        CardView classYoga;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            _teacher = itemView.findViewById(R.id.teacher);
            _type = itemView.findViewById(R.id.type);
            _time = itemView.findViewById(R.id.time);
            classYoga = itemView.findViewById(R.id.classYoga);
        }
    }
}
