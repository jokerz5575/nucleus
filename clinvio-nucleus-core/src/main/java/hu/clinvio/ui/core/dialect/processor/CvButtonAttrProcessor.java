package hu.clinvio.ui.core.dialect.processor;

import org.springframework.web.util.HtmlUtils;
import org.thymeleaf.context.ITemplateContext;
import org.thymeleaf.engine.AttributeName;
import org.thymeleaf.model.IProcessableElementTag;
import org.thymeleaf.processor.element.AbstractAttributeTagProcessor;
import org.thymeleaf.processor.element.IElementTagStructureHandler;
import org.thymeleaf.templatemode.TemplateMode;

/**
 * Processes cv:button="label" attributes.
 * Transforms a simple element into a Clinvio-styled button.
 */
public class CvButtonAttrProcessor extends AbstractAttributeTagProcessor {

    private static final String ATTRIBUTE_NAME = "button";
    private static final int PRECEDENCE = 1000;

    public CvButtonAttrProcessor(String dialectPrefix) {
        super(
                TemplateMode.HTML,
                dialectPrefix,
                null,
                false,
                ATTRIBUTE_NAME,
                true,
                PRECEDENCE,
                true
        );
    }

    @Override
    protected void doProcess(
            ITemplateContext context,
            IProcessableElementTag tag,
            AttributeName attributeName,
            String attributeValue,
            IElementTagStructureHandler structureHandler) {

        // Get existing attributes and HTML-escape them to prevent XSS
        String existingClass = HtmlUtils.htmlEscape(tag.getAttributeValue("class"));
        String type = tag.getAttributeValue("cv:type");
        String size = tag.getAttributeValue("cv:size");
        String icon = HtmlUtils.htmlEscape(tag.getAttributeValue("cv:icon"));

        // Build CSS classes
        StringBuilder classes = new StringBuilder("btn");
        if (existingClass != null) classes.append(" ").append(existingClass);

        if ("primary".equals(type)) classes.append(" btn-primary");
        else if ("secondary".equals(type)) classes.append(" btn-secondary");
        else if ("success".equals(type)) classes.append(" btn-success");
        else if ("danger".equals(type)) classes.append(" btn-danger");
        else if ("accent".equals(type)) classes.append(" btn-accent");
        else if ("outline-primary".equals(type)) classes.append(" btn-outline-primary");
        else if ("outline-secondary".equals(type)) classes.append(" btn-outline-secondary");
        else if ("soft-primary".equals(type)) classes.append(" btn-soft-primary");
        else if ("ghost".equals(type)) classes.append(" btn-ghost");
        else classes.append(" btn-primary");

        if ("lg".equals(size)) classes.append(" btn-lg");
        else if ("sm".equals(size)) classes.append(" btn-sm");

        // Build icon HTML with escaped icon name
        String iconHtml = "";
        if (icon != null && !icon.isEmpty()) {
            iconHtml = "<i class=\"bi bi-" + icon + "\"></i> ";
        }

        // HTML-escape the label to prevent XSS
        String label = attributeValue != null ? HtmlUtils.htmlEscape(attributeValue) : "";

        // Create button HTML
        String buttonHtml = "<button class=\"" + classes + "\">" + iconHtml + label + "</button>";

        structureHandler.setBody(buttonHtml, false);
    }
}
