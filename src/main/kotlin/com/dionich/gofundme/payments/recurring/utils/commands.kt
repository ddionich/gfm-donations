package com.dionich.gofundme.payments.recurring.utils

import com.dionich.gofundme.payments.recurring.model.Campaign
import com.dionich.gofundme.payments.recurring.model.Donor

fun processCommands(commands: List<String>) {
    val donors = mutableMapOf<String, Donor>()
    val campaigns = mutableMapOf<String, Campaign>()

    commands.forEach { line ->
        val parts = line.split(" ")
        when (parts[0]) {
            "Add" -> {
                if (parts[1] == "Donor") {
                    val donor = Donor(parts[2], parts[3].trim('$').toInt())
                    donors[donor.name] = donor
                } else if (parts[1] == "Campaign") {
                    val campaign = Campaign(parts[2])
                    campaigns[campaign.name] = campaign
                }
            }
            "Donate" -> {
                val donor = donors[parts[1]]
                val campaign = campaigns[parts[2]]
                val amount = parts[3].trim('$').toInt()
                if (donor != null && campaign != null && donor.donate(amount)) {
                    campaign.totalReceived += amount
                }
            }
        }
    }

    printSummary(donors, campaigns)
}

fun printSummary(donors: Map<String, Donor>, campaigns: Map<String, Campaign>) {
    println("Donors:")
    donors.toSortedMap().forEach { (name, donor) ->
        println("$name: Total: $${donor.totalDonated} Average: $${donor.averageDonation()}")
    }

    println("\nCampaigns:")
    campaigns.toSortedMap().forEach { (name, campaign) ->
        println("$name: Total: $${campaign.totalReceived}")
    }
}
