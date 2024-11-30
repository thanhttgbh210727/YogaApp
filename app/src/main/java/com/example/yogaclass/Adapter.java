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

    private final Context context;
    private final ArrayList class_id, dayOfWeek, time, capacity, duration, price, type, description, teacher;
    private final Activity activity;

    public Adapter(Activity activity, Context context, ArrayList class_id, ArrayList dayOfWeek, ArrayList time, ArrayList capacity, ArrayList duration, ArrayList price, ArrayList type, ArrayList description, ArrayList teacher) {
        this.activity = activity;
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
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View itemView = inflater.inflate(R.layout.card_view, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder._teacher.setText(teacher.get(position).toString());
        holder._type.setText(type.get(position).toString());
        holder._time.setText(time.get(position).toString());
        holder.classYoga.setOnClickListener(v -> handleCardClick(holder.getAdapterPosition()));
    }

    private void handleCardClick(int position) {
        if (position != RecyclerView.NO_POSITION) {
            Intent intent = new Intent(context, Update.class);
            intent.putExtra("class", class_id.get(position).toString());
            intent.putExtra("day", dayOfWeek.get(position).toString());
            intent.putExtra("time", time.get(position).toString());
            intent.putExtra("capacity", capacity.get(position).toString());
            intent.putExtra("duration", duration.get(position).toString());
            intent.putExtra("price", price.get(position).toString());
            intent.putExtra("type", type.get(position).toString());
            intent.putExtra("description", description.get(position).toString());
            intent.putExtra("teacher", teacher.get(position).toString());
            activity.startActivityForResult(intent, 1);
        }
    }

    @Override
    public int getItemCount() {
        return class_id.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        private final TextView _teacher, _type, _time;
        private final CardView classYoga;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            _teacher = itemView.findViewById(R.id.teacher);
            _type = itemView.findViewById(R.id.type);
            _time = itemView.findViewById(R.id.time);
            classYoga = itemView.findViewById(R.id.classYoga);
        }
    }
}
