package com.dionich.gofundme.payments.recurring.di

@Suppress("UNCHECKED_CAST")
object DI {
    private val registry = mutableMapOf<Class<*>, Any>()
    private val lazyRegistry = mutableMapOf<Class<*>, () -> Any>()

    fun <T : Any> register(serviceType: Class<T>, instance: T) = serviceType.let { registry[it] = instance }

    fun <T : Any> registerLazy(serviceType: Class<T>, provider: () -> T) {
        lazyRegistry[serviceType] = provider
    }

    fun <T> resolve(serviceType: Class<T>): T {
        return (registry[serviceType] ?: lazyRegistry[serviceType]?.invoke()?.also {
            registry[serviceType] = it
        }) as T
    }

}