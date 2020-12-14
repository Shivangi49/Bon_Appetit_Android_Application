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

public class ManageCategory extends AppCompatActivity {
    EditText category_name,category_id;
    Button add_category,choose,upload;
    ImageView category_image;
    public StorageReference StorageRef;
    public Uri imguri;
    DatabaseReference databaseReference;
    private StorageTask uploadTask;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_category);

        category_name=findViewById(R.id.categoryName);
        category_id=findViewById(R.id.categoryId);
        choose=findViewById(R.id.choose);
        category_image=findViewById(R.id.categoryImage);
        add_category=findViewById(R.id.add_category);

        String btnName=getIntent().getStringExtra("btnname");
        add_category.setText(btnName);

        //toolbar
        Toolbar toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle(btnName);

        if(btnName.equals("Delete Category")){
            category_name.setVisibility(View.GONE);
            category_image.setVisibility(View.GONE);
            choose.setVisibility(View.GONE);

            add_category.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    final String categoryId=category_id.getText().toString();
                    if(TextUtils.isEmpty(categoryId)){
                        Toast.makeText(getApplicationContext(),"Provide category Id",Toast.LENGTH_SHORT).show();
                    }
                    else{
                        DatabaseReference reference=FirebaseDatabase.getInstance().getReference().child("category").child(categoryId);
                        reference.removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(getApplicationContext(),"Category deleted",Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
            });

        }
        if(btnName.equals("Add Category")){
                StorageRef= FirebaseStorage.getInstance().getReference("Image");

                choose.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        FileChooser();
                    }
                });

                add_category.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String categoryId=category_id.getText().toString();
                        String categoryName=category_name.getText().toString();
                        if(imguri==null){
                            Toast.makeText(ManageCategory.this,"Category Image is mandatory",Toast.LENGTH_SHORT).show();
                        }
                        if(TextUtils.isEmpty(categoryId))
                        {
                            Toast.makeText(ManageCategory.this,"Category Id is mandatory",Toast.LENGTH_SHORT).show();
                        }
                        else if(TextUtils.isEmpty(categoryName)){
                            Toast.makeText(ManageCategory.this,"Category Name is mandatory",Toast.LENGTH_SHORT).show();
                        }
                        else{
                            FileUploader(categoryId,categoryName);
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
            category_image.setImageURI(imguri);
        }
    }

    private String getExtension(Uri uri){
        ContentResolver cr=getContentResolver();
        MimeTypeMap mimeTypeMap=MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(cr.getType(uri));
    }
    private void FileUploader(final String categoryId, final String categoryName) {

        final StorageReference reference=StorageRef.child(imguri.getLastPathSegment()+"."+getExtension(imguri));

        uploadTask=reference.putFile(imguri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                databaseReference=FirebaseDatabase.getInstance().getReference("category").child(categoryId);
                                HashMap<String,Object> hashMap=new HashMap<>();
                                hashMap.put("name",categoryName);
                                hashMap.put("image",uri.toString());
                                databaseReference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isSuccessful()) {
                                            Toast.makeText(getApplicationContext(), "Category uploaded", Toast.LENGTH_SHORT).show();
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