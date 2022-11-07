package se.miun.ebni2100.dt031g.dialer

import android.annotation.SuppressLint
import android.content.Context
import android.media.AudioAttributes
import android.media.AudioManager
import android.media.SoundPool
import android.os.Build


class SoundPlayer private constructor(private val context: Context) {

    private enum class Dials {
        ZERO, ONE, TWO, THREE, FOUR, FIVE, SIX, SEVEN, EIGHT, NINE, POUND, STAR
    }

    private var soundPool: SoundPool? = null
    private lateinit var soundIds: MutableMap<Dials, Int>

    init {
        createSoundPool()
        createSounds()
    }

    companion object {
        @SuppressLint("StaticFieldLeak")
        private var theInstance: SoundPlayer? = null

        fun getInstance(context: Context): SoundPlayer {
            if (theInstance == null) {
                theInstance = SoundPlayer(context)
            }
            return theInstance!!
        }
    }

    private fun createSoundPool() {

        soundPool = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            SoundPool.Builder()
                .setMaxStreams(3)
                .setAudioAttributes(
                    AudioAttributes.Builder()
                        .setUsage(AudioAttributes.USAGE_ASSISTANCE_SONIFICATION)
                        .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                        .build()
                )
                .build()
        } else {
            SoundPool(
                3,
                AudioManager.STREAM_MUSIC,
                0
            )
        }
    }

    private fun createSounds() {
        soundIds = mutableMapOf()
        soundIds[Dials.ZERO] = soundPool?.load(context, R.raw.zero, 1) ?: 1
        soundIds[Dials.ONE] = soundPool?.load(context, R.raw.one, 1) ?: 1
        soundIds[Dials.TWO] = soundPool?.load(context, R.raw.two, 1) ?: 1
        soundIds[Dials.THREE] = soundPool?.load(context, R.raw.three, 1) ?: 1
        soundIds[Dials.FOUR] = soundPool?.load(context, R.raw.four, 1) ?: 1
        soundIds[Dials.FIVE] = soundPool?.load(context, R.raw.five, 1) ?: 1
    }

    fun playSound(dialpadButton: DialpadButton) {
        println("tjenixen hej")
        when (dialpadButton.getTitle()) {
            "0" -> soundIds[Dials.ZERO]?.let { soundPool?.play(it, 1f, 1f, 1, 0, 1f) ?: 1 }
            "1" -> soundIds[Dials.ONE]?.let { soundPool?.play(it, 1f, 1f, 1, 0, 1f) ?: 1 }
            "2" -> soundIds[Dials.TWO]?.let { soundPool?.play(it, 1f, 1f, 1, 0, 1f) ?: 1 }
            "3" -> soundIds[Dials.THREE]?.let { soundPool?.play(it, 1f, 1f, 1, 0, 1f) ?: 1 }
            "4" -> soundIds[Dials.FOUR]?.let { soundPool?.play(it, 1f, 1f, 1, 0, 1f) ?: 1 }
            "5" -> soundIds[Dials.FIVE]?.let { soundPool?.play(it, 1f, 1f, 1, 0, 1f) ?: 1 }
            "6" -> soundIds[Dials.SIX]?.let { soundPool?.play(it, 1f, 1f, 1, 0, 1f) ?: 1 }
            "7" -> soundIds[Dials.SEVEN]?.let { soundPool?.play(it, 1f, 1f, 1, 0, 1f) ?: 1 }
            "8" -> soundIds[Dials.EIGHT]?.let { soundPool?.play(it, 1f, 1f, 1, 0, 1f) ?: 1 }
            "9" -> soundIds[Dials.NINE]?.let { soundPool?.play(it, 1f, 1f, 1, 0, 1f) ?: 1 }
            "pound" -> soundIds[Dials.POUND]?.let { soundPool?.play(it, 1f, 1f, 1, 0, 1f) ?: 1 }
            "star" -> soundIds[Dials.STAR]?.let { soundPool?.play(it, 1f, 1f, 1, 0, 1f) ?: 1 }
            else -> {
                return
            }
        }

    }

    fun destroy() {
        if (soundPool != null) {
            soundPool!!.release();
            soundPool = null;
        }
    }
}