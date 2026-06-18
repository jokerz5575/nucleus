package hu.clinvio.ui.components.basic;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CvCardTest {

    @Test
    void getComponentTypeReturnsCard() {
        CvCard card = new CvCard();
        assertEquals("card", card.getComponentType());
    }

    @Test
    void ofFactoryCreatesCardWithTitle() {
        CvCard card = CvCard.of("Test Title");
        var model = card.getTemplateModel();
        assertEquals("Test Title", model.get("title"));
    }

    @Test
    void fluentSettersReturnThis() {
        CvCard card = CvCard.of("Title");
        assertSame(card, card.variant(CvCard.CardVariant.FLAT));
        assertSame(card, card.body("<p>Body</p>"));
        assertSame(card, card.subtitle("Sub"));
        var model = card.getTemplateModel();
        assertEquals(CvCard.CardVariant.FLAT, model.get("variant"));
        assertEquals("<p>Body</p>", model.get("body"));
        assertEquals("Sub", model.get("subtitle"));
    }

    @Test
    void addActionAddsToActionsList() {
        CvCard card = CvCard.of("Title");
        card.addAction("Details", "/details");
        var model = card.getTemplateModel();
        var actions = (java.util.List<?>) model.get("actions");
        assertNotNull(actions);
        assertEquals(1, actions.size());
    }

    @Test
    void getTemplateModelContainsExpectedKeys() {
        CvCard card = CvCard.of("Title").body("Body").variant(CvCard.CardVariant.FLAT);
        card.addAction("Action", "/action");
        var model = card.getTemplateModel();
        assertEquals("Title", model.get("title"));
        assertEquals("Body", model.get("body"));
        assertEquals(CvCard.CardVariant.FLAT, model.get("variant"));
        assertNotNull(model.get("actions"));
    }

    @Test
    void defaultVariantIsDefault() {
        CvCard card = new CvCard();
        var model = card.getTemplateModel();
        assertEquals(CvCard.CardVariant.DEFAULT, model.get("variant"));
    }

    @Test
    void cardActionHasLabelAndUrl() {
        CvCard card = CvCard.of("Title");
        card.addAction("Action", "/url");
        var model = card.getTemplateModel();
        var actions = (java.util.List<?>) model.get("actions");
        assertNotNull(actions);
        assertEquals(1, actions.size());
        assertTrue(actions.get(0) instanceof CvCard.CardAction);
    }
}
