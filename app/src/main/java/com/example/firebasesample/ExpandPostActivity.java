package com.example.firebasesample;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Comment;

import java.util.ArrayList;
import java.util.List;

public class ExpandPostActivity extends AppCompatActivity {

    private TextView mPostTitle, mPostDescription, mPostAuthor;
    private RecyclerView recyclerView;
    private EditText mInputCommentEdit;
    private Button mSubmitCommentButton;

    private DatabaseReference mDatabase;
    final List<PostComments> currentPostComments = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expand_post);

        mPostTitle = findViewById(R.id.epost_title);
        mPostDescription = findViewById(R.id.epost_description);
        mPostAuthor = findViewById(R.id.epost_author);
        mInputCommentEdit = findViewById(R.id.input_comment_edit);
        mSubmitCommentButton = findViewById(R.id.submit_comment_button);


        //Use to fetch current user "nickname"
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        final String displayName = user.getDisplayName();

        //Assign post class received from intent and populate the expanded post screen
        final Post clickedPost = (Post) getIntent().getSerializableExtra("Clicked Post");
        //Save this post Id for use later
        String currentPostId = clickedPost.getPostId();

        //Display post comments
        recyclerView = findViewById(R.id.comments_recyclerview);
        final CommentAdapter adapter = new CommentAdapter(getCommentsFromFirebase(currentPostId));
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        mPostTitle.setText(clickedPost.getPostTitle());
        mPostAuthor.setText(clickedPost.getPostAuthor());
        mPostDescription.setText(clickedPost.getPostBody());


        mSubmitCommentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Push an instance of the post's comments along the path /comments/{postId}
                DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference("comments")
                        .child(clickedPost.getPostId()).push();

                //Push the comment along the path
                PostComments newComment = new PostComments(clickedPost.getPostId(),
                        mInputCommentEdit.getText().toString(), "mhassan1");
                dbRef.setValue(newComment);

                //Add new comment to list of comments in this activity
                //and update recyclerview
                currentPostComments.add(newComment);
                adapter.notifyItemInserted(currentPostComments.size()-1);

                //Clear the comment edit text field
                mInputCommentEdit.getText().clear();

                // updatePostInFirebase(clickedPost);
            }
        });


    }

    public List<PostComments> getCommentsFromFirebase(String currentPostId) {

        //Get a ref to the post's comments
        DatabaseReference databaseReference = FirebaseDatabase.getInstance()
                .getReference("comments").child(currentPostId);

        //First-add to the current post's comments
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot commentSnapshot : snapshot.getChildren()) {
                    currentPostComments.add(commentSnapshot.getValue(PostComments.class));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return currentPostComments;
    }

    //Deprecated
    public void updatePostInFirebase(Post post) {
        mDatabase = FirebaseDatabase.getInstance().getReference("posts");
        Query query = mDatabase.child("posts").orderByChild("postId").equalTo(post.getPostId());
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


}