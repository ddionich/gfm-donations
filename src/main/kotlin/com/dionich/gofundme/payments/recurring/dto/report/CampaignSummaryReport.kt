package com.dionich.gofundme.payments.recurring.dto.report

import com.dionich.gofundme.payments.recurring.model.Campaign
import com.dionich.gofundme.payments.recurring.utils.format

data class CampaignSummaryReport(val campaign: Campaign, val amountReceived: Double) {
    override fun toString(): String = "${campaign.name}: Total $${amountReceived.format()}"
}