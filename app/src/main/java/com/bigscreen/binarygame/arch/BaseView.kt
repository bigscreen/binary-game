package com.bigscreen.binarygame.arch

import android.arch.lifecycle.LifecycleObserver
import io.reactivex.Observable

interface BaseView<in M> : LifecycleObserver {
    fun showData(model: M): Observable<out UserAction>
    fun showError(error: Throwable): Observable<out UserAction> = Observable.never()
    fun showLoading() {}
    fun back() {}
    fun actions(): Observable<UserAction> = Observable.never()
}

interface BaseDelegatingView<in M> : BaseView<M> {
    val activityDelegate: BaseView<M>

    override fun actions(): Observable<UserAction> = activityDelegate.actions()
    override fun showData(model: M) = activityDelegate.showData(model)
    override fun showError(error: Throwable) = activityDelegate.showError(error)

    override fun showLoading() = activityDelegate.showLoading()
    override fun back() = activityDelegate.back()
}
