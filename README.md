# Encurtador de URL

API RESTful para um encurtador de URLs

## Requisitos

* Apache Maven 3.3.9
* Java 8

## Como utilizar

* Execute o script install.sh no diretório raiz do projeto
* Confira os logs
* O plugin wildfly do Maven roda o application server (WildFly embarcado) e faz deploy da aplicação
* O download do WildFly será feito automaticamente pelo Maven
* A aplicação utiliza o banco de dados h2 em modo embarcado. As tabelas são geradas automaticamente pelo Hibernate.
* A aplicação estará disponível no endereço: http://localhost:8080/encurtadordeurl/
* Para testar o serviço de uma maneira mais cômoda, sugiro a instalação do plugin Poster, para o navegador Mozilla Firefox, disponível em https://addons.mozilla.org/pt-br/firefox/addon/poster. O plugin também está disponível para o Google Chrome, em http://tinyurl.com/chrome-poster.

## Endpoints

Todos os endpoints com exceção do primeiro são RESTful com Content-Type: application/json.

## GET /urls/:id

Retorna um 301 redirect para o endereço original da URL.

301 Redirect
Location: 

Caso o id não existe no sistema, retorna um 404 Not Found.

## POST /users/:userid/urls

Cadastra uma nova url no sistema

{"url": "http://www.chaordic.com.br/folks"}

## GET /stats

Retorna estatísticas globais do sistema.

## GET /users/:userId/stats

Retorna estatísticas das urls de um usuário. O resultado é o mesmo que GET /stats mas com o escopo dentro de um usuário. 

Caso o usuário não exista o retorno deverá ser com código 404 Not Found.

## GET /stats/:id

Retorna estatísticas de uma URL específica.

## DELETE /urls/:id

Apaga uma URL do sistema. Deverá retornar vazio em caso de sucesso.

## POST /users

Cria um usuário. O conteúdo do request deverá ser com código 201 Created e retornar um objeto JSON com o conteúdo no seguinte formato. Caso já exista um usuário com o mesmo id retornar código 409 Conflict 

{"id": "jibao"}

## DELETE /user/:userId

Apaga um usuário.
