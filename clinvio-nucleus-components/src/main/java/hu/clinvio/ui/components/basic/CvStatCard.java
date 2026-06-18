package hu.clinvio.ui.components.basic;

import hu.clinvio.ui.core.component.AbstractCvComponent;
import hu.clinvio.ui.core.component.CvComponentType;

import java.util.Map;

/**
 * Clinvio Stat Card component.
 * Maps to the cv-stat-card CSS class for dashboard statistics.
 */
public class CvStatCard extends AbstractCvComponent {

    private String value;
    private String label;
    private String delta;
    private DeltaType deltaType;
    private StatVariant variant;
    private String icon;
    private String hxGet;
    private String hxTarget;

    public CvStatCard() {
        super();
        this.variant = StatVariant.PRIMARY;
        this.deltaType = DeltaType.NEUTRAL;
    }

    public static CvStatCard of(String value, String label) {
        CvStatCard card = new CvStatCard();
        card.setValue(value);
        card.setLabel(label);
        return card;
    }

    public CvStatCard variant(StatVariant variant) {
        this.variant = variant;
        return this;
    }

    public CvStatCard delta(String delta, DeltaType type) {
        this.delta = delta;
        this.deltaType = type;
        return this;
    }

    public CvStatCard icon(String icon) {
        this.icon = icon;
        return this;
    }

    // Getters and Setters
    public String getValue() { return value; }
    public void setValue(String value) { this.value = value; }

    public String getLabel() { return label; }
    public void setLabel(String label) { this.label = label; }

    public String getDelta() { return delta; }
    public void setDelta(String delta) { this.delta = delta; }

    public DeltaType getDeltaType() { return deltaType; }
    public void setDeltaType(DeltaType deltaType) { this.deltaType = deltaType; }

    public StatVariant getVariant() { return variant; }
    public void setVariant(StatVariant variant) { this.variant = variant; }

    public String getIcon() { return icon; }
    public void setIcon(String icon) { this.icon = icon; }

    public String getHxGet() { return hxGet; }
    public void setHxGet(String hxGet) { this.hxGet = hxGet; }

    public String getHxTarget() { return hxTarget; }
    public void setHxTarget(String hxTarget) { this.hxTarget = hxTarget; }

    @Override
    public String getComponentType() {
        return CvComponentType.STAT_CARD.getType();
    }

    @Override
    public String getTemplate() {
        return CvComponentType.STAT_CARD.getDefaultTemplate();
    }

    @Override
    public Map<String, Object> getTemplateModel() {
        Map<String, Object> model = super.getTemplateModel();
        model.put("value", value);
        model.put("label", label);
        model.put("delta", delta);
        model.put("deltaType", deltaType);
        model.put("variant", variant);
        model.put("icon", icon);
        model.put("hxGet", hxGet);
        model.put("hxTarget", hxTarget);
        return model;
    }

    public enum StatVariant {
        PRIMARY(""),
        SUCCESS("cv-stat-success"),
        WARNING("cv-stat-warning"),
        DANGER("cv-stat-danger"),
        ACCENT("cv-stat-accent");

        private final String cssClass;

        StatVariant(String cssClass) {
            this.cssClass = cssClass;
        }

        public String getCssClass() {
            return cssClass;
        }
    }

    public enum DeltaType {
        POSITIVE("positive"),
        NEGATIVE("negative"),
        NEUTRAL("neutral");

        private final String cssClass;

        DeltaType(String cssClass) {
            this.cssClass = cssClass;
        }

        public String getCssClass() {
            return cssClass;
        }
    }
}
