package hu.clinvio.ui.components.basic;

import hu.clinvio.ui.core.component.AbstractCvComponent;
import hu.clinvio.ui.core.component.CvComponentType;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CvNavbar extends AbstractCvComponent {

    private String brand;
    private String brandUrl = "#";
    private List<NavLink> links = new ArrayList<>();
    private String userMenu;

    public CvNavbar() {
        super();
    }

    public CvNavbar(String brand) {
        super();
        this.brand = brand;
    }

    public static CvNavbar of(String brand) {
        return new CvNavbar(brand);
    }

    public CvNavbar brandUrl(String brandUrl) { this.brandUrl = brandUrl; return this; }
    public CvNavbar addLink(NavLink link) { this.links.add(link); return this; }
    public CvNavbar links(List<NavLink> links) { this.links = links; return this; }
    public CvNavbar userMenu(String userMenu) { this.userMenu = userMenu; return this; }

    public String getBrand() { return brand; }
    public void setBrand(String brand) { this.brand = brand; }
    public String getBrandUrl() { return brandUrl; }
    public void setBrandUrl(String brandUrl) { this.brandUrl = brandUrl; }
    public List<NavLink> getLinks() { return links; }
    public void setLinks(List<NavLink> links) { this.links = links; }
    public String getUserMenu() { return userMenu; }
    public void setUserMenu(String userMenu) { this.userMenu = userMenu; }

    @Override
    public String getComponentType() {
        return CvComponentType.NAVBAR.getType();
    }

    @Override
    public String getTemplate() {
        return CvComponentType.NAVBAR.getDefaultTemplate();
    }

    @Override
    public Map<String, Object> getTemplateModel() {
        Map<String, Object> model = super.getTemplateModel();
        model.put("brand", brand);
        model.put("brandUrl", brandUrl);
        model.put("links", links);
        model.put("userMenu", userMenu);
        return model;
    }

    public static class NavLink {
        private final String label;
        private final String url;
        private boolean active;

        public NavLink(String label, String url) {
            this.label = label;
            this.url = url;
        }

        public NavLink(String label, String url, boolean active) {
            this.label = label;
            this.url = url;
            this.active = active;
        }

        public String getLabel() { return label; }
        public String getUrl() { return url; }
        public boolean isActive() { return active; }
        public void setActive(boolean active) { this.active = active; }
    }
}
