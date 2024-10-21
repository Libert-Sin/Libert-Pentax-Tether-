// Copyright (c) 2017 Ricoh Co., Ltd. All Rights Reserved.
package com.ricoh.camera.sdk.wireless.sample;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.ricoh.camera.sdk.wireless.api.CameraImage;
import com.ricoh.camera.sdk.wireless.sample.ImageListFragment.OnImageListFragmentInteractionListener;

import java.util.List;

public class ImageListRecyclerViewAdapter extends
        RecyclerView.Adapter<ImageListRecyclerViewAdapter.ViewHolder> {

    private final List<CameraImage> mValues;
    private final OnImageListFragmentInteractionListener mListener;

    public ImageListRecyclerViewAdapter(List<CameraImage> items,
            OnImageListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.listitem_images, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.mIdView.setText( holder.mItem.getName());

        holder.mShowInfoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onImageListFragmentInteraction(holder.mItem,
                    OnImageListFragmentInteractionListener.OPTION.SHOW_INFO);
            }
        });

        holder.mShowImgButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onImageListFragmentInteraction(holder.mItem,
                    OnImageListFragmentInteractionListener.OPTION.SHOW_DATA);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public final View mView;
        public final TextView mIdView;
        public final Button mShowInfoButton;
        public final Button mShowImgButton;
        public CameraImage mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mIdView = (TextView) view.findViewById(R.id.text_view_name);
            mShowInfoButton = (Button) view.findViewById(R.id.button_info_show);
            mShowImgButton = (Button) view.findViewById(R.id.button_image_show);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mIdView.getText() + "'";
        }

        @Override
        public void onClick(View v) {

        }
    }
}
