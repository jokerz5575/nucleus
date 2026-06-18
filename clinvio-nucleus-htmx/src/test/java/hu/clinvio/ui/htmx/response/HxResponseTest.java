package hu.clinvio.ui.htmx.response;

import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class HxResponseTest {

    @Test
    void builder_shouldSetTrigger() {
        HxResponse response = HxResponse.builder()
                .trigger("myEvent")
                .build();
        assertEquals("myEvent", response.getTrigger());
    }

    @Test
    void builder_shouldSetRedirect() {
        HxResponse response = HxResponse.builder()
                .redirect("/dashboard")
                .build();
        assertEquals("/dashboard", response.getRedirect());
    }

    @Test
    void builder_shouldSetPushUrl() {
        HxResponse response = HxResponse.builder()
                .pushUrl("/new-url")
                .build();
        assertEquals("/new-url", response.getPushUrl());
    }

    @Test
    void builder_shouldSetSwap() {
        HxResponse response = HxResponse.builder()
                .swap(HxSwap.OUTER_HTML)
                .build();
        assertEquals(HxSwap.OUTER_HTML, response.getSwap());
    }

    @Test
    void trigger_shouldSetTriggerAndDetail() {
        Map<String, Object> detail = Map.of("key", "value");
        HxResponse response = HxResponse.builder()
                .trigger("update", detail)
                .build();
        assertEquals("update", response.getTrigger());
        assertEquals(detail, response.getTriggerDetail());
    }

    @Test
    void triggerDetail_shouldSetTriggerDetail() {
        Map<String, Object> detail = Map.of("id", 42);
        HxResponse response = HxResponse.builder()
                .trigger("event")
                .triggerDetail(detail)
                .build();
        assertEquals("event", response.getTrigger());
        assertEquals(detail, response.getTriggerDetail());
    }

    @Test
    void build_shouldCreateResponseWithCorrectFields() {
        HxResponse response = HxResponse.builder()
                .content("<div>hello</div>")
                .fragment("::frag")
                .model(Map.of("name", "world"))
                .swap(HxSwap.AFTER_BEGIN)
                .target("#myDiv")
                .redirect("/home")
                .trigger("loaded")
                .pushUrl("/page")
                .build();

        assertEquals("<div>hello</div>", response.getContent());
        assertEquals("::frag", response.getFragment());
        assertEquals(Map.of("name", "world"), response.getModel());
        assertEquals(HxSwap.AFTER_BEGIN, response.getSwap());
        assertEquals("#myDiv", response.getTarget());
        assertEquals("/home", response.getRedirect());
        assertEquals("loaded", response.getTrigger());
        assertEquals("/page", response.getPushUrl());
    }

    @Test
    void builder_shouldUseDefaultSwapWhenNotSet() {
        HxResponse response = HxResponse.builder()
                .content("test")
                .build();
        assertEquals(HxSwap.INNER_HTML, response.getSwap());
    }

    @Test
    void builder_shouldUseEmptyMapWhenModelNotSet() {
        HxResponse response = HxResponse.builder().build();
        assertTrue(response.getModel().isEmpty());
    }
}
