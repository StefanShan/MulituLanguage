package com.stefan.multiplelanguage

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.stefan.multilanguage.impl.MultiLanguageService

class MainActivity2 : AppCompatActivity() {

    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(MultiLanguageService.changeContextLocale(newBase))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)
    }
}