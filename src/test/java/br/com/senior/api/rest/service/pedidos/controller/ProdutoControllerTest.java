package br.com.senior.api.rest.service.pedidos.controller;

import br.com.senior.api.rest.service.pedidos.model.cadastro.Produto;
import br.com.senior.api.rest.service.pedidos.model.cadastro.TipoFinalidadeProduto;
import br.com.senior.api.rest.service.pedidos.service.negocio.ProdutoService;
import br.com.senior.api.rest.service.pedidos.util.uteis.RandomicoUtil;
import br.com.senior.api.rest.service.pedidos.util.uteis.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalDate;
import java.util.*;

import static org.junit.Assert.assertTrue;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Slf4j
@RunWith(SpringRunner.class)
@WebMvcTest(ProdutoController.class)
public class ProdutoControllerTest {

    private static final String PATH_PADRAO = "/produtos";
    private static final String URI = PATH_PADRAO + "/{action}";

    @Autowired
    private MockMvc mvc;

    @MockBean
    private ProdutoService produtoService;

    @Before
    public void setUp() throws Exception {
    }

    @Test
    public void deveRetornarProducerJSONContendoUmaListaProdutosPorIntervaloDatas() throws Exception {
        log.info("\n#TEST: deveRetornarProducerJSONContendoUmaListaProdutosPorIntervaloDatas: ");

        // -- 01_Cenário
        LocalDate dataInicio = LocalDate.now();
        LocalDate dataFim = LocalDate.now();

        List<Produto> produtoList = Arrays.asList(constroiProdutoValido(), constroiProdutoValido(), constroiProdutoValido(),
                constroiProdutoValido(), constroiProdutoValido(), constroiProdutoValido(), constroiProdutoValido());

        // -- 02_Ação
        given(produtoService.recuperarTodosPorPeriodo(dataInicio, dataFim)).willReturn(produtoList);
        String uri = PATH_PADRAO.concat("/buscarPorPeriodo?dataInicio=" + StringUtil.formatLocalDate(dataInicio) + "&" + "dataFim=" + StringUtil.formatLocalDate(dataInicio));
        ResultActions response = getResponseEntityEndPointsMethodGET(uri, MediaType.APPLICATION_JSON);

        // -- 03_Verificação_Validação
        response.andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").exists())
                .andExpect(jsonPath("$.[*].dataCadastro").isNotEmpty());
        Assert.assertNotNull(response.andReturn().getResponse().getContentAsString());

        toStringEnd(response, MediaType.APPLICATION_JSON);
    }

    @Test
    public void deveRetornarProducerJSONContendoUmaListaProdutosApenasPorUmaDataFiltroInsercao() throws Exception {
        log.info("\n#TEST: deveRetornarProducerJSONContendoUmaListaProdutosApenasPorUmaDataFiltroInsercao: ");

        // -- 01_Cenário
        LocalDate dataFiltro = LocalDate.now();
        List<Produto> produtoList = Arrays.asList(constroiProdutoValido(), constroiProdutoValido());

        // -- 02_Ação
        given(produtoService.recuperarPorDataCadastro(dataFiltro)).willReturn(produtoList);
        String uri = PATH_PADRAO.concat("/buscarPorDtCadastro?data=" + StringUtil.formatLocalDate(dataFiltro));
        ResultActions response = getResponseEntityEndPointsMethodGET(uri, MediaType.APPLICATION_JSON);

        // -- 03_Verificação_Validação
        response.andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").exists())
                .andExpect(jsonPath("$.[*].dataCadastro").isNotEmpty());
        Assert.assertNotNull(response.andReturn().getResponse().getContentAsString());

        toStringEnd(response, MediaType.APPLICATION_JSON);
    }

    @Test
    public void deveRetornarProducerJSONContendoUmaListaProdutosPorFiltroTipoFinalidadeProd() throws Exception {
        log.info("\n#TEST: deveRetornarProducerJSONContendoUmaListaProdutosPorFiltroTipoFinalidadeProd: ");

        // -- 01_Cenário
        TipoFinalidadeProduto tipoFinalidadeProduto = TipoFinalidadeProduto.PRODUTO_1;
        List<Produto> produtoList = Arrays.asList(constroiProdutoValido(), constroiProdutoValido(), constroiProdutoValido(), constroiProdutoValido());

        // -- 02_Ação
        given(produtoService.recuperarTodosPorTipoFinalidade(tipoFinalidadeProduto)).willReturn(produtoList);
        String uri = PATH_PADRAO.concat("/buscarPorTipoFinalidade?codigo=" + tipoFinalidadeProduto.getCodigo());
        ResultActions response = getResponseEntityEndPointsMethodGET(uri, MediaType.APPLICATION_JSON);

        // -- 03_Verificação_Validação
        response.andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").exists())
                .andExpect(jsonPath("$.[*].dataCadastro").isNotEmpty());
        Assert.assertNotNull(response.andReturn().getResponse().getContentAsString());

        toStringEnd(response, MediaType.APPLICATION_JSON);
    }

    @Test
    public void deveRetornarProducerJSONContendoUmaListaProdutosPorFiltroDescricaoProd() throws Exception {
        log.info("\n#TEST: deveRetornarProducerJSONContendoUmaListaProdutosPorFiltroDescricaoProd: ");

        // -- 01_Cenário
        String descricao = "Cadeira";
        List<Produto> produtoList = Arrays.asList(constroiProdutoValido(), constroiProdutoValido(), constroiProdutoValido());

        // -- 02_Ação
        given(produtoService.recuperarPorDescricao(descricao)).willReturn(produtoList);
        String uri = PATH_PADRAO.concat("/buscarPorDescricao?descricao=" + descricao);
        ResultActions response = getResponseEntityEndPointsMethodGET(uri, MediaType.APPLICATION_JSON);

        // -- 03_Verificação_Validação
        response.andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").exists())
                .andExpect(jsonPath("$.[*].dataCadastro").isNotEmpty())
                .andExpect(jsonPath("$.[*].dataCadastro").isArray());
        Assert.assertNotNull(response.andReturn().getResponse().getContentAsString());

        toStringEnd(response, MediaType.APPLICATION_JSON);
    }

    @Test
    public void deveRetornarPeloMenosUnicoProduto() throws Exception {
        log.info("\n#TEST: deveRetornarPeloMenosUnicoProduto: ");

        // -- 01_Cenário
        UUID idProduto = UUID.randomUUID();
        Produto produto = constroiProdutoValido();

        // -- 02_Ação
        given(produtoService.recuperarPorId(idProduto)).willReturn(Optional.of(produto));
        String uri = PATH_PADRAO.concat("/").concat(idProduto.toString());
        ResultActions response = getResponseEntityEndPointsMethodGET(uri, MediaType.APPLICATION_JSON);

        // -- 03_Verificação_Validação
        response.andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.valorCusto").isNotEmpty())
                .andExpect(jsonPath("$.valorCusto").isNumber())
                .andExpect(jsonPath("$.codigoProduto").isNumber());
        Assert.assertNotNull(response.andReturn().getResponse().getContentAsString());

        toStringEnd(response, MediaType.APPLICATION_JSON);
    }

    @Test
    public void deveRetornarErrorComStatus400EGerarMensagemAoNaoEncontrarResourceEndpointGetProdutosPorDescricao() throws Exception {
        log.info("\n#TEST: deveRetornarErrorComStatus400EGerarMensagemAoNaoEncontrarResourceEndpointGetProdutosPorDescricao: ");

        // -- 01_Cenário
        String resultReponseServer = "Responser server: Nenhum pedido(s) encontrado.";

        // -- 02_Ação
        given(produtoService.recuperarPorDescricao("")).willReturn(null);
        String uri = PATH_PADRAO.concat("/buscarPorDescricao?descricao=" + "");
        ResultActions response = getResponseEntityEndPointsMethodGET(uri, MediaType.APPLICATION_JSON);

        // -- 03_Verificação_Validação
        response.andExpect(status().isNotFound());
        String result = response.andReturn().getResponse().getContentAsString();
        assertTrue(Objects.nonNull(result) || !result.isEmpty() && result.contains(resultReponseServer));

        log.info("#TEST_RESULT: ".concat(result));
        log.info("-------------------------------------------------------------");
    }

    @Test
    public void deveRetornarErrorComStatus400EGerarMensagemAoNaoEncontrarResourceEndpointGetProdutosPorData() throws Exception {
        log.info("\n#TEST: deveRetornarErrorComStatus400EGerarMensagemAoNaoEncontrarResourceEndpointGetProdutosPorData: ");

        // -- 01_Cenário
        String resultReponseServer = "Responser server: Nenhum pedido(s) encontrado.";

        // -- 02_Ação
        given(produtoService.recuperarPorDataCadastro(null)).willReturn(null);
        String uri = PATH_PADRAO.concat("/buscarPorDtCadastro?data=" + "");
        ResultActions response = getResponseEntityEndPointsMethodGET(uri, MediaType.APPLICATION_JSON);

        // -- 03_Verificação_Validação
        response.andExpect(status().isNotFound());
        String result = response.andReturn().getResponse().getContentAsString();
        assertTrue(Objects.nonNull(result) || !result.isEmpty() && result.contains(resultReponseServer));

        log.info("#TEST_RESULT: ".concat(result));
        log.info("-------------------------------------------------------------");
    }

    private Produto constroiProdutoValido() {
        Produto produto = Produto.builder()
                .valorCusto(RandomicoUtil.gerarValorRandomicoDecimal())
                .descricao(String.valueOf(RandomicoUtil.gerarValorRandomicoLong()))
                .tipoFinalidadeProduto(TipoFinalidadeProduto.PRODUTO_1)
                .build()
                .geraValorCustoAutomatico()
                .geraCodigoProdutoAutomatico()
                .geraCodigoBarrasAutomatico();

        produto.geraId();
        produto.ativado();
        produto.gerarDataCorrente();
        return produto;
    }

    private void toStringEnd(ResultActions response, MediaType mediaType) throws Exception {
        if (Objects.isNull(response) || Objects.isNull(mediaType)) {
            log.info("#TEST_RESULT: ".concat("Error ao gerar saida. Não existem dados..."));
            log.info("-------------------------------------------------------------");
            return;
        }

        String result = response.andReturn().getResponse().getContentAsString();
        String out = "";

        if (mediaType == MediaType.APPLICATION_JSON)
            out = StringUtil.formatConteudoJSONFrom(result);

        if (mediaType == MediaType.APPLICATION_XML)
            out = StringUtil.formatConteudoXMLFrom(result);

        log.info("#TEST_RESULT: ".concat(out));
        log.info("-------------------------------------------------------------");
    }

    private ResultActions getResponseEntityEndPointsMethodGET(String url, MediaType mediaType) throws Exception {
        return this.mvc.perform(get(url).accept(mediaType));
    }
}
