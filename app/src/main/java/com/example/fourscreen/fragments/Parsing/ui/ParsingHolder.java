package com.example.fourscreen.fragments.Parsing.ui;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.fourscreen.R;
import com.example.fourscreen.fragments.Parsing.model.QuoteResponse;

class ParsingHolder extends RecyclerView.ViewHolder {

    private TextView mTvId, mTvDescription, mTvTime, mTvRating;

    ParsingHolder(@NonNull View itemView) {
        super(itemView);
        mTvId = itemView.findViewById(R.id.tv_id);
        mTvDescription = itemView.findViewById(R.id.tv_desc);
        mTvTime = itemView.findViewById(R.id.tv_time);
        mTvRating = itemView.findViewById(R.id.tv_rating);
    }

    void bind(QuoteResponse.QuotesBean quote) {
        mTvId.setText(String.valueOf(quote.getId()));
        mTvDescription.setText(quote.getDescription());
        mTvTime.setText(quote.getTime());
        mTvRating.setText(String.valueOf(quote.getRating()));
    }
}