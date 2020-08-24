# API RESTFul WebService - Pedidos/Produtos - © Copyright 2020 Senior Sistemas – Tecnologia para Gestão Empresarial.
  Projeto em Spring Boot de uma construção API RESTFul voltado a atender o desafio da Senior Sistemas <link>https://www.senior.com.br/.
   
  Uma solução criada em Java em formato de API REST que atenda aos requisitos para a recepção e/ou criação de pedidos contendo produtos. Onde todos os serviços devem trabalhar com JSON em suas chamadas e retornos.

 #### Stack do projeto
  - Escrito em Java 8;
  - Utilizando as facilidades e recursos framework Spring Boot;
  - Lombok na classes para evitar o boilerplate do Java;
  - Framework Hibernarte e Spring Data JPA para garantir a persistência dos dados e facilitar as operações CRUD (aumentando o nivel de desempenho e escalabilidade);
  - Boas práticas de programação, utilizando Design Patterns (Builder, Strategy);
  - Testes unitátios (TDD);
  - Banco de dados PostgreSQL;
  - Docker utilizando o compose;
  
  #### Visão Geral
  
  A aplicaçao tem como objetivo disponibilizar endpoints para consulta de informações e operações à respeito de:
  - Produtos;
  - Pedidos efetuados, com os seus respectivos valores, percentual de desconto, status e produto(s) adicionado(s) aos itens do pedido. 
  
  #### Instruções Inicialização - Projeto
    
      1. Clone o repositório git@github.com:NecoDan/senior-api-rest-service-pedidos.git
      
      2. Ou faça o download do arquivo ZIP do projeto em https://github.com/NecoDan/senior-api-rest-service-pedidos
          
      3. Importar o projeto em sua IDE de preferência (lembre-se, projeto baseado em Spring & Maven)
      
      4. Buildar o projeto e executá-lo.
    
  #### Instruções Inicialização - Database
  
 O comando ```docker-compose up``` inicializará uma instancia do Postgres 9.3, nesse momento será criado um novo banco ```senior_system``` com apenas um schema denominado ```senior_services``` , assim como, suas respectivas tabelas no database ```senior_system```. 
 <br><br>Com a finalidade de gerenciar os produtos e os pedidos com informações necessárias para a demonstração do projeto. Em seguida a aplicação de ```  senior-api-rest-service-pedidos``` pode ser executada e inicializada.
 
 
  #### Endpoints: 
  
  Utilizando a ferramenta de documentação de endpoints ```Swagger```, pode-se visualizar todos os endpoints disponíveis. Basta acessar a documentação da API por meio da URL <link>http://localhost:8080/swagger-ui.html , logo após a sua inicialização. <br><br> 
  De sorte que, segue a lista de alguns endpoints para conhecimento: 
  
  - Retornar uma lista completa de pedidos páginável em formato (JSON):
    - `http://localhost:8080/pedidos/`

 - Retornar uma lista completa de produtos páginável em formato (JSON):
   - `http://localhost:8080/produtos/`
  
 - Retornar uma lista de pedidos a partir dos filtros como parametros:   
     - `http://localhost:8080/pedidos/buscaPorPeriodo?dataInicio=01/01/2020&dataFim=01/06/2020`
     
 Entre outros, aos quais podem ser identificados no endereço fornecido pelo Swagger: <link>http://localhost:8080/swagger-ui.html.


  
 #### Autor e mantenedor do projeto
 - Daniel Santos Gonçalves - Bachelor in Information Systems, Federal Institute of Maranhão - IFMA / Software Developer Fullstack.
 - GitHub: https://github.com/NecoDan
 
 - Linkedin: <link>https://www.linkedin.com/in/daniel-santos-bb072321 
 - Twiter: <link>https://twitter.com/necodaniel.
