package com.example.randomquotes;

public class QuotesModel {
   private String quote;
   private String favoritequote;


    private int FavQuoteId ;


    public int getFavQuoteId() {
        return FavQuoteId;
    }
    public String getQuote() {
        return quote;
    }

    public String getFavoritequote() {
        return favoritequote;
    }

    public void setQuote(String quote) {
        this.quote = quote;
    }

    public void setFavoritequote(String favoritequote) {
        this.favoritequote = favoritequote;
    }

    public void setFavQuoteId(int favQuoteId) {
        FavQuoteId = favQuoteId;
    }
}
