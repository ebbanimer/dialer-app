package se.miun.ebni2100.dt031g.dialer.customviews

import android.content.Context
import android.util.AttributeSet
import androidx.constraintlayout.widget.ConstraintLayout
import se.miun.ebni2100.dt031g.dialer.R
import se.miun.ebni2100.dt031g.dialer.databinding.ProgressbarLayoutBinding

class ProgressbarDownload @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet,
    defStyle: Int = 0
) : ConstraintLayout(context, attrs, defStyle) {

    private var binding: ProgressbarLayoutBinding

    init {
        binding = ProgressbarLayoutBinding.bind(
            inflate(context, R.layout.progressbar_layout, this)
        )
    }

    fun setTitle(fileName: String){
        binding.fileName.text = fileName
    }

    fun setProgress(percentage: Int){
        binding.progressBar.progress = percentage
        binding.progressText.text = percentage.toString()
    }
}
