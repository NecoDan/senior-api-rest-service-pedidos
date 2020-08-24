package br.com.senior.api.rest.service.pedidos.repository;

import br.com.senior.api.rest.service.pedidos.model.cadastro.Produto;
import br.com.senior.api.rest.service.pedidos.model.cadastro.TipoFinalidadeProduto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Repository
public interface IProdutoRepository extends JpaRepository<Produto, UUID> {

    Page<Produto> findAllByAtivo(@Param("ativo") boolean ativo, Pageable pageable);

    List<Produto> findAllByDescricaoContainingIgnoreCase(@Param("descricao") String descricao);

    @Query(value = "select produto.* from senior_services.ss01_produto as produto where produto.id_tipo_finalidade = :#{#tipoFinalidadeProduto.codigo} order by descricao", nativeQuery = true)
    List<Produto> recuperarTodosPorTipoFinalidade(@Param("tipoFinalidadeProduto") TipoFinalidadeProduto tipoFinalidadeProduto);

    @Query(value = "select * from senior_services.ss01_produto where date(dt_cadastro) = :#{#data} order by descricao", nativeQuery = true)
    List<Produto> recuperarTodosPorDataCadastro(@Param("data") LocalDate data);

    @Query(value = "select * from senior_services.ss01_produto where date(dt_cadastro) between :#{#dataInicio} and :#{#dataFim} order by descricao", nativeQuery = true)
    List<Produto> recuperarTodosPorPeriodoDtCadastro(@Param("dataInicio") LocalDate dataInicio, @Param("dataFim") LocalDate dataFim);

    @Query(value = "select (count(produto.id) > 0) as result from senior_services.ss02_pedido as pedido inner join senior_services.ss03_item_pedido as item_pedido on (item_pedido.id_pedido = pedido.id) inner join senior_services.ss01_produto as produto on (produto.id = item_pedido.id_produto) where produto.id = :#{#idProduto}", nativeQuery = true)
    boolean isProdutoPossuiMovimentosEmPedidos(@Param("idProduto") UUID idProduto);
}
