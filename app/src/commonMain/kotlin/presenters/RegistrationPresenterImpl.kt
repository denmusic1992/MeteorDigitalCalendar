package presenters

import com.russhwolf.settings.Settings
import enums.ClientType
import enums.RegisterError
import enums.ResponseCode
import helpers.Storage
import interfaces.CoroutinePresenter
import interfaces.RegistrationPresenter
import interfaces.RegistrationViewInterface
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import models.RegistrationCredentials
import models.User
import network.CoreRequestAPI

/**
 * Презентер для работы с регистрацией устройства и пользователя
 * @param registrationView интерфейс для обращения по результату к view
 * @param settings доступ к key-value хранилищу
 */
class RegistrationPresenterImpl(
    private val registrationView: RegistrationViewInterface,
    private val settings: Settings
) : RegistrationPresenter, CoroutinePresenter {

    override val job: Job
        get() = Job()

    // данные о текущем пользователе
    private var currentUser: User? = null

    /**
     * Метод регистрации приложения в системе
     */
    override fun registerDevice() {
        // Если ещё не зарегистрированы, то регистрируемся
        if (Storage.getPrivateKey(settings) == null) {
            val deviceID = Storage.getDeviceID(settings)
            // Отправляем запрос на сервер
            launch {
                val data = CoreRequestAPI.postDeviceID(deviceID, ClientType.Android, settings)
                // Если получили ответ с сервера, разбираем его
                // получили отклик
                if (data != null) {
                    // Если дата есть и все хорошо, то добавляем в хранилище ключ и говорим, что регистрация успешна
                    when (data.code) {
                        ResponseCode.Created.code -> {
                            Storage.setPrivateKey(settings, data.data!!.privateKey)
                            withContext(Dispatchers.Main) {
                                registrationView.registerResult(true, data.data.toString())
                            }
                        }
                        // Иначе передаем ошибку в вызывающий класс
                        else -> {
                            withContext(Dispatchers.Main) {
                                registrationView.registerResult(false, data.errorList.toString())
                            }
                        }
                    }
                } else
                // Здесь то же самое, передаем ошибку
                    withContext(Dispatchers.Main) {
                        registrationView.registerResult(false, "Unable to get data, null!")
                    }
            }
        } else {
            registrationView.registerResult(
                true,
                "device already registered, uuid = ${Storage.getPrivateKey(settings)}"
            )
        }
    }

    /**
     * Метод авторизации пользователя в приложении
     */
    override fun authorizeUser() {
        // Ищем пользователя на устройстве
        currentUser = Storage.getCurrentUser(settings)
        // Если есть такой, то говорим серверу, что логинется такой то пользователь
        if (currentUser != null) {
            val deviceID = Storage.getDeviceID(settings)
            launch {
                val user = CoreRequestAPI.getCurrentUser(deviceID, settings)
                if (user?.data != null && user.data.userId == currentUser?.userId) {
                    Storage.setCurrentUser(settings, user.data)
                    currentUser = user.data
                    withContext(Dispatchers.Main) {
                        registrationView.authorizationResult(user.data.toString())
                    }
                } else {
                    Storage.removeCurrentUser(settings)
                    withContext(Dispatchers.Main) {
                        registrationView.authorizationResult("Пользователь удален!")
                    }
                }
            }
        } else {
            registrationView.authorizationResult("Нет пользователя, нет логина")
        }
    }

    override fun authorizeUser(credentials: RegistrationCredentials) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    /**
     * Метод регистрации пользователя в системе
     */
    override fun registerUser(credentials: RegistrationCredentials) {
        val errors = arrayListOf<RegisterError>()
        // сначала проверяем, все ли данные у нас введены корректно
        if (!credentials.isEmailValid())
        // если email не валидный или пустой, то добавляем ошибку
            errors.add(RegisterError.EmailError)
        if (!credentials.isPasswordCorrect())
        // если пароли не совпадают
            errors.add(RegisterError.PasswordError)
        if (!credentials.isNameValid())
        // если имя не введено
            errors.add(RegisterError.NameError)
        if (!credentials.isPhoneValid())
        // если телефон не валиден
            errors.add(RegisterError.PhoneError)

        // Если есть ошибки, то возвращаем ответ во View, иначе регистрируемся
        if (errors.size > 0) {
            registrationView.registrationFailed(errors)
        } else {
            launch {
                val deviceID = Storage.getDeviceID(settings)
                val registrationData =
                    CoreRequestAPI.postRegistrationData(deviceID, credentials, settings)
                if (registrationData != null) {
                    when (registrationData.code) {
                        ResponseCode.Created.code -> {
                            currentUser = registrationData.data
                            Storage.setCurrentUser(settings, currentUser!!)
                            withContext(Dispatchers.Main) {
                                registrationView.registrationSuccess()
                            }
                        }
                        else -> {
                            withContext(Dispatchers.Main) {
                                registrationView.registrationFailed(
                                    arrayListOf(RegisterError.RegistrationError),
                                    registrationData.errorList!![0]
                                )
                            }
                        }
                    }
                }
            }
        }

    }
}