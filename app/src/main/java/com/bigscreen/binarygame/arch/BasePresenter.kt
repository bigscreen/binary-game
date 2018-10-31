package com.bigscreen.binarygame.arch

import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

abstract class BasePresenter<in M, V : BaseView<M>> {
    protected var s = CompositeDisposable()
    private lateinit var view: V

    fun attach(v: V) {
        this.view = v
        onAttach()
    }

    fun detach() {
        s.clear()
        onDetach()
    }

    fun view(): V = view

    protected fun observe(execute: () -> Disposable) {
        s.add(execute())
    }

    protected open fun onAttach() {}
    protected open fun onDetach() {}
}
