package com.dionich.gofundme.payments.recurring.utils

import com.dionich.gofundme.payments.recurring.di.DI
import com.dionich.gofundme.payments.recurring.di.initializeDependencies
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class CommandProcessorTest {

    lateinit var commandProcessor: CommandProcessor

    @BeforeEach
    fun setUp() {
        initializeDependencies()
        commandProcessor = DI.resolve(CommandProcessor::class.java)
    }

    @Test
    fun `normal case should work`() {
        val list = mutableListOf(
            "Add Donor Greg \$1000",
            "Add Donor Janine \$100",
            "Add Campaign SaveTheDogs",
            "Add Campaign HelpTheKids",
            "Donate Greg SaveTheDogs \$100",
            "Donate Greg HelpTheKids \$200",
            "Donate Janine SaveTheDogs \$50",
        )
        commandProcessor.processCommands(list)
    }

}