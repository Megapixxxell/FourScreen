package com.example.fourscreen.fragments.Parsing.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class QuoteResponse {

    @SerializedName("total")
    private int total;
    @SerializedName("last")
    private String last;
    @SerializedName("quotes")
    private List<QuotesBean> quotes;

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public String getLast() {
        return last;
    }

    public void setLast(String last) {
        this.last = last;
    }

    public List<QuotesBean> getQuotes() {
        return quotes;
    }

    public void setQuotes(List<QuotesBean> quotes) {
        this.quotes = quotes;
    }

    public static class QuotesBean {

        @SerializedName("id")
        private int id;
        @SerializedName("description")
        private String description;
        @SerializedName("time")
        private String time;
        @SerializedName("rating")
        private int rating;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public int getRating() {
            return rating;
        }

        public void setRating(int rating) {
            this.rating = rating;
        }
    }
}
