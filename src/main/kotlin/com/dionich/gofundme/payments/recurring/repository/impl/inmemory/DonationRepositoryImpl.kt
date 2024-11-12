package com.dionich.gofundme.payments.recurring.repository.impl.inmemory

import com.dionich.gofundme.payments.recurring.model.Campaign
import com.dionich.gofundme.payments.recurring.model.Donation
import com.dionich.gofundme.payments.recurring.model.Donor
import com.dionich.gofundme.payments.recurring.repository.DonationRepository
import com.dionich.gofundme.payments.recurring.repository.common.InMemoryRepository

class DonationRepositoryImpl : DonationRepository, InMemoryRepository<Donation, String>() {
    override fun findByCampaign(campaign: Campaign) = super.storage.values.filter { it.campaign == campaign }
    override fun findByDonor(donor: Donor): List<Donation> = super.storage.values.filter { it.donor ==  donor}
}
