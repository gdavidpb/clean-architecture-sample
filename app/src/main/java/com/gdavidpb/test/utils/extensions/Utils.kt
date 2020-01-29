package com.gdavidpb.test.utils.extensions

inline fun <T> T?.notNull(exec: (T) -> Unit): T? = this?.also { exec(this) }