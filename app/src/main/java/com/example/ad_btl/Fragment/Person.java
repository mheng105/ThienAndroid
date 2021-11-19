package com.example.ad_btl.Fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Person extends Fragment{
        private Button btnR, btnDX;
        private RecyclerView mRycycler;
        private List<Posts> listP;
        private ChildEventListener valueEventListener;
        private FirebaseStorage mstorge;
        private DatabaseReference mdataref;
        private Accounts account;
        private TextView tk, mk;
        private ProgressBar progressBar;
        private View view;
        private KindAdapter mItemAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fg_person, container, false);
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
            mRycycler.setLayoutManager(new LinearLayoutManager(getActivity()));
            listP = new ArrayList<>();
            mItemAdapter = new KindAdapter(getActivity(), listP, account, new KindAdapter.OnItemClickListener() {
                @Override
                public void onDeleteClick(Posts posts) {
                    DeleteItem(posts);
                }
            });
            mRycycler.setAdapter(mItemAdapter);
            mstorge = FirebaseStorage.getInstance();
            mdataref = FirebaseDatabase.getInstance().getReference("Posts");
            Query query = mdataref.orderByChild("susername").equalTo(account.getUsername());
            valueEventListener = mdataref.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                    Posts posts = snapshot.getValue(Posts.class);
                    if(posts.getSusername().equals(account.getUsername())) {
                        listP.add(posts);
                    }
                    mItemAdapter.notifyDataSetChanged();

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
                    mItemAdapter.notifyDataSetChanged();
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
    public void DeleteItem(Posts posts){
        new AlertDialog.Builder(getActivity())
                .setTitle("Xóa bài viết")
                .setMessage("Có chắc muốn xóa")
                .setNegativeButton("Cancel", null)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        ProgressDialog dialog = new ProgressDialog(getActivity());
                        dialog.show();
                        DatabaseReference mDataref = FirebaseDatabase.getInstance().getReference("Posts");
                        mDataref.child(posts.getIdpost()+posts.getSusername()).removeValue(new DatabaseReference.CompletionListener() {
                            @Override
                            public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                                StorageReference mStorgeref = FirebaseStorage.getInstance().getReferenceFromUrl("gs://contfirebase.appspot.com/images");
                                mStorgeref.child(posts.getSusername()+posts.getIdpost()+".png").delete();
                                Toast.makeText(getActivity(), "Xóa thành công", Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                            }
                        });
                    }
                })
                .show();

    }
}