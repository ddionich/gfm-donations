package com.dionich.gofundme.payments.recurring.repository.impl.inmemory

import com.dionich.gofundme.payments.recurring.model.Campaign
import com.dionich.gofundme.payments.recurring.repository.CampaignRepository
import com.dionich.gofundme.payments.recurring.repository.common.InMemoryRepository

class CampaignRepositoryImpl : CampaignRepository, InMemoryRepository<Campaign, String>()