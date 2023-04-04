package com.andi.storyapp.ui.customui

import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.util.Patterns
import android.view.View
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.ContextCompat
import com.andi.storyapp.R

class EmailEditText:AppCompatEditText {
    private var showError = true // status error

    private lateinit var borderRed: Drawable
    private lateinit var bordergrey: Drawable

    constructor(context: Context) : super(context) {
        init()
    }
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init()
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        hint = "Masukan email anda"
        textAlignment = View.TEXT_ALIGNMENT_VIEW_START
    }

    private fun init(){
        setupBorder()
        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                background = bordergrey
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable) {
                if (Patterns.EMAIL_ADDRESS.matcher(s).matches()) {
                    showError = false
                    background = bordergrey
                    setError(null)
                } else {
                    showError = true
                    background = borderRed
                    setError("Inputan harus email")

                }
            }
        })
    }

    private fun setupBorder(){
        borderRed =  ContextCompat.getDrawable(context, R.drawable.stroke_round5_red) as Drawable
        bordergrey =  ContextCompat.getDrawable(context, R.drawable.stroke_round5_grey) as Drawable
    }
    private fun setError(error: String?) {
        this.error = error
    }

}