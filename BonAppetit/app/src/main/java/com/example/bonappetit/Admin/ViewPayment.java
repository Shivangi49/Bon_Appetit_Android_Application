package com.example.bonappetit.Admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.TextView;

import com.example.bonappetit.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ViewPayment extends AppCompatActivity {
    EditText txtuserid,txtorderid;
    Button viewpay;
    GridLayout gridLayout;
    TextView paymentstatus,orderId,ucontact,uaddress;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_payment);

        txtuserid=findViewById(R.id.UserId);
        txtorderid=findViewById(R.id.userorderid);
        viewpay=findViewById(R.id.ViewPayment);
        gridLayout=findViewById(R.id.userpaymentdetails);
        paymentstatus=findViewById(R.id.paymentstatus);
        orderId=findViewById(R.id.orderid);
        ucontact=findViewById(R.id.contact);
        uaddress=findViewById(R.id.address);

        viewpay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String id=txtuserid.getText().toString();
                final String orderid=txtorderid.getText().toString();
                gridLayout.setVisibility(View.VISIBLE);
                final DatabaseReference reference= FirebaseDatabase.getInstance().getReference("Users");
                reference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String payment=snapshot.child(id).child("payment").child(orderid).child("payment staus").getValue().toString();
                        String contact=snapshot.child(id).child("payment").child(orderid).child("contact").getValue().toString();
                        String address=snapshot.child(id).child("payment").child(orderid).child("address").getValue().toString();
                        paymentstatus.setText(payment);
                        orderId.setText(orderid);
                        ucontact.setText(contact);
                        uaddress.setText(address);


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });



            }
        });
    }
}