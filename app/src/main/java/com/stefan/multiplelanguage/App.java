package com.stefan.multiplelanguage;

import android.app.Application;

import com.stefan.multilanguage.impl.MultiLanguageService;

/**
 * @author chenshan
 * @date 2021.01.03
 * @since
 */
public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        MultiLanguageService.INSTANCE.init(this);
    }
}
