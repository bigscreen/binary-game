package com.bigscreen.binarygame.setting

import com.bigscreen.binarygame.arch.BaseDelegatingView
import com.bigscreen.binarygame.arch.BasePresenter
import com.bigscreen.binarygame.arch.BaseView
import com.bigscreen.binarygame.arch.UserAction

class SettingContract {

    interface DelegatingView : BaseDelegatingView<ViewModel>

    interface View : BaseView<ViewModel>

    abstract class Presenter : BasePresenter<ViewModel, View>() {
        abstract fun onUserAction(action: UserAction)
    }

    sealed class ViewModel
}

sealed class HomeUserAction : UserAction
