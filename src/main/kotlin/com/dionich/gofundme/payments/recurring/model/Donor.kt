package com.dionich.gofundme.payments.recurring.model

data class Donor(val name: String, val limit: Double) : Identifiable<String> {
    init {
        require(limit > 0) { "Limit must be greater than 0" }
    }

    override val id: String get() = name
}