package com.example.fourscreen.fragments.Parsing.ui;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.fourscreen.R;
import com.example.fourscreen.fragments.Parsing.ApiUtils;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class ParsingFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private ProgressBar mProgressBar;
    private ParsingAdapter mParsingAdapter;
    private Disposable mDisposable;

    public ParsingFragment() {
    }

    public static ParsingFragment newInstance() {
        return new ParsingFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_parsing, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        mRecyclerView = view.findViewById(R.id.recycler_parsing);
        mProgressBar = view.findViewById(R.id.parsing_progress);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mParsingAdapter = new ParsingAdapter();
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setAdapter(mParsingAdapter);
        getQuotes();
    }

    @Override
    public void onDetach() {
        if (mDisposable != null) {
            mDisposable.dispose();
        }
        super.onDetach();
    }

    private void getQuotes() {
        mDisposable = ApiUtils.getApiService().getQuotes()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(disposable -> mProgressBar.setVisibility(View.VISIBLE))
                .doFinally(() -> mProgressBar.setVisibility(View.GONE))
                .subscribe(
                        response -> {
                            mProgressBar.setVisibility(View.GONE);
                            mParsingAdapter.addData(response.getQuotes());
                        },
                        throwable -> Toast.makeText(getActivity(), getString(R.string.error), Toast.LENGTH_SHORT).show());
    }
}