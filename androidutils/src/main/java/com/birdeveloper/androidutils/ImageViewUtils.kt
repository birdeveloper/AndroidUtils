package com.birdeveloper.androidutils

import android.widget.ImageView
import java.io.File

fun ImageView.setImageFile(imageFile: File) {
    setImageURI(imageFile.toUri())
}