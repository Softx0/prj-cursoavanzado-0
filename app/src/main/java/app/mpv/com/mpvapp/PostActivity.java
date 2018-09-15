package app.mpv.com.mpvapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PostActivity extends AppCompatActivity {

    //initialise views
    private ImageView uploadPost;
    private ImageView iconMPV;

    private ImageView signOut;
    //objects
    private ProgressDialog mProgresDialog;

    private RecyclerView mRecyclerView;

    private RecyclerView.Adapter mAdapter;

    private DatabaseReference mSecondDatabaseReference;

    private List<Post> postsUsers;

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        mAuth = FirebaseAuth.getInstance();
        iconMPV = findViewById(R.id.ic_iconActionBar);

        setSupportActionBar(toolbar);

        mRecyclerView = findViewById(R.id.post_rv);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(PostActivity.this));

        postsUsers = new ArrayList<>();
        mProgresDialog = new ProgressDialog(this);
        signOut = findViewById(R.id.sign_out);
        signOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAuth.signOut();
                Intent intent = new Intent(PostActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
        uploadPost = findViewById(R.id.ic_upload);
        uploadPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PostActivity.this, ChoosePost.class);
                startActivity(intent);
            }
        });

        mProgresDialog.setTitle("Buscando productos mas recientes!");
        mProgresDialog.setMessage("Porfavor espera...");
        mProgresDialog.show();

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
                    Collections.reverse(postsUsers);
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
