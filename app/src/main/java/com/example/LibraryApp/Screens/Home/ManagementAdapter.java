package com.example.LibraryApp.Screens.Home;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import LibraryApp.R;

import java.util.List;

public class ManagementAdapter extends RecyclerView.Adapter<ManagementAdapter.ViewHolder> {

    private final List<String> itemList;
    private final Context context;

    public ManagementAdapter(Context context, List<String> itemList) {
        this.context = context;
        this.itemList = itemList;
    }

    @NonNull
    @Override
    public ManagementAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_management_card, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ManagementAdapter.ViewHolder holder, int position) {
        String title = itemList.get(position);
        holder.titleTextView.setText(title);

        // Map each item to an icon
        int iconResId;
        switch (title) {
            case "Books":
                iconResId = R.drawable.ic_book;
                break;
            case "Publishers":
                iconResId = R.drawable.ic_publisher;
                break;
            case "Branches":
                iconResId = R.drawable.ic_branch;
                break;
            case "Members":
                iconResId = R.drawable.ic_member;
                break;
            case "Authors":
                iconResId = R.drawable.ic_author;
                break;
            case "Book Copies":
                iconResId = R.drawable.ic_copy;
                break;
            case "Book Loans":
                iconResId = R.drawable.ic_loan;
                break;
            default:
                iconResId = R.drawable.ic_default;
                break;
        }

        holder.iconImageView.setImageResource(iconResId);
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView titleTextView;
        ImageView iconImageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.titleTextView);
            iconImageView = itemView.findViewById(R.id.iconImageView);
        }
    }
}
