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
    private final Activity activity;
    private final ArrayList<String> classId, dayOfWeek, time, capacity, duration, price, type, description, teacher;

    private static final int REQUEST_CODE_UPDATE = 1;

    public Adapter(Activity activity, Context context,
                   ArrayList<String> classId, ArrayList<String> dayOfWeek, ArrayList<String> time,
                   ArrayList<String> capacity, ArrayList<String> duration, ArrayList<String> price,
                   ArrayList<String> type, ArrayList<String> description, ArrayList<String> teacher) {
        this.activity = activity;
        this.context = context;
        this.classId = classId;
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
        View view = LayoutInflater.from(context).inflate(R.layout.card_view, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.bindData(position);
    }

    @Override
    public int getItemCount() {
        return classId.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private final TextView teacherTextView, typeTextView, timeTextView;
        private final CardView classCard;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            teacherTextView = itemView.findViewById(R.id.teacher);
            typeTextView = itemView.findViewById(R.id.type);
            timeTextView = itemView.findViewById(R.id.time);
            classCard = itemView.findViewById(R.id.classYoga);
        }

        void bindData(int position) {
            teacherTextView.setText(teacher.get(position));
            typeTextView.setText(type.get(position));
            timeTextView.setText(time.get(position));

            classCard.setOnClickListener(view -> {
                int index = getAdapterPosition();
                if (index != RecyclerView.NO_POSITION) {
                    Intent intent = new Intent(context, Update.class);
                    intent.putExtra("class", classId.get(index));
                    intent.putExtra("day", dayOfWeek.get(index));
                    intent.putExtra("time", time.get(index));
                    intent.putExtra("capacity", capacity.get(index));
                    intent.putExtra("duration", duration.get(index));
                    intent.putExtra("price", price.get(index));
                    intent.putExtra("type", type.get(index));
                    intent.putExtra("description", description.get(index));
                    intent.putExtra("teacher", teacher.get(index));
                    activity.startActivityForResult(intent, REQUEST_CODE_UPDATE);
                }
            });
        }
    }
}
