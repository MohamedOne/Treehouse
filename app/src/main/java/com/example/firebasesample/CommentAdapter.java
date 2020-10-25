package com.example.firebasesample;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentViewHolder> {

    private List<PostComments> mCurrentPostComments;

    public CommentAdapter(List<PostComments> postComments) {
        this.mCurrentPostComments = postComments;
    }


    public class CommentViewHolder extends RecyclerView.ViewHolder {
        public TextView comment, commentAuthor;


        public CommentViewHolder(@NonNull View itemView) {
            super(itemView);
            comment = itemView.findViewById(R.id.comment_textview);
            commentAuthor = itemView.findViewById(R.id.comment_author);
        }
    }

    @NonNull
    @Override
    public CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        //Inflate the layout
        View view = inflater.inflate(R.layout.comment_recycler_item, parent, false);
        //Return instance of viewHolder
        return new CommentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentViewHolder holder, int position) {
        PostComments postComments = mCurrentPostComments.get(position);

        //Populate the two textviews with our post
        holder.comment.setText(postComments.getComment());
        holder.commentAuthor.setText(postComments.getCommentAuthor());

    }

    @Override
    public int getItemCount() {
        return mCurrentPostComments.size();
    }


}
