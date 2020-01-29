package com.gdavidpb.test.utils

import kotlinx.coroutines.*

open class IdempotentLocker {
    private var job: Job = Job()

    fun executeLast(withIn: Long, task: () -> Unit) {
        job.cancel()

        job = CoroutineScope(Dispatchers.Main).launch {
            withContext(Dispatchers.IO) {
                delay(withIn)
            }

            task()
        }
    }
}