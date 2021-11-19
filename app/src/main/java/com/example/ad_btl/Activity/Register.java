package com.example.ad_btl.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.ad_btl.Characters.Accounts;
import com.example.ad_btl.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Register extends AppCompatActivity implements View.OnClickListener {
    DatabaseReference mData;
    EditText edtusename, edtpass, edtrepass;
    Button btnregister, btnreturn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        getView();
    }
    public void getView() {
        edtusename = (EditText) findViewById(R.id.edtusename);
        edtpass= (EditText) findViewById(R.id.edtpass);
        edtrepass= (EditText) findViewById(R.id.edtrepass);
        btnregister = (Button) findViewById(R.id.btnregister);
        btnregister.setOnClickListener(this);
        btnreturn = (Button) findViewById(R.id.btnreturn);
        btnreturn.setOnClickListener(this);

        edtusename.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() ==0) {
                    edtusename.setError("Bạn bắt buộc phải nhập tài khoản");
                } else {
                    edtusename.setError(null);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }
    @Override
    public void onClick(View view) {
        if(view== btnreturn ){
            Intent intent = new Intent(Register.this, Log_In.class);
            startActivity(intent);
        }
        if(view== btnregister){
            checkRegister();
        }
    }
    public void checkRegister(){
        if(edtpass.getText().toString().equals(edtrepass.getText().toString())){
            registerUser();
        }
        else{
            Toast.makeText(this, "Mật khẩu nhập lại không đúng!", Toast.LENGTH_SHORT).show();
        }
    }
    public  void registerUser(){
        mData = FirebaseDatabase.getInstance().getReference("Accounts").child(edtusename.getText().toString());
        mData.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.getValue() == null) {
                    Accounts acc = new Accounts();
                    acc.susername = edtusename.getText().toString();
                    acc.spassword = edtpass.getText().toString();
                    mData.setValue(acc, new DatabaseReference.CompletionListener() {
                        @Override
                        public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                            if (error == null) {
                                Toast.makeText(Register.this, "Đăng ký thành công!", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(Register.this, Log_In.class);
                                Bundle bun = new Bundle();
                                bun.putString("tk", edtusename.getText().toString().trim());
                                bun.putString("mk", edtpass.getText().toString().trim());
                                intent.putExtras(bun);
                                startActivity(intent);
                            }
                            else {
                                Toast.makeText(Register.this, ""+error.toString(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
                else {
                    Toast.makeText(Register.this, "Tài khoản đã tồn tại!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}