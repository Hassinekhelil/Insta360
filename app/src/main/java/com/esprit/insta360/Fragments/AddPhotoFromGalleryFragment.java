package com.esprit.insta360.Fragments;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.Toast;

import com.esprit.insta360.Activity.MainActivity;
import com.esprit.insta360.R;
import com.esprit.insta360.Utils.AppConfig;

import net.gotev.uploadservice.MultipartUploadRequest;
import net.gotev.uploadservice.UploadNotificationConfig;

import java.io.IOException;
import java.util.UUID;

import static android.app.Activity.RESULT_OK;

public class AddPhotoFromGalleryFragment extends Fragment {

    private Button buttonChoose;
    private Button buttonUpload, backBtn;
    private ImageView linearMain;
    private EditText editText;
    Toolbar toolbar;
    HorizontalScrollView horizontalScrollView;
    //Image request code
    private int PICK_IMAGE_REQUEST = 1;

    //storage permission code
    private static final int STORAGE_PERMISSION_CODE = 123;

    //Bitmap to get image from gallery
    private Bitmap bitmap;

    //Uri to store the image uri
    private Uri filePath;

    public AddPhotoFromGalleryFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_photo_from_gallery, container, false);
        toolbar = (Toolbar) view.findViewById(R.id.secondary_toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        //Requesting storage permission
        //requestStoragePermission();
        //Initializing views


        buttonChoose = (Button) view.findViewById(R.id.buttonChoose);
        buttonUpload = (Button) view.findViewById(R.id.nextBtn);
        editText = (EditText) view.findViewById(R.id.editTextName);
        linearMain = (ImageView) view.findViewById(R.id.linearMain);
        backBtn = (Button) view.findViewById(R.id.returnBtn);
        buttonChoose.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                showFileChooser();
            }
        });
        buttonUpload.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (filePath != null) {
                    addPost();
                    Intent intent = new Intent(getContext(), MainActivity.class);
                    startActivity(intent);
                    //getActivity().finish();
                } else {
                    Toast.makeText(getContext(), "Your friends need at least one picture ",
                            Toast.LENGTH_LONG).show();
                }
            }
        });
        backBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), MainActivity.class);
                startActivity(intent);
                //getActivity().finish();

            }
        });
        //_______________________________________FILTER ACTION_____________________________________________________________


        return view;
    }


    private void addPost() {
        //description
        String name = editText.getText().toString().trim();

        //getting the actual path of the image
        String path = getPath(filePath);
        //Uploading code
        try {

            String uploadId = UUID.randomUUID().toString();

            //Creating a multi part request
            new MultipartUploadRequest(getContext(), uploadId, AppConfig.URL_ADD_POST)
                    .addFileToUpload(path, "image") //Adding file
                    .addParameter("content", editText.getText().toString()) //Adding text parameter to the request
                    .addParameter("id_user", "7")//TODO HAJRI session user_id
                    .setNotificationConfig(new UploadNotificationConfig())
                    .startUpload(); //Starting the upload

        } catch (Exception exc) {
            //Toast.makeText(this, exc.getMessage(), Toast.LENGTH_SHORT).show();
        }

    }


    //method to get the file path from uri
    public String getPath(Uri uri) {
        Cursor cursor = getContext().getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        String document_id = cursor.getString(0);
        document_id = document_id.substring(document_id.lastIndexOf(":") + 1);
        cursor.close();

        cursor = getContext().getContentResolver().query(
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                null, MediaStore.Images.Media._ID + " = ? ", new String[]{document_id}, null);
        cursor.moveToFirst();
        String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
        cursor.close();

        return path;
    }

    //method to show file chooser
    private void showFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filePath = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), filePath);
                linearMain.setImageBitmap(bitmap);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }


    //Requesting permission
   /* private void requestStoragePermission() {
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
            return;

        if (ActivityCompat.shouldShowRequestPermissionRationale(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE)) {
            //If the user has denied the permission previously your code will come to this block
            //Here you can explain why you need this permission
            //Explain here why you need this permission
        }
        //And finally ask for the permission
        ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
    }*/


    //This method will be called when the user will tap on allow or deny
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        //Checking the request code of our request
        if (requestCode == STORAGE_PERMISSION_CODE) {

            //If permission is granted
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //Displaying a toast
                Toast.makeText(getContext(), "Permission granted now you can read the storage", Toast.LENGTH_LONG).show();
            } else {
                //Displaying another toast if permission is not granted
                Toast.makeText(getContext(), "Oops you just denied the permission", Toast.LENGTH_LONG).show();
            }
        }
    }


}
