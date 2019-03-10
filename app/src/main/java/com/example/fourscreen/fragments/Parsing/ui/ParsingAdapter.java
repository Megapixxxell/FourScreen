package com.example.fourscreen.fragments.Parsing.ui;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.fourscreen.R;
import com.example.fourscreen.fragments.Parsing.model.QuoteResponse;

import java.util.ArrayList;
import java.util.List;

public class ParsingAdapter extends RecyclerView.Adapter<ParsingHolder> {

    private final List<QuoteResponse.QuotesBean> mQuotes = new ArrayList<>();

    ParsingAdapter() {
    }

    @NonNull
    @Override
    public ParsingHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View view = inflater.inflate(R.layout.parsing_item, viewGroup, false);
        return new ParsingHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ParsingHolder parsingHolder, int i) {
        QuoteResponse.QuotesBean quote = mQuotes.get(i);
        parsingHolder.bind(quote);
    }

    @Override
    public int getItemCount() {
        return mQuotes.size();
    }

    void addData(List<QuoteResponse.QuotesBean> data) {
        mQuotes.addAll(data);
        notifyDataSetChanged();
    }
}