[![CircleCI](https://circleci.com/gh/academiadev-jlle/backend-suicidesquad/tree/master.svg?style=shield)](https://circleci.com/gh/academiadev-jlle/backend-suicidesquad/tree/master) [![Codecov branch](https://img.shields.io/codecov/c/github/academiadev-jlle/backend-suicidesquad/master.svg)](https://codecov.io/gh/academiadev-jlle/backend-suicidesquad) [![Waffle.io - Columns and their card count](https://badge.waffle.io/academiadev-jlle/wiki-suicidesquad.svg?columns=all)](https://waffle.io/academiadev-jlle/wiki-suicidesquad)
# AcademiaDev Joinville - projeto PetCodes

Projeto da aplicação para gerenciador de animais achados, perdidos e para adoção.

## Configuração

Após clonar o projeto, será necessário configurar alguns parâmetros
para a autenticação dos usuários durante o desenvolvimento.

Faça uma cópia do arquivo ```src/main/resources/application-dev.properties.sample```
com o nome ```application-dev.properties```, no mesmo diretório, e configure os valores
nele descritos.

### Requisitos mínimos

Java 8

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

TODO

## Autores

* **Time do backend:**
  * **Julio Caye** - [JCaye](https://github.com/JCaye)
  * **Yan Henning** - [Yanhennning](https://github.com/Yanhenning)
  * **Yuri Salvador** - [Yurihs](https://github.com/yurihs)
* **Time do frontend:**
  * **Larissa Lopes** - [lopeslarissa](https://github.com/lopeslarissa)
  * **Iago Elias** - [imarinheiro](https://github.com/imarinheiro)

## Agradecimentos

* À [Code:Nation](https://www.codenation.com.br/) pela oportunidade de participar do projeto AcademiaDev Joinville
* Ao [Bruno de souza](https://github.com/bnubruno) por ser um tutor supimpa
* etc
