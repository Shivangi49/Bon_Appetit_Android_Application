package com.example.bonappetit.Admin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.bonappetit.LoginActivity;
import com.example.bonappetit.R;

public class AdminActivity extends AppCompatActivity {
    Button  addFood,deleteFood,addCategory,deleteCategory,signOut,viewfeedback,viewpayment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        addFood=findViewById(R.id.AddFood);
        deleteFood=findViewById(R.id.DeleteFood);
        viewfeedback=findViewById(R.id.vfeedback);
        viewpayment=findViewById(R.id.vpayment);
        viewfeedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(), ViewFeedback.class);
                startActivity(intent);
            }
        });

        addFood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(getApplicationContext(), ManageFood.class);
                String btn=addFood.getText().toString();
                intent.putExtra("btnname",btn);
                startActivity(intent);
            }
        });

        deleteFood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(getApplicationContext(), ManageFood.class);
                String btn=deleteFood.getText().toString();
                intent.putExtra("btnname",btn);
                startActivity(intent);
            }
        });


        addCategory=findViewById(R.id.AddCategory);
        deleteCategory=findViewById(R.id.DeleteCategory);


        addCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(getApplicationContext(), ManageCategory.class);
                String btn=addCategory.getText().toString();
                intent.putExtra("btnname",btn);
                startActivity(intent);
            }
        });

        deleteCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(getApplicationContext(), ManageCategory.class);
                String btn=deleteCategory.getText().toString();
                intent.putExtra("btnname",btn);
                startActivity(intent);
            }
        });

        viewpayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent (getApplicationContext(), OrderActivity.class);
                startActivity(intent);
            }
        });

        signOut=findViewById(R.id.sign_out);
        signOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent (getApplicationContext(), LoginActivity.class);
                startActivity(intent);
            }
        });



    }


}