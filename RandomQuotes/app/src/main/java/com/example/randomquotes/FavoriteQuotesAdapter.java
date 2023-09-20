package com.example.randomquotes;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class FavoriteQuotesAdapter extends RecyclerView.Adapter {
    Context context;
    ArrayList<QuotesModel> arrayList;
    public FavoriteQuotesAdapter(Context context , ArrayList<QuotesModel> arrayList) {
        this.arrayList = arrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.favorite_quotes_view , parent , false);
        FavoriteQuotesViewHolder obj = new FavoriteQuotesViewHolder(v);
        return obj;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        FavoriteQuotesViewHolder viewHolder = (FavoriteQuotesViewHolder) holder;
        QuotesModel list = arrayList.get(position);
        viewHolder.FavoriteQuotestxtView.setText(list.getFavoritequote());
        viewHolder.imgbtnFavQuoteShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sharequotes(list.getFavoritequote());
            }
        });
    }
    public void sharequotes(String quote) {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        //set data type
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_SUBJECT , "quote");
        //take text from textview and put into putextra to share text
        shareIntent.putExtra(Intent.EXTRA_TEXT ,quote );
        context.startActivity(Intent.createChooser(shareIntent, "Share Via"));
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }
    public class FavoriteQuotesViewHolder extends RecyclerView.ViewHolder{
        TextView FavoriteQuotestxtView;
        ImageButton imgbtnFavQuoteShare;

        public FavoriteQuotesViewHolder(@NonNull View v) {
            super(v);
            FavoriteQuotestxtView = v.findViewById(R.id.FavQuote);
            imgbtnFavQuoteShare = v.findViewById(R.id.FavQuoteShare);
        v.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Delete Quote");
                builder.setMessage("Do You Want To Delete Quote From List?");
                builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
//                        arrayList = new ArrayList<>();
                        MyDBHelper helper = new MyDBHelper(context);
                        QuotesModel quotesModel = arrayList.get(getAdapterPosition());
                        int id = quotesModel.getFavQuoteId();
                        helper.deleteQuoteFromFavQuotes(id);
                        arrayList.remove(getAdapterPosition());
                        notifyItemRemoved(getAdapterPosition());
                    }
                });
                builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        notifyItemChanged(getAdapterPosition());
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
                return false;
            }
        });
        }
    }
}
