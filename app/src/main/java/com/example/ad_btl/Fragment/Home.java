package com.example.ad_btl.Fragment;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ad_btl.Activity.Read;
import com.example.ad_btl.Adapter.ItemAdapter;
import com.example.ad_btl.Adapter.KindAdapter;
import com.example.ad_btl.Characters.Accounts;
import com.example.ad_btl.Characters.Kinds;
import com.example.ad_btl.Characters.Posts;
import com.example.ad_btl.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Home extends Fragment {
    private RecyclerView mRycycler;
    public ItemAdapter mItemAdapter;
    private KindAdapter mKindAdapter;
    private List<Posts> listP;
    private List<Kinds> listK;
    private DatabaseReference mdataref;
    private ProgressBar progressBar;
    private View view;
    private SearchView searchView;
    private EditText edtS;
    private Button btnS;
    private Accounts account;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fg_home, container, false);
        account =(Accounts) getArguments().getSerializable("acc");
        init();
        LinearLayoutManager verticalLayoutManagaer = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        mRycycler.setLayoutManager(verticalLayoutManagaer);
        mItemAdapter = new ItemAdapter(getActivity(), getData(), account);
        mRycycler.setAdapter(mItemAdapter);

        return view;
    }
    private void init(){
        progressBar= view.findViewById(R.id.progress_circle);
        btnS = view.findViewById(R.id.id_btn_search);
        btnS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edtS.setText("");
            }
        });
        edtS= view.findViewById(R.id.id_edt_search);
        edtS.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                mItemAdapter.getFilter().filter(charSequence);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        mRycycler = view.findViewById(R.id.id_recyclerview);
    }
    public ArrayList<Posts> getData() {
        ArrayList<Posts> Arr = new ArrayList<>();
        ArrayList<Posts> Brr = new ArrayList<>();
        mdataref = FirebaseDatabase.getInstance().getReference("Posts");
        mdataref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()) {
                    for( DataSnapshot snap : snapshot.getChildren())
                    {
                        Posts posts = snap.getValue(Posts.class);
                        Arr.add(posts);
                    }
                    for(int i = Arr.size()-1; i>=0; i--)
                        Brr.add(Arr.get(i));
                    mItemAdapter.notifyDataSetChanged();
                }
                progressBar.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                progressBar.setVisibility(View.VISIBLE);

            }
        });
        return Brr;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_search, menu);
        searchView = (SearchView) menu.findItem(R.id.id_nv_search).getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                mItemAdapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                mItemAdapter.getFilter().filter(newText);
                return false;
            }
        });
    }

}
