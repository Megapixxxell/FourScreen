package com.example.fourscreen.fragments.scaling;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.fourscreen.R;

import java.io.IOException;

import static android.app.Activity.RESULT_OK;

public class ScalingFragment extends Fragment {

    public static final int PERMISSION_REQUEST_CODE = 101;
    public static final int RESULT_LOAD_IMAGE = 110;
    public static final int RESULT_TAKE_PICTURE = 111;

    ImageButton mBtnCamera, mBtnGallery, mBtnZoomIn, mBtnZoomOut;
    View rootView;
    Bitmap mBitmap;
    protected PinchZoomPan mPinchZoomPan;

    String[] permissions = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA};

    public ScalingFragment() {
    }

    public static ScalingFragment newInstance() {
        return new ScalingFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_scaling, container, false);
        mPinchZoomPan = view.findViewById(R.id.iv_scale_image);
        mBtnCamera = view.findViewById(R.id.btn_camera);
        mBtnGallery = view.findViewById(R.id.btn_gallery);
        mBtnZoomIn = view.findViewById(R.id.btn_zoom_in);
        mBtnZoomOut = view.findViewById(R.id.btn_zoom_out);
        rootView = view.findViewById(R.id.frame_layout);
        askRequestPermissions();


        mBtnZoomIn.setOnClickListener((v -> mPinchZoomPan.buttonZoomIn()));
        mBtnZoomOut.setOnClickListener((v -> mPinchZoomPan.buttonZoomOut()));
        mBtnCamera.setOnClickListener(onCameraButtonClickListener);
        mBtnGallery.setOnClickListener(onGalleryButtonClickListener);

        if (savedInstanceState != null) {
            mBitmap = savedInstanceState.getParcelable("Bitmap");
            if (mBitmap != null) {
                mPinchZoomPan.loadImageOnCanvas(mBitmap);
            }
        }
        return view;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.clear();
        super.onSaveInstanceState(outState);

        if (mBitmap != null) {
            outState.putParcelable("Bitmap", mBitmap);
        }
    }

    private boolean hasPermission() {
        int res;
        for (String perms : permissions) {
            res = getActivity().checkCallingOrSelfPermission(perms);
            if (!(res == PackageManager.PERMISSION_GRANTED)) {
                return false;
            }
        }
        return true;
    }

    private void askRequestPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(permissions, PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        boolean allowed = true;
        String[] curPerm = new String[1];

        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                for (int i = 0; i < permissions.length; i++) {
                    allowed = allowed && (grantResults[i] == PackageManager.PERMISSION_GRANTED);
                    if (!allowed) {
                        curPerm[0] = permissions[i];
                    }
                }
                break;
            default:
                allowed = false;
                break;
        }
        if (!allowed && Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (shouldShowRequestPermissionRationale(curPerm[0])) {
                Toast.makeText(getActivity(), R.string.need_perm_toast, Toast.LENGTH_SHORT).show();
            } else {
                showNoPermissionSnackBar();
            }
        }

    }

    private void showNoPermissionSnackBar() {
        Snackbar.make(rootView, "Permissions is not granted", Snackbar.LENGTH_SHORT)
                .setAction("SETTINGS", v -> {
                    Intent appSettingsIntent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                            Uri.parse("package:" + getActivity().getPackageName()));
                    startActivityForResult(appSettingsIntent, PERMISSION_REQUEST_CODE);

                    Toast.makeText(getActivity(), "Open Permission and grant Storage permissions",
                            Toast.LENGTH_SHORT).show();
                })
                .show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                Toast.makeText(getActivity(), "I hope you grant permission in settings", Toast.LENGTH_SHORT).show();
                break;
            case RESULT_LOAD_IMAGE: {
                if (resultCode == RESULT_OK && data != null) {
                    Uri selectedImage = data.getData();
                    Bitmap bitmap = null;
                    try {
                        bitmap = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), selectedImage);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    mBitmap = bitmap;
                    mPinchZoomPan.loadImageOnCanvas(bitmap);
                }
            }
            break;
            case RESULT_TAKE_PICTURE:
                if (resultCode == RESULT_OK && data != null) {
                    Bitmap bitmap;
                    bitmap = (Bitmap) data.getExtras().get("data");
                    mBitmap = bitmap;
                    mPinchZoomPan.loadImageOnCanvas(bitmap);
                }
            default:
                super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void requestPermissionWithRationale() {

        if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) ||
                ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) ||
                ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.CAMERA)) {
            Snackbar.make(rootView, "Permissions need for application", Snackbar.LENGTH_LONG)
                    .setAction("GRANT", v -> askRequestPermissions())
                    .show();
        } else {
            askRequestPermissions();
        }
    }

    View.OnClickListener onCameraButtonClickListener = v -> {
        if (hasPermission()) {
            Intent takePhotoIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(takePhotoIntent, RESULT_TAKE_PICTURE);
        } else requestPermissionWithRationale();
    };

    View.OnClickListener onGalleryButtonClickListener = v -> {
        if (hasPermission()) {
            Intent pickImageIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(pickImageIntent, RESULT_LOAD_IMAGE);
        } else requestPermissionWithRationale();
    };
}