package com.dionich.gofundme.payments.recurring.repository

import com.dionich.gofundme.payments.recurring.model.Donor
import com.dionich.gofundme.payments.recurring.repository.common.Repository

interface DonorRepository : Repository<Donor, String>