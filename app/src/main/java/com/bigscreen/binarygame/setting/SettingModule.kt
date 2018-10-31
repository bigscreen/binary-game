package com.bigscreen.binarygame.setting

import dagger.Module
import dagger.Provides
import dagger.Subcomponent

@Subcomponent(modules = [(SettingModule::class)])
interface SettingComponent {
    fun inject(activityDelegate: SettingActivityDelegate)
}

@Module
class SettingModule {

    @Provides
    fun providesPresenter(presenter: SettingPresenter): SettingContract.Presenter = presenter
}