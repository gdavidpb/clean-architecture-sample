package com.gdavidpb.test.utils.extensions

import retrofit2.HttpException
import java.io.InterruptedIOException
import java.net.ConnectException
import java.net.SocketException
import java.net.UnknownHostException
import java.util.concurrent.ExecutionException
import javax.net.ssl.SSLHandshakeException

fun Throwable.isConnectionIssue() = when (this) {
    is SocketException -> true
    is InterruptedIOException -> true
    is UnknownHostException -> true
    is ConnectException -> true
    is SSLHandshakeException -> true
    is HttpException -> true
    is ExecutionException -> true
    else -> false
}