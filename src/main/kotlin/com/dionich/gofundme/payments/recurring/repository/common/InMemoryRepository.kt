package com.dionich.gofundme.payments.recurring.repository.common

import com.dionich.gofundme.payments.recurring.model.Identifiable


abstract class InMemoryRepository<Entity : Identifiable<Id>, Id> : Repository<Entity, Id> {
    protected val storage: MutableMap<Id, Entity> = mutableMapOf()

    override fun save(entity: Entity): Entity = entity.also { storage[it.id] = it }

    override fun remove(entity: Entity) : Unit = entity.let { storage.remove(it.id) }

    override fun removeAll() : Unit = storage.clear()

    override fun findAll(): List<Entity> = storage.values.toList()

    override fun findById(id: Id): Entity? = storage[id]
}
