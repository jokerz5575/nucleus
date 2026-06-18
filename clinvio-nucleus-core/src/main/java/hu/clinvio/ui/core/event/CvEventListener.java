package hu.clinvio.ui.core.event;

/**
 * Listener interface for Clinvio UI component events.
 */
@FunctionalInterface
public interface CvEventListener {

    void onEvent(CvEvent event);
}
