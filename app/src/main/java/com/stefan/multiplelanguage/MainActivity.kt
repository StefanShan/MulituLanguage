package com.stefan.multiplelanguage

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.stefan.multilanguage.impl.LanguageType
import com.stefan.multilanguage.impl.MultiLanguageService
import java.util.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<Button>(R.id.btn_jump_activity).setOnClickListener {
            startActivity(Intent(this, MainActivity2::class.java))
        }

        findViewById<Button>(R.id.btn_jump_dialog).setOnClickListener {
            MultiLanguageService.changeContextLocale(this)
            Dialog(this).apply {
                setContentView(R.layout.dialog_main)
                show()
            }
        }

        /*
         * 举例 拓展多语言
         */
//        MultiLanguageService.setExpandLocal { languageType ->
//            return@setExpandLocal when (languageType) {
//                //朝鲜
//                ExpandLanguage.CX -> {
//                    Locale("chaoxian", "CX")
//                }
//                //刚果
//                ExpandLanguage.GG -> {
//                    Locale("gangguo", "GG")
//                }
//                //其他语言
//                else -> {
//                    Locale(LanguageType.LANGUAGE_SYSTEM)
//                }
//            }
//        }

        findViewById<Button>(R.id.btn_change_system).setOnClickListener {
            MultiLanguageService.changeLanguage(this, LanguageType.LANGUAGE_SYSTEM)
            startActivity(Intent(this, MainActivity::class.java).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK))
        }

        findViewById<Button>(R.id.btn_change_jp).setOnClickListener {
            MultiLanguageService.changeLanguage(this, LanguageType.LANGUAGE_JA)
            startActivity(Intent(this, MainActivity::class.java).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK))
        }

        findViewById<Button>(R.id.btn_show).text = "当前语言(${MultiLanguageService.getCurrentLanguage()})"
    }
}