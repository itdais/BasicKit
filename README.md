# BasicKit
androidx版本的项目依赖库，基于android Jetpack，提供MVVM的kotlin依赖库

gradle引入
``` gradle
allprojects {
		repositories {
			maven { url 'https://jitpack.io' }
		}
}
```
当前版本
``` gradle
dependencies {
    implementation 'com.github.itdais:BasicKit:0.1.0'
}
```

## 项目简介

项目依赖了：
>    //jetpack
     api 'androidx.appcompat:appcompat:1.2.0'
     api "androidx.recyclerview:recyclerview:1.1.0"
     api 'com.google.android.material:material:1.2.1'
     api "androidx.lifecycle:lifecycle-extensions:2.2.0"
     api "androidx.lifecycle:lifecycle-livedata-ktx:2.2.0"
     api "androidx.lifecycle:lifecycle-viewmodel-ktx:2.2.0"
     api 'androidx.lifecycle:lifecycle-runtime-ktx:2.2.0'
     //kotlin
     api 'androidx.core:core-ktx:1.3.2'
     api "org.jetbrains.kotlin:kotlin-stdlib:$kotlin_version"
     //协程
     api 'org.jetbrains.kotlinx:kotlinx-coroutines-android:1.3.9'
     api "org.jetbrains.kotlinx:kotlinx-coroutines-core:1.3.9"
     // gson
     api 'com.google.code.gson:gson:2.8.5'

项目使用到的权限
``` xml
<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
<uses-permission android:name="android.permission.KILL_BACKGROUND_PROCESSES" />
```

## 项目使用

依赖库四大类，如下：1
base：BaseActvitiy，BaseMvvmActvitiy，BaseFragment,BaseMvvmFragment，BaseViewModel
util：工具类
widet：控件类
adapter：提供简单的CommonAdapter(),TabLayout的TablayoutAdapter()

如果未使用rxjava，而是使用了协程coroutines
在viewmodel中可以直接使用viewModelScope.launch或者viewModelScope.async