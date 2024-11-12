package com.dionich.gofundme.payments.recurring.model

data class Campaign(val name: String) : Identifiable<String> {
    override val id: String get() = name
}
