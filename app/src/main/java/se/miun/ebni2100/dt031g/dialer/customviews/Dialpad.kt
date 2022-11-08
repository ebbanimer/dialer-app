package se.miun.ebni2100.dt031g.dialer.customviews

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import se.miun.ebni2100.dt031g.dialer.R
import se.miun.ebni2100.dt031g.dialer.databinding.DialpadBinding

class Dialpad @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet,
    defStyle: Int = 0
) : ConstraintLayout(context, attrs, defStyle) {

    private var binding : DialpadBinding

    init {
        binding = DialpadBinding.bind(
            View.inflate(context, R.layout.dialpad, this)
        )
    }

    fun setListener(listener: DialpadButton.OnClickListener){
        listPads().forEach {
            it.setListener(listener)
        }
    }


    private fun listPads() = mutableListOf<DialpadButton>().apply{
        add(binding.dialpadButton0)
        add(binding.dialpadButton1)
        add(binding.dialpadButton2)
        add(binding.dialpadButton3)
        add(binding.dialpadButton4)
        add(binding.dialpadButton5)
        add(binding.dialpadButton6)
        add(binding.dialpadButton7)
        add(binding.dialpadButton8)
        add(binding.dialpadButton9)
        add(binding.dialpadButtonPound)
        add(binding.dialpadButtonStar)
    }
    
}