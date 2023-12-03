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
import com.example.medihub.models.DoctorProfile;
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

public class appointmentRecycleAdapterPatient extends RecyclerView.Adapter<appointmentRecycleAdapterPatient.MyViewHolder> {
    private ArrayList<Appointment> appointmentsList;
    private ArrayList<DoctorProfile> doctorList;
    private appointmentRecycleAdapterPatient.RecyclerViewClickListener listener;

    public appointmentRecycleAdapterPatient(ArrayList<Appointment> appointmentsList, ArrayList<DoctorProfile> doctorList, appointmentRecycleAdapterPatient.RecyclerViewClickListener listener)
    {
        this.appointmentsList = appointmentsList;
        this.doctorList = doctorList;
        this.listener = listener;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private TextView doctorText;
        private TextView dateText;

        public MyViewHolder(final View view)
        {
            super(view);
            doctorText = view.findViewById(R.id.textView4);
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
    public appointmentRecycleAdapterPatient.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_patient_appointmentlist, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull appointmentRecycleAdapterPatient.MyViewHolder holder, int position) {
        Appointment appointment = appointmentsList.get(position);
        DoctorProfile doctor = doctorList.get(position);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

        holder.dateText.append(appointment.localStartDate().format(formatter));

        holder.doctorText.append(doctor.getFirstName() + " " + doctor.getLastName());
    }

    @Override
    public int getItemCount() {
        return appointmentsList.size();
    }

    public interface RecyclerViewClickListener{
        void onClick(View v, int position);
    }
}
