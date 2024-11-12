package com.dionich.gofundme.payments.recurring.utils

import com.dionich.gofundme.payments.recurring.di.DI
import com.dionich.gofundme.payments.recurring.di.initializeDependencies
import com.dionich.gofundme.payments.recurring.exception.DonationLimitExceededException
import com.dionich.gofundme.payments.recurring.exception.EntityNotFoundException
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows

class CommandProcessorTest {

    private lateinit var commandProcessor: CommandProcessor

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
        assertDoesNotThrow { commandProcessor.processCommands(list) }
    }

    @Test
    fun `blank lines should work`() {
        val list = mutableListOf(
            "Add Donor Greg \$1000",
            "Add Donor Janine \$100",
            "Add Campaign SaveTheDogs",
            "Add Campaign HelpTheKids",
            "",
            "",
            "Donate Greg SaveTheDogs \$100",
            "Donate Greg HelpTheKids \$200",
            "Donate Janine SaveTheDogs \$50",
        )
        assertDoesNotThrow { commandProcessor.processCommands(list) }
    }

    @Test
    fun `wrong commands should be ignored`() {
        val list = mutableListOf(
            "Add Donor Greg \$1000",
            "Add Donor Janine \$100",
            "Add Campaign SaveTheDogs",
            "Add Campaign HelpTheKids",
            "bla bla bla",
            "lol lol lol",
            "Donate Greg SaveTheDogs \$100",
            "Donate Greg HelpTheKids \$200",
            "Donate Janine SaveTheDogs \$50",
        )
        assertDoesNotThrow { commandProcessor.processCommands(list) }
    }

    @Test
    fun `exceeded limit should throw exception`() {
        val list = mutableListOf(
            "Add Donor Greg \$1000",
            "Add Donor Janine \$100",
            "Add Campaign SaveTheDogs",
            "Add Campaign HelpTheKids",
            "Donate Greg SaveTheDogs \$10000",
            "Donate Greg HelpTheKids \$200",
            "Donate Janine SaveTheDogs \$50",
        )
        assertThrows<DonationLimitExceededException> { commandProcessor.processCommands(list) }
    }

    @Test
    fun `campaign not exists should throw exception`() {
        val list = mutableListOf(
            "Add Donor Greg \$1000",
            "Add Donor Janine \$100",
            "Add Campaign SaveTheDogs",
            "Add Campaign HelpTheKids",
            "Donate Greg SaveTheCats \$1000",
            "Donate Greg HelpTheAdults \$200",
            "Donate Janine SaveTheFish \$50",
        )
        assertThrows<EntityNotFoundException> { commandProcessor.processCommands(list) }
    }

}