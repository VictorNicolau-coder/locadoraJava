# Projeto Locadora - POO

Este é um projeto de um sistema de locadora desenvolvido para a disciplina de Programação Orientada a Objetos.

## Pré-requisitos

* Java 8 (ou superior)
* MySQL Server

## Como Executar o Projeto

1.  **Clone o Repositório:**
    `git clone https://github.com/SEU_USUARIO/locadora-java-poo.git`

2.  **Crie o Banco de Dados:**

CREATE DATABASE IF NOT EXISTS LOCADORA_AV;

USE LOCADORA_AV;

CREATE TABLE filme (
    id INT AUTO_INCREMENT PRIMARY KEY,
    titulo VARCHAR(255) NOT NULL,
    genero VARCHAR(100),
    ano_lancamento INT,
    status VARCHAR(50)
);


INSERT INTO filme (titulo, genero, ano_lancamento, status) VALUES
('O Poderoso Chefão', 'DRAMA', 1972, 'DISPONIVEL'),
('Pulp Fiction', 'Crime', 1994, 'ALUGADO'),
('O Senhor dos Anéis: A Sociedade do Anel', 'FANTASIA', 2001, 'DISPONIVEL'),
('Matrix', 'FICCAO_CIENTIFICA', 1999, 'DISPONIVEL'),
('A Origem', 'FICCAO_CIENTIFICA', 2010, 'ALUGADO');

CREATE TABLE cliente (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(255) NOT NULL,
    email VARCHAR(255) UNIQUE,
    telefone VARCHAR(20)
);


CREATE TABLE aluguel (
    id INT AUTO_INCREMENT PRIMARY KEY,
    id_filme INT,
    id_cliente INT,
    data_aluguel DATE NOT NULL,
    data_devolucao DATE,
    FOREIGN KEY (id_filme) REFERENCES filme(id),
    FOREIGN KEY (id_cliente) REFERENCES cliente(id)
);


INSERT INTO cliente (nome, email, telefone) VALUES
('João Silva', 'joao.silva@example.com', '(11) 98765-4321'),
('Maria Oliveira', 'maria.o@example.com', '(21) 91234-5678'),
('Carlos Pereira', 'carlos.p@example.com', '(31) 99999-8888');


INSERT INTO aluguel (id_filme, id_cliente, data_aluguel, data_devolucao) VALUES
(2, 1, '2025-07-10', NULL), 
(5, 2, '2025-07-15', NULL); 

3.  **Configure a Conexão:**
    * No arquivo `ConexaoMySql.java`, verifique se o usuário e a senha do banco de dados estão corretos para a sua máquina.

4. **Adicione o Driver JDBC do MySQL**

Baixe o arquivo(Esta no class de Poo) .jar (ex: mysql-connector-j-8.x.x.jar).
No VS Code, no painel "JAVA PROJECTS", clique no + ao lado de "Referenced Libraries" e adicione o arquivo .jar que você baixou

5.  **Execute a Aplicação:**
    * Abra o projeto em sua no VS Code e execute teste colocando os comandos SQL ex: SELECT * FROM filme
    * Execute o arquivo `Teste.java`.
