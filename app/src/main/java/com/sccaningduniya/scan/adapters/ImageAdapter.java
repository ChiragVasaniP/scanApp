package com.sccaningduniya.scan.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.sccaningduniya.scan.R;

import java.io.File;
import java.util.ArrayList;

import kotlin.jvm.internal.Intrinsics;

@SuppressWarnings("ALL")


public final class ImageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final Context context;
    private ArrayList<File> imageList;
    private ImageAdapterListener listener;

    public ImageAdapter(Context context, ArrayList<File> imageList, ImageAdapterListener listener) {
        Intrinsics.checkNotNullParameter(imageList, "imageList");
        Intrinsics.checkNotNullParameter(listener, "listener");
        this.context = context;
        this.imageList = imageList;
        this.listener = listener;
    }

    public final Context getContext() {
        return this.context;
    }

    public final ArrayList<File> getImageList() {
        return this.imageList;
    }

    public final void setImageList(ArrayList<File> arrayList) {
        Intrinsics.checkNotNullParameter(arrayList, "<set-?>");
        this.imageList = arrayList;
    }

    public final ImageAdapterListener getListener() {
        return this.listener;
    }

    public final void setListener(ImageAdapterListener imageAdapterListener) {
        Intrinsics.checkNotNullParameter(imageAdapterListener, "<set-?>");
        this.listener = imageAdapterListener;
    }

    @Override 
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Intrinsics.checkNotNullParameter(parent, "parent");
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View inflate = layoutInflater.inflate(R.layout.image_preview_adapter, parent, false);
        if (inflate == null) {
            throw new NullPointerException("null cannot be cast to non-null type android.view.ViewGroup");
        }
        ViewGroup view = (ViewGroup) inflate;
        return new ImageViewHolder(view);
    }

    @Override 
    public int getItemCount() {
        return this.imageList.size();
    }

    @Override 
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Intrinsics.checkNotNullParameter(holder, "holder");
        ImageViewHolder imageViewHolder = (ImageViewHolder) holder;
        Context context = this.context;
        File file = this.imageList.get(position);
        Intrinsics.checkNotNullExpressionValue(file, "imageList[position]");
        ((ImageViewHolder) holder).bindData(context, file, this.listener, position, this.imageList.size());
    }

    public final void updateList() {
        notifyDataSetChanged();
    }
}
