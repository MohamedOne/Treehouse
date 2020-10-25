package com.example.firebasesample;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class MainScreenActivity extends AppCompatActivity implements PostAdapter.OnPostClickListener {

    private DatabaseReference mDatabase;
    private FirebaseUser mUser;

    private RecyclerView recyclerView;
    PostAdapter adapter;
    final List<Post> posts = new ArrayList<Post>();

    //Get instance of shared preferences
    final SharedPreferences prefs = this.getSharedPreferences(
            "com.local.treehouse", Context.MODE_PRIVATE);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);

        mDatabase = FirebaseDatabase.getInstance().getReference("users");
        mUser = FirebaseAuth.getInstance().getCurrentUser();

        final SharedPreferences sharedPref = getSharedPreferences("UserInfo" , Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("UserNickname", "mhassan1");
        editor.commit();

        //Todo check if you can pull nick name from 'prefs'

        //Instantiate recyclerview on main screen
        recyclerView = findViewById(R.id.recyclerview);
        setUpRecyclerView();
        //recyclerView.


        final FloatingActionButton fab = findViewById(R.id.fab);

        Toolbar mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);


        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        fab.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.CUPCAKE)
            @Override
            public void onClick(View v) {
                Button closePopup;
                Button cancelPopup;
                final PopupWindow popupWindow;
                final EditText newPostTitle, newPostDescription;

                LayoutInflater layoutInflater = (LayoutInflater)
                        getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View customView = layoutInflater.inflate(R.layout.popup_window, null);

                closePopup = customView.findViewById(R.id.createNewPost);
                cancelPopup = customView.findViewById(R.id.cancel_popup);
                newPostTitle = customView.findViewById(R.id.editTextPostTitle);
                newPostDescription = customView.findViewById(R.id.editTextPostBody);

                popupWindow = new PopupWindow(customView, WindowManager.LayoutParams.WRAP_CONTENT,
                        WindowManager.LayoutParams.WRAP_CONTENT);

                popupWindow.showAtLocation(v.getRootView(), Gravity.CENTER, 0, 0);
                popupWindow.setFocusable(true);
                popupWindow.setElevation(12f);
                popupWindow.setAnimationStyle(android.R.style.Animation_Dialog);
                popupWindow.update();

                closePopup.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String postId, postTitle, postBody, postAuthor, postDate;



                        postTitle = newPostTitle.getText().toString();
                        postBody = newPostDescription.getText().toString();
                        postAuthor = mUser.getDisplayName();
                        postDate = getTimePostCreated();

                        //Upon closing the popup, push the new post to the database
                        pushToFirebase(new Post(String.valueOf(generatePostId()), postTitle,
                                postBody, sharedPref.getString("UserNickname", "One"),
                                postDate));

                        Toast.makeText(MainScreenActivity.this, getPostAuthor(),
                                Toast.LENGTH_SHORT).show();
                        popupWindow.dismiss();
                    }
                });

                cancelPopup.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        popupWindow.dismiss();
                    }
                });


            }
        });

    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.top_app_bar, menu);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.user_account:
                openAccountActivity();
                return true;
            case R.id.overflow:

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void openAccountActivity() {
        Intent intent = new Intent(this, AccountActivity.class);
        startActivity(intent);
    }

    private String getUserDisplayName() {
        if (mUser != null) {
            return mUser.getDisplayName();
        } else {
            return null;
        }
    }

    private String getTimePostCreated() {
        Date currentTime = Calendar.getInstance().getTime();
        return currentTime.toString();
    }

    private void pushToFirebase(Post post) {
        mDatabase = FirebaseDatabase.getInstance().getReference("users");
        mDatabase.child(mUser.getUid()).setValue(post);

        mDatabase = FirebaseDatabase.getInstance().getReference("posts").push();
        mDatabase.setValue(post);
        //Add to list of posts in this activity. The activity holds an instance of the data
        posts.add(post);
        adapter.notifyItemInserted(posts.size()-1);

    }

    private List<Post> getPostsFromFirebase() {
        mDatabase = FirebaseDatabase.getInstance().getReference("posts");

        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot postsnapshot : snapshot.getChildren()) {
                    String id, title, body, author, timeCreated;
                    id = postsnapshot.child("postId").getValue(String.class);
                    title = postsnapshot.child("postTitle").getValue(String.class);
                    body = postsnapshot.child("postBody").getValue(String.class);
                    author = postsnapshot.child("postAuthor").getValue(String.class);
                    timeCreated = postsnapshot.child("postCreated").getValue(String.class);

                    //First-add to the list of posts.
                    posts.add(new Post(id, title, body, author, timeCreated));
                    }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return posts;
    }

    private void setUpRecyclerView() {
        adapter  = new PostAdapter(getPostsFromFirebase(), this);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);



    }

    private String getPostAuthor() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user != null) {
            return user.getDisplayName();
        } else {
            return null;
        }
    }


    @Override
    public void onPostClick(int position) {
        Post clickPost = posts.get(position);
        Intent intent = new Intent(this, ExpandPostActivity.class);
        intent.putExtra("Clicked Post", clickPost);
        startActivity(intent);


    }

    public int generatePostId() {
        Random random = new Random();
        return random.nextInt(25_000 - 10_000 + 1) + 10_000;
    }


}