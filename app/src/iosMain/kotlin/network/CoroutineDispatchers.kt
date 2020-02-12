package network

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Runnable
import platform.darwin.dispatch_async
import platform.darwin.dispatch_get_main_queue
import platform.darwin.dispatch_queue_t
import kotlin.coroutines.CoroutineContext

actual object CoroutineDispatchers {
    internal actual val Main: CoroutineDispatcher
        get() = NsQueueDispatcher(dispatch_get_main_queue())
    internal actual val Background: CoroutineDispatcher
        get() = Main

    internal class NsQueueDispatcher(
        private val dispatchQueue: dispatch_queue_t
    ): CoroutineDispatcher() {
        override fun dispatch(context: CoroutineContext, block: Runnable) {
            dispatch_async(dispatchQueue) {
                block.run()
            }
        }
    }
}