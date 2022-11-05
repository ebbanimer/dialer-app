package se.miun.ebni2100.dt031g.dialer

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.constraintlayout.widget.ConstraintLayout
import se.miun.ebni2100.dt031g.dialer.databinding.DialpadlayoutBinding

/**
 * Class for custom view, extending ConstraintLayout
 * @author Ebba Nimér
 */
class DialpadButton @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet,
    defStyle: Int = 0
) : ConstraintLayout(context, attrs, defStyle) {

    private var binding : DialpadlayoutBinding

    /**
     * Initialize class by retrieving view, getting attributes, and setting animation.
     */
    init {
        binding = DialpadlayoutBinding.bind(
            View.inflate(context, R.layout.dialpadlayout,this)
        )

        // Retrieve custom attributes
        attrs.let { attributeSet ->
            val attributes = context.obtainStyledAttributes(attributeSet, R.styleable.DialpadButton,
                0, 0)
            try {
                val title = attributes.getString(R.styleable.DialpadButton_title).toString()
                val message = attributes.getString(R.styleable.DialpadButton_message).toString()

                setTitle(title)
                setMessage(message)

            } finally {
                attributes.recycle()
            }
        }

        setClickEvents();
    }

    /**
     * Initialize animation for click-events.
     */
    @SuppressLint("ClickableViewAccessibility")
    private fun setClickEvents(){
        val scaleUp = AnimationUtils.loadAnimation(context, R.anim.scale_up)
        val scaleDown = AnimationUtils.loadAnimation(context, R.anim.scale_down)

        binding.root.setOnTouchListener { v, event ->
            when (event?.action) {
                MotionEvent.ACTION_DOWN -> startAnimation(scaleDown)
                MotionEvent.ACTION_UP -> startAnimation(scaleUp)
            }
            v?.onTouchEvent(event) ?: true
        }

    }

    /**
     * Set first character in title
     */
    private fun setTitle(title: String){
        binding.btnTitle.text = title[0].toString()
    }

    /**
     * Set the 4 first characters in message.
     */
    private fun setMessage(message: String){
        binding.btnMessage.text = message.take(4)
    }
}