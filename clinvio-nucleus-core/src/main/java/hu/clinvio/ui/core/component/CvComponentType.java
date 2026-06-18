package hu.clinvio.ui.core.component;

/**
 * Enumeration of all built-in Clinvio UI component types.
 */
public enum CvComponentType {

    BUTTON("button", "components/button"),
    TEXT_FIELD("textField", "components/text-field"),
    SELECT("select", "components/select"),
    SEARCHABLE_SELECT("searchableSelect", "components/searchable-select"),
    CHECKBOX("checkbox", "components/checkbox"),
    TOGGLE("toggle", "components/toggle"),
    CARD("card", "components/card"),
    STAT_CARD("statCard", "components/stat-card"),
    DATA_TABLE("dataTable", "components/data-table"),
    ALERT("alert", "components/alert"),
    TOAST("toast", "components/toast"),
    BADGE("badge", "components/badge"),
    AVATAR("avatar", "components/avatar"),
    MODAL("modal", "components/modal"),
    TABS("tabs", "components/tabs"),
    BREADCRUMB("breadcrumb", "components/breadcrumb"),
    STEPPER("stepper", "components/stepper"),
    TIMELINE("timeline", "components/timeline"),
    DROPDOWN("dropdown", "components/dropdown"),
    PROGRESS_BAR("progressBar", "components/progress-bar"),
    NAVBAR("navbar", "components/navbar"),
    SIDEBAR("sidebar", "components/sidebar"),
    WEEKLY_SCHEDULE("weeklySchedule", "components/weekly-schedule");

    private final String componentType;
    private final String templatePath;

    CvComponentType(String componentType, String templatePath) {
        this.componentType = componentType;
        this.templatePath = templatePath;
    }

    public String getType() {
        return componentType;
    }

    public String getDefaultTemplate() {
        return templatePath;
    }

    public static CvComponentType fromType(String type) {
        for (CvComponentType ct : values()) {
            if (ct.componentType.equals(type)) {
                return ct;
            }
        }
        throw new IllegalArgumentException("Unknown component type: " + type);
    }
}
