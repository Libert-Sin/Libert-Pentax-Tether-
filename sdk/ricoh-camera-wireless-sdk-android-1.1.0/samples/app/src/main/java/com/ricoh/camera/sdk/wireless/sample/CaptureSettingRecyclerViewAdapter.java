// Copyright (c) 2017 Ricoh Co., Ltd. All Rights Reserved.
package com.ricoh.camera.sdk.wireless.sample;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.ricoh.camera.sdk.wireless.api.setting.capture.CaptureSetting;

import java.util.List;

public class CaptureSettingRecyclerViewAdapter extends
        RecyclerView.Adapter<CaptureSettingRecyclerViewAdapter.ViewHolder> {

    private final List<CaptureSetting> mValues;
    private final CaptureSettingFragment.OnCaptureSettingFragmentInteractionListener mListener;

    public CaptureSettingRecyclerViewAdapter(List<CaptureSetting> items,
            CaptureSettingFragment.OnCaptureSettingFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.listitem_capture_setting, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        if (mValues.get(position).getName() != null) {
            holder.mIdView.setText(mValues.get(position).getName());
        } else {
            holder.mIdView.setText("");
        }
        if (mValues.get(position).getValue() != null) {
            holder.mContentView.setText(mValues.get(position).getValue().toString());
        } else {
            holder.mContentView.setText("");
        }

        holder.mSetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    mListener.onCaptureSettingFragmentInteraction(holder.mItem);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mIdView;
        public final TextView mContentView;
        public final Button mSetButton;
        public CaptureSetting mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mIdView = (TextView) view.findViewById(R.id.text_view_name);
            mContentView = (TextView) view.findViewById(R.id.text_view_val);
            mSetButton = (Button) view.findViewById(R.id.button_val_set);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }
}
