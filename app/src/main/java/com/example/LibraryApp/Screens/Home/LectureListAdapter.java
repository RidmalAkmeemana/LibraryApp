package com.example.LibraryApp.Screens.Home;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import LibraryApp.R;
import com.example.LibraryApp.Screens.SubmitFragment;

import java.util.List;

public class LectureListAdapter extends  RecyclerView.Adapter<LectureListAdapter.LectureViewHolder> {
    List<LectureModel> lectureModelList;
    Context context;

    public LectureListAdapter(List<LectureModel> lectureModelList, Context context) {
        this.lectureModelList = lectureModelList;
        this.context = context;
    }

    @NonNull
    @Override
    public LectureViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


        View view ;
        LayoutInflater inflater = LayoutInflater.from(context);
        view = inflater.inflate( R.layout.lecuter_card,parent,false) ;
        final LectureViewHolder viewHolder = new LectureViewHolder(view) ;

        viewHolder.cardView.setOnClickListener(view1 -> {

            if ( lectureModelList.get(viewHolder.getAdapterPosition()).getExpired().equals("true")){
                dialogBox("Your Linked Now Disabled !! \n Now You Can't Submit Your Attendance ","Attendance ");
            }else{

                FragmentTransaction fragmentTransaction = ((FragmentActivity) context).getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.container,new SubmitFragment(
                        lectureModelList.get(viewHolder.getAdapterPosition())
                ))
                        .addToBackStack(null)
                        .commit();
            }

        });

        return viewHolder;
    }
    public void dialogBox(String message,String title) {
        AlertDialog alertDialog = new AlertDialog.Builder(context).create();
        alertDialog.setTitle(title);
        alertDialog.setIcon(R.drawable.waning);
        alertDialog.setMessage(message);
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK", (dialog, which) -> {
        });

        alertDialog.show();


    }
    @Override
    public void onBindViewHolder(@NonNull LectureViewHolder holder, int position) {

        holder.ct1.setText(lectureModelList.get(position).getM_name());
        holder.time.setText("Time: "+lectureModelList.get(position).getTime_start() +" - "+ lectureModelList.get(position).getTime_end());


        if(lectureModelList.get(position).getExpired().equals("true")){
            holder.img1.setImageResource(R.drawable.inactive);
        }else{
            holder.img1.setImageResource(R.drawable.active);
        }

    }

    @Override
    public int getItemCount() {
        return lectureModelList.size();
    }

    public class LectureViewHolder extends RecyclerView.ViewHolder{

        ImageView img1;
        TextView ct1;
        TextView time;
        CardView cardView;
        public LectureViewHolder(@NonNull View itemView) {
            super(itemView);
            img1= itemView.findViewById(R.id.img1);
            ct1= itemView.findViewById(R.id.ct1);
            time= itemView.findViewById(R.id.time);
            cardView= itemView.findViewById(R.id.cardView);

        }
    }
}
