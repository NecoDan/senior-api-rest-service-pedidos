package br.com.senior.api.rest.service.pedidos.util.uteis;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Random;

public final class RandomicoUtil {

    private static final int LIMITE_MAX_RANDOMICO_INTEIRO = 500000;
    private static final double LIMITE_MAX_RANDOMICO_DOUBLE = 10000D;

    private RandomicoUtil() {

    }

    private static Random getRandom() {
        return new Random();
    }

    public static int gerarValorRandomico() {
        int min = 1;
        return min + getRandom().nextInt(LIMITE_MAX_RANDOMICO_INTEIRO);
    }

    public static Long gerarValorRandomicoLong() {
        return (long) gerarValorRandomico();
    }

    public static Integer gerarValorRandomicoInteger() {
        return gerarValorRandomico();
    }

    public static BigDecimal gerarValorRandomicoDecimal() {
        double leftLimit = 1D;
        return BigDecimal.valueOf(leftLimit + new Random().nextDouble() * (LIMITE_MAX_RANDOMICO_DOUBLE - leftLimit)).setScale(2, RoundingMode.HALF_UP);
    }

    public static BigDecimal gerarValorRandomicoDecimalAte(double limit) {
        double leftLimit = 1D;
        return BigDecimal.valueOf(leftLimit + new Random().nextDouble() * (limit - leftLimit)).setScale(2, RoundingMode.HALF_UP);
    }
}
