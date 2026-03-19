    CREATE TABLE usuarios (
                             ID_USUARIOS BIGINT AUTO_INCREMENT PRIMARY KEY,
                             EMAIL VARCHAR(150) NOT NULL UNIQUE,
                             NOME VARCHAR(150) NOT NULL,
                             CPF VARCHAR(14) NOT NULL UNIQUE,
                             TELEFONE VARCHAR(20),
                             DATA_NASCIMENTO DATE,
                             ENDERECO VARCHAR(255),
                             CEP VARCHAR(10),
                             BAIRRO VARCHAR(100),
                             CIDADE VARCHAR(100),
                             SENHA_HASH VARCHAR(255) NOT NULL,
                             ROLE VARCHAR(20) NOT NULL,
                             DATA_CRIACAO TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                             DATA_ATUALIZACAO TIMESTAMP DEFAULT CURRENT_TIMESTAMP
    );
    CREATE TABLE servicos (
                             ID_SERVICOS BIGINT AUTO_INCREMENT PRIMARY KEY,
                             NOME VARCHAR(150) NOT NULL,
                             DESCRICAO VARCHAR(255),
                             DURACAO_MINUTOS INT NOT NULL,
                             PRECO DECIMAL(10,2) NOT NULL
    );
    CREATE TABLE barbeiro_servicos (
                                      ID_BARBEIRO BIGINT NOT NULL,
                                      ID_SERVICOS BIGINT NOT NULL,

                                      PRIMARY KEY (ID_BARBEIRO, ID_SERVICOS),

                                      CONSTRAINT FK_BS_BARBEIRO FOREIGN KEY (ID_BARBEIRO)
                                          REFERENCES USUARIO(ID_USUARIOS),

                                      CONSTRAINT FK_BS_SERVICO FOREIGN KEY (ID_SERVICOS)
                                          REFERENCES SERVICO(ID_SERVICOS)
    );
    CREATE TABLE agendamentos (
                                 ID_AGENDAMENTOS BIGINT AUTO_INCREMENT PRIMARY KEY,
                                 ID_CLIENTE BIGINT NOT NULL,
                                 ID_BARBEIROS BIGINT NOT NULL,
                                 ID_SERVICOS BIGINT NOT NULL,
                                 DATA_HORA TIMESTAMP NOT NULL,

                                 CONSTRAINT FK_AG_CLIENTE FOREIGN KEY (ID_CLIENTE)
                                     REFERENCES USUARIO(ID_USUARIOS),

                                 CONSTRAINT FK_AG_BARBEIRO FOREIGN KEY (ID_BARBEIRO)
                                     REFERENCES USUARIO(ID_USUARIOS),

                                 CONSTRAINT FK_AG_SERVICO FOREIGN KEY (ID_SERVICOS)
                                     REFERENCES SERVICO(ID_SERVICOS)
    );