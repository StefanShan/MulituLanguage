# MulituLanguageService

多语言切换(基于Androidx)

适配了Androidx appcomapt 导致切换多语言失效问题

# 调用说明

## 初始化

在自定义的`Application # onCreate()`中调用`MultiLanguageService # init(Context ctx)`</br>
在杀死进程，下次进入时，仍使用上次切换的语言。例如上次切换为阿拉伯语，则杀死进程，再启动时仍为阿拉伯语。如果不调用会在下次启动时为跟随系统语言

```java
public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        MultiLanguageService.INSTANCE.init(this);
    }
}
```

## 拓展语言类型

目前已支持16种语言，如果不满足可以调用拓展方法`MultiLanguageService # setExpandLocal()`，语言类型拓展可继承
该方法需要在调用 `changeLanguage()` 前调用！

```kotlin
MultiLanguageService.setExpandLocal { languageType ->
    return@setExpandLocal when (languageType) {
        //朝鲜
        ExpandLanguage.CX -> {
            Locale("chaoxian", "CX")
        }
        //刚果
        ExpandLanguage.GG -> {
            Locale("gangguo", "GG")
        }
        //其他语言
        else -> {
            Locale(LanguageType.LANGUAGE_SYSTEM)
        }
    }
}
```

当前支持语言：

| 语言     | 字段名称        | 字段值  |
| -------- | --------------- | ------- |
| 跟随系统 | LANGUAGE_SYSTEM | system  |
| 英文     | LANGUAGE_EN     | en      |
| 简体中文 | LANGUAGE_ZH_CN  | zh-Hans |
| 繁体中文 | LANGUAGE_ZH_TW  | zh-Hant |
| 泰语     | LANGUAGE_TH     | th      |
| 日语     | LANGUAGE_JA     | ja      |
| 韩语     | LANGUAGE_KO     | ko      |
| 阿拉伯语 | LANGUAGE_AR     | ar      |
| 印尼语   | LANGUAGE_IN     | id      |
| 西班牙语 | LANGUAGE_ES     | es      |
| 葡萄牙语 | LANGUAGE_PT     | pt      |
| 越南语   | LANGUAGE_VI     | vi      |
| 俄语     | LANGUAGE_RU     | ru      |
| 土耳其语 | LANGUAGE_TR     | tr      |
| 德语     | LANGUAGE_DE     | de      |
| 法语     | LANGUAGE_FR     | fr      |
| 意大利语 | LANGUAGE_IT     | it      |

## 切换指定语言

通过调用 `MultiLanguageService # (context: Context, language: String)` 可以切换为指定语言，入参`language`可以使用`LanguageType`类中的常量。

```kotlin
findViewById<Button>(R.id.btn_change_jp).setOnClickListener {
  //切换语言
  MultiLanguageService.changeLanguage(this, LanguageType.LANGUAGE_JA)
  //重启MainActivity
  startActivity(Intent(this, MainActivity::class.java).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK))
}
```

## 适配切换语言

- 对于Activity，需要在 `attachBaseContext()` 方法中调用``MultiLanguageService # changeContextLocale(context: Context)`

- 其他场景(Dialog等)，需要在展示第一步调用`MultiLanguageService # changeContextLocale(context: Context)`

- **如果不提前调用`changeContextLocale()`，可能在调用`切换指定语言` 时出现无效问题。**

```kotlin
//Activity
class MainActivity2 : AppCompatActivity() {

    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(MultiLanguageService.changeContextLocale(newBase))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)
    }
}

//Dialog
findViewById<Button>(R.id.btn_jump_dialog).setOnClickListener {
  MultiLanguageService.changeContextLocale(this)
  Dialog(this).apply {
    setContentView(R.layout.dialog_main)
    show()
  }
}
```

## 获取当前语言

调用`MultiLanguageService # getCurrentLanguage()` 可以获取当前切换的语言

```kotlin
findViewById<Button>(R.id.btn_show).text = "当前语言(${MultiLanguageService.getCurrentLanguage()})"
```

