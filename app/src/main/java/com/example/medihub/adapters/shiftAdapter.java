package com.example.medihub.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.medihub.R;
import com.example.medihub.models.Appointment;
import com.example.medihub.models.PatientProfile;
import com.example.medihub.models.Shift;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class shiftAdapter extends RecyclerView.Adapter<shiftAdapter.MyViewHolder> {

    private ArrayList<Shift> shiftList;
    private RecyclerViewClickListener listener;

    public shiftAdapter(ArrayList<Shift> shiftList, RecyclerViewClickListener listener)
    {
        this.shiftList = shiftList;
        this.listener = listener;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private TextView startDate;
        private TextView endDate;

        public MyViewHolder(final View view)
        {
            super(view);
            startDate = view.findViewById(R.id.textView4);
            endDate = view.findViewById(R.id.textView10);


            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            listener.onClick(view, getAdapterPosition());
        }
    }
    @NonNull
    @Override
    public shiftAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_shift,parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull shiftAdapter.MyViewHolder holder, int position) {

        Shift shift = shiftList.get(position);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

        holder.startDate.append(shift.localStartDate().format(formatter));
        holder.endDate.append(shift.localEndDate().format(formatter));

    }

    @Override
    public int getItemCount() {
        return shiftList.size();
    }

    public interface RecyclerViewClickListener{
        void onClick(View v, int position);

    }



}
