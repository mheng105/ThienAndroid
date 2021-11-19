package com.example.ad_btl.Adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ad_btl.Activity.Read;
import com.example.ad_btl.Characters.Accounts;
import com.example.ad_btl.Characters.Kinds;
import com.example.ad_btl.Characters.Posts;
import com.example.ad_btl.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class KindAdapter extends RecyclerView.Adapter<KindAdapter.KindViewHolder>{
    private Context context;
    private List<Posts> listP;
    private List<Posts> listS;
    private String name;
    private Posts PostDele;
    private Accounts accounts;
    protected OnItemClickListener mListener;

    public interface OnItemClickListener{
        void onDeleteClick(Posts posts);
    }
    public KindAdapter(Context context, List<Posts> listP, Accounts account, OnItemClickListener listener) {
        this.listP = listP;
        this.accounts = account;
        this.context= context;
        this.listS = listP;
        this.mListener = listener;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public KindViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_personpost, parent, false);
        return new KindViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull KindViewHolder holder, int position) {
        Posts aP = listP.get(position);
        PostDele = aP;
        if (aP == null) {
            return;
        }
        holder.txttitle.setText(aP.getTitle());
        Picasso.with(context)
                .load(aP.getImageurl())
                .fit()
                .into(holder.ivimage);
        holder.btnX.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onDeleteClick(aP);
                /*DatabaseReference mDataref = FirebaseDatabase.getInstance().getReference("Posts");
                mDataref.child(aP.getIdpost()+aP.getSusername()).removeValue();
                StorageReference mStorgeref = FirebaseStorage.getInstance().getReferenceFromUrl("gs://contfirebase.appspot.com/images");
                mStorgeref.child(aP.getSusername()+aP.getIdpost()).delete();
                Toast.makeText(context, "Xóa", Toast.LENGTH_SHORT).show();
                notifyDataSetChanged();*/
            }
        });
        holder.btnS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onGotoread(aP, accounts);
            }
        });
    }
    public void onGotoread(Posts post, Accounts accou){
        Intent intent = new Intent(context, Read.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("post", post);
        bundle.putSerializable("acc", accou);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

    @Override
    public int getItemCount() {
        if (listP != null) {
            return listP.size();
        }
        return 0;
    }

    public class KindViewHolder extends RecyclerView.ViewHolder {
        public CardView cardView;
        public TextView txttitle;
        public ImageView ivimage;
        public Button btnS, btnX;

        public KindViewHolder(View itemView) {
            super(itemView);
            txttitle = itemView.findViewById(R.id.id_cv_title);
            ivimage = itemView.findViewById(R.id.id_cv_image);
            btnS = itemView.findViewById(R.id.id_btn_xem);
            btnX= itemView.findViewById(R.id.id_btn_xoa);
        }

    }
}
