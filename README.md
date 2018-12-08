[![CircleCI](https://circleci.com/gh/academiadev-jlle/backend-suicidesquad/tree/master.svg?style=shield)](https://circleci.com/gh/academiadev-jlle/backend-suicidesquad/tree/master) [![Codecov branch](https://img.shields.io/codecov/c/github/academiadev-jlle/backend-suicidesquad/master.svg)](https://codecov.io/gh/academiadev-jlle/backend-suicidesquad) [![Waffle.io - Columns and their card count](https://badge.waffle.io/academiadev-jlle/wiki-suicidesquad.svg?columns=all)](https://waffle.io/academiadev-jlle/wiki-suicidesquad)

Este repositório contém o código backend do 404 Pets.  
Conheça mais sobre o projeto no outro repositório, [wiki-suicidesquad](https://github.com/academiadev-jlle/wiki-suicidesquad/).

## Configuração

Antes de tudo, tenha certeza de instalar o Java JDK 8. Após clonar o projeto,
será necessário configurar alguns parâmetros para a autenticação dos usuários durante o desenvolvimento.

Faça uma cópia do arquivo ```src/main/resources/application-dev.properties.sample```
com o nome ```application-dev.properties```, no mesmo diretório, e configure os valores
nele descritos.

## Execução

**Testes:** Use a task do gradle para executar os testes:

    ./gradlew test

**Desenvolvimento**: A task abaixo executa a aplicação na porta 8080
do seu ambiente de desenvolvimento.

    ./gradlew bootRun

## _Deployment_ com Heroku

Pré-requisitos:

* Uma _API key_ do [SendGrid](https://sendgrid.com/), para o envio de emails. 

Após criar um app no [Heroku](https://heroku.com/), é necessário adicionar as seguintes _config vars_:

* `SPRING_PROFILES_ACTIVE=prod`: ativa a configuração de produção
* `SENDGRID_API_KEY`: sua _API key_ do sendgrid
* `JWT_TOKEN_SECRET_KEY`: insira 256 caracteres aleatórios, usados na geração de tokens

Siga as instruções do Heroku para fazer um _deploy_  via Heroku CLI. Os demais parâmetros serão configurados
automaticamente no momento do _push_. 

## Tecnologias utilizadas

* Gradle
* [Spring](https://spring.io/)
  * Web: controladores, repostas HTTP
  * Data: interafaces de acesso aos dados
  * Security: autenticação
  * Mail: envio de emails por SMTP
  * Social: interação com a API do Facebook
* [Banco de dados H2](http://www.h2database.com/html/main.html)
* [Lombok](https://projectlombok.org/): geração de código repetitivo (_getters_, _setters_, etc.)
* [MapStruct](http://mapstruct.org/): mapeamento entre objetos, principalmente DAOs e DTOs
* [Querydsl](http://www.querydsl.com/): construção de consultas de dados complexas, como a pesquisa de pets
* [Swagger](https://swagger.io/): geração de documentação da API
* [Circle CI](https://circleci.com/): integração contínua
* [Codecov](https://codecov.io/): relatórios de cobertura dos testes
