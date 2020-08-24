package br.com.senior.api.rest.service.pedidos.repository;

import br.com.senior.api.rest.service.pedidos.model.pedido.Pedido;
import br.com.senior.api.rest.service.pedidos.model.pedido.StatusPedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Repository
public interface IPedidoRepository extends JpaRepository<Pedido, UUID> {

    @Query(value = "select * from senior_services.ss02_pedido where id_status_pedido = :#{#statusPedido.codigo} order by dt_cadastro desc", nativeQuery = true)
    List<Pedido> recuperarTodosPorStatusPedido(@Param("statusPedido") StatusPedido statusPedido);

    @Query(value = "select * from senior_services.ss02_pedido where date(dt_cadastro) = :#{#data} order by dt_cadastro", nativeQuery = true)
    List<Pedido> recuperarTodosPorDataCadastro(@Param("data") LocalDate data);

    @Query(value = "select * from senior_services.ss02_pedido where date(dt_cadastro) between :#{#dataInicio} and :#{#dataFim} order by dt_cadastro", nativeQuery = true)
    List<Pedido> recuperarTodosPorPeriodoDtCadastro(@Param("dataInicio") LocalDate dataInicio, @Param("dataFim") LocalDate dataFim);

}
