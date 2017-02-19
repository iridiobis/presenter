package es.iridiobis.presenter

abstract class Presenter<V> {
    var view: V? = null
        private set

    fun attach(view: V) {
        if (this.view != null) {
            throw IllegalStateException("Already attached to a view")
        }
        this.view = view
        onViewAttached()
    }

    fun detach(view: V) {
        if (this.view == null) {
            throw IllegalStateException("Detaching a presenter not attached to a view")
        } else if (this.view !== view) {
            throw IllegalStateException("This is not the attached view")
        }
        beforeViewDetached()
        this.view = null
    }

    fun hasView() = view != null

    abstract protected fun onViewAttached()

    open protected fun beforeViewDetached() {
        //Empty by default, could be necessary to override on children
    }

}
