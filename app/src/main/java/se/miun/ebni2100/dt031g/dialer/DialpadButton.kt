package se.miun.ebni2100.dt031g.dialer

import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.constraintlayout.widget.ConstraintLayout
import se.miun.ebni2100.dt031g.dialer.databinding.DialpadlayoutBinding

class DialpadButton @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet,
    defStyle: Int = 0
) : ConstraintLayout(context, attrs, defStyle) {

    private var binding : DialpadlayoutBinding



    init {
        binding = DialpadlayoutBinding.bind(
            View.inflate(context, R.layout.dialpadlayout,this)
        )

        attrs.let { attributeSet ->
            val attributes = context.obtainStyledAttributes(attributeSet, R.styleable.DialpadButton, 0, 0)
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

    private fun setClickEvents(){
        val scaleUp = AnimationUtils.loadAnimation(context, R.anim.scale_up)
        val scaleDown = AnimationUtils.loadAnimation(context, R.anim.scale_down)

        binding.root.setOnClickListener {
            binding.root.startAnimation(scaleUp)
        }

    }

    private fun setTitle(title: String){
        binding.btnTitle.text = title[0].toString()
    }

    private fun setMessage(message: String){
        binding.btnMessage.text = message.take(4)
    }
}