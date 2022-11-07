package se.miun.ebni2100.dt031g.dialer

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import se.miun.ebni2100.dt031g.dialer.databinding.DialpadBinding
import se.miun.ebni2100.dt031g.dialer.databinding.DialpadlayoutBinding

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
    
}