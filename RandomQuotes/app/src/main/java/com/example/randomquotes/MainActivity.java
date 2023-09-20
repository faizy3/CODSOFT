package com.example.randomquotes;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.os.Bundle;
import android.text.TextPaint;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
    ImageButton imgbtnShare , imgbtnAddFavQuote, imgbtnFavView;
    TextView quotetextView;
    ArrayList<QuotesModel> list;
    MyDBHelper helper;
    SQLiteDatabase db;
    TextView titletext;

    QuotesModel quotesModel;
    private static int sizeOfTable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imgbtnShare = findViewById(R.id.imgbtnShare);
        imgbtnAddFavQuote = findViewById(R.id.imgbtnFav);
        quotetextView = findViewById(R.id.quotestextview);
        imgbtnFavView = findViewById(R.id.imgbtnFavView);
//        titletext = findViewById(R.id.titletext);
//        TextPaint textPaint = titletext.getPaint();
//        float textwidth = textPaint.measureText(titletext.getText().toString());
//        Shader textShader = new LinearGradient(0,0 ,textwidth , titletext.getTextSize() ,
//                new int[]{
////                        Color.parseColor("#553bdd"),
////                        Color.parseColor("#fe2485"),
////                        Color.parseColor("#fdd55b"),
////                        Color.parseColor("#00aeff"),
//                        Color.parseColor("#8e0e5e"),
//                        Color.parseColor("#1D2671"),
//                }, null , Shader.TileMode.CLAMP);
//        titletext.getPaint().setShader(textShader);
        //this method retrieve all data from quote table in database and return size of table
        getSizeOfQuoteTableFromDB();
        //this method generate random number and then retrieve random quote
        String randomQuote = getRandomQuote();
        quotetextView.setText(randomQuote);


        imgbtnShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String quote = quotetextView.getText().toString();
                sharequotes(quote);
            }
        });
        imgbtnAddFavQuote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String textviewstring = quotetextView.getText().toString();
                String Favquote ="";
                helper = new MyDBHelper(MainActivity.this);
                db = helper.getWritableDatabase();
                String qry = "SELECT * FROM favoritequotes";
                Cursor cursor = db.rawQuery(qry , null);
                while(cursor.moveToNext()){
                    Favquote = cursor.getString(1);
                    if(textviewstring.equals(Favquote)){
                        break;
                    }

                }
                //

                if(textviewstring.equals(Favquote)){
                    Toast.makeText(MainActivity.this, "You Already Mark Favorite This Quote", Toast.LENGTH_SHORT).show();
                }else {
                    quotesModel = new QuotesModel();
                    quotesModel.setFavoritequote(quotetextView.getText().toString());
                    helper.insertFavoriteQuote(quotesModel);
                    Toast.makeText(MainActivity.this, "Quote added to Favorite List", Toast.LENGTH_SHORT).show();
                }



            }
        });
        imgbtnFavView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this , FavoriteQuotesActivity.class);
                startActivity(intent);
            }
        });
    }

    private String getRandomQuote() {
        Random random = new Random();
        int randomNumber = random.nextInt(sizeOfTable);
        if(randomNumber == 0){
            randomNumber = 1;
        }
        //load data from database
        helper = new MyDBHelper(this);
        db = helper.getWritableDatabase();
        String qry = "SELECT * FROM quotes WHERE quote_id = "+randomNumber;
        Cursor cursor = db.rawQuery(qry ,null );
        cursor.moveToFirst();
        return cursor.getString(1);
    }

    private void getSizeOfQuoteTableFromDB() {

         helper = new MyDBHelper(this);
        //load data from database
         db = helper.getWritableDatabase();
        String sizeqry = "SELECT * FROM quotes ";
        Cursor cursor = db.rawQuery(sizeqry , null);
        sizeOfTable = cursor.getCount();
        //this is run only run once at first time app install or size of table 0
        // then insert data into db
        if(sizeOfTable == 0){
            helper.insertQuote();
        }
        //this is run only run once at first time app install or size of table 0
        // and recursively call current method for calculating size of db table
        if(sizeOfTable == 0){
            getSizeOfQuoteTableFromDB();
        }
    }

    public void sharequotes(String quote) {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        //set data type
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_SUBJECT , "quote");
        //take text from textview and put into putextra to share text
        shareIntent.putExtra(Intent.EXTRA_TEXT ,quote );
        startActivity(Intent.createChooser(shareIntent, "Share Via"));
    }
}