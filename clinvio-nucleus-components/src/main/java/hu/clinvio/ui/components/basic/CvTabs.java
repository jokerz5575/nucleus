package hu.clinvio.ui.components.basic;

import hu.clinvio.ui.core.component.AbstractCvComponent;
import hu.clinvio.ui.core.component.CvComponentType;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CvTabs extends AbstractCvComponent {

    private List<Tab> tabs = new ArrayList<>();
    private int activeIndex = 0;

    public static CvTabs of() {
        return new CvTabs();
    }

    public CvTabs addTab(String id, String label, String content) {
        tabs.add(new Tab(id, label, content));
        return this;
    }

    public CvTabs activeIndex(int index) { this.activeIndex = index; return this; }

    @Override
    public String getComponentType() { return CvComponentType.TABS.getType(); }

    @Override
    public String getTemplate() { return "cv/components/tabs"; }

    @Override
    public Map<String, Object> getTemplateModel() {
        Map<String, Object> model = super.getTemplateModel();
        model.put("tabs", tabs);
        model.put("activeIndex", activeIndex);
        return model;
    }

    public List<Tab> getTabs() { return tabs; }
    public int getActiveIndex() { return activeIndex; }

    public static class Tab {
        private final String id;
        private final String label;
        private final String content;

        public Tab(String id, String label, String content) {
            this.id = id;
            this.label = label;
            this.content = content;
        }

        public String getId() { return id; }
        public String getLabel() { return label; }
        public String getContent() { return content; }
    }
}
