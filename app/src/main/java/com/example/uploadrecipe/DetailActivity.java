package com.example.uploadrecipe;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class DetailActivity extends AppCompatActivity {
    ImageView imageView;
    TextView tvItemName;
    TextView tvItemIngredients;
    TextView tvItemInstructions;
    String photoUrl="";
    String key="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        imageView = findViewById(R.id.image);
        tvItemName = findViewById(R.id.nameItem);
        tvItemIngredients = findViewById(R.id.ingredientsItem);
        tvItemInstructions = findViewById(R.id.instructionsItem);

        Intent intent = getIntent();
        photoUrl = intent.getStringExtra("IMAGE");

        Glide.with(this).load(photoUrl).into(imageView);

        String itemName = intent.getStringExtra("NAME");
        tvItemName.setText(itemName);

        String itemIngredients = intent.getStringExtra("INGREDIENTS");
        tvItemIngredients.setText(itemIngredients);

        String itemInstructions = intent.getStringExtra("INSTRUCTIONS");
        tvItemInstructions.setText(itemInstructions);

        key = intent.getStringExtra("KEY");


    }

    public void deleteItem(View view) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("UploadRecipe");
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageReference = storage.getReferenceFromUrl(photoUrl);

        storageReference.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {

                reference.child(key).removeValue();
                Toast.makeText(DetailActivity.this,"Data Deleted Successfully",Toast.LENGTH_SHORT).show();
                startActivity(new Intent(DetailActivity.this,MainActivity.class));
                finish();

            }


        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(DetailActivity.this,"Error"+e.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void UpdateItem(View view) {
        startActivity(new Intent(getApplicationContext(),UpdateRecipeActivity.class)
        .putExtra("tvItemNameKey",tvItemName.getText().toString())
        .putExtra("tvItemIngredientsKey",tvItemIngredients.getText().toString())
                        .putExtra("tvItemInstructionsKey", tvItemInstructions.getText().toString())
                .putExtra("OldphotoUrl",photoUrl)
                .putExtra("key",key)
        );


    }
}