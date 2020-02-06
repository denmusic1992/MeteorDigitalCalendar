package sample

import Config
import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.russhwolf.settings.AndroidSettings
import enums.RegisterError
import interfaces.CommonPresenter
import interfaces.CommonViewInterface
import interfaces.RegistrationPresenter
import interfaces.RegistrationViewInterface
import kotlinx.android.synthetic.main.activity_main.*
import models.RegistrationCredentials
import presenters.CommonPresenterImpl
import presenters.RegistrationPresenterImpl
import sample.utils.MainUtils

actual class Sample {
    actual fun checkMe() = 44
}

actual object Platform {
    actual val name: String = "Android"
}

class MainActivity : AppCompatActivity(), CommonViewInterface, RegistrationViewInterface,
    View.OnClickListener {

    private lateinit var commonPresenter: CommonPresenter
    private lateinit var registrationPresenter: RegistrationPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Sample().checkMe()
        setContentView(R.layout.activity_main)
        findViewById<TextView>(R.id.main_text).text = hello()
        // Создаем настройки для key-value в MPP
        val preferences = getSharedPreferences(Config.preferences, Context.MODE_PRIVATE)
        val settings = AndroidSettings(preferences)
        commonPresenter = CommonPresenterImpl(this, settings)
        registrationPresenter = RegistrationPresenterImpl(this, settings)
        // После того как передали настройки, регистрируем девайс
        registrationPresenter.registerDevice()

        //TODO: добавить data binding
        btnRegisterTest.setOnClickListener(this)
        btnAuthorize.setOnClickListener(this)
        btnResend.setOnClickListener(this)
        btnFavourite.setOnClickListener(this)
    }

    override fun registerResult(success: Boolean, reason: String?) {
        MainUtils.longToast(
            this,
            if (success) "Регистрация прошла успешно! " +
                    "\n$reason" else reason
                ?: ""
        )
        if (success)
            registrationPresenter.authorizeUser()
    }

    override fun authorizationResult(success: Boolean, reason: String?) {
        if (reason != null)
            MainUtils.longToast(this, reason)
        commonPresenter.setCategories()
    }

    override fun registrationResult(
        success: Boolean,
        errors: ArrayList<RegisterError>?,
        messageError: String?
    ) {
        if (success) {
            MainUtils.shortToast(this, "Регистрация прошла успешно!")
        } else {
            if (errors != null)
                for (error in errors) {
                    println(error.error)
                }
            if (messageError != null) {
                MainUtils.longToast(this, "Ошибка: $messageError")
            }
        }
    }

    override fun resendEmailResult(success: Boolean, message: String?) {
        //TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        if (success) {
            MainUtils.shortToast(this, "Новые данные отправлены на почту!")
        } else if (message != null)
            MainUtils.longToast(this, message)
    }

    override fun categoriesReceivedResult(success: Boolean, reason: String?) {
        if (success) {
            MainUtils.shortToast(this, "Категории получены!")
            commonPresenter.setEvents()
        }
    }

    override fun eventsReceivedResult(success: Boolean, reason: String?) {
        if (success)
            MainUtils.shortToast(this, "События получены!")
    }

    override fun favouriteResult(success: Boolean, reason: String?) {
        if (success) {
            MainUtils.shortToast(this, "Добавлено в избранное!")
        } else if (reason != null) {
            MainUtils.shortToast(this, reason)
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btnRegisterTest -> {
                // Здесь мы тестируем регистрацию приложения
//                val credentials = RegistrationCredentials("qwerty", "2323", "2323", "test1", "2222222222")
//                val credentials = RegistrationCredentials("qwerty@asdf.rr", "", "2323","test1", "2222222222")
//                val credentials = RegistrationCredentials("qwerty@asdf.rr", "2323", "2323","", "2222222222")
//                val credentials = RegistrationCredentials("qwerty@asdf.rr", "2323", "2323","test1", "222222222")
                val credentials = RegistrationCredentials(
                    "2322qwerty@asddf.rr",
                    "2323",
                    "2323",
                    "test1",
                    "2222222223"
                )
                registrationPresenter.registerUser(credentials)
            }
            R.id.btnAuthorize -> {
                val credentials =
                    RegistrationCredentials(email = "2322qwerty@asddf.rr", password = "2323")
                registrationPresenter.loginUser(credentials)
            }
            R.id.btnResend -> {
                val credentials = RegistrationCredentials(email = "denmusic91@mail.ru")
                registrationPresenter.emailResendData(credentials)
            }
            R.id.btnFavourite -> {
                val eventId = 1
                commonPresenter.setFavourite(eventId)
            }
            else -> return
        }
    }
}