package com.example.ad_btl.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ad_btl.AdminMain;
import com.example.ad_btl.Characters.Accounts;
import com.example.ad_btl.MainActivity;
import com.example.ad_btl.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.huawei.hms.ads.AdListener;
import com.huawei.hms.ads.AdParam;
import com.huawei.hms.ads.BannerAdSize;
import com.huawei.hms.ads.banner.BannerView;

public class Log_In extends AppCompatActivity implements  View.OnClickListener{
    DatabaseReference mData;
    Button btndangnhap,btndangky;
    EditText etMK, etTK;
    TextView txtnotify;
    BannerView bannerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);
        try {
            getViews();
            SignUpReceive();
        }catch (Exception e)
        {
            Toast.makeText(this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        loadAds();

    }
    private void getViews(){
        btndangky= (Button) findViewById(R.id.btndangky);
        btndangky.setOnClickListener(this);
        btndangnhap= (Button) findViewById(R.id.btndangnhap);
        btndangnhap.setOnClickListener(this);
        etTK= findViewById(R.id.etTK);
        etMK= findViewById(R.id.etMK);
        txtnotify= findViewById(R.id.txtnotify);
        bannerView = (BannerView) findViewById(R.id.hw_banner_view);


    }
    public void  SignUpReceive() {
        Intent iSu = getIntent();
        Bundle bunrcv = iSu.getExtras();
        if(bunrcv != null)
        {
            etTK.setText(""+bunrcv.getString("tk"));
            etMK.setText(""+bunrcv.getString("mk"));
        }
    }
    @Override
    public void onClick(View view) {
        if(view== btndangky ){
            Intent intent = new Intent(Log_In.this, Register.class);
            startActivity(intent);
        }
        if(view== btndangnhap){
            signIn(view);
        }
    }
    public void signIn(View view) {
        Accounts accounts= new Accounts(etTK.getText().toString(), etMK.getText().toString());
        mData = FirebaseDatabase.getInstance().getReference("Accounts").child(etTK.getText().toString().trim());
        mData.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.getValue() == null) {
                    etTK.setText("");
                    etMK.setText("");
                    txtnotify.setText("Tài khoản hoặc Mật khẩu không chính xác!");
                }
                else {
                    Accounts accounts = snapshot.getValue(Accounts.class);
                    if(etMK.getText().toString().equals(accounts.spassword)){
                        if(etTK.getText().toString().equals("admin")){
                            Intent intent = new Intent(Log_In.this, AdminMain.class);
                            Bundle bundle = new Bundle();
                            bundle.putSerializable("acc",accounts);
                            intent.putExtras(bundle);
                            startActivity(intent);
                        }
                        else {
                            Intent intent = new Intent(Log_In.this, MainActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putSerializable("acc", accounts);
                            intent.putExtras(bundle);
                            startActivity(intent);
                        }
                    }
                    else {
                        etMK.setText("");
                        txtnotify.setText("Mật khẩu sai!");
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    public void loadAds(){
        bannerView.setAdId("testw6vs28auh3");
        bannerView.setBannerAdSize(BannerAdSize.BANNER_SIZE_360_57);
        bannerView.setBannerRefresh(30);
        AdParam adParam = new AdParam.Builder().build();
        bannerView.loadAd(adParam);
    }
    private AdListener adListener = new AdListener() {
        @Override
        public void onAdLoaded() {
            // Called when an ad is loaded successfully.
        }
        @Override
        public void onAdFailed(int errorCode) {
            // Called when an ad fails to be loaded.
        }
        @Override
        public void onAdOpened() {
            // Called when an ad is opened.
        }
        @Override
        public void onAdClicked() {
            // Called when an ad is clicked.
        }
        @Override
        public void onAdLeave() {
            // Called when an ad leaves an app.
        }
        @Override
        public void onAdClosed() {
            // Called when an ad is closed.
        }
    };

}