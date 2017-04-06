package com.esprit.insta360.Activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.esprit.insta360.DAO.UserDao;
import com.esprit.insta360.R;
import com.esprit.insta360.Utils.SessionManager;

import java.util.ArrayList;
import java.util.List;

public class EditProfileActivity extends AppCompatActivity {
    private EditText name,login,email,phone,biographie;
    private Spinner sexe;
    private Button save;
    private SessionManager sessionManager;
    private UserDao userDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        sessionManager= new SessionManager(getApplicationContext());
        userDao = new UserDao(this);
        name =(EditText) findViewById(R.id.name);
        login =(EditText) findViewById(R.id.login);
        email =(EditText) findViewById(R.id.email);
        phone =(EditText) findViewById(R.id.phone);
        biographie =(EditText) findViewById(R.id.bio);
        sexe =(Spinner) findViewById(R.id.sexe);
        save =(Button) findViewById(R.id.btnSave);
        save.setVisibility(View.INVISIBLE);
        List<String> sexeList = new ArrayList<String>();
        sexeList.add("Man");
        sexeList.add("Woman");
        sexeList.add("Other");
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,sexeList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sexe.setAdapter(adapter);
        sexe.setSelection(0);
        sexe.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                save.setVisibility(View.VISIBLE);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                save.setVisibility(View.VISIBLE);
            }
        });
        login.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                save.setVisibility(View.VISIBLE);
            }
        });
        email.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count){
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after){
            }

            @Override
            public void afterTextChanged(Editable s)
            {
                if (email.getText().toString().matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z]+.[a-zA-Z]{2,4}$"))
                {
                    save.setVisibility(View.VISIBLE);
                }
                else
                {
                    save.setVisibility(View.INVISIBLE);
                    email.setError("invalid Email");
                }
            }
        });
        phone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                save.setVisibility(View.VISIBLE);
            }
        });
        biographie.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                save.setVisibility(View.VISIBLE);
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userDao.updateUser(name.getText().toString().trim(),email.getText().toString().trim()
                        ,phone.getText().toString().trim(),login.getText().toString().trim()
                        ,sexe.getSelectedItem().toString().trim(),biographie.getText().toString().trim());
            }
        });

        setParams();
    }

    public void setParams(){
        name.setText(sessionManager.getUserName());
        login.setText(sessionManager.getUserLogin());
        email.setText(sessionManager.getUserEmail());
        phone.setText(sessionManager.getUserPhone());
        biographie.setText(sessionManager.getUserBio());
    }
}
