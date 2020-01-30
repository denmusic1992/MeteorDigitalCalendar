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
import models.CategoryEvent
import models.Event
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
    }

    override fun registerResult(success: Boolean, message: String?) {
        MainUtils.shortToast(
            this,
            if (success) "Регистрация прошла успешно! " +
                    "\n$message" else message
                ?: ""
        )
        if (success)
            registrationPresenter.authorizeUser()
    }

    override fun authorizationResult(message: String?) {
        if (message != null)
            MainUtils.shortToast(this, message)
        commonPresenter.setCategories()
    }

    override fun registrationFailed(errors: ArrayList<RegisterError>, messageError: String?) {
        for (error in errors) {
            println(error.error)
            if (messageError != null) {
                MainUtils.longToast(this, "Ошибка: $messageError")
            }
        }
    }

    override fun registrationSuccess() {
        //TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        MainUtils.longToast(this, "Регистрация прошла успешно!")
    }

    override fun categoriesReceived(categories: ArrayList<CategoryEvent>?) {
        if (categories != null) {
            MainUtils.longToast(this, "Категории получены!")
            commonPresenter.setEvents()
        }
    }

    override fun eventsReceived(events: ArrayList<Event>?) {
        if (events != null)
            MainUtils.longToast(this, "События получены!")
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
                    "232qwerty@asdf.rr",
                    "2323",
                    "2323",
                    "test1",
                    "2222222222"
                )
                registrationPresenter.registerUser(credentials)
            }
            else -> return
        }
    }
}