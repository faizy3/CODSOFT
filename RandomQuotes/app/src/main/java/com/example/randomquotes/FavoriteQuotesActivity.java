package com.example.randomquotes;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Random;

public class FavoriteQuotesActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    FavoriteQuotesAdapter myAdapter;
    ArrayList<QuotesModel> list;

    QuotesModel quotesModel ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite_quotes);
        recyclerView = findViewById(R.id.recyclerView);
        list = new ArrayList<>();

        //load data from database
        MyDBHelper helper = new MyDBHelper(this);
        SQLiteDatabase db = helper.getWritableDatabase();
        String qry = "SELECT * FROM favoritequotes ";
        Cursor cursor = db.rawQuery(qry , null);
        while(cursor.moveToNext()){
            quotesModel = new QuotesModel();
            quotesModel.setFavQuoteId(cursor.getInt(0));
            quotesModel.setFavoritequote(cursor.getString(1));
            list.add(quotesModel);
        }
        if(list.isEmpty()){
            Toast.makeText(this, "add quote to the fav list", Toast.LENGTH_SHORT).show();
        }else {
            //set adapter to recyclerview
            myAdapter = new FavoriteQuotesAdapter(this , list);
            LinearLayoutManager manager = new LinearLayoutManager(this , LinearLayoutManager.VERTICAL , false);
            recyclerView.setLayoutManager(manager);
            recyclerView.setAdapter(myAdapter);
        }


    }

}