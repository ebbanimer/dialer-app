package se.miun.ebni2100.dt031g.dialer

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import se.miun.ebni2100.dt031g.dialer.databinding.DialpadlayoutBinding

class DialpadButton @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet,
    defStyle: Int = 0
) : ConstraintLayout(context, attributeSet, defStyle) {

    private var binding : DialpadlayoutBinding

    init {
        binding = DialpadlayoutBinding.bind(
            View.inflate(context, R.layout.dialpadlayout,this)
        )
    }

    fun setTitle(title: String){
        binding.btnTitle.text = title[0].toString()
    }

    fun setMessage(message: String){
        binding.btnMessage.text = message.take(4)
    }
}