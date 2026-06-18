package hu.clinvio.ui.core.dialect.processor;

import hu.clinvio.ui.core.component.CvComponent;
import hu.clinvio.ui.core.renderer.CvRenderer;
import org.springframework.context.ApplicationContext;
import org.thymeleaf.context.ITemplateContext;
import org.thymeleaf.engine.AttributeName;
import org.thymeleaf.model.IProcessableElementTag;
import org.thymeleaf.processor.element.AbstractAttributeTagProcessor;
import org.thymeleaf.processor.element.IElementTagStructureHandler;
import org.thymeleaf.spring6.context.SpringContextUtils;
import org.thymeleaf.templatemode.TemplateMode;

/**
 * Processes cv:component="componentObject" attributes.
 * Renders a CvComponent inline at the element location.
 */
public class CvComponentAttrProcessor extends AbstractAttributeTagProcessor {

    private static final String ATTRIBUTE_NAME = "component";
    private static final int PRECEDENCE = 1000;

    public CvComponentAttrProcessor(String dialectPrefix) {
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

        ApplicationContext appCtx = SpringContextUtils.getApplicationContext(context);
        CvRenderer renderer = appCtx.getBean(CvRenderer.class);

        Object componentObj = context.getVariable(attributeValue);
        if (componentObj instanceof CvComponent component) {
            String html = renderer.render(component, context.getLocale());
            structureHandler.setBody(html, false);
        }
    }
}
