package com.gdavidpb.test.utils.extensions

import android.annotation.SuppressLint
import java.text.Normalizer
import java.text.SimpleDateFormat
import java.util.*

private val ISO8601format = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US)
private val yearFormat = SimpleDateFormat("yyyy", Locale.US)
private val intervalFormat = SimpleDateFormat("mm:ss", Locale.US)

fun String.parseISO8601Date(): Date = ISO8601format.parse(this)!!

@SuppressLint("DefaultLocale")
fun String.normalize(): String = Normalizer
    .normalize(toLowerCase(), Normalizer.Form.NFD)
    .replace("[^\\p{ASCII}]".toRegex(), "")

fun Long.formatInterval(): String = intervalFormat.format(Date(this))

fun Date.formatYear(): String = yearFormat.format(this)