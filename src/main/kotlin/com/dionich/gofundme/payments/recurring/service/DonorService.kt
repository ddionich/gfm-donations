package com.dionich.gofundme.payments.recurring.service

import com.dionich.gofundme.payments.recurring.dto.report.DonorSummaryReport
import com.dionich.gofundme.payments.recurring.model.Donor
import com.dionich.gofundme.payments.recurring.repository.DonationRepository
import com.dionich.gofundme.payments.recurring.repository.DonorRepository

class DonorService(
    private val donorRepository: DonorRepository,
    private val donationRepository: DonationRepository,
) {

    fun add(donor: Donor) = donorRepository[donor.id] ?: donorRepository.save(donor)

    operator fun get(id: String) = donorRepository[id]

    fun findAll(): List<Donor> = donorRepository.findAll()

    fun generateSummaryReport(donor: Donor): DonorSummaryReport {
        val donations = donationRepository.findByDonor(donor)
        val totalDonated = donations.sumOf { it.amount }
        return DonorSummaryReport(
            donor = donor,
            totalDonated = totalDonated,
            averageDonation = totalDonated / donations.size
        )
    }

}