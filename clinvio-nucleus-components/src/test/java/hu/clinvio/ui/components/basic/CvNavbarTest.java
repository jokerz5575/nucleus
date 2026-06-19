package hu.clinvio.ui.components.basic;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CvNavbarTest {

    @Test
    void getComponentTypeReturnsNavbar() {
        CvNavbar navbar = new CvNavbar();
        assertEquals("navbar", navbar.getComponentType());
    }

    @Test
    void fluentSettersReturnThisAndSetState() {
        CvNavbar navbar = CvNavbar.of("Clinvio");
        var links = List.of(new CvNavbar.NavLink("Home", "/"));

        assertSame(navbar, navbar.brandUrl("/home"));
        assertSame(navbar, navbar.links(links));
        assertSame(navbar, navbar.userMenu("Profile"));

        assertEquals("/home", navbar.getBrandUrl());
        assertEquals(links, navbar.getLinks());
        assertEquals("Profile", navbar.getUserMenu());
    }

    @Test
    void addLinkStoresLink() {
        CvNavbar navbar = CvNavbar.of("Clinvio").addLink(new CvNavbar.NavLink("Home", "/", true));

        assertEquals(1, navbar.getLinks().size());
        assertTrue(navbar.getLinks().get(0).isActive());
    }

    @Test
    void getTemplateModelContainsNavbarState() {
        CvNavbar navbar = CvNavbar.of("Clinvio").brandUrl("/home").addLink(new CvNavbar.NavLink("Home", "/"));
        var model = navbar.getTemplateModel();

        assertEquals("Clinvio", model.get("brand"));
        assertEquals("/home", model.get("brandUrl"));
        assertEquals(1, ((List<?>) model.get("links")).size());
        assertNotNull(navbar.getTemplate());
    }
}
