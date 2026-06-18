package hu.clinvio.ui.components.basic;

import hu.clinvio.ui.core.component.AbstractCvComponent;
import hu.clinvio.ui.core.component.CvComponentType;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CvBreadcrumb extends AbstractCvComponent {

    private List<BreadcrumbItem> items = new ArrayList<>();

    public static CvBreadcrumb of() {
        return new CvBreadcrumb();
    }

    public CvBreadcrumb addItem(String label, String url) {
        items.add(new BreadcrumbItem(label, url, false));
        return this;
    }

    public CvBreadcrumb addActive(String label) {
        items.add(new BreadcrumbItem(label, null, true));
        return this;
    }

    @Override
    public String getComponentType() { return CvComponentType.BREADCRUMB.getType(); }

    @Override
    public String getTemplate() { return "cv/components/breadcrumb"; }

    @Override
    public Map<String, Object> getTemplateModel() {
        Map<String, Object> model = super.getTemplateModel();
        model.put("items", items);
        return model;
    }

    public List<BreadcrumbItem> getItems() { return items; }

    public static class BreadcrumbItem {
        private final String label;
        private final String url;
        private final boolean active;

        public BreadcrumbItem(String label, String url, boolean active) {
            this.label = label;
            this.url = url;
            this.active = active;
        }

        public String getLabel() { return label; }
        public String getUrl() { return url; }
        public boolean isActive() { return active; }
    }
}
