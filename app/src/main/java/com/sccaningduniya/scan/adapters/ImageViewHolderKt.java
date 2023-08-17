package com.sccaningduniya.scan.adapters;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.github.chrisbanes.photoview.PhotoView;

import java.io.File;



public final class ImageViewHolderKt {
    
    public static final void setImageFile(PhotoView $this$setImageFile, File image) {
        Glide.with($this$setImageFile).load(image).diskCacheStrategy(DiskCacheStrategy.NONE).skipMemoryCache(true).into($this$setImageFile);
    }
}
