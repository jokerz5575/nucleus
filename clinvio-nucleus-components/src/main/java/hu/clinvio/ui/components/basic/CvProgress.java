package hu.clinvio.ui.components.basic;

import hu.clinvio.ui.core.component.AbstractCvComponent;
import hu.clinvio.ui.core.component.CvComponentType;

import java.util.Map;

public class CvProgress extends AbstractCvComponent {

    private int value;
    private int max = 100;
    private String variant; // primary, success, warning, danger
    private boolean striped;
    private boolean animated;
    private boolean showLabel;

    public static CvProgress of(int value) {
        CvProgress progress = new CvProgress();
        progress.value = value;
        return progress;
    }

    public CvProgress max(int max) { this.max = max; return this; }
    public CvProgress variant(String variant) { this.variant = variant; return this; }
    public CvProgress striped(boolean striped) { this.striped = striped; return this; }
    public CvProgress animated(boolean animated) { this.animated = animated; return this; }
    public CvProgress showLabel(boolean showLabel) { this.showLabel = showLabel; return this; }

    @Override
    public String getComponentType() { return CvComponentType.PROGRESS_BAR.getType(); }

    @Override
    public String getTemplate() { return "cv/components/progress"; }

    @Override
    public Map<String, Object> getTemplateModel() {
        Map<String, Object> model = super.getTemplateModel();
        model.put("value", value);
        model.put("max", max);
        model.put("percent", max > 0 ? (int) ((double) value / max * 100) : 0);
        model.put("variant", variant);
        model.put("striped", striped);
        model.put("animated", animated);
        model.put("showLabel", showLabel);
        return model;
    }

    public int getValue() { return value; }
    public int getMax() { return max; }
    public String getVariant() { return variant; }
    public boolean isStriped() { return striped; }
    public boolean isAnimated() { return animated; }
    public boolean isShowLabel() { return showLabel; }
}
