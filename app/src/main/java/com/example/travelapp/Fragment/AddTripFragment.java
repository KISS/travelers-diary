package com.example.travelapp.Fragment;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.travelapp.R;
import com.example.travelapp.activities.TravelHistoryActivity;
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

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import static android.text.TextUtils.isEmpty;

public class AddTripFragment extends Fragment implements SelectPhotoDialog.OnPhotoSelectedListener {

    public interface AddTripFragmentHandler {
    }

    AddTripFragmentHandler mHandler;

    private static final String TAG = "AddTripFragment";
    private Bitmap mSelectedBitmap;
    private Uri mSelectedUri;
    private byte[] mUploadBytes;

    private ImageView mImage;
    private Spinner mSpinner;
    private EditText mCity, mTags, mDescription;
    private Button mPost;
    private double mProgress = 0;

    DatabaseReference mDatabaseReference;
    private String mImageUrl;
    private String mTripId;
    private int mState;

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
        mImage = view.findViewById(R.id.post_image);
        mCity = view.findViewById(R.id.input_city);
        mSpinner = view.findViewById(R.id.input_state);
        // ADD dates later!!!!!
        mTags = view.findViewById(R.id.input_tags);
        mDescription = view.findViewById(R.id.input_description);
        mPost = view.findViewById(R.id.post_button);
        mDatabaseReference = FirebaseDatabase.getInstance().getReference();
        mTripId = "";
        mState = -1;

        init();
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    private void init(){
        mImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: opening dialog to choose new photo");
                SelectPhotoDialog dialog = new SelectPhotoDialog();
                dialog.show(getFragmentManager(), getString(R.string.dialog_select_photo));
                dialog.setTargetFragment(AddTripFragment.this, 1);
            }
        });

        mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mState = position - 1;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        mPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: attempting to post...");
                // Add restrictions on date inputs!!!!!!!!!!!!!!!!!!!!!!!!1
                if (mState < 0) {
                    Toast.makeText(getActivity(), R.string.please_choose_a_state,
                            Toast.LENGTH_SHORT).show();
                } else if (isEmpty(mCity.getText().toString().trim())) {
                    Toast.makeText(getActivity(), R.string.please_specify_a_city,
                            Toast.LENGTH_SHORT).show();
                } else {
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
    }

    private void uploadNewPhoto(Bitmap bitmap){
        Log.d(TAG, "uploadNewPhoto: uploading a new image bitmap to storage");
        AddTripFragment.BackgroundImageResize resize = new AddTripFragment.BackgroundImageResize(bitmap);
        resize.execute((Uri) null);
    }

    private void uploadNewPhoto(Uri imagePath){
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
            Toast.makeText(getActivity(), R.string.toast_compressing_photo, Toast.LENGTH_SHORT).show();
        }

        @Override
        protected byte[] doInBackground(Uri... params) {
            Log.d(TAG, "doInBackground: started.");
            if (mBitmap == null) {
                try {
                    RotateBitmap rotateBitmap = new RotateBitmap();
                    mBitmap = rotateBitmap.HandleSamplingAndRotationBitmap(getActivity(), params[0]);
                } catch (IOException e){
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
            uploadNewTrip();
        }
    }

    private byte[] getBytesFromBitmap(Bitmap bitmap, int quality){
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, quality, stream);
        return stream.toByteArray();
    }

    private void uploadImage(){
        Toast.makeText(getActivity(), R.string.toast_uploading_photo, Toast.LENGTH_SHORT).show();
        mTripId = mDatabaseReference.push().getKey();
        final StorageReference storageReference = FirebaseStorage.getInstance().getReference()
                .child("Trip_Images/" + FirebaseAuth.getInstance().getCurrentUser().getUid() +
                        "/" + mTripId + "/trip_photo");

        UploadTask uploadTask = storageReference.putBytes(mUploadBytes);
        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(getActivity(), R.string.toast_photo_uploaded_successfully, Toast.LENGTH_SHORT).show();

                Task<Uri> urlTask = taskSnapshot.getStorage().getDownloadUrl();
                while (!urlTask.isSuccessful());
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
                if ( currentProgress > (mProgress + 15)) {
                    mProgress = currentProgress;
                    Log.d(TAG, "onProgress: upload is " + mProgress + "& done");
                    Toast.makeText(getActivity(), mProgress + "%", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void uploadNewTrip() {
        String user_id = FirebaseAuth.getInstance().getCurrentUser().getUid();
        Trip trip = new Trip(mTripId, user_id, mImageUrl,
                mCity.getText().toString(), mState, mTags.getText().toString(),
                mDescription.getText().toString());

        // Add record in Trips
        mDatabaseReference.child("Trips")
                .child(mTripId)
                .setValue(trip);

        // Add record in TravelHistory
        mDatabaseReference.child("TravelHistory")
                .child(user_id)
                .child(mTripId)
                .child("tripId")
                .setValue(mTripId);

        // Update stateInfos in Users!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!1
    }

    private void resetFields(){
        mImage.setImageURI(null);
        mImage.setImageBitmap(null);
        mCity.setText("");
        mSpinner.setSelection(0);
        // Reset days as well!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!11
        mTags.setText("");
        mDescription.setText("");
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

}
