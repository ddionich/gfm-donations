package com.dionich.gofundme.payments.recurring.utils

import com.dionich.gofundme.payments.recurring.exception.EntityNotFoundException
import com.dionich.gofundme.payments.recurring.model.Campaign
import com.dionich.gofundme.payments.recurring.model.Donation
import com.dionich.gofundme.payments.recurring.model.Donor
import com.dionich.gofundme.payments.recurring.service.CampaignService
import com.dionich.gofundme.payments.recurring.service.DonationService
import com.dionich.gofundme.payments.recurring.service.DonorService

class CommandProcessor(
    private val campaignService: CampaignService,
    private val donationService: DonationService,
    private val donorService: DonorService,
) {

    fun processCommands(commands: List<String>) {
        commands.forEach { line ->
            val parts = line.split(" ")
            val commandType = parts[0]
            when (commandType) {
                "Add" -> processAddCommand(parts)
                "Donate" -> processDonateCommand(parts)
            }
        }
        printSummary()
    }

    private fun processAddCommand(parts: List<String>) {
        when (parts[1]) {
            "Donor" -> {
                val name = parts[2]
                val limit = parts[3].trim('$').toDouble()
                val donor = Donor(name = name, limit = limit)
                donorService.add(donor)
            }

            "Campaign" -> {
                val name = parts[2]
                val campaign = Campaign(name = name)
                campaignService.add(campaign)
            }
        }
    }

    private fun processDonateCommand(parts: List<String>) {
        val donorId = parts[1]
        val campaignId = parts[2]
        val amount = parts[3].trim('$').toDouble()

        val donor = donorService[donorId] ?: throw EntityNotFoundException(Donor::class.java, donorId)
        val campaign = campaignService[campaignId] ?: throw EntityNotFoundException(Campaign::class.java, campaignId)

        donationService.save(Donation(campaign = campaign, donor = donor, amount = amount))
    }

    private fun printSummary() {
        println("Donors:")
        donorService.findAll().map { donor -> donorService.generateSummaryReport(donor) }.forEach { println(it) }
        println("\nCampaigns:")
        campaignService.findAll().map { campaign -> campaignService.generateSummaryReport(campaign) }
            .forEach { println(it) }
    }

}