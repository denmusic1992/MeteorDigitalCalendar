package sample

import Config
import android.content.Context
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.russhwolf.settings.AndroidSettings
import interfaces.CommonPresenter
import interfaces.CommonViewInterface
import models.CategoryEvent
import presenters.CommonPresenterImpl
import sample.utils.MainUtils

actual class Sample {
    actual fun checkMe() = 44
}

actual object Platform {
    actual val name: String = "Android"
}

class MainActivity : AppCompatActivity(), CommonViewInterface {

    private lateinit var commonPresenter: CommonPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Sample().checkMe()
        setContentView(R.layout.activity_main)
        findViewById<TextView>(R.id.main_text).text = hello()
        // Создаем настройки для key-value в MPP
        val preferences = getSharedPreferences(Config.preferences, Context.MODE_PRIVATE)
        val settings = AndroidSettings(preferences)
        commonPresenter = CommonPresenterImpl(this, settings)
        // После того как передали настройки, регистрируем девайс
        commonPresenter.registerDevice()
    }

    override fun registerResult(success: Boolean, message: String?) {
        MainUtils.shortToast(this,
            if (success) "Регистрация прошла успешно! " +
                    "\n$message" else message
                ?: ""
        )
        if(success)
            commonPresenter.authorizeUser()
    }

    override fun authorizationResult(message: String?) {
        if(message != null)
            MainUtils.shortToast(this, message)
        commonPresenter.setCategories()
    }

    override fun categoriesReceived(categories: ArrayList<CategoryEvent>?) {
        if(categories != null){
            MainUtils.longToast(this, "Категории получены!")
        }
    }
}