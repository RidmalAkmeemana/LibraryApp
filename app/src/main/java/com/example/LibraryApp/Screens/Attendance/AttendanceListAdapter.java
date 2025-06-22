package com.example.LibraryApp.Screens.Attendance;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import LibraryApp.R;

import java.util.List;

public class AttendanceListAdapter extends RecyclerView.Adapter<AttendanceListAdapter.AttendanceViewHolder> {
    List<AttendanceModel> attendanceModelList;
    Context context;

    public AttendanceListAdapter(List<AttendanceModel> attendanceModelList, Context context) {
        this.attendanceModelList = attendanceModelList;
        this.context = context;
    }

    @NonNull
    @Override
    public AttendanceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        LayoutInflater inflater = LayoutInflater.from(context);
        view = inflater.inflate(R.layout.attendance_card, parent, false);
        final AttendanceViewHolder viewHolder = new AttendanceViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull AttendanceViewHolder holder, int position) {
        holder.ct1.setText(attendanceModelList.get(position).getM_name());
        holder.ct2.setText("End Date: " + attendanceModelList.get(position).getE_time());
        holder.time.setText("Check In: " + attendanceModelList.get(position).getCheck_in());

        if (attendanceModelList.get(position).getIspresent().equals("true")) {
            holder.img1.setImageResource(R.drawable.present);
        } else {
            holder.img1.setImageResource(R.drawable.absent);
        }
    }

    @Override
    public int getItemCount() {
        return attendanceModelList.size();
    }

    public static class AttendanceViewHolder extends RecyclerView.ViewHolder {
        ImageView img1;
        TextView ct1;
        TextView ct2;
        TextView time;
        CardView cardView;

        public AttendanceViewHolder(@NonNull View itemView) {
            super(itemView);
            img1 = itemView.findViewById(R.id.img1);
            ct1 = itemView.findViewById(R.id.ct1);
            ct2 = itemView.findViewById(R.id.ct2);
            time = itemView.findViewById(R.id.time);
            cardView = itemView.findViewById(R.id.cardView);
        }
    }
}