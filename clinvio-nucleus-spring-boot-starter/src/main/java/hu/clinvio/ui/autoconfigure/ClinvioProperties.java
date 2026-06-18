package hu.clinvio.ui.autoconfigure;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Configuration properties for Clinvio UI Framework.
 */
@ConfigurationProperties(prefix = ClinvioProperties.PREFIX)
public class ClinvioProperties {

    public static final String PREFIX = "clinvio.ui";

    private boolean enabled = true;
    private Theme theme = new Theme();
    private Htmx htmx = new Htmx();
    private Components components = new Components();

    public boolean isEnabled() { return enabled; }
    public void setEnabled(boolean enabled) { this.enabled = enabled; }

    public Theme getTheme() { return theme; }
    public void setTheme(Theme theme) { this.theme = theme; }

    public Htmx getHtmx() { return htmx; }
    public void setHtmx(Htmx htmx) { this.htmx = htmx; }

    public Components getComponents() { return components; }
    public void setComponents(Components components) { this.components = components; }

    public static class Theme {
        private String defaultTheme = "light";
        private boolean switchable = true;
        private boolean cookieBased = true;

        public String getDefaultTheme() { return defaultTheme; }
        public void setDefaultTheme(String defaultTheme) { this.defaultTheme = defaultTheme; }

        public boolean isSwitchable() { return switchable; }
        public void setSwitchable(boolean switchable) { this.switchable = switchable; }

        public boolean isCookieBased() { return cookieBased; }
        public void setCookieBased(boolean cookieBased) { this.cookieBased = cookieBased; }
    }

    public static class Htmx {
        private boolean enabled = true;
        private String version = "2.0.4";
        private String defaultSwap = "innerHTML";

        public boolean isEnabled() { return enabled; }
        public void setEnabled(boolean enabled) { this.enabled = enabled; }

        public String getVersion() { return version; }
        public void setVersion(String version) { this.version = version; }

        public String getDefaultSwap() { return defaultSwap; }
        public void setDefaultSwap(String defaultSwap) { this.defaultSwap = defaultSwap; }
    }

    public static class Components {
        private String idPrefix = "cv";
        private boolean stateful = true;

        public String getIdPrefix() { return idPrefix; }
        public void setIdPrefix(String idPrefix) { this.idPrefix = idPrefix; }

        public boolean isStateful() { return stateful; }
        public void setStateful(boolean stateful) { this.stateful = stateful; }
    }
}
