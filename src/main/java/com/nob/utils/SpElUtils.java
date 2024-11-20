package com.nob.utils;

import org.springframework.expression.EvaluationException;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.ParseException;
import org.springframework.expression.spel.standard.SpelExpressionParser;

import java.util.function.Supplier;

/**
 * Spring Expression Language utility
 * @author Truong Ngo
 * */
public class SpElUtils {

    /**
     * Resolve SpEl expression
     * @param expression the SpEl expression
     * @param context the expression's context
     * @param clazz the desired type
     * @return {@code T} - the expression resolve result, null if expression is invalid
     * */
    public static <T> T resolve(String expression, Object context, Class<T> clazz) {
        try {
            ExpressionParser parser = new SpelExpressionParser();
            Expression exp = parser.parseExpression(expression);
            return exp.getValue(context, clazz);
        } catch (ParseException | EvaluationException | IllegalAccessError e) {
            return null;
        }
    }


    /**
     * Resolve SpEl expression
     * @param expression the SpEl expression
     * @param context the expression's context
     * @param clazz the desired type
     * @param supplier the exception will be throw if invalid expression
     * @return {@code T} - the expression resolve result, null if expression is invalid
     * */
    public static <T, X extends Throwable> T resolveOrThrow(String expression, Object context, Class<T> clazz, Supplier<X> supplier) throws X {
        try {
            ExpressionParser parser = new SpelExpressionParser();
            Expression exp = parser.parseExpression(expression);
            return exp.getValue(context, clazz);
        } catch (ParseException | EvaluationException | IllegalAccessError e) {
            throw supplier.get();
        }
    }
}
