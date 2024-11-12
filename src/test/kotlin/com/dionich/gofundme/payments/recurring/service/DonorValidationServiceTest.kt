package com.dionich.gofundme.payments.recurring.service

import com.dionich.gofundme.payments.recurring.model.Campaign
import com.dionich.gofundme.payments.recurring.model.Donation
import com.dionich.gofundme.payments.recurring.model.Donor
import com.dionich.gofundme.payments.recurring.repository.DonationRepository
import io.mockk.every
import io.mockk.mockk
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

/**
 * Tests for [DonorValidationService].
 */
internal class DonorValidationServiceTest {

    private lateinit var donationRepository: DonationRepository

    private lateinit var donorValidationService: DonorValidationService

    @BeforeTest
    fun setUp() {
        donationRepository = mockk()
        donorValidationService = DonorValidationService(donationRepository)
    }

    /**
     * Testing [DonorValidationService.canDonate] method.
     */
    @Test
    fun `canDonate returns true when the donor has not reached his limit`() {
        val donor = Donor("Peter", 60.0)
        val previousDonations = listOf(Donation(donor = donor, amount = 40.0, campaign = Campaign("HireDylan")))
        every { donationRepository.findByDonor(donor) } returns previousDonations

        val canDonate = donorValidationService.canDonate(donor, 10.0)

        assertTrue { canDonate }
    }

    @Test
    fun `canDonate returns false when adding donation would exceed donor's limit`() {
        val donor = Donor("John", 100.0)
        val previousDonations = listOf(Donation(donor = donor, amount = 40.0, campaign = Campaign("HireDylan")))
        every { donationRepository.findByDonor(donor) } returns previousDonations

        val canDonate = donorValidationService.canDonate(donor, 60.01)

        assertFalse { canDonate }
    }

    @Test
    fun `canDonate returns true when the donor has exactly reached his limit`() {
        val donor = Donor("Michael", 100.0)
        val previousDonations = listOf(Donation(donor = donor, amount = 40.0, campaign = Campaign("HireDylan")))
        every { donationRepository.findByDonor(donor) } returns previousDonations

        val canDonate = donorValidationService.canDonate(donor, 60.0)

        assertTrue { canDonate }
    }
}