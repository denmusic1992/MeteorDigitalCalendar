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
 * TODO: Возможно, стоит добавить получение device ID только в один метод, а после считывать его из перезентера, тем самым обезопасить от частого входа в хранилище
 * TODO: Перенести все string в ресурс
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
                        registrationView.authorizationResult(true, user.data.toString())
                    }
                } else {
                    Storage.removeCurrentUser(settings)
                    withContext(Dispatchers.Main) {
                        registrationView.authorizationResult(false, "Пользователь удален!")
                    }
                }
            }
        } else {
            registrationView.authorizationResult(false, "Нет пользователя, нет логина")
        }
    }

    /**
     * Логинем пользователя
     */
    override fun loginUser(credentials: RegistrationCredentials) {
        // проверяем, может ли пользователь логиниться
        if (credentials.canTryLogin()) {
            launch {
                // отправляем запрос
                val deviceID = Storage.getDeviceID(settings)
                val authorization =
                    CoreRequestAPI.postAuthorizationData(deviceID, credentials, settings)
                if (authorization != null) {
                    // если данные пришли, то проверяем код ошибки
                    when (authorization.code) {
                        ResponseCode.Created.code -> {
                            // Если есть такой пользователь, то записываем его в память устройства
                            Storage.setCurrentUser(settings, authorization.data!!)
                            withContext(Dispatchers.Main) {
                                registrationView.authorizationResult(
                                    true,
                                    "Поздравляю, вы авторизованы!"
                                )
                            }
                        }
                        // Если ошибки, то передаем их
                        ResponseCode.BadRequest.code -> {
                            withContext(Dispatchers.Main) {
                                registrationView.authorizationResult(
                                    false,
                                    authorization.errorList.toString()
                                )
                            }
                        }
                        else -> {
                            withContext(Dispatchers.Main) {
                                registrationView.authorizationResult(
                                    false,
                                    "Данные введены некорректно! Проверьте поля ввода!"
                                )
                            }
                        }
                    }
                } else {
                    registrationView.authorizationResult(
                        false,
                        "Ошибка при связи с сервером"
                    )
                }
            }
        } else {
            registrationView.authorizationResult(
                false,
                "Данные введены некорректно! Проверьте поля ввода!"
            )
        }
    }

    /**
     * Сбрасываем пароль
     */
    override fun emailResendData(credentials: RegistrationCredentials) {
        //TODO("not implemented") //To change body of created functions use File | Settings | File Templates.

        //если email введен правильно, то продолжаем
        if (credentials.isEmailValid()) {
            launch {
                val deviceID = Storage.getDeviceID(settings)
                val resend =
                    CoreRequestAPI.postEmailResendData(deviceID, credentials.email, settings)
                if (resend != null) {
                    // ответ есть, значит смотрим код
                    when (resend.code) {
                        ResponseCode.Created.code -> {
                            withContext(Dispatchers.Main) {
                                registrationView.resendEmailResult(true)
                            }
                        }
                        ResponseCode.BadRequest.code -> {
                            withContext(Dispatchers.Main) {
                                registrationView.resendEmailResult(
                                    true,
                                    "Пользователь с указанным e-mail не найден")
                            }
                        }
                        else -> {
                            withContext(Dispatchers.Main) {
                                registrationView.resendEmailResult(
                                    true,
                                    "Часть полей заполнена некорректно")
                            }
                        }
                    }
                } else {
                    withContext(Dispatchers.Main) {
                        registrationView.resendEmailResult(
                            false,
                            "Ошибка при связи с сервером"
                        )
                    }
                }
            }
        } else {
            registrationView.resendEmailResult(false, "Поле с e-mail неправильно заполнено.")
        }
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
            registrationView.registrationResult(false, errors)
        } else {
            launch {
                val deviceID = Storage.getDeviceID(settings)
                val registrationData =
                    CoreRequestAPI.postRegistrationData(deviceID, credentials, settings)
                // Если получили отклик с сервера
                if (registrationData != null) {
                    when (registrationData.code) {
                        ResponseCode.Created.code -> {
                            currentUser = registrationData.data
                            Storage.setCurrentUser(settings, currentUser!!)
                            withContext(Dispatchers.Main) {
                                registrationView.registrationResult(true)
                            }
                        }
                        ResponseCode.BadRequest.code -> {
                            withContext(Dispatchers.Main) {
                                registrationView.registrationResult(
                                    false,
                                    arrayListOf(RegisterError.RegistrationError),
                                    registrationData.errorList!![0]
                                )
                            }
                        }
                        else -> {
                            withContext(Dispatchers.Main) {
                                registrationView.registrationResult(
                                    false,
                                    arrayListOf(RegisterError.RegistrationError),
                                    "Часть полей заполнена некорректно!"
                                )
                            }
                        }
                    }
                } else {
                    registrationView.registrationResult(
                        false,
                        arrayListOf(RegisterError.RegistrationError),
                        "Ошибка при связи с сервером"
                    )
                }
            }
        }

    }
}