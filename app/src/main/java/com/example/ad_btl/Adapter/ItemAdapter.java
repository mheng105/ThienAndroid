package com.example.ad_btl.Adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
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
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ItemViewHolder> implements Filterable {
    private Context context;
    private List<Posts> listP;
    private List<Posts> listS;
    private String name;
    private Accounts accounts;

    public ItemAdapter(Context context, List<Posts> listP, Accounts account) {
        this.listP = listP;
        this.accounts = account;
        this.context= context;
        this.listS = listP;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_post, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        Posts aP = listP.get(position);
        if (aP == null) {
            return;
        }
        holder.txttitle.setText(aP.getTitle());
        Picasso.with(context)
                .load(aP.getImageurl())
                .fit()
                .into(holder.ivimage);
        holder.cardView.setOnClickListener(new View.OnClickListener() {
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

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String search = charSequence.toString();
                if(search.isEmpty()){
                    listP = listS;
                }
                else{
                    List<Posts> list = new ArrayList<>();
                    for(Posts posts: listS){
                        if(posts.getContent().toLowerCase().contains(search.toLowerCase())){
                            list.add(posts);
                        }
                    }
                    listP= list;
                }
                FilterResults filterResults= new FilterResults();
                filterResults.values= listP;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                listP= (List<Posts>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {
        public CardView cardView;
        public TextView txttitle;
        public ImageView ivimage;

        public ItemViewHolder(View itemView) {
            super(itemView);
            txttitle = itemView.findViewById(R.id.id_cv_title);
            ivimage = itemView.findViewById(R.id.id_cv_image);
            cardView = itemView.findViewById(R.id.id_item_apost);
        }

    }
}