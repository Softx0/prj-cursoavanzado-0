package app.mpv.com.mpvapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PostActivity extends AppCompatActivity {

    //initialise views
    private Button mUploadBtn;

    //objects
    private ProgressDialog mProgresDialog;

    private RecyclerView mRecyclerView;

    private RecyclerView.Adapter mAdapter;

    private DatabaseReference mSecondDatabaseReference;

    private List<Post> postsUsers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        mRecyclerView = (RecyclerView) findViewById(R.id.post_rv);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(PostActivity.this));

        postsUsers = new ArrayList<>();
        mProgresDialog = new ProgressDialog(this);

        mUploadBtn = (Button) findViewById(R.id.redirect_post);
        mUploadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PostActivity.this, ChoosePost.class);
                startActivity(intent);
            }
        });

        mProgresDialog.setTitle("Loading Lastest Posts!");
        mProgresDialog.setMessage("Please wait...");
        mProgresDialog.show();
        Post mPost = new Post();
        Toast.makeText(this, ""+mPost.getImageUrl(),Toast.LENGTH_LONG).show();
        mSecondDatabaseReference = FirebaseDatabase.getInstance().getReference(Constant.DATABASE_PATH_UPLOADS);

        //adding an event listener to fetch values
        mSecondDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                //dismissing the progress dialog
                mProgresDialog.dismiss();

                //iterating through all the values in database
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    Post post = postSnapshot.getValue(Post.class);
                    postsUsers.add(post);
                }
                //creating adapter
                mAdapter = new PostAdapter(getApplicationContext(), postsUsers);

                //adding adapter to recyclerview
                mRecyclerView.setAdapter(mAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                mProgresDialog.dismiss();
            }
        });
    }

}
