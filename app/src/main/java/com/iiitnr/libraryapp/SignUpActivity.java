package com.iiitnr.libraryapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener{

    private TextInputLayout editName;
    private TextInputLayout editEnrollNo;
    private TextInputLayout editCardNo;
    private TextInputLayout editPass1;
    private TextInputLayout editID;
    private TextInputLayout editPass;
    private Button buttonRegister;
    private TextView toSignIn;
    private ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth;
    private CheckBox check1;
    private FirebaseFirestore db;
    private Spinner userType;
    private String type;
    private int type1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);


        editID = (TextInputLayout) findViewById(R.id.editID);
        editPass = (TextInputLayout) findViewById(R.id.editPass);
        editPass1=(TextInputLayout)findViewById(R.id.editPass1);
        editName=(TextInputLayout)findViewById(R.id.editName);
        editEnrollNo=(TextInputLayout)findViewById(R.id.editEnrollNo);
        editCardNo=(TextInputLayout)findViewById(R.id.editCardNo);
        buttonRegister = (Button) findViewById(R.id.buttonRegister);
        toSignIn = (TextView) findViewById(R.id.toSignIn);
        progressDialog=new ProgressDialog(this);
        check1=(CheckBox)findViewById(R.id.check1);
        userType=(Spinner)findViewById(R.id.userType);

        List<String> list = new ArrayList<>();
        list.add("Select Account Type");
        list.add("User");
        list.add("Admin");

        ArrayAdapter adapter =new ArrayAdapter(this,android.R.layout.simple_spinner_dropdown_item,list);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        userType.setAdapter(adapter);
        userType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                type=parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        FirebaseApp.initializeApp(this);
        firebaseAuth=FirebaseAuth.getInstance();
        db=FirebaseFirestore.getInstance();
        buttonRegister.setOnClickListener(this);
        toSignIn.setOnClickListener(this);
        check1.setOnClickListener(this);


    }


    private boolean verifyName()
    {
        String name=editName.getEditText().getText().toString().trim();
        if(name.isEmpty())
        {   editName.setErrorEnabled(true);
            editName.setError("Name Required");
            return true;
        }
        else
        {
            editName.setErrorEnabled(false);
            return false;
        }
    }


    private boolean verifyCardNo()
    {
        String cardNo=editCardNo.getEditText().getText().toString().trim();
        if(cardNo.isEmpty())
        {   editCardNo.setErrorEnabled(true);
            editCardNo.setError("Card No. Required");
            return true;
        }
        else
        {
            editCardNo.setErrorEnabled(false);
            return false;
        }
    }


    private boolean verifyEnrollNo()
    {
        String enrollNo=editEnrollNo.getEditText().getText().toString().trim();
        if(enrollNo.isEmpty())
        {   editEnrollNo.setErrorEnabled(true);
            editEnrollNo.setError("Enrollment No. Required");
            return true;
        }
        else
        {
            editEnrollNo.setErrorEnabled(false);
            return false;
        }
    }




    private boolean verifyEmailId()
    {
        String emailId=editID.getEditText().getText().toString().trim();
        if(emailId.isEmpty())
        {   editID.setErrorEnabled(true);
            editID.setError(" Email ID Required");
            return true;
        }
        else if(!emailId.endsWith("@iiitnr.edu.in"))
        {
            editID.setErrorEnabled(true);
            editID.setError(" Enter Valid Email ID");
            return true;
        }
        else
        {
            editID.setErrorEnabled(false);
            return false;
        }
    }


    private boolean verifyPass()
    {
        String pass=editPass.getEditText().getText().toString().trim();
        if(pass.isEmpty())
        {   editPass.setErrorEnabled(true);
            editPass.setError(" Password Required");
            return true;
        }
        else
        {
            editPass.setErrorEnabled(false);
            return false;
        }
    }

    private boolean verifyPass1()
    {
        String pass1=editPass1.getEditText().getText().toString().trim();
        String pass=editPass.getEditText().getText().toString().trim();
        if(pass1.isEmpty())
        {   editPass1.setErrorEnabled(true);
            editPass1.setError("Confirm Password Required");
            return true;
        }
        else if(pass.equals(pass1))
        {
            editPass1.setErrorEnabled(false);
            return false;
        }
        else
        {
            editPass1.setErrorEnabled(true);
            editPass1.setError("Passwords do not match");
            return true;
        }
    }

    private boolean verifyType()
    {
       if (type.equals("Select Account Type"))
       {
           Toast.makeText(this, "Please select account type !", Toast.LENGTH_SHORT).show();
           return true;
       }
       return false;
    }
    private void registerUser()
    {
        boolean res= (verifyName()|verifyCardNo()|verifyEmailId()|verifyEnrollNo()|verifyPass()|verifyPass1()|verifyType());
        if(res==true)
            return;
        String id=editID.getEditText().getText().toString().trim();
        String pass=editPass.getEditText().getText().toString().trim();
        if(type.equals("User")){
            type1=0;
        }
        else
            type1=1;

        progressDialog.setMessage("Registering User ... ");
        progressDialog.show();

        firebaseAuth.createUserWithEmailAndPassword(id,pass).addOnCompleteListener(SignUpActivity.this, new OnCompleteListener<AuthResult>()
        {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful())
                {
                    String id=editID.getEditText().getText().toString().trim();
                    int enroll=Integer.parseInt(editEnrollNo.getEditText().getText().toString().trim());
                    int card=Integer.parseInt(editCardNo.getEditText().getText().toString().trim());
                    String name=editName.getEditText().getText().toString().trim();
                    User user =new User(name,id,enroll,card,type1);

                   db.collection("User").document(id).set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                       @Override
                       public void onSuccess(Void aVoid) {
                           progressDialog.cancel();
                           String id=editID.getEditText().getText().toString().trim();
                           db.document("User/"+id).update("id","Hello");
                           Toast.makeText(SignUpActivity.this,"Registered Successfully !",Toast.LENGTH_SHORT).show();
                           startActivity(new Intent(getApplicationContext(),MainActivity.class));
                           finish();
                       }
                   });


                }

                else
                {   progressDialog.cancel();
                    Toast.makeText(SignUpActivity.this,"Registration Failed ! Try Again ",Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

    @Override
    public void onClick(View v) {

        if(v==check1)
        {
            if(check1.isChecked())
                buttonRegister.setEnabled(true);
            else
                buttonRegister.setEnabled(false);
        }
        else if(v==buttonRegister)
            registerUser();

        else if(v==toSignIn) {
            startActivity(new Intent(getApplicationContext(),SignInActivity.class));
            finish();
        }
    }
}

/* db.collection("User").add(user).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {

                            String id1=documentReference.getId();
                            db.document("User/"+id1).update("id",id1);
                            progressDialog.cancel();
                            Toast.makeText(SignUpActivity.this,"Registered Successfully !",Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(),MainActivity.class));
                            finish();
                        }
                    });*/