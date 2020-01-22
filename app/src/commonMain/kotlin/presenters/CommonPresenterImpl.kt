package presenters

import com.russhwolf.settings.Settings
import enums.ClientType
import enums.ResponseCode
import helpers.Storage
import interfaces.CommonPresenter
import interfaces.CommonViewInterface
import interfaces.CoroutinePresenter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import network.RequestAPI

/**
 * Презентер для работы с главными функциями приложения
 */
class CommonPresenterImpl(
    private val commonView: CommonViewInterface,
    private val settings: Settings
) :
    CommonPresenter, CoroutinePresenter {

    override val job: Job
        get() = Job()

    /**
     * Метод регистрации приложения в системе
     */
    override fun registerDevice() {
        // Если ещё не зарегистрированы, то регистрируемся
        if (Storage.getPrivateKey(settings) == null) {
            val deviceID = Storage.getDeviceID(settings)
            // Отправляем запрос на сервер
            launch {
                val data = RequestAPI.postDeviceID(deviceID, ClientType.Android, settings)
                // Если получили ответ с сервера, разбираем его
                // получили отклик
                if (data != null) {
                    // Если дата есть и все хорошо, то добавляем в хранилище ключ и говорим, что регистрация успешна
                    when (data.code) {
                        ResponseCode.Created.code -> {
                            Storage.setPrivateKey(settings, data.data!!.privateKey)
                            withContext(Dispatchers.Main) {
                                commonView.registerResult(true, data.data.toString())
                            }
                        }
                        // Иначе передаем ошибку в вызывающий класс
                        else -> {
                            withContext(Dispatchers.Main) {
                                commonView.registerResult(false, data.errorList.toString())
                            }
                        }
                    }
                } else
                // Здесь то же самое, передаем ошибку
                    withContext(Dispatchers.Main) {
                        commonView.registerResult(false, "Unable to get data, null!")
                    }
            }
        } else {
            commonView.registerResult(
                true,
                "device already registered, uuid = ${Storage.getPrivateKey(settings)}"
            )
        }
    }

    override fun authorizeUser() {
        // Ищем пользователя на устройстве
        val previousUser = Storage.getCurrentUser(settings)
        // Если есть такой, то говорим серверу, что логинется такой то пользователь
        if (previousUser != null) {
            val deviceID = Storage.getDeviceID(settings)
            launch {
                val user = RequestAPI.getCurrentUser(deviceID, settings)
                if (user?.data != null && user.data.userId == previousUser.userId) {
                    Storage.setCurrentUser(settings, user.data)
                    withContext(Dispatchers.Main) {
                        commonView.authorizationResult(user.data.toString())
                    }
                } else {
                    Storage.removeCurrentUser(settings)
                    withContext(Dispatchers.Main) {
                        commonView.authorizationResult("Пользователь удален!")
                    }
                }
            }
        } else {
            commonView.authorizationResult("Нет пользователя, нет логина")
        }
    }

    override fun setCategories() {
        val deviceID = Storage.getDeviceID(settings)
        launch {
            val response = RequestAPI.getCategories(deviceID, settings)
            if (response?.data != null) {
                withContext(Dispatchers.Main) {
                    commonView.categoriesReceived(response.data)
                }
            }
        }
    }
}