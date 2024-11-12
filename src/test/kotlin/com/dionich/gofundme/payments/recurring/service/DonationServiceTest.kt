package com.dionich.gofundme.payments.recurring.service

import com.dionich.gofundme.payments.recurring.exception.DonationLimitExceededException
import com.dionich.gofundme.payments.recurring.exception.EntityNotFoundException
import com.dionich.gofundme.payments.recurring.model.Campaign
import com.dionich.gofundme.payments.recurring.model.Donation
import com.dionich.gofundme.payments.recurring.model.Donor
import com.dionich.gofundme.payments.recurring.repository.CampaignRepository
import com.dionich.gofundme.payments.recurring.repository.DonationRepository
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.assertDoesNotThrow
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class DonationServiceTest {
    private lateinit var donationService: DonationService
    private lateinit var donorService: DonorService
    private lateinit var donorValidationService: DonorValidationService
    private lateinit var donationRepository: DonationRepository
    private lateinit var campaignRepository: CampaignRepository

    @BeforeEach
    fun setup() {
        donorService = mockk()
        donorValidationService = mockk()
        donationRepository = mockk()
        campaignRepository = mockk()
        donationService = DonationService(donationRepository, donorService, campaignRepository, donorValidationService)
    }

    @Test
    fun `test save donation where donor is not found`() {
        val campaign = Campaign(name = "HireDylan")
        val donation = Donation(campaign = campaign, donor = Donor("1", 200.0), amount = 100.0)
        every { (donorService[donation.donor.id]) } returns (null)

        assertThrows(EntityNotFoundException::class.java) { donationService.save(donation) }
    }

    @Test
    fun `test save donation where donor could not donate`() {
        val campaign = Campaign(name = "HireDylan")
        val donation = Donation(campaign = campaign, donor = Donor("1", 200.0), amount = 100.0)
        every { (donorService[donation.donor.id]) } returns (donation.donor)
        every { (donorValidationService.canDonate(donation.donor, donation.amount)) } returns (false)

        assertThrows(DonationLimitExceededException::class.java) { donationService.save(donation) }
    }

    @Test
    fun `test save donation where campaign is not found`() {
        val campaign = Campaign(name = "HireDylan")
        val donation = Donation(campaign = campaign, donor = Donor("1", 200.0), amount = 100.0)
        every { (donorService[donation.donor.id]) } returns (donation.donor)
        every { (donorValidationService.canDonate(donation.donor, donation.amount)) } returns (true)
        every { (campaignRepository[donation.campaign.id]) } returns (null)

        assertThrows(EntityNotFoundException::class.java) { donationService.save(donation) }
    }

    @Test
    fun `test save valid donation`() {
        val campaign = Campaign(name = "HireDylan")
        val donation = Donation(campaign = campaign, donor = Donor("1", 200.0), amount = 100.0)
        every { (donorService[donation.donor.id]) } returns (donation.donor)
        every { (donorValidationService.canDonate(donation.donor, donation.amount)) } returns (true)
        every { (campaignRepository[donation.campaign.id]) } returns (campaign)
        every { (donationRepository.save(donation)) } returns (donation)

        assertDoesNotThrow { donationService.save(donation) }
    }
}