package com.example.stockproject.ui.dashboard

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.ViewGroup
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.LinearLayout.*
import androidx.core.view.marginStart
import androidx.fragment.app.DialogFragment
import com.example.stockproject.R

class AddStockDialogFragment : DialogFragment() {

    private lateinit var input: EditText
    private var onClickListener: DialogInterface.OnClickListener? = null
    fun setPositiveCallback(cb: (String) -> Unit) {
        onClickListener = DialogInterface.OnClickListener { dialog, which ->
            cb(input.text.toString().toUpperCase()) }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val layout = LinearLayout(context).apply {
                layoutParams = LayoutParams(
                        LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT
                )
                orientation = VERTICAL
                setPadding(32,0,32,0)
                input = EditText(context)
                addView(input)
            }
            val builder = AlertDialog.Builder(it)
                    .setMessage("Add ticker name")
                    .setView(layout)
                    .setPositiveButton("OK", onClickListener)
                    .setNegativeButton("Cancel", null)
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }
}