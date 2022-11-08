package se.miun.ebni2100.dt031g.dialer.customviews

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.animation.AnimationUtils
import androidx.constraintlayout.widget.ConstraintLayout
import se.miun.ebni2100.dt031g.dialer.R
import se.miun.ebni2100.dt031g.dialer.support.SoundPlayer
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

    private lateinit var listener : OnClickListener

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

    interface OnClickListener{
        fun onClick(button: DialpadButton)
    }

    fun setListener(listener: OnClickListener){
        this.listener = listener
    }

    /**
     * Initialize animation for click-events.
     */
    @SuppressLint("ClickableViewAccessibility")
    private fun setClickEvents() {
        val scaleUp = AnimationUtils.loadAnimation(context, R.anim.scale_up)
        val scaleDown = AnimationUtils.loadAnimation(context, R.anim.scale_down)

        binding.root.setOnClickListener {
            startAnimation(scaleUp)
            startAnimation(scaleDown)
            listener.onClick(this@DialpadButton)
            soundPlayer.playSound(this)
        }
    }


    /**
     * Set first character in title.
     */
    private fun setTitle(newTitle: String) {
        binding.btnTitle.text = newTitle[0].toString()
    }

    /**
     * Return title to calling client.
     */
    fun getTitle(): String {
        return title
    }

    /**
     * Set the 4 first characters in message.
     */
    private fun setMessage(message: String) {
        binding.btnMessage.text = message.take(4)
    }

    private fun setDialInput(s : String){

    }
}