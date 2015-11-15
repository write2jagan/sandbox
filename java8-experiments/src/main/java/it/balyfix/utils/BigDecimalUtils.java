package it.balyfix.utils;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.function.BiFunction;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;


public class BigDecimalUtils
{

    public static final BinaryOperator<BigDecimal> doAdd()
    {
        return (x, y) -> x.add(y);
    }

    public static final BinaryOperator<BigDecimal> doSubtract()
    {
        return (x, y) -> x.subtract(y);
    }

    public static Predicate<BigDecimal> isZero()
    {
        return (BigDecimal bd) -> bd != null && bd.signum() == 0;
    }

    public static Function<BigInteger, BigInteger> abs()
    {
        return (BigInteger value) -> value != null ? value.abs() : null;
    }

//    public static TriFunction<BigInteger,BigInteger,BigInteger,BigInteger> isDiffMoreThanAbsThreshold()
//    {
//        return (BigInteger a,BigInteger b,BigInteger c) -> abs(a.subtract(b)) < 
//    }

}
