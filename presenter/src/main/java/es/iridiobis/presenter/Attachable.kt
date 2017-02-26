package es.iridiobis.presenter


interface Attachable<in V> {
    fun attach(view : V)
    fun detach(view : V)
}