package app.mpv.com.mpvapp;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.myhexaville.smartimagepicker.ImagePicker;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ChoosePost extends AppCompatActivity {

    //Initialise Views
    @BindView(R.id.mBtnChoose)
    Button btnChoose;
    @BindView(R.id.mTitleProduct)
    EditText titleProduct;
    @BindView(R.id.mBtnUpload)
    Button btnUpload;
    @BindView(R.id.mPriceProduct)
    EditText priceProduct;
    @BindView(R.id.mViewAll)
    TextView viewAll;
    @BindView(R.id.mProductImage)
    ImageView productImage;
    @BindView(R.id.mAddress)
    TextView mAdress;
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    //uri to store file
    private Uri filePath;

    //firebase objects
    private StorageReference mStorageReference;
    private DatabaseReference mDatabaseReference;

    private Location userLocation;
    private LocationCallback locationCallback;
    private LocationRequest locationRequest;
    private FusedLocationProviderClient fusedLocationProviderClient;

    private Geocoder geocoder;
    private ImagePicker imagePicker;
    private ImageView imageSelected;
    private Uri setectedUri;
    private int MY_PERMISSIONS_REQUEST_READ_CONTACTS;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_post);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);

        geocoder = new Geocoder(this, Locale.getDefault());
        //mAdress = findViewById(R.id.mAddress);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        locationRequest = new LocationRequest();

        locationCallback = new LocationCallback() {

            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);
                if (locationResult != null) {
                    userLocation = locationResult.getLocations().get(0);
                    stopLocationUpdates();
                    setAddressNameFromLocation();
                }
            }
        };

        mStorageReference = FirebaseStorage.getInstance().getReference();
        mDatabaseReference = FirebaseDatabase.getInstance().getReference(Constant.DATABASE_PATH_UPLOADS);
        //  initImagePicker();
        startLocationUpdates();
    }


   /* private void initImagePicker() {
        imageSelected = findViewById(R.id.imageSelected);
        imageSelected.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (imagePicker == null) {
                    imagePicker = new ImagePicker(NewPostActivity.this, null, new OnImagePickedListener() {
                        @Override
                        public void onImagePicked(Uri imageUri) {
                            setectedUri = imageUri;
                            imageSelected.setImageURI(imageUri);
                        }
                    });
                }
                imagePicker.choosePicture(true);
            }
        });
    }*/

    private void setAddressNameFromLocation() {
        try {
            List<Address> addresses = new ArrayList<>(geocoder.getFromLocation(
                    userLocation.getLatitude(),
                    userLocation.getLongitude(),
                    1));
            mAdress.setText(addresses.get(0).getAddressLine(0));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void startLocationUpdates() {

        if (ActivityCompat
                .checkSelfPermission(this,
                        Manifest
                                .permission.ACCESS_FINE_LOCATION) != PackageManager
                .PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {


            ActivityCompat.requestPermissions(ChoosePost.this,
                    new String[]{Manifest.permission.READ_CONTACTS},
                    MY_PERMISSIONS_REQUEST_READ_CONTACTS);

            return;
        }
        fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, null);
    }

    private void stopLocationUpdates() {
        fusedLocationProviderClient.removeLocationUpdates(locationCallback);
    }
/*
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        imagePicker.handlePermission(requestCode, grantResults);
    }*/

    @OnClick(R.id.mBtnChoose)
    public void openGallery(View view) {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(Intent.createChooser(intent, "Selecciona un Producto: "), Constant.GALLERY_MAGIC);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constant.GALLERY_MAGIC && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                productImage.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        //  imagePicker.handleActivityResult(resultCode, requestCode, data);

    }

    public String getFileExtension(Uri uri) {
        ContentResolver mContentResolver = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(mContentResolver.getType(uri));
    }

    private void uploadFile() {
        //checking if file is available
        if (filePath != null) {
            //displaying progress dialog while image is uploading
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Subiendo a la plataforma");
            progressDialog.show();

            //getting the storage reference
            StorageReference storageReference = mStorageReference
                    .child(Constant.STORAGE_PATH_UPLOADS + System.currentTimeMillis() +
                            "." + getFileExtension(filePath));

            //adding the file to reference
            storageReference.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            //dismissing the progress dialog
                            progressDialog.dismiss();

                            //displaying success toast
                            Toast.makeText(getApplicationContext(), "Podructo Publicado!", Toast.LENGTH_LONG).show();

                            //creating the upload object to store uploaded image details
                            Post post = new Post(taskSnapshot.getDownloadUrl().toString(),
                                    priceProduct.getText().toString().trim(),
                                    titleProduct.getText().toString().trim(),
                                    mAdress.getText().toString().trim());

                            //adding an upload to firebase database
                            String postId = mDatabaseReference.push().getKey();
                            assert postId != null;
                            mDatabaseReference.child(postId).setValue(post);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            progressDialog.dismiss();
                            Toast.makeText(getApplicationContext(), exception.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            //displaying the upload progress
                            double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                            progressDialog.setTitle("Actualizando plataforma");
                            progressDialog.setMessage("Cargando " + ((int) progress) + "%...");
                        }
                    });
        } else {
            //display an error if no file is selected
        }
    }

    @OnClick(R.id.mBtnUpload)
    public void uploadPost(View view) {
        uploadFile();
    }

    @OnClick(R.id.mViewAll)
    public void viewAllPosts(View view) {
        Intent intent = new Intent(getApplicationContext(), PostActivity.class);
        startActivity(intent);
    }

}
