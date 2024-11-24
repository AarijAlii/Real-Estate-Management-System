package com.example.realestatemanagementsystem.util

import android.content.Context
import coil.ImageLoader
import coil.disk.DiskCache
import coil.request.CachePolicy


fun createCustomImageLoader(context: Context): ImageLoader {
    return ImageLoader.Builder(context)
        .diskCache(
            DiskCache.Builder()
                .directory(context.cacheDir.resolve("image_cache")) // Disk cache directory
                .maxSizeBytes(500L * 1024 * 1024) // Set max size to 50MB
                .build()
        )
        .memoryCachePolicy(CachePolicy.ENABLED) // Enable in-memory cache
        .diskCachePolicy(CachePolicy.ENABLED) // Enable disk cache
        .build()
}