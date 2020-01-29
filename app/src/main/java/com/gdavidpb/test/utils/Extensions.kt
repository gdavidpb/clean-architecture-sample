package com.gdavidpb.test.utils

import android.annotation.SuppressLint
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.view.View
import android.widget.CheckBox
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.*
import androidx.lifecycle.Observer
import com.gdavidpb.test.domain.usecase.coroutines.BaseUseCase
import com.gdavidpb.test.domain.usecase.coroutines.Completable
import com.gdavidpb.test.domain.usecase.coroutines.Result
import kotlinx.coroutines.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.HttpException
import retrofit2.Response
import java.text.Normalizer
import java.text.SimpleDateFormat
import java.util.*
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

/* Context */

fun ConnectivityManager.isNetworkAvailable(): Boolean {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        getNetworkCapabilities(activeNetwork)?.run {
            when {
                hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                else -> false
            }
        }
    } else {
        @Suppress("DEPRECATION")
        activeNetworkInfo?.run {
            when (type) {
                ConnectivityManager.TYPE_WIFI -> true
                ConnectivityManager.TYPE_MOBILE -> true
                else -> false
            }
        }
    } ?: false
}

/* Live data */

typealias LiveResult<T> = MutableLiveData<Result<T>>
typealias LiveCompletable = MutableLiveData<Completable>

/* LiveResult */

@JvmName("postCompleteResult")
fun <T> LiveResult<T>.postSuccess(value: T) = postValue(Result.OnSuccess(value))

@JvmName("postThrowableResult")
fun <T> LiveResult<T>.postThrowable(throwable: Throwable) = postValue(Result.OnError(throwable))

@JvmName("postLoadingResult")
fun <T> LiveResult<T>.postLoading() = postValue(Result.OnLoading())

@JvmName("postCancelResult")
fun <T> LiveResult<T>.postCancel() = postValue(Result.OnCancel())

@JvmName("postEmptyResult")
fun <T> LiveResult<T>.postEmpty() = postValue(Result.OnEmpty())

/* LiveCompletable */

@JvmName("postCompleteCompletable")
fun LiveCompletable.postComplete() = postValue(Completable.OnComplete)

@JvmName("postThrowableCompletable")
fun LiveCompletable.postThrowable(throwable: Throwable) = postValue(Completable.OnError(throwable))

@JvmName("postLoadingCompletable")
fun LiveCompletable.postLoading() = postValue(Completable.OnLoading)

@JvmName("postCancelCompletable")
fun LiveCompletable.postCancel() = postValue(Completable.OnCancel)

fun <T : BaseUseCase<Q, W>, Q, W : MutableLiveData<*>> ViewModel.execute(useCase: T, params: Q, liveData: W) {
    useCase.execute(params, liveData, viewModelScope)
}

/* Observers */

fun <T, L : LiveData<T>> FragmentActivity.observe(liveData: L, body: (T?) -> Unit) =
    liveData.observe(this, Observer(body))

fun <T, L : LiveData<T>> Fragment.observe(liveData: L, body: (T?) -> Unit) =
    liveData.observe(viewLifecycleOwner, Observer(body))

/* Coroutines */

suspend fun <T> Call<T>.await() = suspendCoroutine<T?> { continuation ->
    enqueue(object : Callback<T?> {
        override fun onResponse(call: Call<T?>, response: Response<T?>) {
            if (response.isSuccessful)
                continuation.resume(response.body())
            else
                continuation.resumeWithException(HttpException(response))
        }

        override fun onFailure(call: Call<T?>, t: Throwable) {
            continuation.resumeWithException(t)
        }
    })
}

/* View */

fun TextView.drawables(
    left: Int = 0,
    top: Int = 0,
    right: Int = 0,
    bottom: Int = 0
) = setCompoundDrawablesWithIntrinsicBounds(left, top, right, bottom)

fun CheckBox.setSafeChecked(checked: Boolean) {
    val enabled = isEnabled
    isEnabled = false
    isChecked = checked
    isEnabled = enabled
}

var View.visible: Boolean
    get() = visibility == View.VISIBLE
    set(value) {
        visibility = if (value) View.VISIBLE else View.INVISIBLE
    }

fun View.onClickOnce(onClick: () -> Unit) {
    setOnClickListener(object : View.OnClickListener {
        override fun onClick(view: View) {
            view.setOnClickListener(null)

            also { listener ->
                CoroutineScope(Dispatchers.Main).launch {
                    onClick()

                    withContext(Dispatchers.IO) { delay(500) }

                    view.setOnClickListener(listener)
                }
            }
        }
    })
}

/* Parsing */

private val ISO8601format = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US)
private val yearFormat = SimpleDateFormat("yyyy", Locale.US)
private val intervalFormat = SimpleDateFormat("mm:ss", Locale.US)

fun String.parseISO8601Date(): Date = ISO8601format.parse(this)!!

@SuppressLint("DefaultLocale")
fun String.normalize() = Normalizer.normalize(toLowerCase(), Normalizer.Form.NFD).replace("[^\\p{ASCII}]".toRegex(), "")

fun Long.formatInterval(): String = intervalFormat.format(Date(this))

fun Date.formatYear(): String = yearFormat.format(this)

/* Utils */

inline fun <T> T?.notNull(exec: (T) -> Unit): T? = this?.also { exec(this) }