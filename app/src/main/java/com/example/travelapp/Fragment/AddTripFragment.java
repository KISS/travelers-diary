package com.example.travelapp.Fragment;

import android.app.DatePickerDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.icu.util.Calendar;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.travelapp.R;
import com.example.travelapp.configs.Constants;
import com.example.travelapp.models.Trip;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Locale;

import static android.text.TextUtils.isEmpty;

public class AddTripFragment extends Fragment implements SelectPhotoDialog.OnPhotoSelectedListener {

    public interface AddTripFragmentHandler {
    }

    AddTripFragmentHandler mHandler;

    private static final String TAG = "AddTripFragment";
    private Bitmap mSelectedBitmap;
    private Uri mSelectedUri;
    private byte[] mUploadBytes;

    TextView tvLName, tvObjects;

    private Toolbar mToolbar;
    private TextView mAddTripTitle, mEditTripTitle;
    private ImageView mImage;
    private Switch mSwitch;
    private Spinner mSpinner;
    private EditText mTitle, mCity, mDays, mDescription;
    private TextView mDate;
    private Button mPost, mSave, mRemove;
    private double mProgress = 0;

    private DatabaseReference mDatabaseReference;
    private String mImageUrl;
    private String mTripId;
    private int mStateSelected;
    private int mState; // Used to store original state when updating State

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mHandler = (AddTripFragmentHandler) context;
        } catch (ClassCastException e) {
            throw new ClassCastException("The activity that this fragment is attached must be a AddTripFragmentHandler!");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_trip, container, false);
        mToolbar = view.findViewById(R.id.toolbar);
        mAddTripTitle = view.findViewById(R.id.add_trip_title);
        mEditTripTitle = view.findViewById(R.id.edit_trip_title);
        mImage = view.findViewById(R.id.post_image);
        mSwitch = view.findViewById(R.id.input_switch);
        mTitle = view.findViewById(R.id.input_title);
        mCity = view.findViewById(R.id.input_city);
        mSpinner = view.findViewById(R.id.input_state);
        mDate = view.findViewById(R.id.input_date);
        mDays = view.findViewById(R.id.input_number_of_days);
        mDescription = view.findViewById(R.id.input_description);
        mPost = view.findViewById(R.id.post_button);
        mSave = view.findViewById(R.id.save_button);
        mRemove = view.findViewById(R.id.remove_button);
        mDatabaseReference = FirebaseDatabase.getInstance().getReference();
        mTripId = getArguments() == null ? "" : getArguments().getString(ViewTripFragment.ARGUMENT_TRIPID);
        mStateSelected = -1;
        mState = -1;

        tvLName = view.findViewById(R.id.tvLName);
        tvObjects = view.findViewById(R.id.tvObjects);

        init();
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    private void init() {
        // For edit in View Trip Fragment
        if (!mTripId.isEmpty()) {
            getTripInfo();
            mAddTripTitle.setVisibility(View.GONE);
            mEditTripTitle.setVisibility(View.VISIBLE);
            mPost.setVisibility(View.GONE);
            mSave.setVisibility(View.VISIBLE);
            mRemove.setVisibility(View.VISIBLE);
        }

        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().onBackPressed();
            }
        });

        mImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: opening dialog to choose new photo");
                SelectPhotoDialog dialog = new SelectPhotoDialog(tvLName, tvObjects);
                dialog.show(getFragmentManager(), getString(R.string.dialog_select_photo));
                dialog.setTargetFragment(AddTripFragment.this, 1);
            }
        });

        mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mStateSelected = position - 1;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        final Calendar myCalendar = Calendar.getInstance();
        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yy", Locale.US);
                mDate.setText(sdf.format(myCalendar.getTime()));
            }
        };

        mDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(AddTripFragment.this.getContext(), date,
                        myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        mPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: attempting to post...");
                if (validateTextInput()) {
                    if (mSelectedBitmap == null && mSelectedUri == null) {
                        Toast.makeText(getActivity(), R.string.please_select_a_photo,
                                Toast.LENGTH_SHORT).show();
                    }
                    //we have a bitmap and no Uri
                    if (mSelectedBitmap != null && mSelectedUri == null) {
                        uploadNewPhoto(mSelectedBitmap);
                    }
                    //we have no bitmap and a uri
                    else if (mSelectedBitmap == null && mSelectedUri != null) {
                        uploadNewPhoto(mSelectedUri);
                    }
                }
            }
        });

        mSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: attempting to save updates...");
                if (validateTextInput()) {
                    // Update trip image.
                    if (mSelectedBitmap != null && mSelectedUri == null) {
                        deleteImage(mImageUrl);
                        uploadNewPhoto(mSelectedBitmap);
                    } else if (mSelectedBitmap == null && mSelectedUri != null) {
                        deleteImage(mImageUrl);
                        uploadNewPhoto(mSelectedUri);
                    }

                    updateTrip();
                }
            }
        });

        mRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteTrip(mImageUrl, mTripId, mState);
                getActivity().getSupportFragmentManager().popBackStack();
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });
    }

    private void getTripInfo() {
        Query query = mDatabaseReference.child("Trips").orderByKey().equalTo(mTripId);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                DataSnapshot singleSnapshot = dataSnapshot.getChildren().iterator().next();
                if (singleSnapshot != null) {
                    Trip trip = singleSnapshot.getValue(Trip.class);
                    mImageUrl = trip.getImage();
                    Picasso.get().load(mImageUrl).into(mImage);
                    mSwitch.setChecked(trip.isIs_public());
                    mTitle.setText(trip.getTitle());
                    mCity.setText(trip.getCity());
                    mSpinner.setSelection(trip.getState() + 1);
                    mState = trip.getState();
                    mDate.setText(trip.getDate());
                    mDays.setText(String.valueOf(trip.getDays()));
                    mDescription.setText(trip.getDescription());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void uploadNewPhoto(Bitmap bitmap) {
        Log.d(TAG, "uploadNewPhoto: uploading a new image bitmap to storage");
        AddTripFragment.BackgroundImageResize resize = new AddTripFragment.BackgroundImageResize(bitmap);
        resize.execute((Uri) null);
    }

    private void uploadNewPhoto(Uri imagePath) {
        Log.d(TAG, "uploadNewPhoto: uploading a new image uri to storage.");
        AddTripFragment.BackgroundImageResize resize = new AddTripFragment.BackgroundImageResize(null);
        resize.execute(imagePath);
    }

    class BackgroundImageResize extends AsyncTask<Uri, Integer, byte[]> {

        Bitmap mBitmap;

        public BackgroundImageResize(Bitmap bitmap) {
            if (bitmap != null) {
                this.mBitmap = bitmap;
            }
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
//            Toast.makeText(getActivity(), R.string.toast_compressing_photo, Toast.LENGTH_SHORT).show();
        }

        @Override
        protected byte[] doInBackground(Uri... params) {
            Log.d(TAG, "doInBackground: started.");
            if (mBitmap == null) {
                try {
                    RotateBitmap rotateBitmap = new RotateBitmap();
                    mBitmap = rotateBitmap.HandleSamplingAndRotationBitmap(getActivity(), params[0]);
                } catch (IOException e) {
                    Log.e(TAG, "doInBackground: IOException: " + e.getMessage());
                }
            }
            Log.d(TAG, "doInBackground: megabytes before compression: " + mBitmap.getByteCount() / 1000000);
            byte[] bytes = getBytesFromBitmap(mBitmap, 100);
            Log.d(TAG, "doInBackground: megabytes before compression: " + bytes.length / 1000000);
            return bytes;
        }

        @Override
        protected void onPostExecute(byte[] bytes) {
            super.onPostExecute(bytes);
            mUploadBytes = bytes;
            uploadImage();
        }
    }

    private byte[] getBytesFromBitmap(Bitmap bitmap, int quality) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, quality, stream);
        return stream.toByteArray();
    }

    private void uploadImage() {
//        Toast.makeText(getActivity(), R.string.toast_uploading_photo, Toast.LENGTH_SHORT).show();
        mTripId = mDatabaseReference.push().getKey();
        final StorageReference storageReference = FirebaseStorage.getInstance().getReference()
                .child("Trip_Images/" + FirebaseAuth.getInstance().getCurrentUser().getUid() +
                        "/" + mTripId + "/trip_photo");

        UploadTask uploadTask = storageReference.putBytes(mUploadBytes);
        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                Toast.makeText(getActivity(), R.string.toast_photo_uploaded_successfully, Toast.LENGTH_SHORT).show();

                Task<Uri> urlTask = taskSnapshot.getStorage().getDownloadUrl();
                while (!urlTask.isSuccessful()) ;
                mImageUrl = urlTask.getResult().toString();

                uploadNewTrip();
                resetFields();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getActivity(), R.string.toast_could_not_upload_photo, Toast.LENGTH_SHORT).show();
            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                double currentProgress = (100 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                if (currentProgress > (mProgress + 15)) {
                    mProgress = currentProgress;
                    Log.d(TAG, "onProgress: upload is " + mProgress + "& done");
                    Toast.makeText(getActivity(), mProgress + "%", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void uploadNewTrip() {
        String user_id = FirebaseAuth.getInstance().getCurrentUser().getUid();
        Trip trip = new Trip(mTripId, user_id, mImageUrl, mSwitch.isChecked(), mTitle.getText().toString(),
                mCity.getText().toString(), mStateSelected, mDate.getText().toString(),
                Integer.parseInt(mDays.getText().toString().trim()), mDescription.getText().toString());

        // Add record in Trips
        mDatabaseReference.child(Constants.DATABASE_PATH_TRIPS)
                .child(mTripId)
                .setValue(trip);

        // Add record in TravelHistory
        mDatabaseReference.child(Constants.DATABASE_PATH_TRAVELHISTORY)
                .child(user_id)
                .child(mTripId)
                .child("tripId")
                .setValue(mTripId);

        // Add record in States.
        mDatabaseReference.child(Constants.DATABASE_PATH_TRIPS_OF_STATES)
                .child(user_id)
                .child(String.valueOf(mStateSelected))
                .child(mTripId)
                .child("tripId")
                .setValue(mTripId);

        // Add record in StatesAll.
        mDatabaseReference.child(Constants.DATABASE_PATH_ALL_TRIPS_OF_STATES)
                .child(String.valueOf(mStateSelected))
                .child(mTripId)
                .child("tripId")
                .setValue(mTripId);

        // Update statesInfo in Users
        addVisitedState(user_id);
        Toast.makeText(getActivity(), R.string.toast_trip_posted, Toast.LENGTH_LONG).show();
    }

    private void addVisitedState(String user_id) {
        mDatabaseReference.child(Constants.DATABASE_PATH_USERS).child(user_id)
                .child(Constants.DATABASE_FIELD_STATESINFO).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Long singleSnapshot = dataSnapshot.getValue(Long.class);
                    if (singleSnapshot != null) {
                        Log.d(TAG, "Visited States: " + singleSnapshot);
                        if ((singleSnapshot & (1L << mStateSelected)) == 0) {
                            mDatabaseReference.child("Users")
                                    .child(user_id)
                                    .child("statesInfo")
                                    .setValue(singleSnapshot | (1L << mStateSelected));

                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void resetFields() {
        mImage.setImageResource(R.drawable.ic_add_a_photo_blue_24dp);
        mTitle.setText("");
        mCity.setText("");
        mSpinner.setSelection(0);
        mDate.setText("");
        mDays.setText("");
        mSwitch.setChecked(true);
        mDescription.setText("");
        tvLName.setText("");
        tvObjects.setText("");
    }

    @Override
    public void getImagePath(Uri imagePath) {
        Log.d(TAG, "getImagePath: setting the image to imageview");
        mImage.setImageURI(imagePath);
        mSelectedBitmap = null;
        mSelectedUri = imagePath;
    }

    @Override
    public void getImageBitmap(Bitmap bitmap) {
        Log.d(TAG, "getImageBitmap: setting the image to imageview");
        mImage.setImageBitmap(bitmap);
        mSelectedBitmap = bitmap;
        mSelectedUri = null;
    }

    @Override
    public void triggerImageUpload() {

    }

    private boolean validateTextInput() {
        if (mStateSelected < 0) {
            Toast.makeText(getActivity(), R.string.please_choose_a_state,
                    Toast.LENGTH_SHORT).show();
            return false;
        } else if (isEmpty(mCity.getText().toString().trim())) {
            Toast.makeText(getActivity(), R.string.please_specify_a_city,
                    Toast.LENGTH_SHORT).show();
            return false;
        } else if (isEmpty(mDays.getText().toString().trim())) {
            Toast.makeText(getActivity(), R.string.please_specify_a_number_of_days,
                    Toast.LENGTH_SHORT).show();
            return false;
        } else if (isEmpty(mDate.getText().toString().trim())) {
            Toast.makeText(getActivity(), R.string.please_specify_the_start_date,
                    Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void updateTrip() {
        // Update public or private
        mDatabaseReference.child(Constants.DATABASE_PATH_TRIPS)
                .child(mTripId)
                .child("is_public")
                .setValue(mSwitch.isChecked());

        // Update trip title.
        mDatabaseReference.child(Constants.DATABASE_PATH_TRIPS)
                .child(mTripId)
                .child("title")
                .setValue(mTitle.getText().toString().trim());

        // Update trip city.
        mDatabaseReference.child(Constants.DATABASE_PATH_TRIPS)
                .child(mTripId)
                .child("city")
                .setValue(mCity.getText().toString().trim());

        if (mStateSelected != mState) {
            String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
            // Update trip state.
            mDatabaseReference.child(Constants.DATABASE_PATH_TRIPS)
                    .child(mTripId)
                    .child("state")
                    .setValue(mStateSelected);

            // Update trips of States.
            mDatabaseReference.child(Constants.DATABASE_PATH_TRIPS_OF_STATES)
                    .child(userId)
                    .child(String.valueOf(mState))
                    .child(mTripId)
                    .removeValue();
            mDatabaseReference.child(Constants.DATABASE_PATH_TRIPS_OF_STATES)
                    .child(userId)
                    .child(String.valueOf(mStateSelected))
                    .child(mTripId)
                    .child("tripId")
                    .setValue(mTripId);

            // Update trips of StatesAll.
            mDatabaseReference.child(Constants.DATABASE_PATH_ALL_TRIPS_OF_STATES)
                    .child(String.valueOf(mState))
                    .child(mTripId)
                    .removeValue();
            mDatabaseReference.child(Constants.DATABASE_PATH_ALL_TRIPS_OF_STATES)
                    .child(String.valueOf(mStateSelected))
                    .child(mTripId)
                    .child("tripId")
                    .setValue(mTripId);

            // Update statesInfo in Users
            updateStatesInfo(userId);
        }

        // Update trip date.
        mDatabaseReference.child(Constants.DATABASE_PATH_TRIPS)
                .child(mTripId)
                .child("date")
                .setValue(mDate.getText().toString().trim());

        // Update trip days.
        mDatabaseReference.child(Constants.DATABASE_PATH_TRIPS)
                .child(mTripId)
                .child("days")
                .setValue(Integer.parseInt(mDays.getText().toString().trim()));

        // Update trip description.
        mDatabaseReference.child(Constants.DATABASE_PATH_TRIPS)
                .child(mTripId)
                .child("description")
                .setValue(mDescription.getText().toString().trim());

        Toast.makeText(getActivity(), R.string.toast_trip_updated,
                Toast.LENGTH_SHORT).show();
        getActivity().getSupportFragmentManager().popBackStack();
    }

    private static void deleteImage(String imageUrl) {
        StorageReference photoRef = FirebaseStorage.getInstance().getReferenceFromUrl(imageUrl);
        photoRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                // File deleted successfully
                Log.d(TAG, "onSuccess: deleted file");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // An error occurred!
                Log.d(TAG, "onFailure: did not delete file");
            }
        });
    }

    public static void deleteTrip(String imageUrl, String tripId, int state) {
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        deleteImage(imageUrl);
        // Delete record from Travel History.
        FirebaseDatabase.getInstance().getReference().child(Constants.DATABASE_PATH_TRAVELHISTORY)
                .child(userId)
                .child(tripId)
                .removeValue();
        // Delete record from Trips.
        FirebaseDatabase.getInstance().getReference().child(Constants.DATABASE_PATH_TRIPS)
                .child(tripId)
                .removeValue();
        // Delete record from States.
        FirebaseDatabase.getInstance().getReference().child(Constants.DATABASE_PATH_TRIPS_OF_STATES)
                .child(userId)
                .child(String.valueOf(state))
                .child(tripId)
                .removeValue();
        // Delete record from StatesAll.
        FirebaseDatabase.getInstance().getReference().child(Constants.DATABASE_PATH_ALL_TRIPS_OF_STATES)
                .child(String.valueOf(state))
                .child(tripId)
                .removeValue();
        // Update statesInfo in Users.
        checkAndUpdateStatesInfo(state, userId);
    }

    private static void checkAndUpdateStatesInfo(int state, String userId) {
        FirebaseDatabase.getInstance().getReference().child(Constants.DATABASE_PATH_TRIPS_OF_STATES)
                .child(userId).child(String.valueOf(state)).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (!dataSnapshot.exists()) {
                    removeVisitedState(state, userId);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private static void removeVisitedState(int state, String userId) {
        FirebaseDatabase.getInstance().getReference().child(Constants.DATABASE_PATH_USERS).child(userId)
                .child(Constants.DATABASE_FIELD_STATESINFO).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Long singleSnapshot = dataSnapshot.getValue(Long.class);
                    if (singleSnapshot != null) {
                        Log.d(TAG, "Visited States: " + singleSnapshot);
                        FirebaseDatabase.getInstance().getReference().child(Constants.DATABASE_PATH_USERS)
                                .child(userId)
                                .child(Constants.DATABASE_FIELD_STATESINFO)
                                .setValue(singleSnapshot & ~(1L << state));
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void updateStatesInfo(String userId) {
        mDatabaseReference.child(Constants.DATABASE_PATH_TRIPS_OF_STATES)
                .child(userId).child(String.valueOf(mState)).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (!dataSnapshot.exists()) {
                    removeAndAddVisitedState(userId);
                } else {
                    addVisitedState(userId);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void removeAndAddVisitedState(String userId) {
        FirebaseDatabase.getInstance().getReference().child(Constants.DATABASE_PATH_USERS).child(userId)
                .child(Constants.DATABASE_FIELD_STATESINFO).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Long singleSnapshot = dataSnapshot.getValue(Long.class);
                    if (singleSnapshot != null) {
                        Log.d(TAG, "Visited States: " + singleSnapshot);
                        FirebaseDatabase.getInstance().getReference().child(Constants.DATABASE_PATH_USERS)
                                .child(userId)
                                .child(Constants.DATABASE_FIELD_STATESINFO)
                                .setValue(singleSnapshot & ~(1L << mState) | (1L << mStateSelected));
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
