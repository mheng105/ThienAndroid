package com.example.ad_btl.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
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

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

public class ApprovingAdapter extends RecyclerView.Adapter<ApprovingAdapter.ApprovingViewHolder>{
    private Context context;
    private List<Posts> listP;
    private List<Posts> listS;
    private String name;
    private Posts PostDele;
    private Accounts accounts;
    protected OnItemClickListener mListener;

    public interface OnItemClickListener{
        void onAproClick(Posts posts);
    }
    public ApprovingAdapter(Context context, List<Posts> listP, Accounts account, OnItemClickListener listener) {
        this.listP = listP;
        this.accounts = account;
        this.context= context;
        this.listS = listP;
        this.mListener = listener;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ApprovingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_approving, parent, false);
        return new ApprovingViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ApprovingViewHolder holder, int position) {
        Posts aP = listP.get(position);
        PostDele = aP;
        if (aP == null) {
            return;
        }
        holder.txtuser.setText(aP.getSusername());
        holder.txttitle.setText(aP.getTitle());
        holder.txtcontent.setText(aP.getContent());
        Picasso.with(context)
                .load(aP.getImageurl())
                .fit()
                .into(holder.ivimage);

        holder.btnD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onAproClick(aP);
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

    public class ApprovingViewHolder extends RecyclerView.ViewHolder {
        public TextView txttitle, txtuser;
        EditText txtcontent;
        public ImageView ivimage;
        public Button btnD;

        public ApprovingViewHolder(View itemView) {
            super(itemView);
            txttitle = itemView.findViewById(R.id.id_txt_title);
            txtuser = itemView.findViewById(R.id.id_txt_user);
            txtcontent = itemView.findViewById(R.id.id_edt_content);
            ivimage = itemView.findViewById(R.id.id_photo);
            btnD = itemView.findViewById(R.id.id_btn_approved);

        }

    }
}
