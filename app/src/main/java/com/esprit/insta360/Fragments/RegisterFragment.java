package com.esprit.insta360.Fragments;

import android.Manifest;
import android.app.Activity;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.esprit.insta360.Activity.MainActivity;
import com.esprit.insta360.DAO.UserDao;
import com.esprit.insta360.R;
import com.esprit.insta360.Utils.SessionManager;

import java.util.Random;

/**
 * Created by TIBH on 14/11/2016.
 */

public class RegisterFragment extends Fragment {
    private Button btnRegister;
    private Button btnLinkToLogin;
    private Button btnLoadPhoto;
    private EditText inputFullName;
    private EditText inputEmail;
    private EditText inputPassword;
    private EditText inputLogin;
    private EditText inputConfirmPassword;
    private ProgressDialog pDialog;
    private SessionManager session;
    private UserDao userDao;
    private int PICK_IMAGE_REQUEST=1;
    private Uri filePath ;
    private static final int STORAGE_PERMISSION_CODE=123;
    private Random random;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.fragment_register,container,false);
        return view;
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        requestStoragePermission();

        inputFullName = (EditText) view.findViewById(R.id.name);
        inputEmail = (EditText) view.findViewById(R.id.email);
        inputLogin=(EditText) view.findViewById(R.id.login);
        inputPassword = (EditText) view.findViewById(R.id.password);
        inputConfirmPassword=(EditText) view.findViewById(R.id.confirm);
        btnLoadPhoto=(Button) view.findViewById(R.id.btnLoadPhoto);
        btnRegister = (Button) view.findViewById(R.id.btnRegister);
        btnLinkToLogin = (Button) view.findViewById(R.id.btnLinkToLoginScreen);
        userDao=new UserDao(getActivity());
        random=new Random();

        // Progress dialog
        pDialog = new ProgressDialog(getActivity());
        pDialog.setCancelable(false);

        // Session manager
        session = new SessionManager(getActivity().getApplicationContext());

        // SQLite database handler
        //db = new SQLiteHandler(getApplicationContext());

        // Check if user is already logged in or not
        if (session.isLoggedIn()) {
            // User is already logged in. Take him to main activity
            Intent intent = new Intent(getActivity(), MainActivity.class);
            startActivity(intent);
            getActivity().finish();
        }

        btnLoadPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
            }
        });

        inputEmail.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count){
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after){
            }

            @Override
            public void afterTextChanged(Editable s)
            {
                if (inputEmail.getText().toString().matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z]+.[a-zA-Z]{2,4}$"))
                {

                }
                else
                {
                    inputEmail.setError("invalid Email");
                }
            }
        });
        inputEmail.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count){
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after){
            }

            @Override
            public void afterTextChanged(Editable s)
            {
                if (inputEmail.getText().toString().matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z]+.[a-zA-Z]{2,4}$"))
                {

                }
                else
                {
                    inputEmail.setError("invalid Email");
                }
            }
        });

        inputConfirmPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }


            @Override
            public void afterTextChanged(Editable editable) {
                String password = inputPassword.getText().toString();
                String confirm = inputConfirmPassword.getText().toString();
                if (confirm.equals(password)){

                }
                else{
                    inputConfirmPassword.setError("Not the same password");
                }

            }
        });


        // Register Button Click event
        btnRegister.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                String name = inputFullName.getText().toString().trim();
                String email = inputEmail.getText().toString().trim();
                String password = inputPassword.getText().toString().trim();
                String login = inputLogin.getText().toString().trim();
                String path = getPath(filePath);
                if (!name.isEmpty() && !email.isEmpty() && !password.isEmpty()) {
                    userDao.registerUser(name,email,password,login);
                    userDao.updateUserImage(email,path,random.nextInt());
                    getFragmentManager().beginTransaction().replace(R.id.container, new LoginFragment()).addToBackStack(null).commit();

                } else {
                    Toast.makeText(getActivity().getApplicationContext(),
                            "Please enter your details!", Toast.LENGTH_LONG)
                            .show();
                }
            }
        });

        // Link to Login Screen
        btnLinkToLogin.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                getFragmentManager().beginTransaction().replace(R.id.container,new LoginFragment()).addToBackStack(null).commit();

            }
        });


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.getData() != null) {
            filePath = data.getData();

        } else {
            Toast.makeText(getActivity(), "You haven't picked an Image", Toast.LENGTH_LONG).show();
        }

    }

    public String getPath(Uri uri) {
        Cursor cursor = getActivity().getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        String document_id = cursor.getString(0);
        document_id = document_id.substring(document_id.lastIndexOf(":") + 1);
        cursor.close();

        cursor = getActivity().getContentResolver().query(
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                null, MediaStore.Images.Media._ID + " = ? ", new String[]{document_id}, null);
        cursor.moveToFirst();
        String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
        cursor.close();

        return path;
    }
    private void requestStoragePermission() {
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
            return;

        if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE)) {
            //If the user has denied the permission previously your code will come to this block
            //Here you can explain why you need this permission
            //Explain here why you need this permission
        }
        //And finally ask for the permission
        ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        //Checking the request code of our request
        if (requestCode == STORAGE_PERMISSION_CODE) {

            //If permission is granted
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //Displaying a toast
                Toast.makeText(getActivity(), "Permission granted now you can read the storage", Toast.LENGTH_LONG).show();
            } else {
                //Displaying another toast if permission is not granted
                Toast.makeText(getActivity(), "Oops you just denied the permission", Toast.LENGTH_LONG).show();
            }
        }
    }

}
