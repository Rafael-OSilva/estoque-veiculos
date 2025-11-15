# 🚗 Sistema de Estoque de Veículos

![Status: Concluído](https://img.shields.io/badge/status-concluído-green)
![Java](https://img.shields.io/badge/Java-21-blue)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.2.0-brightgreen)
![Database](https://img.shields.io/badge/MySQL-8.0-orange)

Projeto acadêmico desenvolvido para a disciplina de **Programação Orientada a Objetos** do 3º semestre do curso de **Análise e Desenvolvimento de Sistemas**.

O objetivo é um sistema CRUD completo para gerenciar o estoque de veículos de uma concessionária, aplicando os conceitos de POO, arquitetura em camadas, persistência de dados e uma API RESTful.


---

## ✨ Funcionalidades

* **Gestão de Marcas:** CRUD completo de Marcas.
* **Gestão de Modelos:** CRUD completo de Modelos, com relacionamento `ManyToOne` com Marcas.
* **Gestão de Veículos:** CRUD completo de Veículos, com relacionamento `ManyToOne` com Modelos.
* **API RESTful:** Endpoints para todas as operações do sistema.
* **Interface Web:** Frontend em HTML/CSS/JS puro consumindo a API.
* **Filtros Dinâmicos:** Filtro de veículos por marca, modelo ou status.
* **Relatórios Gerenciais:** Contagem de veículos por status e valor total do estoque.
* **Validação de Regras de Negócio:**
    * Impede a exclusão de Marcas que possuem Modelos.
    * Impede a exclusão de Modelos que possuem Veículos.

---

## 🛠️ Tecnologias Utilizadas

| Categoria | Tecnologia |
| :--- | :--- |
| **Backend** | Java 21 (LTS) |
| | Spring Boot 3.2.0 |
| | Spring Data JPA |
| | Maven |
| **Banco de Dados** | MySQL 8.0 |
| **Frontend** | HTML5 |
| | CSS3 |
| | JavaScript (ES6+) |
| **Desenvolvimento** | IntelliJ IDEA |
| | Git & GitHub |
| | Postman (Testes de API) |

---

## 🎓 Conceitos de POO Aplicados

* **Abstração:** Uso de `Interfaces` (ex: `VeiculoRepository`, `MarcaService`) que definem contratos sem se preocupar com a implementação, permitindo que o Spring Data JPA e o Spring IoC gerenciem a lógica de baixo nível.
* **Encapsulamento:** Todos os atributos das entidades (`Marca`, `Modelo`, `Veiculo`) são `private`, com acesso controlado por métodos `getters` e `setters`. As validações (`@NotNull`, `@Size`) também garantem a integridade do objeto.
* **Herança (Composição):** Embora a herança direta não seja o foco, o projeto utiliza intensamente a **Composição**, que é um pilar de POO. Um `Veiculo` **possui um** `Modelo`, e um `Modelo` **possui uma** `Marca`.
* **Polimorfismo:** Aplicado no `GlobalExceptionHandler`, que captura diferentes tipos de exceções (`BusinessException`, `EntityNotFoundException`) e as trata de forma padronizada, retornando uma `ResponseEntity` polimórfica (Erro 400 ou 404).

---

## 🚀 Como Executar o Projeto

### Pré-requisitos

* [Java JDK 21](https://www.oracle.com/java/technologies/javase/jdk21-archive-downloads.html)
* [Apache Maven 3.6+](https://maven.apache.org/download.cgi)
* [MySQL 8.0+](https://dev.mysql.com/downloads/mysql/)

### Passo a Passo

1.  **Clone o repositório:**
    ```bash
    git clone [URL_DO_SEU_REPOSITORIO]
    cd estoque-veiculos
    ```

2.  **Configure o Banco de Dados (MySQL):**
    * Execute o script a seguir para criar o banco e um usuário dedicado:
    ```sql
    CREATE DATABASE estoque_veiculos;
    CREATE USER 'estoque_user'@'localhost' IDENTIFIED BY 'senha123';
    GRANT ALL PRIVILEGES ON estoque_veiculos.* TO 'estoque_user'@'localhost';
    FLUSH PRIVILEGES;
    ```
    *(As tabelas `marcas`, `modelos` e `veiculos` serão criadas automaticamente pelo Spring Boot na primeira execução)*

3.  **Configure a Aplicação:**
    * Abra o arquivo `src/main/resources/application.yml`.
    * Verifique se as credenciais do banco de dados estão corretas:
    ```yaml
    spring:
      datasource:
        url: jdbc:mysql://localhost:3306/estoque_veiculos
        username: estoque_user
        password: senha123
      jpa:
        hibernate:
          ddl-auto: update # (ou 'validate' em produção)
        show-sql: true
    ```

4.  **Execute a Aplicação:**
    * Pela linha de comando (na raiz do projeto):
    ```bash
    mvn clean spring-boot:run
    ```
    * Ou pela sua IDE (ex: IntelliJ), executando o método `main` da classe `EstoqueVeiculosApplication.java`.

5.  **Acesse o Sistema:**
    * Abra seu navegador e acesse: **`http://localhost:8080`**

---

## 👨‍💻 Autor

**Rafael Oliveira Da Silva**

* **Email:** `rafinha101419.silva@gmail.com`
* **Email 2:** `rafael5555@hotmail.com.br`
