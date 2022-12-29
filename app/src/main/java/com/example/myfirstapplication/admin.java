package com.example.myfirstapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class admin extends AppCompatActivity {
    EditText StudName;
    EditText StudID;
    EditText Email;
    EditText Gender;
    EditText RoomNo;
    EditText Pass;
    Button bSave;

    String KEY_NAME = "Name";
    String KEY_ID = "ID";
    String KEY_EMAIL = "Email";
    String KEY_GENDER ="Gender";
    String KEY_ROOM = "Room";
    String KEY_PASS = "Password";
    String KEY_IN = "Checkin";
    String KEY_OUT = "Checkout";
    String KEY_ROOMMATE = "Roommate";

    int numberInRoom;

    FirebaseAuth fAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        // hide the status bar
        ActionBar actionBar=getSupportActionBar();
        actionBar.hide();

        //Connecting to Firebase Firestore
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        fAuth= FirebaseAuth.getInstance();

        //getting data from user
        StudName = findViewById(R.id.et_StudentName);
        StudID = findViewById(R.id.et_StudentID);
        Email = findViewById(R.id.et_Email);
        Gender = findViewById(R.id.et_Gender);
        RoomNo = findViewById(R.id.et_RoomNumber);
        Pass = findViewById(R.id.et_CreatePassword);
        bSave = findViewById(R.id.btn_CreateUser);

        bSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String Name  = StudName.getText().toString().trim();
                String ID  = StudID.getText().toString().trim();
                String email = Email.getText().toString().trim();
                String gender = Gender.getText().toString().trim();
                String room  = RoomNo.getText().toString().trim();
                Integer roomNumber = Integer.parseInt(room);
                String password  = Pass.getText().toString().trim();



                if(Name.isEmpty())
                {
                    StudName.setError("A name is needed");
                    return;
                }
                else if(ID.isEmpty())
                {
                    StudID.setError("An ID is required");
                    return;
                }
                else if(email.isEmpty())
                {
                    Email.setError("An email is required");
                    return;
                }
               else if(gender.isEmpty())
                {
                    Gender.setError("The gender of the student is required");
                    return;
                }
                else if(room.isEmpty())
                {
                    RoomNo.setError("A room number is needed");
                    return;
                }

                else if(password.isEmpty())
                {
                    Pass.setError("A password is required");
                    return;
                }
                else if(password.length()<6)
                {
                    Pass.setError("Passwords must be 6 characters or more");
                    return;
                }
                else if(ID.length()!=6)
                {
                    StudID.setError("ID number must be 6 characters");
                    return;
                }

// If all fields are empty, there is an issue.
                else{
                    // Get count for how many members are in the room allocated
                    Map<String, String> RoomNumber = new HashMap<String, String>();
                    db.collection("Room")
                            .document(room)
                            .get()
                            .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                    if (task.isSuccessful()) {
                                        DocumentSnapshot document = task.getResult();
                                        if(document.exists()){
                                            //Room.put("Members",document.get("Roommate1").toString());
                                            //Room.put("Members",document.get("Roommate2").toString());
                                            RoomNumber.put("Number",document.get("Number").toString());
                                            Log.d("Thedata", "DocumentSnapshot data: " + document.getData());
                                            numberInRoom = Integer.parseInt(RoomNumber.get("Number"));
                                            if(numberInRoom >= 2){
                                                RoomNo.setError("Room is already full");
                                                return;
                                            }
                                            else{
                                                //STORE DATA INTO HASHMAP USING KEY AND VALUE FORMAT

                                                Map<String, Object> user = new HashMap<>();
                                                user.put(KEY_NAME, Name);
                                                user.put(KEY_ID, ID);
                                                user.put(KEY_EMAIL, email);
                                                user.put(KEY_GENDER,gender);
                                                user.put(KEY_ROOM, room);
                                                user.put(KEY_PASS, password);
                                                user.put(KEY_IN, "notset");
                                                user.put(KEY_OUT, "notset");

                                                fAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                                        if(task.isSuccessful()){
                                                            Toast.makeText(getApplicationContext(), "User created", Toast.LENGTH_SHORT).show();


                                                            // ADD THE USER TO THAT ROOM AND INCREASE THE COUNT
                                                            Map<String, String> updatedRoom = new HashMap<String, String>();
                                                            updatedRoom.put("Number", String.valueOf(numberInRoom+1));

                                                            if(numberInRoom==0){
                                                                updatedRoom.put("Roommate1", fAuth.getCurrentUser().getEmail());
                                                                updatedRoom.put("Roommate2", document.get("Roommate2").toString());
                                                                user.put(KEY_ROOMMATE, document.get("Roommate2").toString());

                                                            }
                                                            else{
                                                                updatedRoom.put("Roommate1", document.get("Roommate1").toString() );
                                                                updatedRoom.put("Roommate2", fAuth.getCurrentUser().getEmail());
                                                                user.put(KEY_ROOMMATE, document.get("Roommate1").toString());
                                                                // Update on the roommates side
                                                                String roommateEmail=  document.get("Roommate1").toString();
                                                                db.collection("User")
                                                                        .document(roommateEmail)
                                                                        .update("Roommate", fAuth.getCurrentUser().getEmail());
                                                            }

                                                            db.collection("Room")
                                                                    .document(room)
                                                                    .set(updatedRoom)
                                                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                        @Override
                                                                        public void onSuccess(Void unused) {
                                                                            Toast.makeText(getApplicationContext(), "Added to Room", Toast.LENGTH_LONG)
                                                                                    .show();

                                                                        }
                                                                    })
                                                                    .addOnFailureListener(new OnFailureListener() {
                                                                        @Override
                                                                        public void onFailure(@NonNull Exception e) {
                                                                            Log.d("Yes", "onFailure: Error: ", e);


                                                                        }
                                                                    });


                                                            //INITILIAZED COLLECTION AS User
                                                            db.collection("User")
                                                                    .document(email) //Made document to be created based off email value
                                                                    .set(user)// Added hashmap values into the database
                                                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                        @Override
                                                                        public void onSuccess(Void unused) {
                                                                            Toast.makeText(getApplicationContext(), "Success", Toast.LENGTH_LONG)
                                                                                    .show();
                                                                            startActivity(new Intent(getApplicationContext(),login.class));
                                                                        }
                                                                    })
                                                                    //In case data doesnt go into firestore
                                                                    .addOnFailureListener(new OnFailureListener() {
                                                                        @Override
                                                                        public void onFailure(@NonNull Exception e) {
                                                                            Log.d("Yes", "onFailure: Error: ", e);
                                                                        }
                                                                    });
                                                        }
                                                        else{
                                                            Toast.makeText(getApplicationContext(), "Sign up failed: " +task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                                        }

                                                    }
                                                });

                                            }

                                        }else {
                                            Log.d("No data", "No such document");
                                            RoomNo.setError("Room does not exist");

                                        }

                                    }else {
                                        Log.d("Failed", "get failed with ", task.getException());
                                    }


                                }
                            });
                    }
            }


        });
    }
}