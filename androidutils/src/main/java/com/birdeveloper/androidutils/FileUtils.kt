package com.birdeveloper.androidutils

import android.net.Uri
import java.io.File

fun File.toUri(): Uri = Uri.fromFile(this)
