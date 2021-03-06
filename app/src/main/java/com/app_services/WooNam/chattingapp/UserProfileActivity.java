package com.app_services.WooNam.chattingapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.app_services.WooNam.chattingapp.UserModel.User;
import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserProfileActivity extends AppCompatActivity {

    TextView tv_name, tv_username, tv_about,tv_school,tv_aword,tv_git,tv_favorite;
    CircleImageView profile_pic;
    DatabaseReference reference;
    FirebaseUser firebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("사용자 정보");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        tv_name = findViewById(R.id.tvName);
        tv_about = findViewById(R.id.tvAbout);
        tv_username = findViewById(R.id.tvUsername);
        tv_school= findViewById(R.id.tvSchool);
        tv_aword=findViewById(R.id.tvAword);
        tv_git=findViewById(R.id.tvGit);
        tv_favorite=findViewById(R.id.tvFavorite);


        profile_pic = findViewById(R.id.user_profile_pic);

        String userId = getIntent().getStringExtra("userId");
        reference = FirebaseDatabase.getInstance().getReference("Users").child(userId);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                assert user != null;
                tv_name.setText(user.getName());
                tv_about.setText(user.getUser_about());
                tv_username.setText(user.getUsername());
                tv_school.setText(user.getSchool());
                tv_aword.setText(user.getAword());
                tv_git.setText(user.getGit());
                tv_favorite.setText(user.getFavorite());
                if(user.getImageURL().equals("default")){
                    profile_pic.setImageResource(R.mipmap.ic_default_profile_pic);
                } else{
                    Glide.with(getApplicationContext()).load(user.getImageURL()).into(profile_pic);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.i("Database Error", "onCancelled: Error -> "+ databaseError.toString());
            }
        });
    }


    private void Status(){
        reference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("status", "online");
        reference.updateChildren(hashMap);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Status();
    }

}

