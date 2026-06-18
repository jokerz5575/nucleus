package hu.clinvio.ui.components.basic;

import hu.clinvio.ui.core.component.AbstractCvComponent;
import hu.clinvio.ui.core.component.CvComponentType;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CvStepper extends AbstractCvComponent {

    private List<Step> steps = new ArrayList<>();
    private int currentStep = 0;

    public static CvStepper of() {
        return new CvStepper();
    }

    public CvStepper addStep(String title, String description) {
        steps.add(new Step(title, description, "pending"));
        return this;
    }

    public CvStepper currentStep(int currentStep) { this.currentStep = currentStep; return this; }

    public CvStepper markCompleted(int index) {
        if (index >= 0 && index < steps.size()) {
            steps.get(index).status = "completed";
        }
        return this;
    }

    public CvStepper markActive(int index) {
        if (index >= 0 && index < steps.size()) {
            steps.get(index).status = "active";
        }
        return this;
    }

    @Override
    public String getComponentType() { return CvComponentType.STEPPER.getType(); }

    @Override
    public String getTemplate() {         return CvComponentType.STEPPER.getDefaultTemplate(); }

    @Override
    public Map<String, Object> getTemplateModel() {
        Map<String, Object> model = super.getTemplateModel();
        model.put("steps", steps);
        model.put("currentStep", currentStep);
        return model;
    }

    public List<Step> getSteps() { return steps; }
    public int getCurrentStep() { return currentStep; }

    public static class Step {
        private final String title;
        private final String description;
        String status; // pending, active, completed

        public Step(String title, String description, String status) {
            this.title = title;
            this.description = description;
            this.status = status;
        }

        public String getTitle() { return title; }
        public String getDescription() { return description; }
        public String getStatus() { return status; }
    }
}
