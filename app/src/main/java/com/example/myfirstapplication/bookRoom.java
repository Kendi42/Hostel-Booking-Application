package com.example.myfirstapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myfirstapplication.ui.home.HomeFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class bookRoom extends AppCompatActivity {
    Button btn_book, btn_cancel;
    DatePickerDialog dateSelect;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_room);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseAuth fauth = FirebaseAuth.getInstance();

        // hide the status bar
        ActionBar actionBar=getSupportActionBar();
        actionBar.hide();

        btn_book = (Button) findViewById(R.id.btn_book);
        btn_cancel = (Button) findViewById(R.id.btn_admin);
        EditText et_room= findViewById(R.id.et_roomNumber);
        TextView checkIn = findViewById(R.id.et_checkin);
        TextView checkOut = findViewById(R.id.et_checkout);

        checkIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar dates = Calendar.getInstance();
                int year = dates.get(Calendar.YEAR);
                int month = dates.get(Calendar.MONTH);
                int day = dates.get(Calendar.DAY_OF_MONTH);
                long start = new Date().getTime();
                long end = start + 999999999;

                dateSelect = new DatePickerDialog(bookRoom.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int day) {
                        checkIn.setText(day+"/"+(month+1)+"/"+year);
//                        checkIn.setText("30/11/2022");
                    }
                },year,month,day);
                dateSelect.getDatePicker().setMinDate(start);
                dateSelect.getDatePicker().setMaxDate(end);
                dateSelect.show();
            }
        });

        checkOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar dates = Calendar.getInstance();
                int year = dates.get(Calendar.YEAR);
                int month = dates.get(Calendar.MONTH);
                int day = dates.get(Calendar.DAY_OF_MONTH);
                long start = new Date().getTime();
                long end = start + 999999999;

                dateSelect = new DatePickerDialog(bookRoom.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker viewTwo, int year, int month, int day) {
                        checkOut.setText(day+"/"+(month+1)+"/"+year);
                    }
                },year,month,day);
                dateSelect.getDatePicker().setMinDate(start);
                dateSelect.getDatePicker().setMaxDate(end);
                dateSelect.show();
            }
        });

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(bookRoom.this, Dashboard.class);
                startActivity(intent);
            }
        });

        btn_book.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String room = et_room.getText().toString().trim();
                String inDate = checkIn.getText().toString().trim();
                String outDate = checkOut.getText().toString().trim();


                if(TextUtils.isEmpty(room))
                {
                    et_room.setError("Room number needed");
                    return;
                }
                if(TextUtils.isEmpty(inDate))
                {
                    checkIn.setError("Check in date is required");
                    return;
                }
                if(TextUtils.isEmpty(outDate))
                {
                    checkOut.setError("Check out date is required");
                    return;
                }
                if(inDate.contentEquals(outDate))
                {
                    checkOut.setError("Check out date cannot be the same as check in date");
                    return;
                }

                else{
                    // Confirm if the room number youve entered is yours
                    // get your room number
                    Map<String, String> user = new HashMap<String, String>();
                    db.collection("User")
                            .document(fauth.getCurrentUser().getEmail()) //Made document to be created based off email value
                            .get()
                            .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                    if (task.isSuccessful()) {
                                        DocumentSnapshot document = task.getResult();
                                        if (document.exists()) {
                                            user.put("Email",document.get("Email").toString());
                                            user.put("ID",document.get("ID").toString());
                                            user.put("Name",document.get("Name").toString());
                                            user.put("Room", String.valueOf(document.get("Room")));
                                            user.put("Roommate", document.get("Roommate").toString());
                                            Log.d("Data Here", "DocumentSnapshot data: " + document.getData());
                                            Log.d("Result", "DocumentSnapshot data: " + user.get("Room"));
                                        } else {
                                            Log.d("No data", "No such document");
                                        }
                                    } else {
                                        Log.d("Failed", "get failed with ", task.getException());
                                    }

                                    // we have the current users room number
                                    String room_No= user.get("Room");

                                    // lets compare with what theyve given us
                                    if(Integer.parseInt(room_No) != Integer.parseInt(room))
                                    {
                                        et_room.setError("You cannot book a room you are not a resident of");
                                    }
                                    else{
                                        // send the data to the database
                                        user.put("Checkin", inDate);
                                        user.put("Checkout", outDate);
                                        db.collection("User")
                                                .document(fauth.getCurrentUser().getEmail()) //Made document to be created based off email value
                                                .set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void unused) {
                                                        Toast.makeText(getApplicationContext(), "Successful booking", Toast.LENGTH_LONG)
                                                                .show();
                                                        startActivity(new Intent(getApplicationContext(), Dashboard.class));

                                                    }
                                                }).addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        Toast.makeText(getApplicationContext(), "Failed booking", Toast.LENGTH_LONG)
                                                                .show();

                                                    }
                                                });

                                    }


                                }
                            });



                }



            }
        });
    }
}