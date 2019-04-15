package cl.yapo.test.presentation.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

abstract class StateViewModel<T>(private val initState: T) : ViewModel() {
    val state = MutableLiveData<T>()

    init {
        state.value = initState
    }

    fun setState(update: T.() -> T) {
        val oldState = state.value
        val newState = oldState?.update()

        state.postValue(newState)
    }
}