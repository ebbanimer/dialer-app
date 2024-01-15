package se.miun.ebni2100.dt031g.dialer.customviews

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.util.AttributeSet
import androidx.annotation.RequiresApi
import androidx.constraintlayout.widget.ConstraintLayout
import se.miun.ebni2100.dt031g.dialer.R
import se.miun.ebni2100.dt031g.dialer.databinding.ProgressbarLayoutBinding

/**
 * Progressbar to display download. Not used.
 */
@RequiresApi(Build.VERSION_CODES.O)
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
        binding.progressBar.min = 0
        binding.progressBar.max = 100
    }

    fun setTitle(fileName: String){
        binding.fileName.text = fileName
    }

    @SuppressLint("SetTextI18n")
    fun setProgress(percentage: Int){
        binding.progressBar.progress = percentage
        binding.progressText.text = "$percentage%"
    }
}
