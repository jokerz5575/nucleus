package hu.clinvio.ui.htmx.response;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Response builder for HTMX partial page updates.
 */
public class HxResponse {

    private final String content;
    private final String fragment;
    private final Map<String, Object> model;
    private final HxSwap swap;
    private final String target;
    private final String redirect;
    private final String trigger;
    private final Map<String, Object> triggerDetail;
    private final String pushUrl;
    private final boolean preventSwap;

    private HxResponse(Builder builder) {
        this.content = builder.content;
        this.fragment = builder.fragment;
        this.model = builder.model != null ? builder.model : Collections.emptyMap();
        this.swap = builder.swap != null ? builder.swap : HxSwap.INNER_HTML;
        this.target = builder.target;
        this.redirect = builder.redirect;
        this.trigger = builder.trigger;
        this.triggerDetail = builder.triggerDetail != null ? builder.triggerDetail : Collections.emptyMap();
        this.pushUrl = builder.pushUrl;
        this.preventSwap = builder.preventSwap;
    }

    public static Builder builder() {
        return new Builder();
    }

    // Getters
    public String getContent() { return content; }
    public String getFragment() { return fragment; }
    public Map<String, Object> getModel() { return model; }
    public HxSwap getSwap() { return swap; }
    public String getTarget() { return target; }
    public String getRedirect() { return redirect; }
    public String getTrigger() { return trigger; }
    public Map<String, Object> getTriggerDetail() { return triggerDetail; }
    public String getPushUrl() { return pushUrl; }
    public boolean isPreventSwap() { return preventSwap; }

    public static class Builder {
        private String content;
        private String fragment;
        private Map<String, Object> model;
        private HxSwap swap;
        private String target;
        private String redirect;
        private String trigger;
        private Map<String, Object> triggerDetail;
        private String pushUrl;
        private boolean preventSwap;

        public Builder content(String content) {
            this.content = content;
            return this;
        }

        public Builder fragment(String fragment) {
            this.fragment = fragment;
            return this;
        }

        public Builder model(Map<String, Object> model) {
            this.model = model;
            return this;
        }

        public Builder swap(HxSwap swap) {
            this.swap = swap;
            return this;
        }

        public Builder target(String target) {
            this.target = target;
            return this;
        }

        public Builder redirect(String redirect) {
            this.redirect = redirect;
            return this;
        }

        public Builder trigger(String trigger) {
            this.trigger = trigger;
            return this;
        }

        public Builder trigger(String trigger, Map<String, Object> detail) {
            this.trigger = trigger;
            this.triggerDetail = detail;
            return this;
        }

        public Builder triggerDetail(Map<String, Object> triggerDetail) {
            this.triggerDetail = triggerDetail;
            return this;
        }

        public Builder pushUrl(String pushUrl) {
            this.pushUrl = pushUrl;
            return this;
        }

        public Builder preventSwap(boolean preventSwap) {
            this.preventSwap = preventSwap;
            return this;
        }

        public HxResponse build() {
            if (trigger != null && triggerDetail != null && !triggerDetail.isEmpty()
                    && !trigger.contains(":")) {
                this.triggerDetail = triggerDetail;
            }
            return new HxResponse(this);
        }
    }
}
