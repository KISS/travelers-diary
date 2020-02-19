package com.example.travelapp.Fragment;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import com.example.travelapp.R;
import com.example.travelapp.activities.ProfileActivity;
import com.example.travelapp.configs.Constants;

public class SelectPhotoDialog extends DialogFragment {

    public interface OnPhotoSelectedListener{
        void getImagePath(Uri imagePath);
        void getImageBitmap(Bitmap bitmap);
        void triggerImageUpload();
    }

    OnPhotoSelectedListener mOnPhotoSelectedListener;

    private static final String TAG = "SelectPhotoDialog";
    private static final int PERMISSION_CAMERA_REQUEST_CODE = 100;
    private static final int PERMISSION_STORAGE_REQUEST_CODE = 200;
    private static final int PICK_FILE_REQUEST_CODE = 1234;
    private static final int CAMERA_REQUEST_CODE = 4321;
    TextView selectPhoto;
    TextView takePhoto;

    @Override
    public void onAttach(Context context) {
        try {
            mOnPhotoSelectedListener = (SelectPhotoDialog.OnPhotoSelectedListener) getTargetFragment();
        } catch (ClassCastException e) {
            Log.e(TAG, "onAttach: ClassCastException: " + e.getMessage() );
        }
        super.onAttach(context);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_select_photo, container, false);
        selectPhoto = (TextView) view.findViewById(R.id.dialogChoosePhoto);
        takePhoto = (TextView) view.findViewById(R.id.dialogOpenCamera);

        setSelectPhotoOnclickListener();
        setTakePhotoOnclickListener();
        return view;
    }

    private void setSelectPhotoOnclickListener() {
        selectPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkStoragePermission()) {
                    choosePhotoFromMemory();
                } else {
                    requestedStoragePermission();
                }
            }
        });
    }

    private void setTakePhotoOnclickListener() {
        takePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkCameraPermission()) {
                    takePhotoByCamera();
                } else {
                    requestedCameraPermission();
                }
            }
        });
    }

    private void choosePhotoFromMemory() {
        Log.d(TAG, "onClick: accessing phones memory.");
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, PICK_FILE_REQUEST_CODE);
    }

    private void takePhotoByCamera() {
        Log.d(TAG, "onClick: starting camera.");
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, CAMERA_REQUEST_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Results from selecting a image from memory
        if (requestCode == PICK_FILE_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            Uri selectedImageUri = data.getData();
            Log.d(TAG, "onActivityResult: image uri: " + selectedImageUri);

            // Send the uri to AddTripFragment & dismiss dialog
            mOnPhotoSelectedListener.getImagePath(selectedImageUri);
            getDialog().dismiss();
        }

        // Results from taking a photo with camera
        else if (requestCode == CAMERA_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            Log.d(TAG, "onActivityResult: done taking new photo");
            Bitmap bitmap = (Bitmap) data.getExtras().get("data");

            //send the bitmap to AddTripFragment and dismiss dialog
            mOnPhotoSelectedListener.getImageBitmap(bitmap);
            getDialog().dismiss();
        }
    }

    private boolean checkStoragePermission () {
        return ContextCompat.checkSelfPermission(((Fragment) mOnPhotoSelectedListener).getContext(),
                Constants.STORAGE_PERMISSION[0]) == (PackageManager.PERMISSION_GRANTED);
    }

    private void requestedStoragePermission () {
        requestPermissions(Constants.STORAGE_PERMISSION, PERMISSION_STORAGE_REQUEST_CODE);
    }

    private boolean checkCameraPermission () {
        return ContextCompat.checkSelfPermission(((Fragment) mOnPhotoSelectedListener).getContext(),
                Constants.CAMERA_PERMISSION[0]) == (PackageManager.PERMISSION_GRANTED)
                && ContextCompat.checkSelfPermission(((Fragment) mOnPhotoSelectedListener).getContext(),
                Constants.CAMERA_PERMISSION[1]) == (PackageManager.PERMISSION_GRANTED);
    }

    private void requestedCameraPermission () {
        requestPermissions(Constants.CAMERA_PERMISSION, PERMISSION_CAMERA_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult (int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults){
        switch (requestCode) {
            case PERMISSION_STORAGE_REQUEST_CODE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    choosePhotoFromMemory();
                } else {
                    Toast.makeText(((Fragment) mOnPhotoSelectedListener).getContext(), "Please Enable the storage permission", Toast.LENGTH_SHORT).show();
                }
                break;
            }
            case PERMISSION_CAMERA_REQUEST_CODE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED
                        && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    takePhotoByCamera();
                } else {
                    Toast.makeText(((Fragment) mOnPhotoSelectedListener).getContext(), "Please Enable the camera permission", Toast.LENGTH_SHORT).show();
                }
                break;
            }
        }
    }
}