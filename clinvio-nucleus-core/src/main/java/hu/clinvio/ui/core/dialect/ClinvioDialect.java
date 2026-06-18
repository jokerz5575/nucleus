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

public class ClinvioDialect extends AbstractProcessorDialect implements IExpressionObjectDialect {

    public static final String DIALECT_NAME = "Clinvio UI";
    public static final String DIALECT_PREFIX = "cv";

    private final ClinvioExpressionObjectFactory expressionObjectFactory;
    private final int buttonPrecedence;
    private final int componentPrecedence;
    private final int renderPrecedence;

    public ClinvioDialect() {
        this(1000, 1000, 900);
    }

    public ClinvioDialect(int buttonPrecedence, int componentPrecedence, int renderPrecedence) {
        super(DIALECT_NAME, DIALECT_PREFIX, 1000);
        this.expressionObjectFactory = new ClinvioExpressionObjectFactory();
        this.buttonPrecedence = buttonPrecedence;
        this.componentPrecedence = componentPrecedence;
        this.renderPrecedence = renderPrecedence;
    }

    @Override
    public Set<IProcessor> getProcessors(String dialectPrefix) {
        Set<IProcessor> processors = new HashSet<>();
        processors.add(new CvComponentAttrProcessor(dialectPrefix, componentPrecedence));
        processors.add(new CvButtonAttrProcessor(dialectPrefix, buttonPrecedence));
        processors.add(new CvRenderAttrProcessor(dialectPrefix, renderPrecedence));
        return processors;
    }

    @Override
    public IExpressionObjectFactory getExpressionObjectFactory() {
        return expressionObjectFactory;
    }
}
