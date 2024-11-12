package com.dionich.gofundme.payments.recurring.model

data class Donation(val campaign: Campaign, val donor: Donor, val amount: Double) : Identifiable<String> {
    init { require(amount > 0) { "Amount must be greater than 0" } }
    override val id: String get() = "${campaign.name}_${donor.name}"
    override fun toString(): String = "Donation(campaign=${campaign.id}, donor=${donor.id}, amount=$amount)"
}
