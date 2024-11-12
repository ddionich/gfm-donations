package com.dionich.gofundme.payments.recurring.service

import com.dionich.gofundme.payments.recurring.exception.DonationLimitExceededException
import com.dionich.gofundme.payments.recurring.exception.EntityNotFoundException
import com.dionich.gofundme.payments.recurring.model.Campaign
import com.dionich.gofundme.payments.recurring.model.Donation
import com.dionich.gofundme.payments.recurring.model.Donor
import com.dionich.gofundme.payments.recurring.repository.CampaignRepository
import com.dionich.gofundme.payments.recurring.repository.DonationRepository

class DonationService(
    private val donationRepository: DonationRepository,
    private val donorService: DonorService,
    private val campaignRepository: CampaignRepository,
    private val donorValidationService: DonorValidationService
) {
    fun findByCampaign(campaign: Campaign): List<Donation> = donationRepository.findByCampaign(campaign)

    fun save(donation: Donation) {
        verifyDonationRequirements(donation)
        donationRepository.save(donation)
    }

    private fun verifyDonationRequirements(donation: Donation) {
        val donor =
            donorService[donation.donor.id] ?: throw throw EntityNotFoundException(Donor::class.java, donation.donor.id)

        if (!donorValidationService.canDonate(donor, donation.amount)) {
            throw DonationLimitExceededException(donor, donation)
        }

        campaignRepository[donation.campaign.id] ?: throw EntityNotFoundException(
            entityClass = Campaign::class.java,
            id = donation.campaign.id
        )
    }
}
