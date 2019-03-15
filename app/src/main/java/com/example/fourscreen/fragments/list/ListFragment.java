package com.example.fourscreen.fragments.list;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;

import com.example.fourscreen.R;
import com.example.fourscreen.fragments.list.data.Content;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class ListFragment extends Fragment implements MyAdapter.OnItemClickListener {
    private static final String ARG_NAME = "name";
    private static final String CONTENT_TAG = "ContentList";
    private static final String PREFS_TAG = "MePreferences";

    MyAdapter.OnItemClickListener mListener;
    MyAdapter mAdapter;
    RecyclerView mRecyclerView;
    List<Content> items;

    Gson mGson = new Gson();
    static SharedPreferences mPreferences;
    static SharedPreferences.Editor mEditor;

    private String mCurName;

    FloatingActionButton mFab;
    private static int mPositionForChange = -1;

    public ListFragment() {
    }

    public static ListFragment newInstance(String name) {
        ListFragment fragment = new ListFragment();
        Bundle args = new Bundle();
        args.putString(ARG_NAME, name);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mCurName = getArguments().getString(ARG_NAME);
            setArguments(null);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list, container, false);
        mFab = view.findViewById(R.id.fab);
        mFab.setOnClickListener(onFabClickListener);
        mRecyclerView = view.findViewById(R.id.list);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mAdapter = new MyAdapter(this, getContentList());
        mRecyclerView.setAdapter(mAdapter);
        return view;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    private void setDataToSharedPreferences(List<Content> contentList) {
        String jsonCurProduct = mGson.toJson(contentList);
        mEditor = mPreferences.edit();
        mEditor.putString(CONTENT_TAG, jsonCurProduct);
        mEditor.apply();
    }

    private List<Content> getContentList() {
        mPreferences = getActivity().getSharedPreferences(PREFS_TAG, Context.MODE_PRIVATE);
        String json = mPreferences.getString(CONTENT_TAG, "");
        Type type = new TypeToken<List<Content>>() {
        }.getType();

        items = new ArrayList<>();
        if (json.equals("")) {
            items.add(new Content("Horse Boy"));
        } else items = mGson.fromJson(json, type);

        if (mCurName != "" && mCurName != null) {
            if (mPositionForChange == -1) {
                items.add(new Content(mCurName));
            } else {
                Content content = items.get(mPositionForChange);
                content.setItemName(mCurName);
                items.set(mPositionForChange, content);
                mPositionForChange = -1;
            }
        }
        //исправление чтобы не добавлялось 2 элемента
        mCurName = "";
        setDataToSharedPreferences(items);
        return items;
    }

    @Override
    public void onLongItemClick(final int position, View v, final String name) {
        PopupMenu popupMenu = new PopupMenu(getActivity(), v);
        popupMenu.inflate(R.menu.popupmenu);

        popupMenu.setOnMenuItemClickListener(item -> {
            switch (item.getItemId()) {
                case R.id.edit_item:
                    mPositionForChange = position;

                    AddOrChangeItemFormFragment fragment = AddOrChangeItemFormFragment.newInstance(name);

                    getFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment)
                            .addToBackStack(fragment.getClass().getSimpleName()).commit();
                    return true;

                case R.id.delete_item:
                    mAdapter.deleteItem(position);
                    setDataToSharedPreferences(items);
                    return true;

                default:
                    return false;
            }
        });
        popupMenu.show();
    }

    @Override
    public void onItemClick(int position, String name) {
        mPositionForChange = position;
        AddOrChangeItemFormFragment fragment = AddOrChangeItemFormFragment.newInstance(name);
        getFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment)
                .addToBackStack(fragment.getClass().getSimpleName()).commit();
    }

    @Override
    public void onCheckBoxClick(int i, boolean isChecked) {
        //Сохранение состояния чекбоксов
        Content content = items.get(i);
        content.setCheckboxState(isChecked);
        items.set(i, content);
        setDataToSharedPreferences(items);

        mAdapter.swapPicture(i, isChecked);
    }

    View.OnClickListener onFabClickListener = view -> {
        AddOrChangeItemFormFragment fragment = AddOrChangeItemFormFragment.newInstance("");
        getFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment)
                .addToBackStack(fragment.getClass().getSimpleName()).commit();
    };
}