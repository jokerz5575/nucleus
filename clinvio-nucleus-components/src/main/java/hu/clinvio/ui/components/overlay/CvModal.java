package hu.clinvio.ui.components.overlay;

import hu.clinvio.ui.core.component.AbstractCvComponent;
import hu.clinvio.ui.core.component.CvComponentType;

import java.util.Map;

public class CvModal extends AbstractCvComponent {

    private String title;
    private String content;
    private String size;
    private boolean closable = true;
    private boolean backdrop = true;
    private String confirmText;
    private String cancelText;
    private String confirmAction;

    public CvModal() {
        super();
    }

    public static CvModal of(String title) {
        CvModal modal = new CvModal();
        modal.title = title;
        return modal;
    }

    public CvModal content(String content) { this.content = content; return this; }
    public CvModal size(String size) { this.size = size; return this; }
    public CvModal closable(boolean closable) { this.closable = closable; return this; }
    public CvModal backdrop(boolean backdrop) { this.backdrop = backdrop; return this; }
    public CvModal confirmText(String text) { this.confirmText = text; return this; }
    public CvModal cancelText(String text) { this.cancelText = text; return this; }
    public CvModal confirmAction(String action) { this.confirmAction = action; return this; }

    @Override
    public String getComponentType() { return CvComponentType.MODAL.getType(); }

    @Override
    public String getTemplate() { return "cv/components/modal"; }

    @Override
    public Map<String, Object> getTemplateModel() {
        Map<String, Object> model = super.getTemplateModel();
        model.put("title", title);
        model.put("content", content);
        model.put("size", size);
        model.put("closable", closable);
        model.put("backdrop", backdrop);
        model.put("confirmText", confirmText);
        model.put("cancelText", cancelText);
        model.put("confirmAction", confirmAction);
        return model;
    }

    public String getTitle() { return title; }
    public String getContent() { return content; }
    public String getSize() { return size; }
    public boolean isClosable() { return closable; }
    public boolean isBackdrop() { return backdrop; }
    public String getConfirmText() { return confirmText; }
    public String getCancelText() { return cancelText; }
    public String getConfirmAction() { return confirmAction; }
}
