package com.dionich.gofundme.payments.recurring.di

/**
 * This object provides basic Dependency Injection capabilities for storing and resolving instances of specified service types.
 * It allows registration of service instances with their corresponding service types and resolves instances based on the provided type.
 */
@Suppress("UNCHECKED_CAST")
object DI {
    private val registry = mutableMapOf<Class<*>, Any>()
    private val lazyRegistry = mutableMapOf<Class<*>, () -> Any>()

    /**
     * Registers a service instance associated with the specified service type.
     *
     * @param serviceType the class representing the service type
     * @param instance the instance of the service to register
     */
    fun <T : Any> register(serviceType: Class<T>, instance: T) = serviceType.let { registry[it] = instance }

    fun <T : Any> registerLazy(serviceType: Class<T>, provider: () -> T) {
        lazyRegistry[serviceType] = provider
    }

    /**
     * Retrieves the instance associated with the specified service type from the service registry.
     *
     * @param serviceType the class representing the service type to resolve
     * @return the instance of the service associated with the provided service type
     */
    fun <T> resolve(serviceType: Class<T>): T {
        return (registry[serviceType] ?: lazyRegistry[serviceType]?.invoke()?.also {
            registry[serviceType] = it
        }) as T
    }

}