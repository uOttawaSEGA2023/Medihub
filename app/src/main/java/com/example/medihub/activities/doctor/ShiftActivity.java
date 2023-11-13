package com.example.medihub.activities.doctor;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.medihub.R;
import com.example.medihub.models.DoctorProfile;
import com.example.medihub.models.Shift;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CalendarView;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.Button;

import java.time.LocalDateTime;
import java.util.Calendar;

public class ShiftActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private DoctorProfile user;
    private FirebaseAuth firebaseAuth;
    private String startTime;
    private String endTime;
    private Button btnCreate;
    private boolean isStartInputted = false;
    private boolean isEndInputted = false;
    private CalendarView calendarView;
    private int day;
    private int month;
    private int year;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shifts);

        user = (DoctorProfile) getIntent().getSerializableExtra("current user");
        firebaseAuth = FirebaseAuth.getInstance();
        Calendar calendar = Calendar.getInstance();

        Spinner spinnerStart = findViewById(R.id.spinnerStart);
        Spinner spinnerEnd = findViewById(R.id.spinnerEnd);
        btnCreate = findViewById(R.id.btnCreate);
        calendarView = findViewById(R.id.calendar);

        ArrayAdapter<CharSequence> theAdapter = ArrayAdapter.createFromResource(this, R.array.time, android.R.layout.simple_spinner_item);
        theAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinnerStart.setAdapter(theAdapter);
        spinnerStart.setOnItemSelectedListener(this);
        spinnerEnd.setAdapter(theAdapter);
        spinnerEnd.setOnItemSelectedListener(this);

        //Sets the default date to the current date
        calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH));
        calendar.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH));
        calendar.set(Calendar.YEAR, calendar.get(Calendar.YEAR));
        long m = calendar.getTimeInMillis();
        calendarView.setDate(m);

        day = Calendar.DAY_OF_MONTH;
        month = Calendar.MONTH;
        year = Calendar.YEAR;

        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int i, int i1, int i2) {

                //Sets the day, month, year from the user's input
                day = i2;
                month = i1 + 1;
                year = i;

            }
        });

        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (check() == true) {

                    String[] temp = startTime.split(":");
                    int tempHour = Integer.parseInt(temp[0]);
                    int tempMinute = Integer.parseInt(temp[1]);

                    LocalDateTime tempStart = LocalDateTime.of(year, month, day, tempHour, tempMinute);

                    temp = endTime.split(":");
                    tempHour = Integer.parseInt(temp[0]);
                    tempMinute = Integer.parseInt(temp[1]);

                    LocalDateTime tempEnd = LocalDateTime.of(year, month, day, tempHour, tempMinute);
                    Shift tempShift = new Shift(user.getKey(), tempStart, tempEnd);

                    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
                    DatabaseReference objectsReference = databaseReference.child("shifts");
                    String objectId = objectsReference.push().getKey();
                    objectsReference.child(objectId).setValue(tempShift);

                    Intent intent = new Intent(ShiftActivity.this, DoctorActivity.class);
                    intent.putExtra("current user", user);
                    startActivity(intent);

                    Toast.makeText(ShiftActivity.this, "Shift saved successfully", Toast.LENGTH_SHORT).show();
                } else {

                    Toast.makeText(ShiftActivity.this, "Invalid input. Please check the selected time.", Toast.LENGTH_SHORT).show();
                }

            }
        });



    }


    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

        if (adapterView.getId() == R.id.spinnerStart) {

            startTime = adapterView.getItemAtPosition(i).toString();
            isStartInputted = true;


        } else if (adapterView.getId() == R.id.spinnerEnd) {

            endTime = adapterView.getItemAtPosition(i).toString();
            isEndInputted = true;

        }

    }

    private String date() {

        Calendar calendar = Calendar.getInstance();

        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        String date = String.format("%04d/%02d/%02d/%02d/%02d", year, month, day, hour, minute);

        return date;
    }

    //Checks if there's any invalid input
    private boolean check() {

        //The user didn't select a start or end time
        if (isStartInputted == false || isEndInputted == false) {

            return false;

        }

        String[] date = date().split("/");
        String[] s = startTime.split(":");
        String[] e = endTime.split(":");

        int currentTime = Integer.parseInt(date[3]) + (Integer.parseInt(date[4]) / 100);
        double startTime = Integer.parseInt(s[0]) + (Double.parseDouble(s[1]) / 100);
        double endTime = Integer.parseInt(e[0]) + (Double.parseDouble(e[1]) / 100);



        if (firstCheck(date) == false) {

            return false;

        } else if (secondCheck(currentTime, startTime, endTime, date) == false) {

            return false;

        } else {

            return true;

        }

    }

    //Checks for invalid input with the calendar
    private boolean firstCheck(String[] date) {

        int currentYear = Integer.parseInt(date[0]);
        int currentMonth = Integer.parseInt(date[1]);
        int currentDay = Integer.parseInt(date[2]);

        if (year < currentYear) {

            return false;

        } else if (year == currentYear && month < currentMonth) {

            return false;

        } else if (year == currentYear && month < currentMonth && day < currentDay) {

            return false;

        }

        return true;

    }

    //Checks for invalid input with the start, end, and current time
    private boolean secondCheck(int currentTime, double selectedStartTime, double selectedEndTime, String[] date) {

        int currentYear = Integer.parseInt(date[0]);
        int currentMonth = Integer.parseInt(date[1]);
        int currentDay = Integer.parseInt(date[2]);

        if (day == currentDay && month == currentMonth && year == currentYear) {

            if (selectedStartTime < currentTime || selectedEndTime <= currentTime) {

                return false;

            }

        }

         if (selectedEndTime <= selectedStartTime) {

            return false;

        }

        return true;

        }



    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

        //Don't care about this

    }
}
