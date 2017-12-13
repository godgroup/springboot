package com.hl.bootssm.dialect;

import org.thymeleaf.dialect.AbstractDialect;
import org.thymeleaf.dialect.IExpressionObjectDialect;
import org.thymeleaf.expression.IExpressionObjectFactory;

/**
 * @author Static
 */
public class MyDialect extends AbstractDialect implements IExpressionObjectDialect {
    private final MyExpressionFactory myExpressionFactory = new MyExpressionFactory();

    public MyDialect() {
        super("myUtils");
    }

    @Override
    public IExpressionObjectFactory getExpressionObjectFactory() {
        return myExpressionFactory;
    }
}