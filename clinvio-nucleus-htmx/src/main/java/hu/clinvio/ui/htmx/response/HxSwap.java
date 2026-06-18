package hu.clinvio.ui.htmx.response;

/**
 * HTMX swap strategies.
 */
public enum HxSwap {

    INNER_HTML("innerHTML"),
    OUTER_HTML("outerHTML"),
    BEFORE_BEGIN("beforebegin"),
    AFTER_BEGIN("afterbegin"),
    BEFORE_END("beforeend"),
    AFTER_END("afterend"),
    DELETE("delete"),
    NONE("none");

    private final String value;

    HxSwap(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
