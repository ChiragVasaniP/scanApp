package com.sccaningduniya.scan.adapters;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.sccaningduniya.scan.R;
import com.github.chrisbanes.photoview.PhotoView;

import java.io.File;

import kotlin.jvm.internal.Intrinsics;



public final class ImageViewHolder extends RecyclerView.ViewHolder {
    private final PhotoView photoImageView;
    private final TextView saveButton;

    
    public ImageViewHolder(View itemView) {
        super(itemView);
        Intrinsics.checkNotNullParameter(itemView, "itemView");
        this.photoImageView = (PhotoView) itemView.findViewById(R.id.photoImageView);
        this.saveButton = (TextView) itemView.findViewById(R.id.saveButton);
    }

    
    
    public static final void m118bindData$lambda0(ImageAdapterListener listener, File image, View it) {
        Intrinsics.checkNotNullParameter(listener, "$listener");
        Intrinsics.checkNotNullParameter(image, "$image");
        listener.onSaveButtonClicked(image);
    }

    public final void bindData(Context context, final File image, final ImageAdapterListener listener, int position, int size) {
        Intrinsics.checkNotNullParameter(image, "image");
        Intrinsics.checkNotNullParameter(listener, "listener");
        PhotoView photoImageView = this.photoImageView;
        Intrinsics.checkNotNullExpressionValue(photoImageView, "photoImageView");
        ImageViewHolderKt.setImageFile(photoImageView, image);
        this.saveButton.setOnClickListener(new View.OnClickListener() { 
            @Override 
            public final void onClick(View view) {
                ImageViewHolder.m118bindData$lambda0(listener, image, view);
            }
        });
    }
}
