package hu.clinvio.ui.components.basic;

import hu.clinvio.ui.core.component.AbstractCvComponent;
import hu.clinvio.ui.core.component.CvComponentType;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CvSidebar extends AbstractCvComponent {

    private List<SidebarItem> items = new ArrayList<>();
    private boolean collapsed;

    public CvSidebar() {
        super();
    }

    public static CvSidebar create() {
        return new CvSidebar();
    }

    public CvSidebar addItem(SidebarItem item) { this.items.add(item); return this; }
    public CvSidebar items(List<SidebarItem> items) { this.items = items; return this; }
    public CvSidebar collapsed(boolean collapsed) { this.collapsed = collapsed; return this; }

    public List<SidebarItem> getItems() { return items; }
    public void setItems(List<SidebarItem> items) { this.items = items; }
    public boolean isCollapsed() { return collapsed; }
    public void setCollapsed(boolean collapsed) { this.collapsed = collapsed; }

    @Override
    public String getComponentType() {
        return CvComponentType.SIDEBAR.getType();
    }

    @Override
    public String getTemplate() {
        return CvComponentType.SIDEBAR.getDefaultTemplate();
    }

    @Override
    public Map<String, Object> getTemplateModel() {
        Map<String, Object> model = super.getTemplateModel();
        model.put("items", items);
        model.put("collapsed", collapsed);
        return model;
    }

    public static class SidebarItem {
        private final String label;
        private final String url;
        private String icon;
        private boolean active;
        private List<SidebarItem> subItems;

        public SidebarItem(String label, String url) {
            this.label = label;
            this.url = url;
        }

        public SidebarItem(String label, String url, String icon) {
            this.label = label;
            this.url = url;
            this.icon = icon;
        }

        public SidebarItem icon(String icon) { this.icon = icon; return this; }
        public SidebarItem active(boolean active) { this.active = active; return this; }
        public SidebarItem addSubItem(SidebarItem subItem) {
            if (this.subItems == null) this.subItems = new ArrayList<>();
            this.subItems.add(subItem);
            return this;
        }

        public String getLabel() { return label; }
        public String getUrl() { return url; }
        public String getIcon() { return icon; }
        public boolean isActive() { return active; }
        public void setActive(boolean active) { this.active = active; }
        public List<SidebarItem> getSubItems() { return subItems; }
        public void setSubItems(List<SidebarItem> subItems) { this.subItems = subItems; }
    }
}
