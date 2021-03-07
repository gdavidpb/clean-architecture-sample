package com.gdavidpb.test.utils.extensions

import androidx.lifecycle.*
import com.gdavidpb.test.domain.usecase.coroutines.BaseUseCase
import com.gdavidpb.test.domain.usecase.coroutines.Completable
import com.gdavidpb.test.domain.usecase.coroutines.Result

/* Live data */

typealias LiveResult<T, Q> = MutableLiveData<Result<T, Q>>
typealias LiveCompletable<Q> = MutableLiveData<Completable<Q>>

/* LiveResult */

@JvmName("postEmptyResult")
fun <T, Q> LiveResult<T, Q>.postEmpty() = postValue(Result.OnEmpty())

@JvmName("postSuccessResult")
fun <T, Q> LiveResult<T, Q>.postSuccess(value: T) = postValue(Result.OnSuccess(value))

@JvmName("postErrorResult")
fun <T, Q> LiveResult<T, Q>.postError(error: Q?) = postValue(Result.OnError(error))

@JvmName("postLoadingResult")
fun <T, Q> LiveResult<T, Q>.postLoading() = postValue(Result.OnLoading())

@JvmName("postCancelResult")
fun <T, Q> LiveResult<T, Q>.postCancel() = postValue(Result.OnCancel())

/* LiveCompletable */

@JvmName("postCompleteCompletable")
fun <Q> LiveCompletable<Q>.postComplete() = postValue(Completable.OnComplete())

@JvmName("postErrorCompletable")
fun <Q> LiveCompletable<Q>.postError(error: Q?) = postValue(Completable.OnError(error))

@JvmName("postLoadingCompletable")
fun <Q> LiveCompletable<Q>.postLoading() = postValue(Completable.OnLoading())

@JvmName("postCancelCompletable")
fun <Q> LiveCompletable<Q>.postCancel() = postValue(Completable.OnCancel())

fun <P, T, Q, L, U : BaseUseCase<P, T, Q, L>> ViewModel.execute(
    useCase: U,
    params: P,
    liveData: L
) = useCase.execute(params, liveData, viewModelScope)

/* Observers */

fun <T, L : MutableLiveData<T>> LifecycleOwner.observe(liveData: L, body: (T?) -> Unit) =
    liveData.observe(this, Observer(body))