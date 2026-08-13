package com.example.sound

import android.media.AudioAttributes
import android.media.AudioFormat
import android.media.AudioTrack
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.math.sin

object SoundEngine {
    private val scope = CoroutineScope(Dispatchers.Default)

    fun playCoinDrop() {
        scope.launch {
            try {
                val sampleRate = 44100
                val durationSec = 0.35
                val numSamples = (sampleRate * durationSec).toInt()
                val buffer = ShortArray(numSamples)

                for (i in 0 until numSamples) {
                    val t = i.toDouble() / sampleRate
                    
                    // First metallic ping at t=0, second at t=0.12
                    val amp1 = kotlin.math.max(0.0, 1.0 - t * 6.0)
                    val freq1 = 2600.0
                    val wave1 = sin(2.0 * Math.PI * freq1 * t) + 0.5 * sin(2.0 * Math.PI * (freq1 * 1.5) * t)

                    val t2 = t - 0.12
                    val amp2 = if (t2 > 0) kotlin.math.max(0.0, 1.0 - t2 * 7.0) else 0.0
                    val freq2 = 3200.0
                    val wave2 = sin(2.0 * Math.PI * freq2 * t) + 0.5 * sin(2.0 * Math.PI * (freq2 * 1.4) * t)

                    val mixed = (wave1 * amp1 + wave2 * amp2) * 0.4
                    buffer[i] = (mixed * Short.MAX_VALUE).toInt().coerceIn(Short.MIN_VALUE.toInt(), Short.MAX_VALUE.toInt()).toShort()
                }

                playBuffer(buffer, sampleRate)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun playCashPing() {
        scope.launch {
            try {
                val sampleRate = 44100
                val durationSec = 0.45
                val numSamples = (sampleRate * durationSec).toInt()
                val buffer = ShortArray(numSamples)

                // High-pitched bright financial chime: C6 (1046.5Hz), E6 (1318.5Hz), G6 (1568Hz)
                val freqs = listOf(1046.5, 1318.5, 1568.0, 2093.0)
                val delays = listOf(0.0, 0.08, 0.16, 0.24)

                for (i in 0 until numSamples) {
                    val t = i.toDouble() / sampleRate
                    var sampleVal = 0.0

                    for (idx in freqs.indices) {
                        val dt = t - delays[idx]
                        if (dt > 0) {
                            val env = kotlin.math.max(0.0, 1.0 - dt * 4.5)
                            sampleVal += sin(2.0 * Math.PI * freqs[idx] * dt) * env * 0.25
                        }
                    }

                    buffer[i] = (sampleVal * Short.MAX_VALUE).toInt().coerceIn(Short.MIN_VALUE.toInt(), Short.MAX_VALUE.toInt()).toShort()
                }

                playBuffer(buffer, sampleRate)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun playBuffer(buffer: ShortArray, sampleRate: Int) {
        val minBufferSize = AudioTrack.getMinBufferSize(
            sampleRate,
            AudioFormat.CHANNEL_OUT_MONO,
            AudioFormat.ENCODING_PCM_16BIT
        )

        val audioTrack = AudioTrack.Builder()
            .setAudioAttributes(
                AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_MEDIA)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .build()
            )
            .setAudioFormat(
                AudioFormat.Builder()
                    .setEncoding(AudioFormat.ENCODING_PCM_16BIT)
                    .setSampleRate(sampleRate)
                    .setChannelMask(AudioFormat.CHANNEL_OUT_MONO)
                    .build()
            )
            .setBufferSizeInBytes(kotlin.math.max(minBufferSize, buffer.size * 2))
            .setTransferMode(AudioTrack.MODE_STATIC)
            .build()

        audioTrack.write(buffer, 0, buffer.size)
        audioTrack.play()
        
        // Clean up after playback
        scope.launch {
            kotlinx.coroutines.delay(600)
            audioTrack.stop()
            audioTrack.release()
        }
    }
}
