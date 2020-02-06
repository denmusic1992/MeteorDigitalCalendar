package network

import kotlinx.coroutines.CoroutineDispatcher

expect object CoroutineDispatchers {
    internal val Main: CoroutineDispatcher
    internal val Background: CoroutineDispatcher
}