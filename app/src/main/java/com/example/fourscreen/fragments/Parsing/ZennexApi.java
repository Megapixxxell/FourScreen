package com.example.fourscreen.fragments.Parsing;

import com.example.fourscreen.fragments.Parsing.model.QuoteResponse;

import io.reactivex.Single;
import retrofit2.http.GET;

public interface ZennexApi {

    @GET("quotes?sort=time")
    Single<QuoteResponse> getQuotes();

}
