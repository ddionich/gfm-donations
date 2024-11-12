package com.dionich.gofundme.payments.recurring.repository.impl.inmemory

import com.dionich.gofundme.payments.recurring.model.Donor
import com.dionich.gofundme.payments.recurring.repository.DonorRepository
import com.dionich.gofundme.payments.recurring.repository.common.InMemoryRepository

class DonorRepositoryImpl : DonorRepository, InMemoryRepository<Donor, String>()