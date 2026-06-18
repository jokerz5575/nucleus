package hu.clinvio.ui.components.basic;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CvStepperTest {

    @Test
    void getComponentTypeReturnsStepper() {
        CvStepper stepper = new CvStepper();
        assertEquals("stepper", stepper.getComponentType());
    }

    @Test
    void ofFactoryReturnsInstance() {
        CvStepper stepper = CvStepper.of();
        assertNotNull(stepper);
    }

    @Test
    void addStepCreatesStepWithTitleAndDescription() {
        CvStepper stepper = CvStepper.of();
        stepper.addStep("Step 1", "First step");
        assertEquals(1, stepper.getSteps().size());
        assertEquals("Step 1", stepper.getSteps().get(0).getTitle());
        assertEquals("First step", stepper.getSteps().get(0).getDescription());
        assertEquals("pending", stepper.getSteps().get(0).getStatus());
    }

    @Test
    void currentStepMarkCompletedMarkActive() {
        CvStepper stepper = CvStepper.of();
        stepper.addStep("One", "");
        stepper.addStep("Two", "");
        stepper.currentStep(1);
        stepper.markCompleted(0);
        stepper.markActive(1);
        assertEquals(1, stepper.getCurrentStep());
        assertEquals("completed", stepper.getSteps().get(0).getStatus());
        assertEquals("active", stepper.getSteps().get(1).getStatus());
    }

    @Test
    void getTemplateModelContainsSteps() {
        CvStepper stepper = CvStepper.of();
        stepper.addStep("One", "First");
        var model = stepper.getTemplateModel();
        assertNotNull(model.get("steps"));
        assertEquals(1, ((java.util.List<?>) model.get("steps")).size());
    }
}
