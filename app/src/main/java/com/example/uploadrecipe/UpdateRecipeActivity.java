package com.example.uploadrecipe;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import android.app.ProgressDialog;
import com.example.uploadrecipe.Model.Recipe;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.DateFormat;
import java.util.Calendar;

public class UpdateRecipeActivity extends AppCompatActivity {
    ImageView imageView;
    EditText EName;
    EditText EIngredients;
    EditText EInstructions;
    String key,OldphotoUrl;
    DatabaseReference databaseReference;
    StorageReference storageReference;
    String name,ingredients,instructions;

    Uri uri;
    String photoUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_recipe);

        imageView=findViewById(R.id.imageView);
        EName=findViewById(R.id.EtName);
        EIngredients=findViewById(R.id.EtIngredients);
        EInstructions=findViewById(R.id.EtInstructions);

        Intent intent = getIntent();
        photoUrl = intent.getStringExtra("OldphotoUrl");

        Glide.with(this).load(photoUrl).into(imageView);

        String itemName = intent.getStringExtra("tvItemNameKey");
        EName.setText(itemName);

        String itemIngredients = intent.getStringExtra("tvItemIngredientsKey");
        EIngredients.setText(itemIngredients);

        String itemInstructions = intent.getStringExtra("tvItemInstructionsKey");
        EIngredients.setText(itemInstructions);

        key = intent.getStringExtra("key");
        OldphotoUrl= intent.getStringExtra("OldphotoUrl");



        databaseReference=FirebaseDatabase.getInstance().getReference("UploadRecipe").child(key);





    }



    public void choosePhoto(View view) {
        Intent intent=new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent,1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode==RESULT_OK)
        {
            uri=data.getData();
            imageView.setImageURI(uri);
        }
        else{
            Toast.makeText(this,"You have not picked photo",Toast.LENGTH_SHORT).show();
        }
    }


    public void update(View view) {
        name=EName.getText().toString().trim();
        ingredients=EIngredients.getText().toString().trim();
        instructions=EInstructions.getText().toString().trim();

        ProgressDialog progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("Updating Recipe");
        progressDialog.show();
        StorageReference storageReference= FirebaseStorage.getInstance().getReference()
                .child("UploadRecipe").child(uri.getLastPathSegment());

        storageReference.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                Task task=taskSnapshot.getStorage().getDownloadUrl();
                while (!task.isSuccessful());
                Uri uriPhoto= (Uri) task.getResult();
                photoUrl=uriPhoto.toString();
                uploadRecipe();
                progressDialog.dismiss();


            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();
                Toast.makeText(UpdateRecipeActivity.this,"Error"+e.getMessage(),Toast.LENGTH_SHORT).show();

            }
        });

    }

        public void uploadRecipe() {
            Recipe recipe = new Recipe(
                    name,
                    ingredients,
                    instructions,
                    photoUrl
            );

            databaseReference.setValue(recipe).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    StorageReference storageReferenceNew = FirebaseStorage.getInstance().getReferenceFromUrl(OldphotoUrl);
                    storageReferenceNew.delete();
                    Toast.makeText(UpdateRecipeActivity.this, "Recipe Updated", Toast.LENGTH_SHORT).show();
                }
            });

        }


}