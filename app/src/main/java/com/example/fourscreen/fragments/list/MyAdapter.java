package com.example.fourscreen.fragments.list;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.fourscreen.R;
import com.example.fourscreen.fragments.list.data.Content;

import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyHolder> {

    private MyAdapter.OnItemClickListener mListener;
    private List<Content> mContentList;

    MyAdapter(OnItemClickListener listener, List<Content> contentList) {
        mListener = listener;
        mContentList = contentList;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View myView = inflater.inflate(R.layout.list_item, viewGroup, false);
        return new MyHolder(myView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder myHolder, int i) {
        myHolder.bind(mContentList.get(i));
        myHolder.setClickListener(mListener, i);
    }

    @Override
    public int getItemCount() {
        return mContentList != null ? mContentList.size() : 0;
    }

    void swapPicture(int i, boolean isChecked) {
        Content content = mContentList.get(i);
        content.setCheckboxState(isChecked);
        mContentList.set(i, content);
        notifyDataSetChanged();
    }

    void deleteItem(int i) {
        mContentList.remove(i);
        notifyDataSetChanged();
    }

    public interface OnItemClickListener {
        void onLongItemClick(int position, View v, String name);

        void onItemClick(int position, String name);

        void onCheckBoxClick(int i, boolean isChecked);
    }

    public class MyHolder extends RecyclerView.ViewHolder implements CompoundButton.OnCheckedChangeListener {

        private ImageView mImageView;
        private TextView mTvName;
        private CheckBox mCheckBox;
        private boolean onBind;

        MyHolder(View view) {
            super(view);
            mImageView = view.findViewById(R.id.iv_picture);
            mTvName = view.findViewById(R.id.tv_name);
            mCheckBox = view.findViewById(R.id.chb_select_item);
            mCheckBox.setOnCheckedChangeListener(this);
        }

        void bind(Content content) {
            if (content.isCheckboxState()) mImageView.setImageResource(content.getFirstPic());
            else mImageView.setImageResource(content.getSecondPic());
            mTvName.setText(content.getItemName());
            onBind = true;
            mCheckBox.setChecked(content.isCheckboxState());
            onBind = false;
        }

        void setClickListener(final MyAdapter.OnItemClickListener onItemClickListener, final int i) {
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    onItemClickListener.onLongItemClick(i, itemView, mTvName.getText().toString());
                    return true;
                }
            });
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListener.onItemClick(i, mTvName.getText().toString());
                }
            });
        }

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if (!onBind) {
                mListener.onCheckBoxClick(getAdapterPosition(), isChecked);
            }
        }
    }
}