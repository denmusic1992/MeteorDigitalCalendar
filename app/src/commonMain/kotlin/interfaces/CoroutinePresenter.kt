package interfaces

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancelChildren
import kotlin.coroutines.CoroutineContext

/**
 * Presenter, определен для представления корутин
 */
interface CoroutinePresenter : CoroutineScope {
    // Job
    val job: Job

    // сама корутина
    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.Default

    /**
     * функция для завершения всех job
     */
    fun cleanup() {
        coroutineContext.cancelChildren()
    }
}