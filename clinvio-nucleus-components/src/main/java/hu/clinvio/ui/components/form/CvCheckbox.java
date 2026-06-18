package hu.clinvio.ui.components.form;

import hu.clinvio.ui.core.component.AbstractCvComponent;
import hu.clinvio.ui.core.component.CvComponentType;

import java.util.Map;

public class CvCheckbox extends AbstractCvComponent {

    private String label;
    private boolean checked;
    private boolean indeterminate;
    private boolean disabled;
    private String helpText;
    private CvTextField.FieldState state;
    private String errorMessage;
    private String hxPost;
    private String hxGet;
    private String hxTarget;
    private String hxTrigger;

    public CvCheckbox() {
        super();
        this.state = CvTextField.FieldState.DEFAULT;
    }

    public CvCheckbox(String label) {
        super();
        this.label = label;
        this.state = CvTextField.FieldState.DEFAULT;
    }

    public static CvCheckbox of(String label) {
        return new CvCheckbox(label);
    }

    public CvCheckbox checked(boolean checked) { this.checked = checked; return this; }
    public CvCheckbox indeterminate(boolean indeterminate) { this.indeterminate = indeterminate; return this; }
    public CvCheckbox disabled(boolean disabled) { this.disabled = disabled; return this; }
    public CvCheckbox helpText(String helpText) { this.helpText = helpText; return this; }
    public CvCheckbox state(CvTextField.FieldState state) { this.state = state; return this; }
    public CvCheckbox errorMessage(String errorMessage) { this.errorMessage = errorMessage; this.state = CvTextField.FieldState.ERROR; return this; }

    public String getLabel() { return label; }
    public void setLabel(String label) { this.label = label; }
    public boolean isChecked() { return checked; }
    public void setChecked(boolean checked) { this.checked = checked; }
    public boolean isIndeterminate() { return indeterminate; }
    public void setIndeterminate(boolean indeterminate) { this.indeterminate = indeterminate; }
    public boolean isDisabled() { return disabled; }
    public void setDisabled(boolean disabled) { this.disabled = disabled; }
    public String getHelpText() { return helpText; }
    public void setHelpText(String helpText) { this.helpText = helpText; }
    public CvTextField.FieldState getState() { return state; }
    public void setState(CvTextField.FieldState state) { this.state = state; }
    public String getErrorMessage() { return errorMessage; }
    public void setErrorMessage(String errorMessage) { this.errorMessage = errorMessage; }

    public String getHxPost() { return hxPost; }
    public void setHxPost(String hxPost) { this.hxPost = hxPost; }
    public String getHxGet() { return hxGet; }
    public void setHxGet(String hxGet) { this.hxGet = hxGet; }
    public String getHxTarget() { return hxTarget; }
    public void setHxTarget(String hxTarget) { this.hxTarget = hxTarget; }
    public String getHxTrigger() { return hxTrigger; }
    public void setHxTrigger(String hxTrigger) { this.hxTrigger = hxTrigger; }

    @Override
    public String getComponentType() {
        return CvComponentType.CHECKBOX.getType();
    }

    @Override
    public String getTemplate() {
        return CvComponentType.CHECKBOX.getDefaultTemplate();
    }

    @Override
    public Map<String, Object> getTemplateModel() {
        Map<String, Object> model = super.getTemplateModel();
        model.put("label", label);
        model.put("checked", checked);
        model.put("indeterminate", indeterminate);
        model.put("disabled", disabled);
        model.put("helpText", helpText);
        model.put("state", state);
        model.put("errorMessage", errorMessage);
        model.put("hxPost", hxPost);
        model.put("hxGet", hxGet);
        model.put("hxTarget", hxTarget);
        model.put("hxTrigger", hxTrigger);
        return model;
    }
}
