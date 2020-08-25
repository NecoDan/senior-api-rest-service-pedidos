package br.com.senior.api.rest.service.pedidos.util.uteis;

import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;

import static br.com.senior.api.rest.service.pedidos.util.uteis.DecimaUtil.*;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@Slf4j
public class DecimaUtilTest {

    @Before
    public void setUp() {
    }

    @Test
    public void deveSerMaiorQueZero() {
        log.info("{} ", "#TEST: deveSerMaiorQueZero: ");

        // -- 01_Cenário
        BigDecimal valor = RandomicoUtil.gerarValorRandomicoDecimal();

        // -- 02_Ação && 03_Verificacao_Validacao
        assertTrue(isMaiorQueZero(valor));
        log.info("{} ", "-------------------------------------------------------------");
    }

    @Test
    public void deveSerNotMaiorQueZero() {
        log.info("{} ", "#TEST: deveSerNotMaiorQueZero: ");

        // -- 01_Cenário
        BigDecimal valor = BigDecimal.ZERO;

        // -- 02_Ação && 03_Verificacao_Validacao
        assertFalse(isMaiorQueZero(valor));
        log.info("{} ", "-------------------------------------------------------------");
    }

    @Test
    public void deveSerMenorQueZero() {
        log.info("{} ", "#TEST: deveSerMenorQueZero: ");

        // -- 01_Cenário
        BigDecimal valor = RandomicoUtil.gerarValorRandomicoDecimal().multiply(BigDecimal.valueOf(-1));

        // -- 02_Ação && 03_Verificacao_Validacao
        assertTrue(isMenorQueZero(valor));
        log.info("{} ", "-------------------------------------------------------------");
    }

    @Test
    public void deveSerNotMenorQueZero() {
        log.info("{} ", "#TEST: deveSerNotMenorQueZero: ");

        // -- 01_Cenário
        BigDecimal valor = RandomicoUtil.gerarValorRandomicoDecimal();

        // -- 02_Ação && 03_Verificacao_Validacao
        assertFalse(isMenorQueZero(valor));
        log.info("{} ", "-------------------------------------------------------------");
    }

    @Test
    public void deveSerMaiorQue() {
        log.info("{} ", "#TEST: deveSerMaiorQue: ");

        // -- 01_Cenário
        BigDecimal valor1 = RandomicoUtil.gerarValorRandomicoDecimal();
        BigDecimal valor2 = RandomicoUtil.gerarValorRandomicoDecimal().multiply(BigDecimal.valueOf(-1));

        // -- 02_Ação && 03_Verificacao_Validacao
        assertTrue(isMaiorQue(valor1, valor2));
        log.info("{} ", "-------------------------------------------------------------");
    }

    @Test
    public void deveSerNotMaiorQue() {
        log.info("{} ", "#TEST: deveSerNotMaiorQue: ");

        // -- 01_Cenário
        BigDecimal valor1 = RandomicoUtil.gerarValorRandomicoDecimal().multiply(BigDecimal.valueOf(-1));
        BigDecimal valor2 = RandomicoUtil.gerarValorRandomicoDecimal();

        // -- 02_Ação && 03_Verificacao_Validacao
        assertTrue(isMaiorQue(valor2, valor1));
        log.info("{} ", "-------------------------------------------------------------");
    }

    @Test
    public void deveSerMenorQue() {
        log.info("{} ", "#TEST: deveSerMenorQue: ");

        // -- 01_Cenário
        BigDecimal valor1 = RandomicoUtil.gerarValorRandomicoDecimal();
        BigDecimal valor2 = RandomicoUtil.gerarValorRandomicoDecimal().multiply(BigDecimal.valueOf(-1));

        // -- 02_Ação && 03_Verificacao_Validacao
        assertTrue(isMenorQue(valor2, valor1));
        log.info("{} ", "-------------------------------------------------------------");
    }

    @Test
    public void deveSerEqualsToZero() {
        log.info("{} ", "#TEST: deveSerEqualsToZero: ");

        // -- 01_Cenário
        BigDecimal valor = RandomicoUtil.gerarValorRandomicoDecimal().multiply(BigDecimal.valueOf(0));

        // -- 02_Ação && 03_Verificacao_Validacao
        assertTrue(isEqualsToZero(valor));
        log.info("{} ", "-------------------------------------------------------------");
    }

    @Test
    public void deveSerEquals() {
        log.info("{} ", "#TEST: deveSerEquals: ");

        // -- 01_Cenário
        BigDecimal valor = RandomicoUtil.gerarValorRandomicoDecimal().multiply(BigDecimal.valueOf(0));

        // -- 02_Ação && 03_Verificacao_Validacao
        assertTrue(isEquals(valor, BigDecimal.ZERO));
        log.info("{} ", "-------------------------------------------------------------");
    }

    @Test
    public void deveSerNotEquals() {
        log.info("{} ", "#TEST: deveSerNotEquals: ");

        // -- 01_Cenário
        BigDecimal valor = RandomicoUtil.gerarValorRandomicoDecimal();

        // -- 02_Ação && 03_Verificacao_Validacao
        assertTrue(isNotEquals(valor, BigDecimal.ZERO));
        log.info("{} ", "-------------------------------------------------------------");
    }

    @Test
    public void deveSerMenorOuIgualAZero() {
        log.info("{} ", "#TEST: deveSerMenorOuIgualAZero: ");

        // -- 01_Cenário
        BigDecimal valor1 = RandomicoUtil.gerarValorRandomicoDecimal().multiply(BigDecimal.valueOf(0));
        BigDecimal valor2 = RandomicoUtil.gerarValorRandomicoDecimal().multiply(BigDecimal.valueOf(-1));

        // -- 02_Ação && 03_Verificacao_Validacao
        assertTrue(isMenorOuIgualAZero(valor1) && isMenorOuIgualAZero(valor2));
        log.info("{} ", "-------------------------------------------------------------");
    }

    @Test
    public void deveSerMaiorOuIgualAZero() {
        log.info("{} ", "#TEST: deveSerMaiorOuIgualAZero: ");

        // -- 01_Cenário
        BigDecimal valor1 = RandomicoUtil.gerarValorRandomicoDecimal().multiply(BigDecimal.valueOf(0));
        BigDecimal valor2 = RandomicoUtil.gerarValorRandomicoDecimal();

        // -- 02_Ação && 03_Verificacao_Validacao
        assertTrue(isMaiorOuIgualAZero(valor1) && isMaiorOuIgualAZero(valor2));
        log.info("{} ", "-------------------------------------------------------------");
    }
}
