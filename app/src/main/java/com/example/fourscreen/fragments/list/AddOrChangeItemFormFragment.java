package com.example.fourscreen.fragments.list;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.fourscreen.R;

//Экран добавления и изменения элемента списка
public class AddOrChangeItemFormFragment extends Fragment implements IOnBackPressed {
    private static final String ARG_NAME = "name";

    private String mName;
    private EditText mEtName;

    public AddOrChangeItemFormFragment() {
    }

    public static AddOrChangeItemFormFragment newInstance(String name) {
        AddOrChangeItemFormFragment fragment = new AddOrChangeItemFormFragment();
        Bundle args = new Bundle();
        args.putString(ARG_NAME, name);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mName = getArguments().getString(ARG_NAME);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_add_or_change_item_form, container, false);
        mEtName = v.findViewById(R.id.et_name);
        mEtName.setText(mName);
        Button btnAddItem = v.findViewById(R.id.btn_done);
        Button btnCancel = v.findViewById(R.id.btn_cancel);
        btnAddItem.setOnClickListener(onDoneClickListener);
        btnCancel.setOnClickListener(onCancelClickListener);
        return v;
    }

    View.OnClickListener onDoneClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (mEtName.getText().length() == 0) {
                getFragmentManager().popBackStack();
            } else {
                mName = mEtName.getText().toString();
                ListFragment fragment = ListFragment.newInstance(mName);
                getFragmentManager().popBackStack();
                getFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).commit();
            }
        }
    };

    View.OnClickListener onCancelClickListener = v -> getFragmentManager().popBackStack();

    @Override
    public boolean onBackPressed() {
        return mEtName.getText().length() > 0;
    }
}