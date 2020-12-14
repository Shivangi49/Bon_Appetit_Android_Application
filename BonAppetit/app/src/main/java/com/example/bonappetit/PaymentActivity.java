package com.example.bonappetit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class PaymentActivity extends AppCompatActivity {

    EditText cardno,cardname,cardval,cardcvv,upiId;
    Button payment;
    TextView totalprice,orderid,name,address,fooditems,paymode,overalltotal,spay,carddet,upidet;
    RadioButton paycard,payupi;
    String mode;
    String uname,uaddress,usercontact;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        cardno=findViewById(R.id.card_number);
        cardname=findViewById(R.id.card_name);
        cardval=findViewById(R.id.valid);
        cardcvv=findViewById(R.id.cvv);
        payment=findViewById(R.id.payment);
        totalprice=findViewById(R.id.totalprice);
        orderid=findViewById(R.id.orderid);
        name=findViewById(R.id.name);
        address=findViewById(R.id.address);
        fooditems=findViewById(R.id.fooditems);
        paymode=findViewById(R.id.paymentmode);
        overalltotal=findViewById(R.id.total);
        paycard=findViewById(R.id.card);
        payupi=findViewById(R.id.upi);
        upiId=findViewById(R.id.upiId);
        carddet=findViewById(R.id.carddetails);
        upidet=findViewById(R.id.upidetails);
        spay=findViewById(R.id.selectpay);
        final int random = ThreadLocalRandom.current().nextInt(01, 5000);
        orderid.setText("Order id: "+String.valueOf(random));
        final String total=getIntent().getStringExtra("total price");
        totalprice.setText("Total price: ₹"+total);

        FirebaseAuth auth=FirebaseAuth.getInstance();
        final FirebaseUser firebaseUser=auth.getCurrentUser();
        final String id=firebaseUser.getUid();

        payment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(payupi.isChecked()==false && paycard.isChecked()==false){
                    Toast.makeText(getApplicationContext(), "Select payment mode", Toast.LENGTH_SHORT).show();
                }

                if(paycard.isChecked()) {
                    mode="Via Card";
                    if (TextUtils.isEmpty(cardno.getText().toString()) || TextUtils.isEmpty(cardname.getText().toString()) ||
                            TextUtils.isEmpty(cardval.getText().toString()) || TextUtils.isEmpty(cardcvv.getText().toString())) {
                        Toast.makeText(getApplicationContext(), "Enter all the card details", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        cardcvv.setVisibility(View.GONE);
                        cardval.setVisibility(View.GONE);
                        totalprice.setVisibility(View.GONE);
                        carddet.setVisibility(View.GONE);
                        upidet.setVisibility(View.GONE);
                        spay.setVisibility(View.GONE);
                        cardname.setVisibility(View.GONE);
                        cardno.setVisibility(View.GONE);
                        name.setVisibility(View.VISIBLE);
                        address.setVisibility(View.VISIBLE);
                        fooditems.setVisibility(View.VISIBLE);
                        paymode.setVisibility(View.VISIBLE);
                        overalltotal.setVisibility(View.VISIBLE);
                        payment.setVisibility(View.GONE);
                        upiId.setVisibility(View.GONE);
                        payupi.setVisibility(View.GONE);
                        paycard.setVisibility(View.GONE);

                        final DatabaseReference reference=FirebaseDatabase.getInstance().getReference("Users").child(id);
                        reference.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                uname=snapshot.child("name").getValue().toString();
                                uaddress=snapshot.child("address").getValue().toString();
                                usercontact=snapshot.child("contact").getValue().toString();
                                name.setText("Name: "+uname);
                                address.setText("Address: "+uaddress);
                                ArrayList<String> listoffood = (ArrayList<String>) getIntent().getSerializableExtra("food items");

                                fooditems.setText("Food items: "+listoffood);
                                paymode.setText("Payment mode: "+mode);
                                overalltotal.setText("Total price: ₹"+total);
                                reference.child("payment").child(String.valueOf(random)).child("payment staus").setValue("Complete");
                                reference.child("payment").child(String.valueOf(random)).child("address").setValue(uaddress);
                                reference.child("payment").child(String.valueOf(random)).child("contact").setValue(usercontact);
                                Toast.makeText(PaymentActivity.this,"Order Confirmed",Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });


                    }
                }
                if(payupi.isChecked()){
                    mode="Via upi";
                    if(TextUtils.isEmpty(upiId.getText().toString())){
                        Toast.makeText(getApplicationContext(), "Enter Upi Id", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        cardcvv.setVisibility(View.GONE);
                        cardval.setVisibility(View.GONE);
                        totalprice.setVisibility(View.GONE);
                        payment.setVisibility(View.GONE);
                        cardname.setVisibility(View.GONE);
                        cardno.setVisibility(View.GONE);
                        name.setVisibility(View.VISIBLE);
                        address.setVisibility(View.VISIBLE);
                        fooditems.setVisibility(View.VISIBLE);
                        paymode.setVisibility(View.VISIBLE);
                        overalltotal.setVisibility(View.VISIBLE);
                        carddet.setVisibility(View.GONE);
                        upidet.setVisibility(View.GONE);
                        spay.setVisibility(View.GONE);
                        upiId.setVisibility(View.GONE);
                        payupi.setVisibility(View.GONE);
                        paycard.setVisibility(View.GONE);

                        final DatabaseReference reference=FirebaseDatabase.getInstance().getReference("Users").child(id);
                        reference.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                 uname=snapshot.child("name").getValue().toString();
                                 uaddress=snapshot.child("address").getValue().toString();
                                 usercontact=snapshot.child("contact").getValue().toString();
                                name.setText("Name: "+uname);
                                address.setText("Address: "+uaddress);
                                ArrayList<String> listoffood = (ArrayList<String>) getIntent().getSerializableExtra("food items");
                                fooditems.setText("Food items: "+listoffood);
                                paymode.setText("Payment mode: "+mode);
                                overalltotal.setText("Total price: ₹"+total);
                                reference.child("payment").child(String.valueOf(random)).child("payment staus").setValue("Complete");
                                reference.child("payment").child(String.valueOf(random)).child("address").setValue(uaddress);
                                reference.child("payment").child(String.valueOf(random)).child("contact").setValue(usercontact);
                                Toast.makeText(PaymentActivity.this,"Order Confirmed",Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                        final DatabaseReference datareference=FirebaseDatabase.getInstance().getReference("Users");
                        datareference.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                datareference.child("orders").child(String.valueOf(random)).child("id").setValue(id);
                                datareference.child("orders").child(String.valueOf(random)).child("orderid").setValue(String.valueOf(random));
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

                    }
                }

            }
        });
        Toolbar toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Payment Details");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        if(item.getItemId()==android.R.id.home){
            finish();
        }
        return true;
    }

}
