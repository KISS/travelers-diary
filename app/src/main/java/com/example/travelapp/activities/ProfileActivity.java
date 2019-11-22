package com.example.travelapp.activities;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.travelapp.Fragment.MyAdapter;
import com.example.travelapp.Fragment.ViewTripFragment;
import com.example.travelapp.R;
import com.example.travelapp.configs.Constants;
import com.example.travelapp.models.Trip;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.google.firebase.storage.FirebaseStorage.getInstance;

public class ProfileActivity extends AppCompatActivity {

    private final String TAG = "Profile Activity";

    FirebaseAuth firebaseAuth;
    FirebaseUser user;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    StorageReference storageReference;
    String storagePath = "User_Profile_Images/";

    private RecyclerView mRecyclerView;

    private FirebaseStorage mStorage;
    private DatabaseReference mDataRef;
    private ValueEventListener mDBListner;


    ImageView photo;
    TextView mfirstname, mlastname, memail, mStateInfo, mPhoneNum;
    FloatingActionButton fab;
    FloatingActionButton logoutBtn;

    ProgressDialog pd;

    private static final int CAMERA_REQUEST_CODE = 100;
    private static final int STORAGE_REQUEST_CODE = 200;
    private static final int IMAGE_PICK_GALLERY_CODE = 300;
    private static final int IMAGE_PICK_CAMERA_CODE = 400;

    String cameraPermission[];
    String storagePermission[];


    Uri image_uri;

    String profilePhoto;

    String mLooggedInUserId;

    List<String> mTripIds;
    List<Trip> mTrips;
    MyAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_nav);
        bottomNavigationView.setOnNavigationItemSelectedListener(navListener);
        bottomNavigationView.getMenu().findItem(R.id.nav_profile).setChecked(true);

        initFirebaseData();

        cameraPermission = new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        storagePermission = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};

        photo = findViewById(R.id.userAddPhoto);
        mfirstname = findViewById(R.id.userAddFirstName);
        mlastname = findViewById(R.id.userAddLastName);
        memail = findViewById(R.id.ShowEmail);
        mStateInfo = findViewById(R.id.userAddStateInfo);
        mPhoneNum = findViewById(R.id.userAddPhoneNum);

        fab = findViewById(R.id.fbtn);
        logoutBtn = findViewById(R.id.logoutBtn);


        pd = new ProgressDialog(ProfileActivity.this);

        mRecyclerView = findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        mStorage = getInstance();
        mDataRef = FirebaseDatabase.getInstance().getReference(Constants.DATABASE_PATH_UPLOADS);


        getUserDetails();

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showEditProfilePopup();
            }
        });

        logoutBtn.setOnClickListener(v -> {
            logoutUser();
        });

        mTripIds = new ArrayList<>();
        mTrips = new ArrayList<>();
        configureRecyclerView();
    }

    private void logoutUser() {
        finish();
        Intent signInActivityIntent = new Intent(ProfileActivity.this, SignInActivity.class);
        startActivity(signInActivityIntent);
    }

    private void initFirebaseData() {
        firebaseAuth = FirebaseAuth.getInstance();

        mLooggedInUserId = firebaseAuth.getCurrentUser().getUid();

        user = firebaseAuth.getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference(Constants.DATABASE_PATH_USERS);
        storageReference = getInstance().getReference();
    }


    private void getUserDetails() {
        Query query = databaseReference.orderByChild("email").equalTo(user.getEmail());
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    String image = "" + ds.child("profilePictureUrl").getValue();
                    String firstname = "" + ds.child("firstName").getValue();
                    String lastname = "" + ds.child("lastName").getValue();
                    String email = "" + ds.child("email").getValue();
                    String stateinfo = "State Info: " + ds.child("stateInfo").getValue();
                    String phonenum = "Phone Num: " + ds.child("phoneNum").getValue();

                    mfirstname.setText(firstname);
                    mlastname.setText(lastname);
                    memail.setText(email);
                    mStateInfo.setText(stateinfo);
                    mPhoneNum.setText(phonenum);

                    if (!image.isEmpty()) {
                        Picasso.get().load(image).into(photo);
                    }

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

        private boolean checkStoragePermission () {
            boolean result = ContextCompat.checkSelfPermission(ProfileActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == (PackageManager.PERMISSION_GRANTED);
            return result;
        }

        @RequiresApi(api = Build.VERSION_CODES.M)
        private void requestedStoragePermission () {
            requestPermissions(storagePermission, STORAGE_REQUEST_CODE);

        }

        private boolean checkCameraPermission () {
            boolean result = ContextCompat.checkSelfPermission(ProfileActivity.this, Manifest.permission.CAMERA)
                    == (PackageManager.PERMISSION_GRANTED);
            boolean result1 = ContextCompat.checkSelfPermission(ProfileActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == (PackageManager.PERMISSION_GRANTED);
            return result && result1;
        }

        @RequiresApi(api = Build.VERSION_CODES.M)
        private void requestedCameraPermission () {
            requestPermissions(cameraPermission, CAMERA_REQUEST_CODE);

        }

        /*
         * Edit pop up and its options
         *
         * */
        private void showEditProfilePopup () {
            String options[] = {"Edit Profile Picture", "Edit First Name", "Edit Last Name", "Edit State Info", "Edit Phone Number"};
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Choose ");
            builder.setItems(options, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (which == 0) {
                        pd.setMessage("Updating ProfileActivity Picture");
                        profilePhoto = "profilePictureUrl";
                        showImagePicDialog();
                    } else if (which == 1) {
                        pd.setMessage("Updating First Name");
                        showEditInfoUpdatePopup("firstName");
                    } else if (which == 2) {
                        pd.setMessage("Updating Last Name");
                        showEditInfoUpdatePopup("lastName");

                    } else if (which == 3) {
                        pd.setMessage("Updating State Info");
                        showEditInfoUpdatePopup("stateInfo");

                    } else if (which == 4) {
                        pd.setMessage("Updating Phone Num");
                        showEditInfoUpdatePopup("phoneNum");

                    }
                }
            });
            builder.create().show();
        }

        private void showEditInfoUpdatePopup ( final String key){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Update" + key);


            final EditText editText = new EditText(this);

            editText.setHint("Enter " + key);

            LinearLayout linearLayout = new LinearLayout(this);
            linearLayout.setOrientation(LinearLayout.VERTICAL);
            linearLayout.setPadding(10, 10, 10, 10);

            linearLayout.addView(editText);

            builder.setView(linearLayout);

            builder.setPositiveButton("update", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    String value = editText.getText().toString().trim();
                    if (!TextUtils.isEmpty(value)) {
                        pd.show();
                        HashMap<String, Object> result = new HashMap<>();
                        result.put(key, value);
                        databaseReference.child(user.getUid()).updateChildren(result).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {

                                pd.dismiss();
                                Toast.makeText(ProfileActivity.this, "Updated", Toast.LENGTH_SHORT).show();

                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                pd.dismiss();
                                Toast.makeText(ProfileActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();

                            }
                        });

                    } else {
                        Toast.makeText(ProfileActivity.this, "Please enter ", Toast.LENGTH_SHORT).show();
                    }


                }
            });

            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });
            builder.create().show();


        }

        private void showImagePicDialog () {

            String options[] = {"Camera", "Gallery"};
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Pick Image From");
            builder.setItems(options, new DialogInterface.OnClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.M)
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (which == 0) {

                        if (!checkCameraPermission()) {
                            requestedCameraPermission();
                        } else {
                            pickFromCamera();
                        }

                    } else if (which == 1) {
                        if (!checkStoragePermission()) {
                            requestedStoragePermission();
                        } else {
                            pickFromGallery();
                        }


                    }
                }
            });
            builder.create().show();
        }

        @Override
        public void onRequestPermissionsResult ( int requestCode, @NonNull String[] permissions,
        @NonNull int[] grantResults){

            switch (requestCode) {
                case CAMERA_REQUEST_CODE: {
                    if (grantResults.length > 0) {
                        boolean cameraAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                        if (cameraAccepted) {
                            pickFromCamera();
                        } else {
                            Toast.makeText(ProfileActivity.this, "Please Enable the camera  permission", Toast.LENGTH_SHORT).show();
                        }

                    }

                }
                break;
                case STORAGE_REQUEST_CODE: {
                    if (grantResults.length > 0) {
                        boolean writeStorageAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                        if (writeStorageAccepted) {
                            pickFromGallery();
                        } else {
                            Toast.makeText(ProfileActivity.this, "Please Enable the Storage permission", Toast.LENGTH_SHORT).show();
                        }

                    }
                }
                break;

            }


        }


        private void pickFromCamera () {

            ContentValues values = new ContentValues();
            values.put(MediaStore.Images.Media.TITLE, "temp pic");
            values.put(MediaStore.Images.Media.DESCRIPTION, "temp description");

            image_uri = this.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, image_uri);
            startActivityForResult(cameraIntent, IMAGE_PICK_CAMERA_CODE);

        }

        private void pickFromGallery () {

            Intent galleryIntent = new Intent(Intent.ACTION_PICK);
            galleryIntent.setType("image/*");
            startActivityForResult(galleryIntent, IMAGE_PICK_GALLERY_CODE);

        }


        @Override
        public void onActivityResult ( int requestCode, int resultCode, @Nullable Intent data){

            if (resultCode == RESULT_OK) {

                if (requestCode == IMAGE_PICK_GALLERY_CODE) {
                    image_uri = data.getData();

                    uploadProfilePhoto(image_uri);

                }
                if (requestCode == IMAGE_PICK_CAMERA_CODE) {

                    uploadProfilePhoto(image_uri);


                }

            }


            super.onActivityResult(requestCode, resultCode, data);


        }

        private void uploadProfilePhoto (Uri uri) {

            pd.show();

            String filePathAndName = storagePath + "" + profilePhoto + "_" + user.getUid();

            StorageReference storageReference2 = storageReference.child(filePathAndName);
            storageReference2.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                    while (!uriTask.isSuccessful()) ;
                    Uri downloadUri = uriTask.getResult();

                    if (uriTask.isSuccessful()) {
                        HashMap<String, Object> results = new HashMap<>();
                        results.put(profilePhoto, downloadUri.toString());

                        databaseReference.child(user.getUid()).updateChildren(results).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                pd.dismiss();
                                Toast.makeText(ProfileActivity.this, "Image Updated", Toast.LENGTH_SHORT).show();

                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                pd.dismiss();
                                Toast.makeText(ProfileActivity.this, "Error Updating image", Toast.LENGTH_SHORT).show();

                            }
                        });
                    } else {
                        pd.dismiss();
                        Toast.makeText(ProfileActivity.this, "Some error", Toast.LENGTH_SHORT).show();
                    }

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    pd.dismiss();
                    Toast.makeText(ProfileActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }

            });

        }

                private BottomNavigationView.OnNavigationItemSelectedListener navListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                        switch (menuItem.getItemId()) {
                            case R.id.nav_home:
                                Intent intent = new Intent(ProfileActivity.this, MainActivity.class);
                                startActivity(intent);
                                break;
                            case R.id.nav_search:
                                Intent intent2 = new Intent(ProfileActivity.this, MainActivity.class);
                                startActivity(intent2);
                                break;
                            case R.id.nav_travel_history:
                                Intent intent3 = new Intent(ProfileActivity.this, TravelHistoryActivity.class);
                                startActivity(intent3);
                                break;
                            case R.id.nav_wishlist:
                                Intent intent4 = new Intent(ProfileActivity.this, MainActivity.class);
                                startActivity(intent4);
                                break;
                            case R.id.nav_profile:
                                Intent intent5 = new Intent(ProfileActivity.this, ProfileActivity.class);
                                startActivity(intent5);
                                break;

                        }
                        return false;
                    }
                };


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void configureRecyclerView() {
        Log.d(TAG, "configure recycler view");
        mAdapter = new MyAdapter(this, mTrips, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = (int) v.getTag();
                Trip trip = mAdapter.getItem(position);

                Bundle args = new Bundle();
                args.putString(ViewTripFragment.ARGUMENT_TRIPID, trip.getTrip_id());
                ViewTripFragment fragment = new ViewTripFragment();
                fragment.setArguments(args);

                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container, fragment, "Trip_Info");
                fragmentTransaction.addToBackStack("Trip_Info");
                fragmentTransaction.commit();
            }
        });
        mRecyclerView.setAdapter(mAdapter);
        getTripIdsAndTrips();
    }

    private void getTripIdsAndTrips(){
        mTripIds.clear();
        mTrips.clear();

        Query query = FirebaseDatabase.getInstance().getReference().child(Constants.DATABASE_PATH_TRAVELHISTORY)
                .orderByKey()
                .equalTo(mLooggedInUserId);
        
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot != null) {
                    if (dataSnapshot.hasChildren()) {
                        DataSnapshot singleSnapshot = dataSnapshot.getChildren().iterator().next();
                        for (DataSnapshot snapshot: singleSnapshot.getChildren()) {
                            String id = snapshot.child(Constants.DATABASE_FIELD_TRIPID).getValue().toString();
                            Log.d(TAG, "onDataChange: found a post id: " + id);
                            mTripIds.add(id);
                        }
                    }
                    getTripsInTripIDList();
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void getTripsInTripIDList(){
        if (mTripIds.size() > 0) {
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
            for (int i  = 0; i < mTripIds.size(); i++) {
                Log.d(TAG, "getPosts: getting post information for: " + mTripIds.get(i));
                Query query = reference.child(Constants.DATABASE_PATH_TRIPS)
                        .orderByKey()
                        .equalTo(mTripIds.get(i));

                query.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.hasChildren()) {
                            DataSnapshot singleSnapshot = dataSnapshot.getChildren().iterator().next();
                            Trip trip = singleSnapshot.getValue(Trip.class);
                            Log.d(TAG, "onDataChange: found a post: " + trip.getTitle());
                            mTrips.add(trip);
                            mAdapter.notifyDataSetChanged();
                        } else {
                            // If we implement delete trip in the future, update here.
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });
            }
        } else {
            mAdapter.notifyDataSetChanged(); //still need to notify the adapter if the list is empty
        }
    }
}
