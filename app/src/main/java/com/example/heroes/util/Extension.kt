package com.example.heroes.util

import android.content.Context
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.heroes.R
import com.example.heroes.data.model.ImageVariant
import java.math.BigInteger
import java.security.MessageDigest

fun String.toMd5(): String {
    val md = MessageDigest.getInstance("MD5")
    return BigInteger(1, md.digest(toByteArray())).toString(16).padStart(32, '0')
}

fun prepareImageURL(path: String, variant: String, extension: String): String {
    return "${path}/${variant}.${extension}"
}

fun getProgressDrawable(context: Context): CircularProgressDrawable {
    return CircularProgressDrawable(context).apply {
        strokeWidth = 8f
        centerRadius = 20f
        setColorSchemeColors(ContextCompat.getColor(context, R.color.colorAccent))
        start()
    }
}

fun ImageView.loadImage(
    uri: String?,
    progressDrawable: CircularProgressDrawable,
    circle: Boolean = false
) {
    var options = RequestOptions()
        .placeholder(progressDrawable)

    if (circle) {
        options = options.circleCrop()
    }

    Glide.with(this.context)
        .setDefaultRequestOptions(options)
        .load(uri)
        .into(this)
}
