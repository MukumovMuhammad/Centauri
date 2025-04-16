import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.content.res.Configuration
import android.os.Build
import androidx.core.content.ContextCompat.startActivity
import java.util.*

object LocaleHelper {

    fun setLocale(context: Context, language: String): Context {
        val locale = Locale(language)
        Locale.setDefault(locale)

        val config = Configuration(context.resources.configuration)
        config.setLocale(locale)

        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            context.createConfigurationContext(config)
        } else {
            @Suppress("DEPRECATION")
            context.resources.updateConfiguration(config, context.resources.displayMetrics)
            context
        }
    }

    fun getLocalizedString(context: Context, name: String): String {
        val lang = getSavedLanguage(context) // Your function from before
        val locale = Locale(lang)

        val config = Configuration(context.resources.configuration)
        config.setLocale(locale)

        val localizedContext = context.createConfigurationContext(config)
        val resId = localizedContext.resources.getIdentifier(name, "string", context.packageName)

        return if (resId != 0) {
            localizedContext.resources.getString(resId)
        } else {
            "[$name not found]"
        }
    }

    fun getSavedLanguage(context: Context): String {
        val prefs = context.getSharedPreferences("Settings", Context.MODE_PRIVATE)
        return prefs.getString("My_Lang", "en") ?: "en"
    }

    fun setLanguage(context: Context,langCode: String, intent: Intent) {
        val prefs = context.getSharedPreferences("Settings", MODE_PRIVATE)
        val editor = prefs.edit()
        editor.putString("My_Lang", langCode)
        editor.apply()

        startActivity(context, intent, null)
    }




}
