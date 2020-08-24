package br.com.senior.api.rest.service.pedidos.util.uteis;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * @author Daniel Santos
 */
public final class DecimaUtil {

    private DecimaUtil() {
    }

    public static boolean isMaiorQueZero(BigDecimal valor) {
        return isMaiorQue(valor, BigDecimal.ZERO);
    }

    public static boolean isMenorQueZero(BigDecimal valor) {
        return isMenorQue(valor, BigDecimal.ZERO);
    }

    public static boolean isEqualsToZero(BigDecimal valor) {
        return isEquals(valor, BigDecimal.ZERO);
    }

    public static boolean isMenorOuIgualAZero(BigDecimal valor) {
        return (isMenorQueZero(valor) || isEqualsToZero(valor));
    }

    public static boolean isMaiorOuIgualAZero(BigDecimal valor) {
        return (isMaiorQueZero(valor) || isEqualsToZero(valor));
    }

    public static boolean isMaiorQue(BigDecimal valor1, BigDecimal valor2) {
        return (Objects.nonNull(valor1) && Objects.nonNull(valor2)) && (valor1.compareTo(valor2) > 0);
    }

    public static boolean isMenorQue(BigDecimal valor1, BigDecimal valor2) {
        return (Objects.nonNull(valor1) && Objects.nonNull(valor2)) && (valor1.compareTo(valor2) < 0);
    }

    public static boolean isEquals(BigDecimal valor1, BigDecimal valor2) {
        return (Objects.nonNull(valor1) && Objects.nonNull(valor2)) && (valor1.compareTo(valor2) == 0);
    }

    public static boolean isNotEquals(BigDecimal valor1, BigDecimal valor2) {
        return !isEquals(valor1, valor2);
    }
}
