package com.birdeveloper.androidutils

import android.annotation.SuppressLint
import android.app.Activity
import android.content.ContentUris
import android.content.Context
import android.content.ContextWrapper
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.os.SystemClock
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.util.DisplayMetrics
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream
import java.net.URISyntaxException
import java.util.*
import java.util.regex.Matcher
import java.util.regex.Pattern

/**
 * User: YourPc
 * Date: 9/15/2017
 */
inline fun consume(f: () -> Unit): Boolean {
    f()
    return true
}

fun Context.dpToPx(dp: Int) = Math.round(dp * (resources.displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT))
fun Fragment.dpToPx(dp: Int) = context?.dpToPx(dp)
fun View.dpToPx(dp: Float): Int = context.dpToPx(dp)
fun Context.dpToPx(dp: Float): Int = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, resources.displayMetrics).toInt()

fun View.visible() {
    visibility = View.VISIBLE
}

fun View.gone() {
    visibility = View.GONE
}

fun View.invisible() {
    visibility = View.INVISIBLE
}
fun View.visibleAlpha() {
    visibility=View.VISIBLE
    animate().alpha(1f).duration = 300
}

fun View.goneAlpha() {
    visibility = View.GONE
    animate().alpha(0f).duration = 300
}
fun View.invisibleAlpha(){
    visibility = View.INVISIBLE
    animate().alpha(0f).duration = 300
}
fun View.scaleAnim() {
    animate().scaleX(1.1f).start()
    animate().withEndAction {
        animate().scaleX(1f).duration = 200
        animate().scaleY(1f).duration = 200
    }.scaleY(1.1f).start()
}
fun View.scaleAnimSmall() {
    animate().scaleX(0.8f).duration = 300
    animate().scaleY(0.8f).duration = 300
}

fun View.scaleAnimBig() {
    animate().scaleX(1.3f).start()
    animate().withEndAction {
        animate().scaleX(1f)
        animate().scaleY(1f)
    }.scaleY(1.3f).withEndAction {
        animate().scaleX(0.8f).start()
        animate().withEndAction {
            animate().scaleX(1f)
            animate().scaleY(1f)
        }.scaleY(0.8f).start()
    }.start()
}
fun View.scaleAnimStandart() {
    animate().scaleX(1f).duration = 300
    animate().scaleY(1f).duration = 300
}

fun View.showKeyboard() {
    requestFocus()
    val imm =
        this.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
    imm!!.showSoftInput(this, InputMethodManager.SHOW_IMPLICIT)
}

fun getScreenShot(view: View): Bitmap {
    val returnedBitmap = Bitmap.createBitmap(view.width, view.height, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(returnedBitmap)
    val bgDrawable = view.background
    if (bgDrawable != null) bgDrawable.draw(canvas)
    else canvas.drawColor(Color.WHITE)
    view.draw(canvas)
    return returnedBitmap
}
class SafeClickListener(
    private var defaultInterval: Int = 4000,
    private val onSafeCLick: (View) -> Unit
) : View.OnClickListener {
    private var lastTimeClicked: Long = 0
    override fun onClick(v: View) {
        if (SystemClock.elapsedRealtime() - lastTimeClicked < defaultInterval) {
            return
        }
        lastTimeClicked = SystemClock.elapsedRealtime()
        onSafeCLick(v)
    }
}
fun View.dontDoubleClick(onSafeClick: (View) -> Unit) {
    val safeClickListener = SafeClickListener {
        onSafeClick(it)
    }
    setOnClickListener(safeClickListener)
}
fun bitmapToFile(bitmap: Bitmap, context: Context): File {
    val wrapper = ContextWrapper(context)

    var file = wrapper.getDir("Images", Context.MODE_PRIVATE)
    file = File(file, "${UUID.randomUUID()}.jpg")

    try {
        // Compress the bitmap and save in jpg format
        val stream: OutputStream = FileOutputStream(file)
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)
        stream.flush()
        stream.close()
    } catch (e: IOException) {
        e.printStackTrace()
    }

    // Return the saved bitmap uri
    // return Uri.parse(file.absolutePath)
    return file
}
@Suppress("DEPRECATION")
fun getShortLanguageCode(context: Context): String {
    val locale: Locale
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        locale = context.resources.configuration.locales.get(0)
    } else {
        locale = context.resources.configuration.locale
    }
    return locale.language
}
@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
fun isEmailValid(email: String?): Boolean {
    val expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$"
    val pattern: Pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE)
    val matcher: Matcher = pattern.matcher(email)
    return matcher.matches()
}
fun loadJSONFromAsset(activity: Activity, fileName: String): String {
    var json: String? = null
    try {
        val `is` = activity.assets.open("$fileName.json")
        val size = `is`.available()
        val buffer = ByteArray(size)
        `is`.read(buffer)
        `is`.close()
        json = String(buffer, Charsets.UTF_8)
    } catch (ex: IOException) {
        ex.printStackTrace()
        return json.toString()
    }

    return json
}
@Suppress("NAME_SHADOWING")
@SuppressLint("NewApi", "ObsoleteSdkInt", "Recycle")
@Throws(URISyntaxException::class)
fun getFilePath(context: Context, uri: Uri): String? {
    var uri = uri
    var selection: String? = null
    var selectionArgs: Array<String>? = null
    // Uri is different in versions after KITKAT (Android 4.4), we need to
    if (Build.VERSION.SDK_INT >= 19 && DocumentsContract.isDocumentUri(
            context.applicationContext,
            uri
        )
    ) {
        if (isExternalStorageDocument(uri)) {
            val docId = DocumentsContract.getDocumentId(uri)
            val split = docId.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            return Environment.getExternalStorageDirectory().toString() + "/" + split[1]
        } else if (isDownloadsDocument(uri)) {
            val id = DocumentsContract.getDocumentId(uri)
            if (id.startsWith("raw:")) {
                return id.replaceFirst("raw:".toRegex(), "")
            }
            uri = ContentUris.withAppendedId(
                Uri.parse("content://downloads/public_downloads"), java.lang.Long.valueOf(id)
            )
        } else if (isMediaDocument(uri)) {
            val docId = DocumentsContract.getDocumentId(uri)
            val split = docId.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            val type = split[0]
            if ("image" == type) {
                uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            } else if ("video" == type) {
                uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI
            } else if ("audio" == type) {
                uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
            }
            selection = "_id=?"
            selectionArgs = arrayOf(split[1])
        }
    }
    if ("content".equals(uri.scheme!!, ignoreCase = true)) {
        val projection = arrayOf("_data")
        var cursor: Cursor?
        try {
            cursor = context.contentResolver
                .query(uri, projection, selection, selectionArgs, null)
            val columnIndex = cursor!!.getColumnIndexOrThrow("_data")
            if (cursor.moveToFirst()) {
                return cursor.getString(columnIndex)
            }
        } catch (e: Exception) {
        }

    } else if ("file".equals(uri.scheme!!, ignoreCase = true)) {
        return uri.path
    }
    return null
}
fun isExternalStorageDocument(uri: Uri): Boolean {
    return "com.android.externalstorage.documents" == uri.authority
}

fun isDownloadsDocument(uri: Uri): Boolean {
    return "com.android.providers.downloads.documents" == uri.authority
}

fun isMediaDocument(uri: Uri): Boolean {
    return "com.android.providers.media.documents" == uri.authority
}
fun View.margin(left: Float? = null, top: Float? = null, right: Float? = null, bottom: Float? = null) {
    layoutParams<ViewGroup.MarginLayoutParams> {
        left?.run { leftMargin = dpToPx(this) }
        top?.run { topMargin = dpToPx(this) }
        right?.run { rightMargin = dpToPx(this) }
        bottom?.run { bottomMargin = dpToPx(this) }
    }
}
inline fun <reified T : ViewGroup.LayoutParams> View.layoutParams(block: T.() -> Unit) {
    if (layoutParams is T) block(layoutParams as T)
}
private val key = "Zx" + Math.log(2.0) / 3




