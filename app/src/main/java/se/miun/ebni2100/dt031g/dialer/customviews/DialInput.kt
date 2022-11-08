package se.miun.ebni2100.dt031g.dialer.customviews

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import se.miun.ebni2100.dt031g.dialer.R
import se.miun.ebni2100.dt031g.dialer.databinding.DialinputBinding

/**
 * Custom-view class representing the numbers clicked.
 * @author Ebba Nimér
 */
class DialInput @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet,
    defStyle: Int = 0
) : ConstraintLayout(context, attrs, defStyle){

    var binding : DialinputBinding

    /**
     * Initialize binding.
     */
    init {
        binding = DialinputBinding.bind(
            View.inflate(context, R.layout.dialinput, this)
        )

        clickEvents();
    }

    /**
     * Add title to text-field.
     */
    fun addNum(dialpadButton: DialpadButton){
        binding.dialText.append(dialpadButton.getTitle())
    }

    private fun clickEvents(){

        binding.imgDelete.setOnClickListener {
            println("img delete was clicked")
            val s = binding.dialText.text
            val newS = s.dropLast(1)
            binding.dialText.text = newS
        }

        binding.imgPhone.setOnClickListener {
            println("img phone was clicked")
            val s = binding.dialText.text
            val newS = s.dropLast(1)
            binding.dialText.text = "phone"
        }
    }
}