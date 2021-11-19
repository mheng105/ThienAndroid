package com.example.ad_btl.Fragment;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.ad_btl.Characters.Accounts;
import com.example.ad_btl.Characters.Posts;
import com.example.ad_btl.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;

import static android.app.Activity.RESULT_OK;
import static com.example.ad_btl.MainActivity.REQUEST_CODE;

public class UpdatePost extends Fragment {
    private Spinner spin;
    private ArrayList<String> list;
    Button btnImage, btnCancel, btnAdd, btnRe;
    EditText edttitle, edttk, edtcontent;
    ImageView img;
    Bitmap btm;
    DatabaseReference mDataref;
    StorageReference mStorgeref;
    FirebaseStorage mfirebasestorage;
    private Uri url;
    String catergory;
    Accounts accounts;
    View vview;
    // Xử lý kết quả ảnh trả về
    final private ActivityResultLauncher<Intent> activityResultLauncher= registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if(result.getResultCode()==RESULT_OK){
                        Intent mintent = result.getData();
                        if(mintent==null){
                            return;
                        }
                        Uri uri = mintent.getData();
                        url = uri;

                        try {
                            Bitmap bitmap= MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), uri);
                            btm = bitmap;
                            setBitmap(bitmap);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        vview = inflater.inflate(R.layout.fg_updatepost, container, false);
         accounts =(Accounts) getArguments().getSerializable("acc");
        getViews();
        initSpinner();
        initListen();
        return vview;
    }

    // setup Spinner
    public void initSpinner(){
        list = new ArrayList<>();
        list.add(0,"Khác");
        list.add("Nấm");
        list.add("Cây cối");
        list.add("Hoa quả");
        spin= (Spinner) vview.findViewById(R.id.spin_kind);
        ArrayAdapter arrayAdapter=new ArrayAdapter(getActivity(),android.R.layout.simple_spinner_item, list);

        spin.setAdapter(arrayAdapter);
        spin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                catergory = list.get(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }
    public void getViews(){
        img = vview.findViewById(R.id.id_photo);
        btnAdd= (Button) vview.findViewById(R.id.btnadd);
        btnCancel= (Button) vview.findViewById(R.id.btncancel);
        btnImage= (Button) vview.findViewById(R.id.btnimage);
        edttk= vview.findViewById(R.id.edtTK);
        edttk.setText(accounts.getUsername());
        edtcontent= vview.findViewById(R.id.edtcontent);
        edttitle= vview.findViewById(R.id.edttitle);

    }
    //ánh xạ đối tượng
    private void initListen(){
        btnImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OnClickRequestPermission();
            }
        });
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addaPost();
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clearEdittext();
            }
        });
    }
    // cho phép truy cập
    private  void OnClickRequestPermission(){

        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.M){
            openGallery();
            return;
        }

        if(getActivity().checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)== PackageManager.PERMISSION_GRANTED){
            openGallery();
        }
        else {
            String[] m = {Manifest.permission.READ_EXTERNAL_STORAGE};
            getActivity().requestPermissions(m, REQUEST_CODE);
        }
    }
    // nhận kết quả trả về
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==REQUEST_CODE){
            if(grantResults.length >0 && grantResults[0]== PackageManager.PERMISSION_GRANTED){
                openGallery();
            }

        }
    }
    // mở Gallery
    public void openGallery(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        activityResultLauncher.launch(Intent.createChooser(intent,"Select Image"));
    }
    //Set bitmap cho ảnh
    public void setBitmap(Bitmap bitmap){
        img.setImageBitmap(bitmap);
    }

    // thêm data post vào firebase
  /*  public void addContentPost(){
        //trước thêm vào firebase realtime
        // kết nối và tạo mục
        Calendar calendar= Calendar.getInstance();
        mDataref = FirebaseDatabase.getInstance().getReference("Posts").child(edttk.getText().toString()+calendar.getTimeInMillis());
        mDataref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.getValue() == null) {
                    Posts acc = new Posts();
                    acc.idpost =""+ System.currentTimeMillis();
                    acc.susername = edttk.getText().toString();
                    acc.title = edttitle.getText().toString();
                    acc.content = edtcontent.getText().toString();
                    acc.kind = catergory;

                    mDataref.setValue(acc, new DatabaseReference.CompletionListener() {
                        @Override
                        public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                            if (error == null) {
                                Toast.makeText(getActivity(), "Thêm bài thành công!", Toast.LENGTH_SHORT).show();
                                addImage();

                            } else {
                                Toast.makeText(getActivity(), "" + error.toString(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                } else {
                    Toast.makeText(getActivity(), "", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }

        });
    }*/
    public void addaPost(){
        //kết nối
        ProgressDialog dialog = new ProgressDialog(getActivity());
        dialog.show();
        Calendar calendar= Calendar.getInstance();
        mDataref = FirebaseDatabase.getInstance().getReference("subPosts");
        String id =edttk.getText().toString()+System.currentTimeMillis();
        mfirebasestorage = FirebaseStorage.getInstance();
        StorageReference storageRef = mfirebasestorage.getReferenceFromUrl("gs://contfirebase.appspot.com/images");
        StorageReference imagesRef = storageRef.child(id+".png");
        Uri link;
        imagesRef.putFile(url).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                imagesRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Posts acc = new Posts();
                        acc.idpost =""+ System.currentTimeMillis();
                        acc.susername = edttk.getText().toString();
                        acc.title = edttitle.getText().toString();
                        acc.content = edtcontent.getText().toString();
                        acc.kind = catergory;
                        acc.imageurl = uri.toString();
                        mDataref.child(acc.idpost+acc.susername).setValue(acc, new DatabaseReference.CompletionListener() {
                            @Override
                            public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                                if(error == null){
                                    Toast.makeText(getContext(), "Thêm bài thành công!", Toast.LENGTH_SHORT).show();
                                    dialog.dismiss();
                                }
                            }
                        });
                    }
                });
            }
        });
  /*      cropImage.setOnClickListener(this);
        spinner = (Spinner) findViewById(R.id.spinner1);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                String[] ratios = getResources().getStringArray(R.array.ratios);
                try {
                    int ratioX = Integer.parseInt(ratios[pos].split(":")[0]);
                    int ratioY = Integer.parseInt(ratios[pos].split(":")[1]);
                    cropLayoutView.setAspectRatio(ratioX, ratioY);
                } catch (Exception e) {

                    cropLayoutView.setFixedAspectRatio(false);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Another interface callback
            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_crop_image:
                Bitmap croppedImage = cropLayoutView.getCroppedImage();
                cropLayoutView.setImageBitmap(croppedImage);
                break;
        }
    }*/
        /*img.setDrawingCacheEnabled(true);
        img.buildDrawingCache();
        Bitmap bitmap = img.getDrawingCache();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100,baos);
        byte[] data = baos.toByteArray();
        UploadTask uploadTask= imagesRef.putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getActivity(), "Lỗi", Toast.LENGTH_SHORT).show();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(getActivity(), "OK", Toast.LENGTH_SHORT).show();
            }
        });*/
    }
    public void clearEdittext(){
        edttitle.setText("");
        edtcontent.setText("");
    }
}
