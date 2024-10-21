// Copyright (c) 2017 Ricoh Co., Ltd. All Rights Reserved.
package com.ricoh.camera.sdk.wireless.sample;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.ricoh.camera.sdk.wireless.api.setting.camera.CameraDeviceSetting;
import com.ricoh.camera.sdk.wireless.api.setting.camera.CameraTime;
import com.ricoh.camera.sdk.wireless.api.setting.camera.Channel;
import com.ricoh.camera.sdk.wireless.api.setting.camera.Key;
import com.ricoh.camera.sdk.wireless.api.setting.camera.SSID;
import com.ricoh.camera.sdk.wireless.sample.CameraSettingFragment.OnCameraSettingFragmentInteractionListener;

import java.util.List;


public class CameraSettingRecyclerViewAdapter extends
        RecyclerView.Adapter<CameraSettingRecyclerViewAdapter.ViewHolder> {

    private final List<CameraDeviceSetting> mValues;
    private final OnCameraSettingFragmentInteractionListener mListener;

    public CameraSettingRecyclerViewAdapter(List<CameraDeviceSetting> items,
            OnCameraSettingFragmentInteractionListener listener) {
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
                    CameraDeviceSetting setting = holder.mItem;
                    if (setting instanceof CameraTime) {
                        mListener.onCameraSettingFragmentInteraction(setting,
                            OnCameraSettingFragmentInteractionListener.OPTION.DATETIME);
                    } else if (setting instanceof Channel||setting instanceof Key||setting instanceof SSID) {
                        mListener.onCameraSettingFragmentInteraction(setting,
                            OnCameraSettingFragmentInteractionListener.OPTION.WIFI);
                    } else {
                        mListener.onCameraSettingFragmentInteraction(setting,
                            OnCameraSettingFragmentInteractionListener.OPTION.AVAILABLE);
                    }
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
        public CameraDeviceSetting mItem;

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
