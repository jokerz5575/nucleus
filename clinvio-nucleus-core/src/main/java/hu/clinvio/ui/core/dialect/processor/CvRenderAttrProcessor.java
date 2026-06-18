package hu.clinvio.ui.core.dialect.processor;

import hu.clinvio.ui.core.renderer.CvRenderer;
import org.springframework.context.ApplicationContext;
import org.thymeleaf.context.ITemplateContext;
import org.thymeleaf.engine.AttributeName;
import org.thymeleaf.model.IProcessableElementTag;
import org.thymeleaf.processor.element.AbstractAttributeTagProcessor;
import org.thymeleaf.processor.element.IElementTagStructureHandler;
import org.thymeleaf.spring6.context.SpringContextUtils;
import org.thymeleaf.templatemode.TemplateMode;

import java.util.Collections;
import java.util.Map;

/**
 * Processes cv:render="templateName" attributes.
 * Renders a named template fragment with optional model data.
 */
public class CvRenderAttrProcessor extends AbstractAttributeTagProcessor {

    private static final String ATTRIBUTE_NAME = "render";
    private static final int PRECEDENCE = 900;

    public CvRenderAttrProcessor(String dialectPrefix) {
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
    @SuppressWarnings("unchecked")
    protected void doProcess(
            ITemplateContext context,
            IProcessableElementTag tag,
            AttributeName attributeName,
            String attributeValue,
            IElementTagStructureHandler structureHandler) {

        ApplicationContext appCtx = SpringContextUtils.getApplicationContext(context);
        CvRenderer renderer = appCtx.getBean(CvRenderer.class);

        // The attribute value is the template name
        String templateName = attributeValue;

        // Get model from the current context variables
        Object modelObj = context.getVariable("cvModel");
        Map<String, Object> model;
        if (modelObj instanceof Map) {
            model = (Map<String, Object>) modelObj;
        } else {
            model = Collections.emptyMap();
        }

        String html = renderer.renderFragment(templateName, model, context.getLocale());
        structureHandler.setBody(html, false);
    }
}
