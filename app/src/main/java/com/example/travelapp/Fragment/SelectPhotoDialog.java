package com.example.travelapp.Fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.travelapp.R;

public class SelectPhotoDialog extends DialogFragment {

    public interface OnPhotoSelectedListener{
        void getImagePath(Uri imagePath);
        void getImageBitmap(Bitmap bitmap);
        void triggerImageUpload();
    }

    OnPhotoSelectedListener mOnPhotoSelectedListener;

    private static final String TAG = "SelectPhotoDialog";
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
                Log.d(TAG, "onClick: accessing phones memory.");
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent, PICK_FILE_REQUEST_CODE);
            }
        });
    }

    private void setTakePhotoOnclickListener() {
        takePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: starting camera.");
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, CAMERA_REQUEST_CODE);
            }
        });
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
}