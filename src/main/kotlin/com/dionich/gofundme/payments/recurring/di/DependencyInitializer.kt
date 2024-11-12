package com.dionich.gofundme.payments.recurring.di

import com.dionich.gofundme.payments.recurring.repository.CampaignRepository
import com.dionich.gofundme.payments.recurring.repository.DonationRepository
import com.dionich.gofundme.payments.recurring.repository.DonorRepository
import com.dionich.gofundme.payments.recurring.repository.impl.inmemory.CampaignRepositoryImpl
import com.dionich.gofundme.payments.recurring.repository.impl.inmemory.DonationRepositoryImpl
import com.dionich.gofundme.payments.recurring.repository.impl.inmemory.DonorRepositoryImpl
import com.dionich.gofundme.payments.recurring.service.CampaignService
import com.dionich.gofundme.payments.recurring.service.DonationService
import com.dionich.gofundme.payments.recurring.service.DonorService
import com.dionich.gofundme.payments.recurring.service.DonorValidationService
import com.dionich.gofundme.payments.recurring.utils.CommandProcessor

fun initializeDependencies() {
    DI.register(CampaignRepository::class.java, CampaignRepositoryImpl())
    DI.register(DonationRepository::class.java, DonationRepositoryImpl())
    DI.register(DonorRepository::class.java, DonorRepositoryImpl())

    DI.registerLazy(CampaignService::class.java) {
        CampaignService(
            campaignRepository = DI.resolve(CampaignRepository::class.java),
            donationService = DI.resolve(DonationService::class.java),
        )
    }

    DI.registerLazy(DonationService::class.java) {
        DonationService(
            donationRepository = DI.resolve(DonationRepository::class.java),
            donorService = DI.resolve(DonorService::class.java),
            campaignRepository = DI.resolve(CampaignRepository::class.java),
            donorValidationService = DI.resolve(DonorValidationService::class.java),
        )
    }

    DI.registerLazy(DonorService::class.java) {
        DonorService(
            donorRepository = DI.resolve(DonorRepository::class.java),
            donationRepository = DI.resolve(DonationRepository::class.java),
        )
    }

    DI.registerLazy(CommandProcessor::class.java) {
        CommandProcessor(
            campaignService = DI.resolve(CampaignService::class.java),
            donorService = DI.resolve(DonorService::class.java),
            donationService = DI.resolve(DonationService::class.java),
        )
    }

    DI.registerLazy(DonorValidationService::class.java) {
        DonorValidationService(
            donationRepository = DI.resolve(DonationRepository::class.java),
        )
    }

}