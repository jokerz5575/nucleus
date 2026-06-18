package hu.clinvio.ui.components.basic;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CvAvatarTest {

    @Test
    void getComponentTypeReturnsAvatar() {
        CvAvatar avatar = new CvAvatar();
        assertEquals("avatar", avatar.getComponentType());
    }

    @Test
    void ofFactoryCreatesAvatarWithName() {
        CvAvatar avatar = CvAvatar.of("John Doe");
        assertEquals("John Doe", avatar.getName());
    }

    @Test
    void fluentSettersReturnThis() {
        CvAvatar avatar = CvAvatar.of("Test");
        assertSame(avatar, avatar.imageUrl("/img.png"));
        assertSame(avatar, avatar.size("lg"));
        assertSame(avatar, avatar.variant("success"));
        assertEquals("/img.png", avatar.getImageUrl());
        assertEquals("lg", avatar.getSize());
        assertEquals("success", avatar.getVariant());
    }

    @Test
    void initialsExtractionWorks() {
        assertEquals("JD", CvAvatar.of("John Doe").getTemplateModel().get("initials"));
        assertEquals("J", CvAvatar.of("John").getTemplateModel().get("initials"));
    }

    @Test
    void nullOrBlankNameReturnsQuestionMark() {
        assertEquals("?", CvAvatar.of(null).getTemplateModel().get("initials"));
        assertEquals("?", CvAvatar.of("   ").getTemplateModel().get("initials"));
    }

    @Test
    void getTemplateModelContainsExpectedKeys() {
        CvAvatar avatar = CvAvatar.of("Jane Smith").size("xl");
        var model = avatar.getTemplateModel();
        assertEquals("Jane Smith", model.get("name"));
        assertEquals("xl", model.get("size"));
        assertEquals("JS", model.get("initials"));
    }
}
