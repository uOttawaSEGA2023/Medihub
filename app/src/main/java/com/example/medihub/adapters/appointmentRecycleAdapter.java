package com.example.medihub.adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.medihub.R;
import com.example.medihub.database.UsersReference;
import com.example.medihub.models.Appointment;
import com.example.medihub.models.PatientProfile;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class appointmentRecycleAdapter extends RecyclerView.Adapter<appointmentRecycleAdapter.MyViewHolder> {
    private ArrayList<Appointment> appointmentsList;
    private recycleAdapter.RecyclerViewClickListener listener;

    public appointmentRecycleAdapter(ArrayList<Appointment> appointmentsList, recycleAdapter.RecyclerViewClickListener listener)
    {
        this.appointmentsList = appointmentsList;
        this.listener = listener;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private TextView patientText;
        private TextView dateText;

        public MyViewHolder(final View view)
        {
            super(view);
            patientText = view.findViewById(R.id.textView4);
            dateText = view.findViewById(R.id.textView5);

            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            listener.onClick(view, getAdapterPosition());
        }
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_appointments, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Appointment appointment = appointmentsList.get(position);

        UsersReference usersReference = new UsersReference();
        DatabaseReference patientRef = usersReference.get(appointment.getPatient_id());

        patientRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    PatientProfile patient = snapshot.getValue(PatientProfile.class);

                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

                    holder.dateText.append(appointment.localStartDate().format(formatter));
                    holder.patientText.append(patient.getFirstName() + " " + patient.getLastName());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return appointmentsList.size();
    }
}
