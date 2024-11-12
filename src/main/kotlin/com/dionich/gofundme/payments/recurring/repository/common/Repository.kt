package com.dionich.gofundme.payments.recurring.repository.common

import com.dionich.gofundme.payments.recurring.model.Identifiable

interface Repository<E : Identifiable<ID>, ID> {
    fun save(entity: E) : E
    fun remove(entity: E)
    fun removeAll()
    fun findAll(): List<E>
    fun findById(id: ID): E?
    operator fun get(id: ID): E? = findById(id)
}