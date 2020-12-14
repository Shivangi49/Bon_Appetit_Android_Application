package com.example.bonappetit.Admin;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.bonappetit.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;

public class ManageFood extends AppCompatActivity {

    EditText food_name,category_id,food_id,price;
    Button add_food,choose;
    ImageView food_image;
    public StorageReference StorageRef;
    public Uri imguri;
    DatabaseReference databaseReference;
    private StorageTask uploadTask;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_food);

        food_id=findViewById(R.id.foodId);
        price=findViewById(R.id.foodPrice);
        food_name=findViewById(R.id.foodName);
        category_id=findViewById(R.id.categoryId);
        choose=findViewById(R.id.choose);
        food_image=findViewById(R.id.foodImage);
        add_food=findViewById(R.id.add_food);


        //toolbar

        Toolbar toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        String btnName=getIntent().getStringExtra("btnname");
        getSupportActionBar().setTitle(btnName);
        add_food.setText(btnName);

        if(btnName.equals("Delete Food")){
            food_name.setVisibility(View.GONE);
            food_image.setVisibility(View.GONE);
            choose.setVisibility(View.GONE);
            price.setVisibility(View.GONE);
            category_id.setVisibility(View.GONE);

            add_food.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    final String foodId=food_id.getText().toString();
                    if(TextUtils.isEmpty(foodId)){
                        Toast.makeText(getApplicationContext(),"Provide food Id",Toast.LENGTH_SHORT).show();
                    }
                    else{
                        DatabaseReference reference=FirebaseDatabase.getInstance().getReference().child("foods").child(foodId);
                        reference.removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(getApplicationContext(),"Food item deleted",Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
            });
        }
        if(btnName.equals("Add Food"))
        {
        StorageRef= FirebaseStorage.getInstance().getReference("Image");

        choose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FileChooser();
            }
        });


        add_food.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String foodId=food_id.getText().toString();
                String foodName=food_name.getText().toString();
                String categoryId=category_id.getText().toString();
                String foodPrice=price.getText().toString();
                if(imguri==null){
                    Toast.makeText(ManageFood.this,"Category Image is mandatory",Toast.LENGTH_SHORT).show();
                }
                if(TextUtils.isEmpty(foodId))
                {
                    Toast.makeText(ManageFood.this,"Category Id is mandatory",Toast.LENGTH_SHORT).show();
                }
                else if(TextUtils.isEmpty(foodName)){
                    Toast.makeText(ManageFood.this,"Category Name is mandatory",Toast.LENGTH_SHORT).show();
                }
                if(uploadTask!=null && uploadTask.isInProgress()){

                    Toast.makeText(getApplicationContext(),"Uplaod in progress",Toast.LENGTH_SHORT).show();
                }
                else{
                    FileUploader(foodId,foodName,categoryId,foodPrice);
                }

            }
        });
        }
    }

    private void FileChooser() {
        Intent intent=new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1 && resultCode==RESULT_OK && data!=null && data.getData()!=null){
            imguri=data.getData();
            food_image.setImageURI(imguri);
        }
    }

    private String getExtension(Uri uri){
        ContentResolver cr=getContentResolver();
        MimeTypeMap mimeTypeMap=MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(cr.getType(uri));
    }
    private void FileUploader(final String foodId, final String foodName,final String categoryId, final  String foodPrice ) {

        final StorageReference reference=StorageRef.child(System.currentTimeMillis()+"."+getExtension(imguri));

        uploadTask=reference.putFile(imguri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                databaseReference= FirebaseDatabase.getInstance().getReference("foods").child(foodId);
                                HashMap<String,Object> hashMap=new HashMap<>();
                                hashMap.put("name",foodName);
                                hashMap.put("price",foodPrice);
                                hashMap.put("category_id",categoryId);
                                hashMap.put("image",uri.toString());
                                databaseReference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isSuccessful()) {
                                            Toast.makeText(getApplicationContext(), "Food item added", Toast.LENGTH_SHORT).show();
                                            Intent intent=new Intent(getApplicationContext(), ManageFood.class);
                                        }
                                    }
                                });
                            }
                        });
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                    }
                });
   }
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        if(item.getItemId()==android.R.id.home){
            finish();
        }
        return true;
    }


}