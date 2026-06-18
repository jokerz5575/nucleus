package hu.clinvio.ui.components.basic;

import hu.clinvio.ui.core.component.AbstractCvComponent;
import hu.clinvio.ui.core.component.CvComponentType;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CvDropdown extends AbstractCvComponent {

    private String triggerText;
    private String triggerIcon;
    private List<DropdownItem> items = new ArrayList<>();
    private String placement = "bottom-start";

    public static CvDropdown of(String triggerText) {
        CvDropdown dropdown = new CvDropdown();
        dropdown.triggerText = triggerText;
        return dropdown;
    }

    public CvDropdown triggerIcon(String icon) { this.triggerIcon = icon; return this; }
    public CvDropdown placement(String placement) { this.placement = placement; return this; }

    public CvDropdown addItem(String label, String url) {
        items.add(new DropdownItem(label, url, null, false, false));
        return this;
    }

    public CvDropdown addItem(String label, String url, String icon) {
        items.add(new DropdownItem(label, url, icon, false, false));
        return this;
    }

    public CvDropdown addDivider() {
        items.add(new DropdownItem(null, null, null, true, false));
        return this;
    }

    public CvDropdown addHeader(String label) {
        items.add(new DropdownItem(label, null, null, false, true));
        return this;
    }

    @Override
    public String getComponentType() { return "DROPDOWN"; }

    @Override
    public String getTemplate() { return "cv/components/dropdown"; }

    @Override
    public Map<String, Object> getTemplateModel() {
        Map<String, Object> model = super.getTemplateModel();
        model.put("triggerText", triggerText);
        model.put("triggerIcon", triggerIcon);
        model.put("items", items);
        model.put("placement", placement);
        return model;
    }

    public String getTriggerText() { return triggerText; }
    public String getTriggerIcon() { return triggerIcon; }
    public List<DropdownItem> getItems() { return items; }
    public String getPlacement() { return placement; }

    public static class DropdownItem {
        private final String label;
        private final String url;
        private final String icon;
        private final boolean divider;
        private final boolean header;

        public DropdownItem(String label, String url, String icon, boolean divider, boolean header) {
            this.label = label;
            this.url = url;
            this.icon = icon;
            this.divider = divider;
            this.header = header;
        }

        public String getLabel() { return label; }
        public String getUrl() { return url; }
        public String getIcon() { return icon; }
        public boolean isDivider() { return divider; }
        public boolean isHeader() { return header; }
    }
}
