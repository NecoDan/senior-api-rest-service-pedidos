package br.com.senior.api.rest.service.pedidos.repository;

import br.com.senior.api.rest.service.pedidos.model.pedido.ItemPedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface IItemPedidoRepository extends JpaRepository<ItemPedido, UUID> {

}
