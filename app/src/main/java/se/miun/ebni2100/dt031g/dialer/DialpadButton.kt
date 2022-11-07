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
 * Class for custom view, extending ConstraintLayout.
 * @author Ebba Nimér
 */
class DialpadButton @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet,
    defStyle: Int = 0
) : ConstraintLayout(context, attrs, defStyle) {

    private var binding: DialpadlayoutBinding
    private var soundPlayer: SoundPlayer
    private val title: String

    /**
     * Initialize class by retrieving view, getting attributes, and setting animation.
     */
    init {
        binding = DialpadlayoutBinding.bind(
            View.inflate(context, R.layout.dialpadlayout, this)
        )

        soundPlayer = SoundPlayer.getInstance(context)

        // Retrieve custom attributes
        attrs.let { attributeSet ->
            val attributes = context.obtainStyledAttributes(
                attributeSet, R.styleable.DialpadButton,
                0, 0
            )
            try {
                title = attributes.getString(R.styleable.DialpadButton_title).toString()
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
    private fun setClickEvents() {
        println("Setting click events")
        val scaleUp = AnimationUtils.loadAnimation(context, R.anim.scale_up)
        val scaleDown = AnimationUtils.loadAnimation(context, R.anim.scale_down)

        binding.root.setOnTouchListener { v, event ->

            if (event?.action == MotionEvent.ACTION_DOWN){
                startAnimation(scaleDown)
                return@setOnTouchListener true
            }
            if (event?.action == MotionEvent.ACTION_UP){
                startAnimation(scaleUp)
                soundPlayer.playSound(this)
            }
            /*
            when (event?.action) {
                MotionEvent.ACTION_DOWN -> startAnimation(scaleDown) -> true
                MotionEvent.ACTION_UP -> soundPlayer.playSound(this) //startAnimation(scaleUp)
            }*/
            v?.onTouchEvent(event) ?: false

        }
    }


        /**
         * Set first character in title
         */
        private fun setTitle(newTitle: String) {
            binding.btnTitle.text = newTitle[0].toString()
        }

        fun getTitle(): String {
            return title
        }

        /**
         * Set the 4 first characters in message.
         */
        private fun setMessage(message: String) {
            binding.btnMessage.text = message.take(4)
        }

}