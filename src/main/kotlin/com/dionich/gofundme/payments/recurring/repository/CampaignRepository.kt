package com.dionich.gofundme.payments.recurring.repository

import com.dionich.gofundme.payments.recurring.model.Campaign
import com.dionich.gofundme.payments.recurring.repository.common.Repository

interface CampaignRepository : Repository<Campaign, String>