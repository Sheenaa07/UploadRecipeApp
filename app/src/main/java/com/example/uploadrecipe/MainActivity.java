package com.example.uploadrecipe;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.uploadrecipe.Adapter.DataAdapter;
import com.example.uploadrecipe.Interface.Callback;
import com.example.uploadrecipe.Model.Recipe;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements Callback {
    RecyclerView recyclerView;
    ArrayList<Recipe>arrayList;
    DataAdapter adapter;
    DatabaseReference databaseReference;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView=findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this,RecyclerView.VERTICAL,false));
        recyclerView.setHasFixedSize(true);
        arrayList=new ArrayList<>();
        progressDialog= new ProgressDialog(this);
        progressDialog.setMessage("Item Loading...");
        databaseReference= FirebaseDatabase.getInstance().getReference("UploadRecipe");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                arrayList.clear();
                for (DataSnapshot ds:dataSnapshot.getChildren()) {

                    Recipe recipe = ds.getValue(Recipe.class);
                    recipe.setKey(ds.getKey());
                    arrayList.add(recipe);


                }
                adapter = new DataAdapter(MainActivity.this, arrayList,MainActivity.this);
                recyclerView.setAdapter(adapter);
                progressDialog.dismiss();


            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(MainActivity.this,"Error"+databaseError.getMessage(),Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        });
    }






    public void uploadClick(View view){
        startActivity(new Intent(MainActivity.this,Upload.class));
    }

    @Override
    public void onClick(int i) {
        Intent intent= new Intent(MainActivity.this,DetailActivity.class);
        intent.putExtra("IMAGE", arrayList.get(i).getPhotoUrl());
        intent.putExtra("NAME",arrayList.get(i).getName());
        intent.putExtra("INGREDIENTS",arrayList.get(i).getIngredients());
        intent.putExtra("INSTRUCTIONS",arrayList.get(i).getInstructions());
        intent.putExtra("KEY",arrayList.get(i).getKey());
        startActivity(intent);

    }
}