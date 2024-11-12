package com.dionich.gofundme.payments.recurring.service

import com.dionich.gofundme.payments.recurring.dto.report.CampaignSummaryReport
import com.dionich.gofundme.payments.recurring.model.Campaign
import com.dionich.gofundme.payments.recurring.repository.CampaignRepository

/**
 * Service class responsible for managing campaigns and generating reports.
 *
 * @property campaignRepository The repository for managing campaign data.
 * @property donationService The service for managing donations related to campaigns.
 */
class CampaignService(
    private val campaignRepository: CampaignRepository,
    private val donationService: DonationService,
) {

    /**
     * Saves the provided campaign to the repository if it does not already exist.
     * If the campaign with the same ID exists in the repository, it returns that existing campaign.
     *
     * @param campaign the campaign to be saved
     */
    fun add(campaign: Campaign) = campaignRepository[campaign.id] ?: campaignRepository.save(campaign)

    fun findAll(): List<Campaign> = campaignRepository.findAll()

    operator fun get(id: String) = campaignRepository[id]

    /**
     * Generates a summary report for the provided campaign, including the total amount of donations received.
     *
     * @param campaign the campaign for which the summary report is generated
     * @return a CampaignSummaryReport object containing the campaign information and total amount received
     */
    fun generateSummaryReport(campaign: Campaign): CampaignSummaryReport =
        CampaignSummaryReport(campaign, donationService.findByCampaign(campaign).sumOf { it.amount })

}
