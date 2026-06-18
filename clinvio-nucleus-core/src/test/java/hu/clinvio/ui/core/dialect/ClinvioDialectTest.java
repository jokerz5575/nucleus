package hu.clinvio.ui.core.dialect;

import hu.clinvio.ui.core.dialect.processor.CvButtonAttrProcessor;
import hu.clinvio.ui.core.dialect.processor.CvComponentAttrProcessor;
import hu.clinvio.ui.core.dialect.processor.CvRenderAttrProcessor;
import org.junit.jupiter.api.Test;
import org.thymeleaf.processor.IProcessor;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class ClinvioDialectTest {

    @Test
    void dialectNameIsClinvioUI() {
        ClinvioDialect dialect = new ClinvioDialect();
        assertEquals("Clinvio UI", dialect.getName());
    }

    @Test
    void dialectPrefixIsCv() {
        ClinvioDialect dialect = new ClinvioDialect();
        assertEquals("cv", dialect.getPrefix());
    }

    @Test
    void registersThreeProcessors() {
        ClinvioDialect dialect = new ClinvioDialect();
        Set<IProcessor> processors = dialect.getProcessors("cv");
        assertEquals(3, processors.size());
    }

    @Test
    void containsButtonProcessor() {
        ClinvioDialect dialect = new ClinvioDialect();
        Set<IProcessor> processors = dialect.getProcessors("cv");
        assertTrue(processors.stream().anyMatch(p -> p instanceof CvButtonAttrProcessor));
    }

    @Test
    void containsComponentProcessor() {
        ClinvioDialect dialect = new ClinvioDialect();
        Set<IProcessor> processors = dialect.getProcessors("cv");
        assertTrue(processors.stream().anyMatch(p -> p instanceof CvComponentAttrProcessor));
    }

    @Test
    void containsRenderProcessor() {
        ClinvioDialect dialect = new ClinvioDialect();
        Set<IProcessor> processors = dialect.getProcessors("cv");
        assertTrue(processors.stream().anyMatch(p -> p instanceof CvRenderAttrProcessor));
    }

    @Test
    void usesCustomPrecedences() {
        ClinvioDialect dialect = new ClinvioDialect(500, 600, 700);
        Set<IProcessor> processors = dialect.getProcessors("cv");
        for (IProcessor p : processors) {
            if (p instanceof CvButtonAttrProcessor) {
                assertEquals(500, p.getPrecedence());
            } else if (p instanceof CvComponentAttrProcessor) {
                assertEquals(600, p.getPrecedence());
            } else if (p instanceof CvRenderAttrProcessor) {
                assertEquals(700, p.getPrecedence());
            }
        }
    }

    @Test
    void providesExpressionObjectFactory() {
        ClinvioDialect dialect = new ClinvioDialect();
        assertNotNull(dialect.getExpressionObjectFactory());
        assertTrue(dialect.getExpressionObjectFactory() instanceof ClinvioExpressionObjectFactory);
    }

    @Test
    void expressionObjectFactoryProvidesCv() {
        ClinvioExpressionObjectFactory factory = new ClinvioExpressionObjectFactory();
        assertTrue(factory.getAllExpressionObjectNames().contains("cv"));
    }
}
