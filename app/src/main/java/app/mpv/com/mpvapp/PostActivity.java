package app.mpv.com.mpvapp;

import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PostActivity extends AppCompatActivity {

    @BindView(R.id.recycler_view_layout)
    RecyclerView mRecyclerView;


    private FirebaseRecyclerAdapter<Post, PostAdapter> mRvAdapter;
    private DatabaseReference mDatabaseReference;

    public static final String REFERENCE_POST = "POST";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        mainMethod();

    }

    public void mainMethod() {
        mDatabaseReference = FirebaseDatabase.getInstance().getReference(REFERENCE_POST);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(PostActivity.this));
        setupAdapter();
    }

    public void setupAdapter() {
        mRvAdapter = new FirebaseRecyclerAdapter<Post, PostAdapter>(

                Post.class, R.layout.recycler_item, PostAdapter.class, mDatabaseReference
        ) {
            @Override
            protected void populateViewHolder(PostAdapter viewHolder, Post model, int position) {
                viewHolder.setPostImage(model.getImageUrl());
                viewHolder.setNumLikes(model.getPriceImage());
            }
        };
    }


    public static class PostAdapter extends RecyclerView.ViewHolder {


        ImageView imagePost;

        public ImageView btnLike;
        public TextView numLikes;

        public PostAdapter(View itemView) {
            super(itemView);

        }

        public void setPostImage (String url){

        }

        public void setNumLikes (long numLikes){

        }
    }
}
