package com.dionich.gofundme.payments.recurring.dto.report

import com.dionich.gofundme.payments.recurring.model.Donor
import com.dionich.gofundme.payments.recurring.utils.format

data class DonorSummaryReport(val donor: Donor, val totalDonated: Double, val averageDonation: Double) {
    override fun toString(): String = "${donor.name}: Total $${totalDonated.format()} Average $${averageDonation.format()}"
}