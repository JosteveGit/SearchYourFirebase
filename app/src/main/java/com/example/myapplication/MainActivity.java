package com.example.myapplication;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private final FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance("https://kirk-30f6a.firebaseio.com/");
    private final List<Data> dataList = new ArrayList<>();
    EditText pop;

    private boolean watch = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final Button nothing = findViewById(R.id.nothing);
        pop = findViewById(R.id.pop);
        monitorSearch();

        nothing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dataList.size() == 0) {
                    firebaseDatabase.getReference().child("JOB").addValueEventListener(new ValueEventListener() {
                        @SuppressLint("SetTextI18n")
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for (DataSnapshot data : dataSnapshot.getChildren()) {
                                String location = data.child("userLocation").getValue(String.class);
                                String name = data.child("userName").getValue(String.class);
                                String phone = data.child("userNumber").getValue(String.class);
                                String profession = data.child("userProfession").getValue(String.class);
                                String address = data.child("userAddress").getValue(String.class);
                                String description = data.child("userDescription").getValue(String.class);
                                Data data1 = new Data(name, phone, profession, location, address, description);
                                dataList.add(data1);

                                RecyclerView recyclerView = findViewById(R.id.recyclerview);
                                recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
                                recyclerView.setAdapter(new RecyclerAdapter(filterList()));
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            Toast.makeText(MainActivity.this, databaseError.toString(), Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    RecyclerView recyclerView = findViewById(R.id.recyclerview);
                    recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
                    recyclerView.setAdapter(new RecyclerAdapter(filterList()));
                }
            }
        });
    }

    private List<Data> filterList() {
        String profession = "";
        boolean postToProfession = true;
        String location = "";
        for (int i = 0; i < pop.getText().toString().length(); i++) {
            if (postToProfession) {
                if (pop.getText().toString().charAt(i) != ' ') {
                    profession += Character.toString(pop.getText().toString().charAt(i));
                } else {
                    postToProfession = false;
                }
            } else {
                location += Character.toString(pop.getText().toString().charAt(i));
            }
        }

        List<Data> list = new ArrayList<>();
        boolean noData = true;
        for (Data data : dataList) {
            if (data.getLocation().toLowerCase().contains(location.toLowerCase()) || location.toLowerCase().contains(data.getLocation().toLowerCase())) {
                if (profession.toLowerCase().contains(data.getProfession().toLowerCase()) || data.getProfession().toLowerCase().contains(profession.toLowerCase())) {
                    list.add(data);
                    noData = false;
                }
            }
        }

        if (noData) {
            Toast.makeText(this, "No data found", Toast.LENGTH_LONG).show();
        }

        return list;
    }

    private void monitorSearch() {
        pop.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (watch) {
                    if (s.toString().contains(" ")) {
                        watch = false;
                        pop.setText(pop.getText().toString().trim() + " In ");
                    }
                } else {
                    if (!s.toString().contains(" ")) {
                        watch = true;
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

}

