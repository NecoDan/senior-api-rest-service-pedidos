package br.com.senior.api.rest.service.pedidos.util.uteis;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public final class DateUtil {

    private static final String MENSAGEM_VALIDACAO = "Parametro data encontra-se inválida e/ou inexistente {NULL}.";

    private DateUtil() {
    }

    public static String toStringLocalDateFormatadaPor(LocalDate data, String strFormato) {
        if (Objects.isNull(data))
            throw new IllegalArgumentException(MENSAGEM_VALIDACAO);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(strFormato);
        return data.format(formatter);
    }

    public static String toStringLocalDateFormatada(LocalDate data) {
        return toStringLocalDateFormatadaPor(data, "dd/MM/yyyy");
    }
}
