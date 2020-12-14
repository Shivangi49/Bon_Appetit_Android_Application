package com.example.bonappetit.Admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.bonappetit.R;
import com.example.bonappetit.model.Order;
import com.example.bonappetit.model.User;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;

public class OrderActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    OrderViewHolder holder;
    Toolbar toolbar;
    Button view;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        view =findViewById(R.id.view);
        recyclerView = findViewById(R.id.recycler_order);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("View Payment");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent (getApplicationContext(), ViewPayment.class);
                startActivity(intent);
            }
        });

        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        FirebaseRecyclerOptions<Order> options = new FirebaseRecyclerOptions.Builder<Order>()
                .setQuery(FirebaseDatabase.getInstance().getReference("Users").child("orders"), Order.class)
                .build();
        holder = new OrderViewHolder(options);
        recyclerView.setAdapter(holder);
    }


    @Override
    public void onStart() {
        super.onStart();
        holder.startListening();
    }

    //order viewholder

    public class OrderViewHolder extends FirebaseRecyclerAdapter<Order, OrderViewHolder.orderViewHolder> {
        public OrderViewHolder(@NonNull FirebaseRecyclerOptions<Order> options) {
            super(options);
        }

        @Override
        protected void onBindViewHolder(@NonNull orderViewHolder orderViewHolder, int i, @NonNull Order order) {
            orderViewHolder.uid.setText("User id: "+order.getId());
            orderViewHolder.orderid.setText("Order id: "+getRef(i).getKey());
        }

        @NonNull
        @Override
        public orderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.order, parent, false);
            return new orderViewHolder(view);
        }


        class orderViewHolder extends RecyclerView.ViewHolder {
            public TextView orderid,uid;

            public orderViewHolder(@NonNull View itemView) {
                super(itemView);
                orderid = (TextView) itemView.findViewById(R.id.userid);
                uid=(TextView) itemView.findViewById(R.id.orderId);
            }


        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        if(item.getItemId()==android.R.id.home){
            finish();
        }
        return true;
    }

}