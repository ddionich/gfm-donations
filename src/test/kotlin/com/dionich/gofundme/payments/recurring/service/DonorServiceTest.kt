package com.dionich.gofundme.payments.recurring.service

import com.dionich.gofundme.payments.recurring.model.Campaign
import com.dionich.gofundme.payments.recurring.model.Donation
import com.dionich.gofundme.payments.recurring.model.Donor
import com.dionich.gofundme.payments.recurring.repository.DonationRepository
import com.dionich.gofundme.payments.recurring.repository.DonorRepository
import com.dionich.gofundme.payments.recurring.repository.impl.inmemory.DonationRepositoryImpl
import com.dionich.gofundme.payments.recurring.repository.impl.inmemory.DonorRepositoryImpl
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

import kotlin.test.assertNotNull

class DonorServiceTest {
    private lateinit var mockDonorRepository: DonorRepository
    private lateinit var mockDonationRepository: DonationRepository
    private lateinit var donorService: DonorService

    @BeforeTest
    fun setup() {
        mockDonorRepository = DonorRepositoryImpl()
        mockDonationRepository = DonationRepositoryImpl()
        donorService = DonorService(mockDonorRepository, mockDonationRepository)
    }

    @Test
    fun `add donor test`() {
        val donor = Donor(name = "Dylan", limit = 100.0)
        val result = donorService.add(donor)
        assertEquals(expected = result, actual = donor)
    }

    @Test
    fun `find donor test`() {
        val donor = Donor(name = "Dylan", limit = 100.0)
        donorService.add(donor)
        val result = donorService["Dylan"]
        assertEquals(expected = result, actual = donor)
    }

    @Test
    fun `find all donors test`() {
        val donor1 = Donor(name = "Dylan", limit = 100.0)
        val donor2 = Donor(name = "John", limit = 50.0)
        donorService.add(donor1)
        donorService.add(donor2)
        val result = donorService.findAll()
        assertEquals(expected = result, actual = listOf(donor1, donor2))
    }

    @Test
    fun `generate summary report test`() {
        val donor = Donor(name = "Dylan", limit = 100.0)
        val campaign1 = Campaign("HireDylan")
        val campaign2 = Campaign("DoNotHireJohn")
        donorService.add(donor)
        mockDonationRepository.save(Donation(campaign1, donor, 12.0))
        mockDonationRepository.save(Donation(campaign2, donor, 18.0))
        val report = donorService.generateSummaryReport(donor)
        assertNotNull(report)
        assertEquals(expected = report.totalDonated, actual = 30.0)
        assertEquals(expected = report.averageDonation, actual = 15.0)
    }

}