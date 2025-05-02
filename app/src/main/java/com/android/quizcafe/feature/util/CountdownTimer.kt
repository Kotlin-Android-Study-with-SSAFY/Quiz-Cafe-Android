package com.android.quizcafe.feature.util

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

internal class CountdownTimer(
    private val coroutineScope: CoroutineScope,
    private val seconds: Int = 180,
    private val onTick: (Int) -> Unit,
    private val onFinish: () -> Unit = {}
) {
    private var job: Job? = null

    fun start() {
        job?.cancel()
        job = coroutineScope.launch {
            (seconds downTo 0).forEach { remaining ->
                onTick(remaining)
                delay(1000L)
            }
            onFinish()
        }
    }

    fun cancel() = job?.cancel()
}
