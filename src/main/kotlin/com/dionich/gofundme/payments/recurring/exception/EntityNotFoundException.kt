package com.dionich.gofundme.payments.recurring.exception

import com.dionich.gofundme.payments.recurring.model.Identifiable

class EntityNotFoundException(val entityClass: Class<out Identifiable<*>>, val id: Any) : Exception("Entity not found: ${entityClass.simpleName} with id $id")