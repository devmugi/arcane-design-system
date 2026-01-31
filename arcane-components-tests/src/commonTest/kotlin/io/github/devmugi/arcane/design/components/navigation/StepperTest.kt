package io.github.devmugi.arcane.design.components.navigation

import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.kotest.matchers.types.shouldBeInstanceOf
import kotlin.test.Test

class StepperTest {

    // ==========================================================================
    // ArcaneStepState enum tests
    // ==========================================================================

    @Test
    fun `ArcaneStepState has three values`() {
        val states = ArcaneStepState.entries
        states.size shouldBe 3
    }

    @Test
    fun `ArcaneStepState values are in correct order`() {
        val states = ArcaneStepState.entries
        states[0] shouldBe ArcaneStepState.Pending
        states[1] shouldBe ArcaneStepState.Active
        states[2] shouldBe ArcaneStepState.Completed
    }

    @Test
    fun `ArcaneStepState Pending has ordinal 0`() {
        ArcaneStepState.Pending.ordinal shouldBe 0
    }

    @Test
    fun `ArcaneStepState Active has ordinal 1`() {
        ArcaneStepState.Active.ordinal shouldBe 1
    }

    @Test
    fun `ArcaneStepState Completed has ordinal 2`() {
        ArcaneStepState.Completed.ordinal shouldBe 2
    }

    @Test
    fun `ArcaneStepState name property returns correct string`() {
        ArcaneStepState.Pending.name shouldBe "Pending"
        ArcaneStepState.Active.name shouldBe "Active"
        ArcaneStepState.Completed.name shouldBe "Completed"
    }

    @Test
    fun `ArcaneStepState can be retrieved by name`() {
        ArcaneStepState.valueOf("Pending") shouldBe ArcaneStepState.Pending
        ArcaneStepState.valueOf("Active") shouldBe ArcaneStepState.Active
        ArcaneStepState.valueOf("Completed") shouldBe ArcaneStepState.Completed
    }

    // ==========================================================================
    // ArcaneStep data class tests - default values
    // ==========================================================================

    @Test
    fun `ArcaneStep can be created with only label`() {
        val step = ArcaneStep(label = "Step 1")
        step.label shouldBe "Step 1"
        step.description shouldBe null
        step.state shouldBe ArcaneStepState.Pending
    }

    @Test
    fun `ArcaneStep can be created with label and description`() {
        val step = ArcaneStep(
            label = "Step 1",
            description = "First step description"
        )
        step.label shouldBe "Step 1"
        step.description shouldBe "First step description"
        step.state shouldBe ArcaneStepState.Pending
    }

    @Test
    fun `ArcaneStep can be created with all parameters`() {
        val step = ArcaneStep(
            label = "Step 1",
            description = "First step",
            state = ArcaneStepState.Active
        )
        step.label shouldBe "Step 1"
        step.description shouldBe "First step"
        step.state shouldBe ArcaneStepState.Active
    }

    @Test
    fun `ArcaneStep default state is Pending`() {
        val step = ArcaneStep(label = "Test")
        step.state shouldBe ArcaneStepState.Pending
    }

    // ==========================================================================
    // ArcaneStep data class equality tests
    // ==========================================================================

    @Test
    fun `ArcaneStep instances with same values are equal`() {
        val step1 = ArcaneStep(
            label = "Step 1",
            description = "Description",
            state = ArcaneStepState.Active
        )
        val step2 = ArcaneStep(
            label = "Step 1",
            description = "Description",
            state = ArcaneStepState.Active
        )
        step1 shouldBe step2
    }

    @Test
    fun `ArcaneStep instances with different labels are not equal`() {
        val step1 = ArcaneStep(label = "Step 1")
        val step2 = ArcaneStep(label = "Step 2")
        step1 shouldNotBe step2
    }

    @Test
    fun `ArcaneStep instances with different descriptions are not equal`() {
        val step1 = ArcaneStep(label = "Step 1", description = "First")
        val step2 = ArcaneStep(label = "Step 1", description = "Second")
        step1 shouldNotBe step2
    }

    @Test
    fun `ArcaneStep instances with different states are not equal`() {
        val step1 = ArcaneStep(label = "Step 1", state = ArcaneStepState.Pending)
        val step2 = ArcaneStep(label = "Step 1", state = ArcaneStepState.Completed)
        step1 shouldNotBe step2
    }

    @Test
    fun `ArcaneStep with null description equals another with null description`() {
        val step1 = ArcaneStep(label = "Step 1", description = null)
        val step2 = ArcaneStep(label = "Step 1", description = null)
        step1 shouldBe step2
    }

    // ==========================================================================
    // ArcaneStep copy tests
    // ==========================================================================

    @Test
    fun `ArcaneStep copy creates independent instance`() {
        val original = ArcaneStep(
            label = "Step 1",
            description = "Description",
            state = ArcaneStepState.Pending
        )
        val copy = original.copy()
        copy shouldBe original
    }

    @Test
    fun `ArcaneStep copy with modified state preserves other values`() {
        val original = ArcaneStep(
            label = "Step 1",
            description = "Description",
            state = ArcaneStepState.Pending
        )
        val updated = original.copy(state = ArcaneStepState.Completed)

        updated.label shouldBe "Step 1"
        updated.description shouldBe "Description"
        updated.state shouldBe ArcaneStepState.Completed
    }

    @Test
    fun `ArcaneStep copy with modified label preserves other values`() {
        val original = ArcaneStep(
            label = "Step 1",
            description = "Description",
            state = ArcaneStepState.Active
        )
        val updated = original.copy(label = "Updated Step")

        updated.label shouldBe "Updated Step"
        updated.description shouldBe "Description"
        updated.state shouldBe ArcaneStepState.Active
    }

    // ==========================================================================
    // ArcaneStep hashCode tests
    // ==========================================================================

    @Test
    fun `ArcaneStep instances with same values have same hashCode`() {
        val step1 = ArcaneStep(
            label = "Step 1",
            description = "Description",
            state = ArcaneStepState.Active
        )
        val step2 = ArcaneStep(
            label = "Step 1",
            description = "Description",
            state = ArcaneStepState.Active
        )
        step1.hashCode() shouldBe step2.hashCode()
    }

    @Test
    fun `ArcaneStep can be used in sets`() {
        val steps = setOf(
            ArcaneStep(label = "Step 1"),
            ArcaneStep(label = "Step 1"),  // duplicate
            ArcaneStep(label = "Step 2"),
            ArcaneStep(label = "Step 3")
        )
        steps.size shouldBe 3
    }

    @Test
    fun `ArcaneStep can be used as map key`() {
        val stepData = mapOf(
            ArcaneStep(label = "Step 1") to "First",
            ArcaneStep(label = "Step 2") to "Second"
        )
        stepData[ArcaneStep(label = "Step 1")] shouldBe "First"
        stepData[ArcaneStep(label = "Step 2")] shouldBe "Second"
    }

    // ==========================================================================
    // ArcaneStep toString tests
    // ==========================================================================

    @Test
    fun `ArcaneStep toString contains all properties`() {
        val step = ArcaneStep(
            label = "Test Step",
            description = "Test Description",
            state = ArcaneStepState.Active
        )
        val str = step.toString()
        str shouldBe "ArcaneStep(label=Test Step, description=Test Description, state=Active)"
    }

    @Test
    fun `ArcaneStep toString with null description`() {
        val step = ArcaneStep(label = "Test Step")
        val str = step.toString()
        str shouldBe "ArcaneStep(label=Test Step, description=null, state=Pending)"
    }

    // ==========================================================================
    // ArcaneStepperOrientation sealed class tests
    // ==========================================================================

    @Test
    fun `ArcaneStepperOrientation Horizontal is a singleton object`() {
        val orientation1 = ArcaneStepperOrientation.Horizontal
        val orientation2 = ArcaneStepperOrientation.Horizontal
        orientation1 shouldBe orientation2
    }

    @Test
    fun `ArcaneStepperOrientation Vertical is a singleton object`() {
        val orientation1 = ArcaneStepperOrientation.Vertical
        val orientation2 = ArcaneStepperOrientation.Vertical
        orientation1 shouldBe orientation2
    }

    @Test
    fun `ArcaneStepperOrientation Horizontal and Vertical are different`() {
        val horizontal = ArcaneStepperOrientation.Horizontal
        val vertical = ArcaneStepperOrientation.Vertical
        horizontal shouldNotBe vertical
    }

    @Test
    fun `ArcaneStepperOrientation variants are subtypes of ArcaneStepperOrientation`() {
        val orientations: List<ArcaneStepperOrientation> = listOf(
            ArcaneStepperOrientation.Horizontal,
            ArcaneStepperOrientation.Vertical
        )
        orientations.forEach { orientation ->
            orientation.shouldBeInstanceOf<ArcaneStepperOrientation>()
        }
    }

    @Test
    fun `ArcaneStepperOrientation can be matched exhaustively with when expression`() {
        val orientations = listOf(
            ArcaneStepperOrientation.Horizontal,
            ArcaneStepperOrientation.Vertical
        )

        val results = orientations.map { orientation ->
            when (orientation) {
                is ArcaneStepperOrientation.Horizontal -> "horizontal"
                is ArcaneStepperOrientation.Vertical -> "vertical"
            }
        }

        results shouldBe listOf("horizontal", "vertical")
    }

    @Test
    fun `two distinct orientation variants exist`() {
        val orientations = setOf(
            ArcaneStepperOrientation.Horizontal::class,
            ArcaneStepperOrientation.Vertical::class
        )
        orientations.size shouldBe 2
    }

    // ==========================================================================
    // Step list validation tests (simulating component behavior)
    // ==========================================================================

    @Test
    fun `step list can have multiple steps with different states`() {
        val steps = listOf(
            ArcaneStep(label = "Step 1", state = ArcaneStepState.Completed),
            ArcaneStep(label = "Step 2", state = ArcaneStepState.Active),
            ArcaneStep(label = "Step 3", state = ArcaneStepState.Pending)
        )

        steps.size shouldBe 3
        steps[0].state shouldBe ArcaneStepState.Completed
        steps[1].state shouldBe ArcaneStepState.Active
        steps[2].state shouldBe ArcaneStepState.Pending
    }

    @Test
    fun `step list index bounds are valid with forEachIndexed`() {
        val steps = listOf(
            ArcaneStep(label = "Step 1"),
            ArcaneStep(label = "Step 2"),
            ArcaneStep(label = "Step 3")
        )

        val indices = mutableListOf<Int>()
        steps.forEachIndexed { index, _ ->
            indices.add(index)
        }

        indices shouldBe listOf(0, 1, 2)
        indices.first() shouldBe 0
        indices.last() shouldBe steps.lastIndex
    }

    @Test
    fun `empty step list has valid lastIndex of -1`() {
        val steps = emptyList<ArcaneStep>()
        steps.lastIndex shouldBe -1
    }

    @Test
    fun `single step list has lastIndex of 0`() {
        val steps = listOf(ArcaneStep(label = "Only Step"))
        steps.lastIndex shouldBe 0
    }

    @Test
    fun `step number is 1-indexed from list index`() {
        val steps = listOf(
            ArcaneStep(label = "First"),
            ArcaneStep(label = "Second"),
            ArcaneStep(label = "Third")
        )

        steps.forEachIndexed { index, _ ->
            val stepNumber = index + 1
            stepNumber shouldBe index + 1
        }
    }

    @Test
    fun `can find active step index in list`() {
        val steps = listOf(
            ArcaneStep(label = "Step 1", state = ArcaneStepState.Completed),
            ArcaneStep(label = "Step 2", state = ArcaneStepState.Active),
            ArcaneStep(label = "Step 3", state = ArcaneStepState.Pending)
        )

        val activeIndex = steps.indexOfFirst { it.state == ArcaneStepState.Active }
        activeIndex shouldBe 1
    }

    @Test
    fun `can count completed steps in list`() {
        val steps = listOf(
            ArcaneStep(label = "Step 1", state = ArcaneStepState.Completed),
            ArcaneStep(label = "Step 2", state = ArcaneStepState.Completed),
            ArcaneStep(label = "Step 3", state = ArcaneStepState.Active),
            ArcaneStep(label = "Step 4", state = ArcaneStepState.Pending)
        )

        val completedCount = steps.count { it.state == ArcaneStepState.Completed }
        completedCount shouldBe 2
    }

    @Test
    fun `previous step state can be checked for connector styling`() {
        val steps = listOf(
            ArcaneStep(label = "Step 1", state = ArcaneStepState.Completed),
            ArcaneStep(label = "Step 2", state = ArcaneStepState.Active),
            ArcaneStep(label = "Step 3", state = ArcaneStepState.Pending)
        )

        // Simulating connector logic: connector between steps[0] and steps[1]
        val previousCompleted = steps[0].state == ArcaneStepState.Completed
        previousCompleted shouldBe true

        // Connector between steps[1] and steps[2]
        val currentCompleted = steps[1].state == ArcaneStepState.Completed
        currentCompleted shouldBe false
    }
}
