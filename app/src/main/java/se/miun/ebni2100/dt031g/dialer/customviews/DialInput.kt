package se.miun.ebni2100.dt031g.dialer.customviews

import android.content.Context
import android.content.Intent
import android.net.Uri
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

    /**
     * Add click-events for buttons.
     */
    private fun clickEvents(){

        // Get current string, pop last character, and update view.
        binding.imgDelete.setOnClickListener {
            val s = binding.dialText.text
            val newS = s.dropLast(1)
            binding.dialText.text = newS
        }

        // Create new dial intent.
        binding.imgPhone.setOnClickListener {
            val intent = Intent(Intent.ACTION_DIAL)
            intent.data = Uri.parse("tel:" + binding.dialText.text)
            context.startActivity(intent)

        }
    }
}