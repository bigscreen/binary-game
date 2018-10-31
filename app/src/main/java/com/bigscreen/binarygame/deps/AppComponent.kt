package com.bigscreen.binarygame.deps

import com.bigscreen.binarygame.MainActivity
import com.bigscreen.binarygame.storage.StorageModule
import com.bigscreen.binarygame.menu.MenuActivity
import com.bigscreen.binarygame.misc.MiscModule
import com.bigscreen.binarygame.score.ScoreActivity
import com.bigscreen.binarygame.setting.SettingActivity
import com.bigscreen.binarygame.setting.SettingComponent
import com.bigscreen.binarygame.setting.SettingModule
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [
    AppModule::class,
    StorageModule::class,
    MiscModule::class
])
interface AppComponent {

    fun inject(activity: MainActivity)

    fun inject(activity: MenuActivity)

    fun inject(activity: SettingActivity)

    fun inject(activity: ScoreActivity)

    fun plus(module: SettingModule): SettingComponent
}