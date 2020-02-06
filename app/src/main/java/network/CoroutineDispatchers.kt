package network

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

actual object CoroutineDispatchers {
    internal actual val Main: CoroutineDispatcher
        get() = Dispatchers.Main
    internal actual val Background: CoroutineDispatcher
        get() = Dispatchers.Default
}