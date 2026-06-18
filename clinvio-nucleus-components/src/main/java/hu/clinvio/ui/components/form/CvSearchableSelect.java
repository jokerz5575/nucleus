package hu.clinvio.ui.components.form;

import hu.clinvio.ui.core.component.AbstractCvComponent;
import hu.clinvio.ui.core.component.CvComponentType;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CvSearchableSelect extends AbstractCvComponent {

    private String label;
    private String placeholder;
    private List<CvSelect.Option> options = new ArrayList<>();
    private boolean disabled;
    private String helpText;
    private String hxPost;
    private String hxGet;
    private String hxTarget;
    private String hxTrigger;

    public CvSearchableSelect() {
        super();
    }

    public CvSearchableSelect(String label) {
        super();
        this.label = label;
    }

    public static CvSearchableSelect of(String label) {
        return new CvSearchableSelect(label);
    }

    public CvSearchableSelect placeholder(String placeholder) { this.placeholder = placeholder; return this; }
    public CvSearchableSelect addOption(String value, String label) { this.options.add(new CvSelect.Option(value, label)); return this; }
    public CvSearchableSelect options(List<CvSelect.Option> options) { this.options = options; return this; }
    public CvSearchableSelect disabled(boolean disabled) { this.disabled = disabled; return this; }
    public CvSearchableSelect helpText(String helpText) { this.helpText = helpText; return this; }

    public String getLabel() { return label; }
    public void setLabel(String label) { this.label = label; }
    public String getPlaceholder() { return placeholder; }
    public void setPlaceholder(String placeholder) { this.placeholder = placeholder; }
    public List<CvSelect.Option> getOptions() { return options; }
    public void setOptions(List<CvSelect.Option> options) { this.options = options; }
    public boolean isDisabled() { return disabled; }
    public void setDisabled(boolean disabled) { this.disabled = disabled; }
    public String getHelpText() { return helpText; }
    public void setHelpText(String helpText) { this.helpText = helpText; }

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
        return CvComponentType.SEARCHABLE_SELECT.getType();
    }

    @Override
    public String getTemplate() {
        return CvComponentType.SEARCHABLE_SELECT.getDefaultTemplate();
    }

    @Override
    public Map<String, Object> getTemplateModel() {
        Map<String, Object> model = super.getTemplateModel();
        model.put("label", label);
        model.put("placeholder", placeholder);
        model.put("options", options);
        model.put("disabled", disabled);
        model.put("helpText", helpText);
        model.put("hxPost", hxPost);
        model.put("hxGet", hxGet);
        model.put("hxTarget", hxTarget);
        model.put("hxTrigger", hxTrigger);
        return model;
    }
}
