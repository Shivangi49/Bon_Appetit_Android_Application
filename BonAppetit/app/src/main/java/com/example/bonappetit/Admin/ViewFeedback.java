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
import android.widget.TextView;

import com.example.bonappetit.R;
import com.example.bonappetit.model.User;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.FirebaseDatabase;

public class ViewFeedback extends AppCompatActivity {
    RecyclerView recyclerView;
    FeedbackViewHolder holder;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_feedback);


        recyclerView = findViewById(R.id.recycler_food);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Feedbacks");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        recyclerView = findViewById(R.id.recycler_feedback);
        recyclerView.setHasFixedSize(true);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        FirebaseRecyclerOptions<User> options = new FirebaseRecyclerOptions.Builder<User>()
                .setQuery(FirebaseDatabase.getInstance().getReference().child("Users"), User.class)
                .build();
        holder = new FeedbackViewHolder(options);
        recyclerView.setAdapter(holder);

    }



    @Override
    public void onStart() {
        super.onStart();
        holder.startListening();
    }

    //feedback viewholder

    public class FeedbackViewHolder extends FirebaseRecyclerAdapter<User,FeedbackViewHolder.feedbackViewHolder>{
        public FeedbackViewHolder(@NonNull FirebaseRecyclerOptions<User> options) {
            super(options);
        }

        @Override
        protected void onBindViewHolder(@NonNull FeedbackViewHolder.feedbackViewHolder feedbackViewHolder, int i, @NonNull User user) {
            feedbackViewHolder.username.setText(user.getName());
            feedbackViewHolder.feedback.setText(user.getFeedback());

        }

        @NonNull
        @Override
        public FeedbackViewHolder.feedbackViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.feedback, parent, false);
            return new feedbackViewHolder(view);
        }


        class feedbackViewHolder extends RecyclerView.ViewHolder {
            public TextView feedback,username;

            public feedbackViewHolder(@NonNull View itemView) {
                super(itemView);
                feedback = (TextView) itemView.findViewById(R.id.feedback);
                username=(TextView) itemView.findViewById(R.id.fusername);
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