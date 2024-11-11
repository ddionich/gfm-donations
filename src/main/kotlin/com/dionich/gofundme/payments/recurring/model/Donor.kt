package com.dionich.gofundme.payments.recurring.model

data class Donor(val name: String, val limit: Int, var totalDonated: Int = 0) {
    private val donations = mutableListOf<Int>()

    fun donate(amount: Int): Boolean {
        if (totalDonated + amount <= limit) {
            donations.add(amount)
            totalDonated += amount
            return true
        }
        return false
    }

    fun averageDonation(): Double = if (donations.isEmpty()) 0.0 else donations.average()
}