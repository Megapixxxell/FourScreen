package com.example.fourscreen.fragments.scaling_v2;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.fourscreen.R;
import com.github.chrisbanes.photoview.PhotoView;
import com.google.android.material.snackbar.Snackbar;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import static android.app.Activity.RESULT_OK;

public class Scalingv2Fragment extends Fragment {

    private static final int PERMISSION_REQUEST_CODE = 101;
    private static final int RESULT_LOAD_IMAGE = 110;
    private static final int RESULT_TAKE_PICTURE = 111;

    private PhotoView mPhotoView;
    private View rootView;
    private float mScale = 1.0f;
    private Uri mUri;
    private String imageFilePath;

    private String[] permissions = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA};

    public Scalingv2Fragment() {
    }

    public static Scalingv2Fragment newInstance() {

        Bundle args = new Bundle();
        Scalingv2Fragment fragment = new Scalingv2Fragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fr_scaling, container, false);
        mPhotoView = view.findViewById(R.id.photoview_image);
        ImageButton btnCamera = view.findViewById(R.id.camera);
        ImageButton btnGallery = view.findViewById(R.id.gallery);
        ImageButton btnZoomIn = view.findViewById(R.id.zoom_in);
        ImageButton btnZoomOut = view.findViewById(R.id.zoom_out);
        rootView = view.findViewById(R.id.frame);
        askRequestPermissions();
        setRetainInstance(true);

        mPhotoView.setMaximumScale(6);
        mPhotoView.setMinimumScale(1);

        btnCamera.setOnClickListener(onCameraButtonClickListener);
        btnGallery.setOnClickListener(onGalleryButtonClickListener);

        btnZoomIn.setOnClickListener(v -> {
            if (mScale < 6) mPhotoView.setScale(mScale += 1, true);
        });

        btnZoomOut.setOnClickListener(v -> {
            if (mScale > 1) mPhotoView.setScale(mScale -= 1, true);
        });

        if (savedInstanceState != null) {
            mUri = savedInstanceState.getParcelable("Uri");
            Glide.with(getActivity()).load(mUri).into(mPhotoView);
        }
        return view;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.clear();
        super.onSaveInstanceState(outState);

        if (mUri != null) {
            outState.putParcelable("Uri", mUri);
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
                    mUri = data.getData();
                    Glide.with(getActivity()).load(mUri).into(mPhotoView);
                }
            }
            break;
            case RESULT_TAKE_PICTURE:
                if (resultCode == Activity.RESULT_OK) {
                    Glide.with(getActivity()).load(imageFilePath).into(mPhotoView);
                } else if (resultCode == Activity.RESULT_CANCELED) {
                    Toast.makeText(getActivity(), "oops...", Toast.LENGTH_SHORT).show();
                }
            default:
                super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private File createImageFile() throws IOException {
        String timeStamp =
                new SimpleDateFormat("yyyyMMdd_HHmmss",
                        Locale.getDefault()).format(new Date());
        String imageFileName = "IMG_" + timeStamp + "_";
        File storageDir =
                getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );


        imageFilePath = image.getAbsolutePath();
        return image;
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

    private View.OnClickListener onCameraButtonClickListener = v -> {
        if (hasPermission()) {
            Intent takePhotoIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (takePhotoIntent.resolveActivity(getActivity().getPackageManager()) != null) {
                //Create a file to store the image
                File photoFile = null;
                try {
                    photoFile = createImageFile();
                } catch (IOException ex) {
                    // Error occurred while creating the File
                }
                if (photoFile != null) {
                    mUri = FileProvider.getUriForFile(getActivity(),
                            "com.example.fourscreen.fileprovider", photoFile);
                    takePhotoIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                            mUri);
                    startActivityForResult(takePhotoIntent,
                            RESULT_TAKE_PICTURE);
                }
            }
        } else requestPermissionWithRationale();
    };

    private View.OnClickListener onGalleryButtonClickListener = v -> {
        if (hasPermission()) {
            Intent pickImageIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(pickImageIntent, RESULT_LOAD_IMAGE);
        } else requestPermissionWithRationale();
    };
}