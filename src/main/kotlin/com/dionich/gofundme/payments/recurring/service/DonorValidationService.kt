package com.dionich.gofundme.payments.recurring.service

import com.dionich.gofundme.payments.recurring.model.Donor
import com.dionich.gofundme.payments.recurring.repository.DonationRepository

class DonorValidationService(private val donationRepository: DonationRepository) {
    fun canDonate(donor: Donor, amount: Double): Boolean = donor.limit >= getTotalDonatedByDonor(donor) + amount

    private fun getTotalDonatedByDonor(donor: Donor): Double = donationRepository.findByDonor(donor).sumOf { it.amount }
}