package hu.clinvio.ui.core.dialect;

import hu.clinvio.ui.core.dialect.processor.CvComponentAttrProcessor;
import hu.clinvio.ui.core.dialect.processor.CvButtonAttrProcessor;
import hu.clinvio.ui.core.dialect.processor.CvRenderAttrProcessor;
import org.thymeleaf.dialect.AbstractProcessorDialect;
import org.thymeleaf.dialect.IExpressionObjectDialect;
import org.thymeleaf.expression.IExpressionObjectFactory;
import org.thymeleaf.processor.IProcessor;

import java.util.HashSet;
import java.util.Set;

/**
 * Custom Thymeleaf dialect for Clinvio UI components.
 * Provides the "cv" namespace for component-specific attributes and expression objects.
 */
public class ClinvioDialect extends AbstractProcessorDialect implements IExpressionObjectDialect {

    public static final String DIALECT_NAME = "Clinvio UI";
    public static final String DIALECT_PREFIX = "cv";

    private final ClinvioExpressionObjectFactory expressionObjectFactory;

    public ClinvioDialect() {
        super(DIALECT_NAME, DIALECT_PREFIX, 1000);
        this.expressionObjectFactory = new ClinvioExpressionObjectFactory();
    }

    @Override
    public Set<IProcessor> getProcessors(String dialectPrefix) {
        Set<IProcessor> processors = new HashSet<>();
        processors.add(new CvComponentAttrProcessor(dialectPrefix));
        processors.add(new CvButtonAttrProcessor(dialectPrefix));
        processors.add(new CvRenderAttrProcessor(dialectPrefix));
        return processors;
    }

    @Override
    public IExpressionObjectFactory getExpressionObjectFactory() {
        return expressionObjectFactory;
    }
}
