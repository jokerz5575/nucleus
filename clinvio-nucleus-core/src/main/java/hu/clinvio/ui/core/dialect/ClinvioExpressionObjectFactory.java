package hu.clinvio.ui.core.dialect;

import org.thymeleaf.context.IExpressionContext;
import org.thymeleaf.expression.IExpressionObjectFactory;

import java.util.Collections;
import java.util.Set;

/**
 * Factory for Clinvio expression objects available in templates as ${cv.*}.
 */
public class ClinvioExpressionObjectFactory implements IExpressionObjectFactory {

    public static final String EXPRESSION_OBJECT_NAME = "cv";

    @Override
    public Set<String> getAllExpressionObjectNames() {
        return Collections.singleton(EXPRESSION_OBJECT_NAME);
    }

    @Override
    public Object buildObject(IExpressionContext context, String expressionObjectName) {
        if (EXPRESSION_OBJECT_NAME.equals(expressionObjectName)) {
            return new ClinvioExpressionUtils(context);
        }
        return null;
    }

    @Override
    public boolean isCacheable(String expressionObjectName) {
        return false;
    }
}
