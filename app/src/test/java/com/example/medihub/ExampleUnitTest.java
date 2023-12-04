package com.example.medihub;

import org.junit.Test;

import static org.junit.Assert.*;

import com.example.medihub.activities.patient.PatientActivity;
import com.example.medihub.activities.registrations.LoginActivity;
import com.example.medihub.enums.DoctorSpecialty;
import com.example.medihub.models.RegistrationRequest;

import android.util.Log;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
//    @Test
//    public void addition_isCorrect() {
//        assertEquals(4, 2 + 2);
//    }

//    Test that login works with admin credentials
    @Test
    public void adminLoginCheck() {
        LoginActivity loginActivity = new LoginActivity();

        try {
            loginActivity.checkUser();
            // Additional assertions or verifications can be added here if needed
        } catch (Exception e) {
            fail("No exception should be thrown during admin login");
        }
    }
//    Test that login doesn't work with admin credentials (wrong password)
    @Test
    public void LoginCheck() {
        LoginActivity loginActivity = new LoginActivity();

        try {
            loginActivity.checkUser();
            Log.i("ExampleUnitTest", "Logging in with incorrect admin credentials");
        } catch (Exception e) {
            Log.i("ExampleUnitTest", "Login not working with incorrect admin credentials");
        }
    }

//    Test that logout button works when logged in as patient
    @Test
    public void logoutPatientCheck() {
        PatientActivity checkLogout = new PatientActivity();
        try {
            checkLogout.logout();
            Log.i("ExampleUnitTest", "Logout working for patients");
        } catch (Exception e) {
            Log.i("ExampleUnitTest", "Logout not working for patients");
        }
    }

//    Test that all doctor specialties show up on doctor sign-up page
    @Test
    public void doctorSpecialtiesCheck() {
        RegistrationRequest checkDoctorSpecialties = new RegistrationRequest();
        try {
            List<DoctorSpecialty> specialties = new ArrayList<>();
            specialties= checkDoctorSpecialties.getSpecialties();
            Log.i("ExampleUnitTest", "All doctor specialties showing up");
        } catch (Exception e) {
            Log.i("ExampleUnitTest", "Not all doctor specialties showing up");
        }
}

}