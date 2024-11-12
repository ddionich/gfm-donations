package com.dionich.gofundme.payments.recurring.repository

import com.dionich.gofundme.payments.recurring.model.Campaign
import com.dionich.gofundme.payments.recurring.model.Donation
import com.dionich.gofundme.payments.recurring.model.Donor
import com.dionich.gofundme.payments.recurring.repository.common.Repository

interface DonationRepository : Repository<Donation, String> {
    fun findByCampaign(campaign: Campaign): List<Donation>
    fun findByDonor(donor: Donor): List<Donation>
}
