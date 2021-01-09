package com.stefan.multilanguage.impl

import android.app.Application
import android.content.ComponentCallbacks
import android.content.Context
import android.content.res.Configuration
import android.content.res.Resources
import android.os.Build
import androidx.appcompat.view.ContextThemeWrapper
import java.util.*

/**
 *
 *
 * @author chenshan
 * @date 2021.01.03
 * @since
 */

object MultiLanguageService{

     fun init(application: Application) {
        changeContextLocale(application)
        application.registerComponentCallbacks(object : ComponentCallbacks{
            override fun onConfigurationChanged(newConfig: Configuration) {
                changeContextLocale(application)
            }

            override fun onLowMemory() {
            }
        })
    }

    private var mSupportLocal: ((String)->Locale)? =null

    fun setExpandLocal(supportLocal: (String)->Locale){
        mSupportLocal = supportLocal
    }

     fun changeLanguage(context: Context, language: String): Context {
        var ctx = context
        val locale: Locale = getLocale(language)
        Locale.setDefault(locale)

        //在非application切换语言同时切换掉applicant
        if (ctx !is Application) {
            val appContext = ctx.applicationContext
            updateConfiguration(appContext, locale, language)
        }
        ctx = updateConfiguration(context, locale, language)
        val configuration = context.resources.configuration
        //兼容appcompat 1.2.0后切换语言失效问题
        return object : ContextThemeWrapper(ctx, R.style.Base_Theme_AppCompat_Empty) {
            override fun applyOverrideConfiguration(overrideConfiguration: Configuration?) {
                overrideConfiguration?.setTo(configuration)
                super.applyOverrideConfiguration(overrideConfiguration)
            }
        }
    }

     fun changeContextLocale(context: Context): Context {
        val lang: String = context.getSharedPreferences("multi_language", Context.MODE_PRIVATE)
            .getString("language_type", LanguageType.LANGUAGE_SYSTEM)?:LanguageType.LANGUAGE_SYSTEM
        return changeLanguage(context, lang)
    }

     fun getCurrentLanguage(): String {
        val language = Locale.getDefault().language
        val country = Locale.getDefault().country
        // 中文繁体: 港澳台
            if ("zh".equals(language, ignoreCase = true)
                && ("tw".equals(country, ignoreCase = true)
                        || "hk".equals(country, ignoreCase = true)
                        || "mo".equals(country, ignoreCase = true))) {
                return "zh-Hant"
            }
        //印尼语特殊处理
        return if ("in".equals(language, ignoreCase = true)) {
            "id"
        } else language
    }

    private fun updateConfiguration(context: Context, locale: Locale, language: String): Context {
        var ctx = context
        val resources = ctx.resources
        val configuration = resources.configuration
        val displayMetrics = resources.displayMetrics
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            configuration.setLocale(locale)
            ctx = ctx.createConfigurationContext(configuration)
        } else {
            configuration.locale = locale
        }
        resources.updateConfiguration(configuration, displayMetrics)
        ctx.getSharedPreferences("multi_language", Context.MODE_PRIVATE)
            .edit()
            .putString("language_type", language)
            .apply()
        return ctx
    }

    private fun getLocale(language: String): Locale {
        when (language) {
            //跟随系统
            LanguageType.LANGUAGE_SYSTEM -> return getLocalWithVersion(Resources.getSystem().configuration)
            //英文
            LanguageType.LANGUAGE_EN -> return Locale.US
            //简体中文
            LanguageType.LANGUAGE_ZH_CN -> return Locale.SIMPLIFIED_CHINESE
            //泰语
            LanguageType.LANGUAGE_TH -> return Locale("th", "TH", "TH")
            //繁体中文
            LanguageType.LANGUAGE_ZH_TW -> return Locale.TRADITIONAL_CHINESE
            //韩语
            LanguageType.LANGUAGE_KO -> return Locale.KOREA
            //日语
            LanguageType.LANGUAGE_JA -> return Locale.JAPAN
            //阿拉伯语
            LanguageType.LANGUAGE_AR -> return Locale("ar", "SA")
            //印尼语
            LanguageType.LANGUAGE_IN -> return Locale("in", "ID")
            //西班牙语
            LanguageType.LANGUAGE_ES -> return Locale("es", "ES")
            //葡萄牙语
            LanguageType.LANGUAGE_PT -> return Locale("pt", "PT")
            //土耳其语
            LanguageType.LANGUAGE_TR -> return Locale("tr", "TR")
            //俄语
            LanguageType.LANGUAGE_RU -> return Locale("ru", "RU")
            //意大利语
            LanguageType.LANGUAGE_IT -> return Locale("it", "IT")
            //法语
            LanguageType.LANGUAGE_FR -> return Locale("fr", "FR")
            //德语
            LanguageType.LANGUAGE_DE -> return Locale("de", "DE")
            //越南语
            LanguageType.LANGUAGE_VI -> return Locale("vi", "VN")
        }
        if (mSupportLocal != null){
            val locale = mSupportLocal!!.invoke(language)
            if (locale.language == LanguageType.LANGUAGE_SYSTEM){
                return getLocalWithVersion(Resources.getSystem().configuration)
            }
            return locale
        }
        return getLocalWithVersion(Resources.getSystem().configuration)
    }

    private fun getLocalWithVersion(configuration: Configuration): Locale {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            configuration.locales[0]
        } else {
            configuration.locale
        }
    }

}