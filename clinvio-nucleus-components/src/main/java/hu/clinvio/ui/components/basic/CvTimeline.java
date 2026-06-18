package hu.clinvio.ui.components.basic;

import hu.clinvio.ui.core.component.AbstractCvComponent;
import hu.clinvio.ui.core.component.CvComponentType;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CvTimeline extends AbstractCvComponent {

    private List<TimelineItem> items = new ArrayList<>();

    public static CvTimeline of() {
        return new CvTimeline();
    }

    public CvTimeline addItem(String title, String description, String time, String icon) {
        items.add(new TimelineItem(title, description, time, icon, null));
        return this;
    }

    public CvTimeline addItem(String title, String description, String time, String icon, String status) {
        items.add(new TimelineItem(title, description, time, icon, status));
        return this;
    }

    @Override
    public String getComponentType() { return CvComponentType.TIMELINE.getType(); }

    @Override
    public String getTemplate() { return "cv/components/timeline"; }

    @Override
    public Map<String, Object> getTemplateModel() {
        Map<String, Object> model = super.getTemplateModel();
        model.put("items", items);
        return model;
    }

    public List<TimelineItem> getItems() { return items; }

    public static class TimelineItem {
        private final String title;
        private final String description;
        private final String time;
        private final String icon;
        private final String status;

        public TimelineItem(String title, String description, String time, String icon, String status) {
            this.title = title;
            this.description = description;
            this.time = time;
            this.icon = icon;
            this.status = status;
        }

        public String getTitle() { return title; }
        public String getDescription() { return description; }
        public String getTime() { return time; }
        public String getIcon() { return icon; }
        public String getStatus() { return status; }
    }
}
