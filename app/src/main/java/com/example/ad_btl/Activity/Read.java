package com.example.ad_btl.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ad_btl.Characters.Accounts;
import com.example.ad_btl.Characters.Posts;
import com.example.ad_btl.MainActivity;
import com.example.ad_btl.R;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

public class Read extends AppCompatActivity {
    Posts post;
    Accounts accounts;
    Button btnR, btnU, btnX;
    TextView txttitle;
    ImageView img_post;
    EditText edtcontent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        init();
        SignUpReceive();
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case android.R.id.home:
                onBackPressed();
                return true;

            default:break;
        }

        return super.onOptionsItemSelected(item);
    }
    public void init(){
        btnU= findViewById(R.id.id_btn_upload);
        txttitle= findViewById(R.id.id_txt_title);
        img_post= findViewById(R.id.id_photo);
        edtcontent= findViewById(R.id.id_edt_content);
        btnX= findViewById(R.id.id_btn_xn);
        btnX.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDialog(post, edtcontent.getText().toString());
            }
        });
        btnU.setVisibility(View.INVISIBLE);
        btnU.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateP();
            }
        });
    }
    public void  SignUpReceive() {
        Intent iSu = getIntent();
        Bundle bunrcv = iSu.getExtras();
        if(bunrcv != null)
        {
            post = (Posts) bunrcv.get("post");
            accounts = (Accounts) bunrcv.get("acc");
        }
        txttitle.setText(post.getTitle());
        edtcontent.setText(post.getContent());
        Picasso.with(this).load(post.getImageurl()).into(img_post);
        if(post.getSusername().equals(accounts.getUsername())){
            btnU.setVisibility(View.VISIBLE);
        }
    }
    private void updateP(){
        btnU.setVisibility(View.INVISIBLE);
        btnX.setVisibility(View.VISIBLE);
        edtcontent.setEnabled(true);
    }
    private void openDialog(Posts post, String content){
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_yn);
        Window window = dialog.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT,WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(false);
        Button btnDY = dialog.findViewById(R.id.id_btnDY);
        Button btnH = dialog.findViewById(R.id.id_btnH);
        btnH.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        btnDY.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference dataref= database.getReference("Posts");
                String newcontent = content;
                post.setContent(newcontent);
                Map<String, Object> newpost =new HashMap<String,Object>();
                newpost.put("content", newcontent);
                dataref.child(String.valueOf(post.getIdpost()+post.getSusername())).updateChildren( newpost
                        , new DatabaseReference.CompletionListener() {
                            @Override
                            public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                                Toast.makeText(Read.this, "Sửa thành công",Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                            }
                        });
            }
        });
        dialog.show();
    }
}