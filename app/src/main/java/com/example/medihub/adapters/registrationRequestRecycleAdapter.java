package com.example.medihub.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.medihub.R;
import com.example.medihub.enums.RequestStatus;
import com.example.medihub.models.RegistrationRequest;

import java.util.ArrayList;

public class registrationRequestRecycleAdapter extends RecyclerView.Adapter<registrationRequestRecycleAdapter.MyViewHolder> {

    private ArrayList<RegistrationRequest> requestList;
    private RecyclerViewClickListener listener;

    public registrationRequestRecycleAdapter(ArrayList<RegistrationRequest> requestList, RecyclerViewClickListener listener)
    {
        this.requestList = requestList;
        this.listener = listener;
    }

    public void updateStatus(int position, RequestStatus newStatus) {
        if (position >= 0 && position < requestList.size()) {
            requestList.get(position).setStatus(newStatus);
            notifyItemChanged(position);
        }
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private TextView nametxt;
        private TextView status;

        public MyViewHolder(final View view)
        {
            super(view);
            nametxt = view.findViewById(R.id.textView4);
            status = view.findViewById(R.id.textView5);

            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            listener.onClick(view, getAdapterPosition());
        }
    }
    @NonNull
    @Override
    public registrationRequestRecycleAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_requests,parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull registrationRequestRecycleAdapter.MyViewHolder holder, int position) {
        String doctor = "Doctor: ";
        String patient = "Patient: ";
        String name;
        if (requestList.get(position).isPatient())
            name = patient+requestList.get(position).getFirstName() + " " + requestList.get(position).getLastName();
        else
            name = doctor+requestList.get(position).getFirstName() + " " + requestList.get(position).getLastName();
        // set name
        holder.nametxt.setText(name);

        // set status
        if (requestList.get(position).getStatus()!=null) {
            String curr_status;
            curr_status = "STATUS: " + requestList.get(position).getStatus().toString();
            holder.status.setText(curr_status);
        }

    }

    @Override
    public int getItemCount() {
        return requestList.size();
    }

    public interface RecyclerViewClickListener{
        void onClick(View v, int position);

    }



}
