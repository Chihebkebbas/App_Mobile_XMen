package fr.ceri.xmen

import android.content.Context
import android.util.Log
import android.widget.Toast

class Display {
    companion object {
        fun showLog(tag: String, message: String) {
            Log.i(tag, message)
        }

        fun showToast(context: Context, message: String) {
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        }
    }
}