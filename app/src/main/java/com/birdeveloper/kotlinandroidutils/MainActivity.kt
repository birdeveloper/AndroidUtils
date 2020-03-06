package com.birdeveloper.kotlinandroidutils

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.birdeveloper.androidutils.d
import com.birdeveloper.androidutils.part
import com.birdeveloper.androidutils.pickImage

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        pickImage { imageFile, uri ->


        }

    }
}
