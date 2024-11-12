package com.dionich.gofundme.payments.recurring.exception

import com.dionich.gofundme.payments.recurring.model.Donation
import com.dionich.gofundme.payments.recurring.model.Donor

class DonationLimitExceededException(donor: Donor, donation: Donation) : Exception("This donation ($donation) exceeds donor limit ($${donor.limit}).")