package hu.clinvio.ui.components.basic;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CvStatCardTest {

    @Test
    void getComponentTypeReturnsStatCard() {
        CvStatCard card = new CvStatCard();
        assertEquals("statCard", card.getComponentType());
    }

    @Test
    void ofFactoryCreatesWithValueAndLabel() {
        CvStatCard card = CvStatCard.of("1,234", "Users");
        assertEquals("1,234", card.getValue());
        assertEquals("Users", card.getLabel());
    }

    @Test
    void fluentSettersReturnThis() {
        CvStatCard card = CvStatCard.of("50", "Orders");
        assertSame(card, card.variant(CvStatCard.StatVariant.SUCCESS));
        assertSame(card, card.delta("+12%", CvStatCard.DeltaType.POSITIVE));
        assertSame(card, card.icon("bi-graph"));
        assertEquals(CvStatCard.StatVariant.SUCCESS, card.getVariant());
        assertEquals("+12%", card.getDelta());
        assertEquals(CvStatCard.DeltaType.POSITIVE, card.getDeltaType());
        assertEquals("bi-graph", card.getIcon());
    }

    @Test
    void statVariantCssClasses() {
        assertEquals("", CvStatCard.StatVariant.PRIMARY.getCssClass());
        assertEquals("cv-stat-success", CvStatCard.StatVariant.SUCCESS.getCssClass());
        assertEquals("cv-stat-warning", CvStatCard.StatVariant.WARNING.getCssClass());
        assertEquals("cv-stat-danger", CvStatCard.StatVariant.DANGER.getCssClass());
        assertEquals("cv-stat-accent", CvStatCard.StatVariant.ACCENT.getCssClass());
    }

    @Test
    void deltaTypeCssClasses() {
        assertEquals("positive", CvStatCard.DeltaType.POSITIVE.getCssClass());
        assertEquals("negative", CvStatCard.DeltaType.NEGATIVE.getCssClass());
        assertEquals("neutral", CvStatCard.DeltaType.NEUTRAL.getCssClass());
    }

    @Test
    void getTemplateModelContainsExpectedKeys() {
        CvStatCard card = CvStatCard.of("99", "Issues")
                .variant(CvStatCard.StatVariant.DANGER)
                .delta("-5", CvStatCard.DeltaType.NEGATIVE);
        var model = card.getTemplateModel();
        assertEquals("99", model.get("value"));
        assertEquals("Issues", model.get("label"));
        assertEquals("-5", model.get("delta"));
        assertEquals(CvStatCard.StatVariant.DANGER, model.get("variant"));
    }
}
