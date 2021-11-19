package com.example.ad_btl.Fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ad_btl.Activity.Log_In;
import com.example.ad_btl.Adapter.ApprovingAdapter;
import com.example.ad_btl.Adapter.ItemAdapter;
import com.example.ad_btl.Adapter.KindAdapter;
import com.example.ad_btl.Characters.Accounts;
import com.example.ad_btl.Characters.Posts;
import com.example.ad_btl.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Approving extends Fragment{
    private Button btnDX;
    private RecyclerView mRycycler;
    private List<Posts> listP;
    private ChildEventListener valueEventListener;
    private FirebaseStorage mstorge;
    private DatabaseReference mdataref;
    private Accounts account;
    private TextView tk, mk;
    private ProgressBar progressBar;
    private View view;
    private ApprovingAdapter mAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fg_aproving, container, false);
        account =(Accounts) getArguments().getSerializable("acc");
        init();
        return view;
    }

    private void init(){
        tk = view.findViewById(R.id.idtk);
        tk.setText(account.getUsername());
        mk = view.findViewById(R.id.idmk);
        mk.setText(account.getPassword());
        btnDX = view.findViewById(R.id.id_btn_dx);
        btnDX.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), Log_In.class);
                startActivity(intent);
            }
        });
        mRycycler = view.findViewById(R.id.id_recyclerview);
        mRycycler.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        listP = new ArrayList<>();
        mAdapter = new ApprovingAdapter(getActivity(), listP, account, new ApprovingAdapter.OnItemClickListener() {
            @Override
            public void onAproClick(Posts posts) {
                AproItem(posts);
            }
        });
        mRycycler.setAdapter(mAdapter);
        mstorge = FirebaseStorage.getInstance();
        mdataref = FirebaseDatabase.getInstance().getReference("subPosts");
        valueEventListener = mdataref.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                Posts posts = snapshot.getValue(Posts.class);
                    listP.add(posts);
                mAdapter.notifyDataSetChanged();

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                Posts posts = snapshot.getValue(Posts.class);
                for(int i = 0; i< listP.size(); i++)
                    if(listP.get(i).getIdpost().equals(posts.getIdpost()))
                        listP.remove(listP.get(i));
                mAdapter.notifyDataSetChanged();
            }


            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                progressBar.setVisibility(View.INVISIBLE);
            }
        });
    }
    public void AproItem(Posts posts){
        new AlertDialog.Builder(getActivity())
                .setTitle("Duyệt bài viết")
                .setMessage("Bạn chắc chứ")
                .setNegativeButton("Cancel", null)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //thêm bài vào Posts
                        ProgressDialog dialog = new ProgressDialog(getActivity());
                        dialog.show();
                        Calendar calendar= Calendar.getInstance();
                        mdataref = FirebaseDatabase.getInstance().getReference("Posts");
                        Posts acc = new Posts();
                        acc.idpost =""+ System.currentTimeMillis();
                        acc.susername = posts.getSusername();
                        acc.title = posts.getTitle();
                        acc.content = posts.getContent();
                        acc.kind = posts.getKind();
                        acc.imageurl = posts.getImageurl();
                        mdataref.child(acc.idpost+acc.susername).setValue(acc, new DatabaseReference.CompletionListener() {
                            @Override
                            public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                                if(error == null){
                                    //Xóa ở subPosts

                                    DatabaseReference mDataref = FirebaseDatabase.getInstance().getReference("subPosts");
                                    mDataref.child(posts.getIdpost()+posts.getSusername()).removeValue(new DatabaseReference.CompletionListener() {
                                        @Override
                                        public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                                                dialog.dismiss();
                                        }
                                    });
                                }
                            }
                        });
                    }
                }).show();

    }
}
