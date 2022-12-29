package com.example.myfirstapplication.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.myfirstapplication.GridAdapter;
import com.example.myfirstapplication.bookRoom;
import com.example.myfirstapplication.databinding.FragmentHomeBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.lang.reflect.Array;
import java.util.HashMap;
import java.util.Map;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private String roommate;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseAuth fauth = FirebaseAuth.getInstance();


        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // Book Button
        binding.btnGoToBookRoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), bookRoom.class);
                startActivity(intent);
            }
        });

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
                                user.put("Checkin",document.get("Checkin").toString());
                                user.put("Checkout",document.get("Checkout").toString());
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


                        fauth.getCurrentUser().getEmail();
                        String [] gridTitles = {"Check-in Date", "Check-out Date", "Room Allocated", "Roommate"};
                        // We need to connect this part to Firebase. Currently using dummy data
                        String [] gridInfo = {user.get("Checkin"), user.get("Checkout"), user.get("Room"), user.get("Roommate")};

                        GridAdapter gridAdapter = new GridAdapter(getContext(), gridTitles, gridInfo);
                        binding.gridview.setAdapter(gridAdapter);

                    }
                });// Added hashmap values into the database
        //The Grid


        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}