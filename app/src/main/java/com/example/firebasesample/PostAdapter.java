package com.example.firebasesample;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostViewHolder> {

    private List<Post> mListPosts;
    private OnPostClickListener mListener;


    public PostAdapter(List<Post> posts, OnPostClickListener listener) {
        mListener = listener;
        mListPosts = posts;

    }

    public class PostViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView postTitle, postBody, postAuthor, timePosted;
        public OnPostClickListener onPostClickListener;

        public PostViewHolder(@NonNull View itemView, OnPostClickListener listener) {
            super(itemView);
            postTitle = itemView.findViewById(R.id.post_title);
            postBody = itemView.findViewById(R.id.post_description);
            postAuthor = itemView.findViewById(R.id.post_author);
            timePosted = itemView.findViewById(R.id.time_created);

            onPostClickListener = listener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onPostClickListener.onPostClick(getAdapterPosition());
        }
    }



    @NonNull
    @Override
    public PostAdapter.PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        //Inflate the layout
        View view = inflater.inflate(R.layout.recycler_item, parent, false);
        //Return instance of viewHolder
        return new PostViewHolder(view, mListener);
    }

    @Override
    public void onBindViewHolder(@NonNull PostAdapter.PostViewHolder holder, int position) {
        Post currentPost = mListPosts.get(position);

        holder.postTitle.setText(currentPost.getPostTitle());
        holder.postBody.setText(currentPost.getPostBody());
        holder.postAuthor.setText(currentPost.getPostAuthor());
        holder.timePosted.setText(currentPost.getPostCreated());
    }

    @Override
    public int getItemCount() {
        return mListPosts.size();
    }

    public void clearList() {
        mListPosts = new ArrayList<Post>();
    }

    public interface OnPostClickListener {
        void onPostClick(int position);
    }


}
