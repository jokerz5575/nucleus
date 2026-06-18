package hu.clinvio.ui.components.basic;

import hu.clinvio.ui.core.component.AbstractCvComponent;
import hu.clinvio.ui.core.component.CvComponentType;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Clinvio Card component.
 * Maps to Bootstrap 5 card with Clinvio theme styling.
 */
@Getter
@Setter
public class CvCard extends AbstractCvComponent {

    private String title;
    private String subtitle;
    private String body;
    private String footer;
    private CardVariant variant;
    private String headerColor;
    private List<CardAction> actions = new ArrayList<>();
    private String hxGet;
    private String hxTarget;

    public CvCard() {
        super();
        this.variant = CardVariant.DEFAULT;
    }

    public CvCard(String title) {
        super();
        this.title = title;
        this.variant = CardVariant.DEFAULT;
    }

    public static CvCard of(String title) {
        return new CvCard(title);
    }

    public CvCard variant(CardVariant variant) {
        this.variant = variant;
        return this;
    }

    public CvCard body(String body) {
        this.body = body;
        return this;
    }

    public CvCard subtitle(String subtitle) {
        this.subtitle = subtitle;
        return this;
    }

    public CvCard addAction(String label, String url) {
        this.actions.add(new CardAction(label, url));
        return this;
    }

    @Override
    public String getComponentType() {
        return CvComponentType.CARD.getType();
    }

    @Override
    public String getTemplate() {
        return CvComponentType.CARD.getDefaultTemplate();
    }

    @Override
    public Map<String, Object> getTemplateModel() {
        Map<String, Object> model = super.getTemplateModel();
        model.put("title", title);
        model.put("subtitle", subtitle);
        model.put("body", body);
        model.put("footer", footer);
        model.put("variant", variant);
        model.put("headerColor", headerColor);
        model.put("actions", actions);
        model.put("hxGet", hxGet);
        model.put("hxTarget", hxTarget);
        return model;
    }

    public enum CardVariant {
        DEFAULT("cv-card"),
        FLAT("cv-card-flat"),
        HOVER("cv-card cv-card-hover"),
        TINTED_PRIMARY("cv-card-tinted cv-card-tinted-primary"),
        TINTED_SUCCESS("cv-card-tinted cv-card-tinted-success"),
        TINTED_WARNING("cv-card-tinted cv-card-tinted-warning"),
        TINTED_DANGER("cv-card-tinted cv-card-tinted-danger");

        private final String cssClass;

        CardVariant(String cssClass) {
            this.cssClass = cssClass;
        }

        public String getCssClass() {
            return cssClass;
        }
    }

    @Getter
    @Setter
    public static class CardAction {
        private final String label;
        private final String url;
        private String hxTarget;

        public CardAction(String label, String url) {
            this.label = label;
            this.url = url;
        }
    }
}
