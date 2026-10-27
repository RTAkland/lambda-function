/*
 * Copyright © 2026 rtakland
 * Author: rtakland
 * Date: 2026-10-27
 */


package cn.rtast.lambdafunction.worker.runtime.engine


import io.ktor.utils.io.*
import io.ktor.utils.io.pool.*
import js.buffer.ArrayBuffer
import js.buffer.ArrayBufferLike
import js.typedarrays.Uint8Array
import js.typedarrays.toByteArray
import js.typedarrays.toUint8Array
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.promise
import web.streams.ReadableStream
import web.streams.ReadableStreamDefaultController
import web.streams.UnderlyingDefaultSource
import web.streams.read

internal fun <T : ArrayBufferLike> ReadableStream<Uint8Array<T>>.toByteChannel(scope: CoroutineScope): ByteChannel {
    val channel = ByteChannel(autoFlush = true)
    val reader = getReader()
    scope.launch {
        try {
            while (true) {
                val result = reader.read()
                if (result.done) break
                channel.writeByteArray(result.value?.toByteArray() ?: break)
            }
            channel.close()
        } catch (t: Throwable) {
            channel.close(t)
        } finally {
            try {
                reader.releaseLock()
            } catch (_: Throwable) {
            }
        }
    }
    return channel
}


internal fun ByteReadChannel.toReadableStream(scope: CoroutineScope): ReadableStream<Uint8Array<ArrayBuffer>> =
    ReadableStream(
        UnderlyingDefaultSource(
            start = { _: ReadableStreamDefaultController<Uint8Array<ArrayBuffer>> -> },
            pull = { controller: ReadableStreamDefaultController<Uint8Array<ArrayBuffer>> ->
                scope.promise {
                    val buffer = ByteArrayPool.borrow()
                    try {
                        val n = readAvailable(buffer)
                        if (n <= 0) {
                            controller.close()
                            return@promise
                        }
                        val src = buffer.sliceArray(0 until n)
                        controller.enqueue(src.toUint8Array())
                    } finally {
                        ByteArrayPool.recycle(buffer)
                    }
                }.asDynamic()
            }, cancel = { reason: Any? ->
                scope.promise {
                    val cause = when (reason) {
                        is Throwable -> reason
                        null -> CancellationException("ReadableStream cancelled")
                        else -> CancellationException(reason.toString())
                    }
                    cancel(cause)
                }.asDynamic()
            }
        )
    )
