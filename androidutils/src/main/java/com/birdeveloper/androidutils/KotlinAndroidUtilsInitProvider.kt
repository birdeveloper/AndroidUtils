package com.birdeveloper.androidutils

import android.content.pm.ApplicationInfo
import com.jakewharton.threetenabp.AndroidThreeTen
import com.birdeveloper.androidutils.appctx.appCtx

class KotlinAndroidUtilsInitProvider : InitProvider() {

    override fun onCreate(): Boolean {

        val isDebuggable = 0 != appCtx.applicationInfo.flags and ApplicationInfo.FLAG_DEBUGGABLE
        initTimber(isDebuggable)

        AndroidThreeTen.init(appCtx)

        return true
    }
}
