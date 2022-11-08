package se.miun.ebni2100.dt031g.dialer

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.AssetManager
import android.media.AudioAttributes
import android.media.AudioManager
import android.media.SoundPool
import android.os.Build


/**
 * Singleton class representing a sound-pool.
 * @author Ebba Nimér
 */
class SoundPlayer private constructor(private val context: Context) {

    private enum class Dials {
        ZERO, ONE, TWO, THREE, FOUR, FIVE, SIX, SEVEN, EIGHT, NINE, POUND, STAR
    }

    private var soundPool: SoundPool? = null
    private lateinit var soundIds: MutableMap<Dials, Int>

    /**
     * Upon initialization, create sound-pool and add sounds.
     */
    init {
        createSoundPool()
        createSounds()
    }

    /**
     * Singleton initialization.
     */
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

    /**
     * Create sound-pool.
     */
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

    /**
     * Add sounds to hashmap
     */
    private fun createSounds() {
        soundIds = mutableMapOf()

        val songFile0 = Util.getDirForVoice(context, "zero.mp3")
        val songFile1 = Util.getDirForVoice(context, "one.mp3")
        val songFile2 = Util.getDirForVoice(context, "two.mp3")
        val songFile3 = Util.getDirForVoice(context, "three.mp3")
        val songFile4 = Util.getDirForVoice(context, "four.mp3")
        val songFile5 = Util.getDirForVoice(context, "five.mp3")
        val songFile6 = Util.getDirForVoice(context, "six.mp3")
        val songFile7 = Util.getDirForVoice(context, "seven.mp3")
        val songFile8 = Util.getDirForVoice(context, "eight.mp3")
        val songFile9 = Util.getDirForVoice(context, "nine.mp3")
        val songFilePound = Util.getDirForVoice(context, "pound.mp3")
        val songFileStar = Util.getDirForVoice(context, "star.mp3")

        soundIds[Dials.ZERO] = soundPool?.load(songFile0.absolutePath, 1) ?: 1
        soundIds[Dials.ONE] = soundPool?.load(songFile1.absolutePath, 1) ?: 1
        soundIds[Dials.TWO] = soundPool?.load(songFile2.absolutePath, 1) ?: 1
        soundIds[Dials.THREE] = soundPool?.load(songFile3.absolutePath, 1) ?: 1
        soundIds[Dials.FOUR] = soundPool?.load(songFile4.absolutePath, 1) ?: 1
        soundIds[Dials.FIVE] = soundPool?.load(songFile5.absolutePath, 1) ?: 1
        soundIds[Dials.SIX] = soundPool?.load(songFile6.absolutePath, 1) ?: 1
        soundIds[Dials.SEVEN] = soundPool?.load(songFile7.absolutePath, 1) ?: 1
        soundIds[Dials.EIGHT] = soundPool?.load(songFile8.absolutePath, 1) ?: 1
        soundIds[Dials.NINE] = soundPool?.load(songFile9.absolutePath, 1) ?: 1
        soundIds[Dials.POUND] = soundPool?.load(songFilePound.absolutePath, 1) ?: 1
        soundIds[Dials.STAR] = soundPool?.load(songFileStar.absolutePath, 1) ?: 1

        //soundIds[Dials.ZERO] = soundPool?.load(context, songFile, 1) ?: 1

        /*
        soundIds[Dials.ZERO] = soundPool?.load(context, R.raw.zero, 1) ?: 1
        soundIds[Dials.ONE] = soundPool?.load(context, R.raw.one, 1) ?: 1
        soundIds[Dials.TWO] = soundPool?.load(context, R.raw.two, 1) ?: 1
        soundIds[Dials.THREE] = soundPool?.load(context, R.raw.three, 1) ?: 1
        soundIds[Dials.FOUR] = soundPool?.load(context, R.raw.four, 1) ?: 1
        soundIds[Dials.FIVE] = soundPool?.load(context, R.raw.five, 1) ?: 1
        soundIds[Dials.SIX] = soundPool?.load(context, R.raw.six, 1) ?: 1
        soundIds[Dials.SEVEN] = soundPool?.load(context, R.raw.seven, 1) ?: 1
        soundIds[Dials.EIGHT] = soundPool?.load(context, R.raw.eight, 1) ?: 1
        soundIds[Dials.NINE] = soundPool?.load(context, R.raw.nine, 1) ?: 1
        soundIds[Dials.POUND] = soundPool?.load(context, R.raw.pound, 1) ?: 1
        soundIds[Dials.STAR] = soundPool?.load(context, R.raw.star, 1) ?: 1*/
    }

    /**
     * Play corresponding sound to provided dialpad-button.
     */
    fun playSound(dialpadButton: DialpadButton) {

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
            "#" -> soundIds[Dials.POUND]?.let { soundPool?.play(it, 1f, 1f, 1, 0, 1f) ?: 1 }
            "*" -> soundIds[Dials.STAR]?.let { soundPool?.play(it, 1f, 1f, 1, 0, 1f) ?: 1 }
            else -> {
                return
            }
        }
    }

    /**
     * Destroy singleton class
     */
    fun destroy() {
        if (soundPool != null){
            soundPool?.release()
            soundPool = null
            theInstance = null
        }
    }
}