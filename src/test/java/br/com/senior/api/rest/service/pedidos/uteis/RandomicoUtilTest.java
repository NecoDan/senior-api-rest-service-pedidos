package br.com.senior.api.rest.service.pedidos.uteis;

import br.com.senior.api.rest.service.pedidos.util.uteis.DecimaUtil;
import br.com.senior.api.rest.service.pedidos.util.uteis.RandomicoUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertTrue;

@Slf4j
public class RandomicoUtilTest {

    @Before
    public void setUp() {
    }

    @Test
    public void deveGerarValorRandomicoInteiro() {
        log.info("{} ", "#TEST: deveGerarValorRandomicoInteiro: ");

        // -- 01_Cenário && -- 02_Ação
        final int valor = RandomicoUtil.gerarValorRandomico();

        // -- 03_Verificacao_Validacao
        assertTrue(valor > 0);
        log.info("{} ", "Valor resultado: ".concat(String.valueOf(valor)));
        log.info("{} ", "-------------------------------------------------------------");
    }

    @Test
    public void deveGerarValorRandomicoLong() {
        log.info("{} ", "#TEST: deveGerarValorRandomicoLong: ");

        // -- 01_Cenário && -- 02_Ação
        final Long valor = RandomicoUtil.gerarValorRandomicoLong();

        // -- 03_Verificacao_Validacao
        assertTrue(valor > 0);
        log.info("{} ", "Valor resultado: ".concat(String.valueOf(valor)));
        log.info("{} ", "-------------------------------------------------------------");
    }

    @Test
    public void deveGerarValorRandomicoInteger() {
        log.info("{} ", "#TEST: deveGerarValorRandomicoInteger: ");

        // -- 01_Cenário && -- 02_Ação
        final Integer valor = RandomicoUtil.gerarValorRandomicoInteger();

        // -- 03_Verificacao_Validacao
        assertTrue(valor > 0);
        log.info("{} ", "Valor resultado: ".concat(String.valueOf(valor)));
        log.info("{} ", "-------------------------------------------------------------");
    }

    @Test
    public void deveGerarValorRandomicoDecimal() {
        log.info("{} ", "#TEST: deveGerarValorRandomicoDecimal: ");

        // -- 01_Cenário && -- 02_Ação
        final BigDecimal valor = RandomicoUtil.gerarValorRandomicoDecimal();

        // -- 03_Verificacao_Validacao
        assertTrue(DecimaUtil.isMaiorQueZero(valor));
        log.info("{} ", "Valor resultado: ".concat(String.valueOf(valor)));
        log.info("{} ", "-------------------------------------------------------------");
    }

    @Test
    public void montarMensagemTextoStringAPartirMap() {
        log.info("{} ", "#TEST: montarMensagemTextoStringAPartirMap: ");

        // -- 01_Cenário && -- 02_Ação
        Map<String, String> mapProdutosDesativadosAoGerarPedido = new HashMap<>();

        // -- -- 02_Ação
        mapProdutosDesativadosAoGerarPedido.put(String.valueOf(RandomicoUtil.gerarValorRandomicoLong()), "Produto não encontrado\n");
        mapProdutosDesativadosAoGerarPedido.put(String.valueOf(RandomicoUtil.gerarValorRandomicoLong()), "Produto encontra-se desativado\n");
        mapProdutosDesativadosAoGerarPedido.put(String.valueOf(RandomicoUtil.gerarValorRandomicoLong()), "Produto encontra-se desativado\n");
        mapProdutosDesativadosAoGerarPedido.put(String.valueOf(RandomicoUtil.gerarValorRandomicoLong()), "Produto encontra-se desativado\n");
        mapProdutosDesativadosAoGerarPedido.put(String.valueOf(RandomicoUtil.gerarValorRandomicoLong()), "Produto encontra-se desativado\n");

        String result = mapProdutosDesativadosAoGerarPedido.keySet().stream()
                .map(key -> key + " : " + mapProdutosDesativadosAoGerarPedido.get(key))
                .collect(Collectors.joining(", ", "{\n", "}"));

        // -- 03_Verificacao_Validacao
        assertTrue(mapProdutosDesativadosAoGerarPedido.size() > 0 && !result.isEmpty());

        log.info("{} ", "ToStringMap - \"Algum(ns) produto(s) adicionado(s) ao Pedido encontram-se desativado e/ou inválidos. Não é permitido adicionar um produto desativado ao pedido: \n"
                .concat(result));
        log.info("{} ", "-------------------------------------------------------------");
    }
}
