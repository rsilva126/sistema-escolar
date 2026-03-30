CREATE DATABASE sistema_escolar;
USE sistema_escolar;

CREATE TABLE escolas (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(150) NOT NULL,
    codigo_inep VARCHAR(30),
    cnpj VARCHAR(20),
    endereco VARCHAR(255),
    telefone VARCHAR(20),
    email VARCHAR(120),
    status VARCHAR(20) NOT NULL DEFAULT 'ATIVA'
);

CREATE TABLE usuarios (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(150) NOT NULL,
    cpf VARCHAR(14) UNIQUE,
    email VARCHAR(120) NOT NULL UNIQUE,
    senha VARCHAR(255) NOT NULL,
    telefone VARCHAR(20),
    escola_id BIGINT NOT NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'ATIVO',
    CONSTRAINT fk_usuario_escola FOREIGN KEY (escola_id) REFERENCES escolas(id)
);

CREATE TABLE anos_letivos (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    descricao VARCHAR(20) NOT NULL,
    data_inicio DATE NOT NULL,
    data_fim DATE NOT NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'ATIVO',
    escola_id BIGINT NOT NULL,
    CONSTRAINT fk_ano_escola FOREIGN KEY (escola_id) REFERENCES escolas(id)
);

CREATE TABLE series (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(50) NOT NULL,
    nivel_ensino VARCHAR(50),
    status VARCHAR(20) NOT NULL DEFAULT 'ATIVA',
    escola_id BIGINT NOT NULL,
    CONSTRAINT fk_serie_escola FOREIGN KEY (escola_id) REFERENCES escolas(id)
);

CREATE TABLE turnos (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(30) NOT NULL,
    horario_inicio TIME,
    horario_fim TIME,
    status VARCHAR(20) NOT NULL DEFAULT 'ATIVO',
    escola_id BIGINT NOT NULL,
    CONSTRAINT fk_turno_escola FOREIGN KEY (escola_id) REFERENCES escolas(id)
);

CREATE TABLE turmas (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(50) NOT NULL,
    serie_id BIGINT NOT NULL,
    turno_id BIGINT NOT NULL,
    ano_letivo_id BIGINT NOT NULL,
    escola_id BIGINT NOT NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'ATIVA',
    CONSTRAINT fk_turma_serie FOREIGN KEY (serie_id) REFERENCES series(id),
    CONSTRAINT fk_turma_turno FOREIGN KEY (turno_id) REFERENCES turnos(id),
    CONSTRAINT fk_turma_ano FOREIGN KEY (ano_letivo_id) REFERENCES anos_letivos(id),
    CONSTRAINT fk_turma_escola FOREIGN KEY (escola_id) REFERENCES escolas(id)
);

CREATE TABLE alunos (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(150) NOT NULL,
    data_nascimento DATE,
    cpf VARCHAR(14),
    rg VARCHAR(20),
    nome_mae VARCHAR(150),
    nome_pai VARCHAR(150),
    telefone_responsavel VARCHAR(20),
    endereco VARCHAR(255),
    escola_id BIGINT NOT NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'ATIVO',
    CONSTRAINT fk_aluno_escola FOREIGN KEY (escola_id) REFERENCES escolas(id)
);

CREATE TABLE matriculas (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    aluno_id BIGINT NOT NULL,
    turma_id BIGINT NOT NULL,
    ano_letivo_id BIGINT NOT NULL,
    data_matricula DATE NOT NULL,
    situacao VARCHAR(30) NOT NULL DEFAULT 'ATIVA',
    numero_matricula VARCHAR(30) NOT NULL UNIQUE,
    CONSTRAINT fk_matricula_aluno FOREIGN KEY (aluno_id) REFERENCES alunos(id),
    CONSTRAINT fk_matricula_turma FOREIGN KEY (turma_id) REFERENCES turmas(id),
    CONSTRAINT fk_matricula_ano FOREIGN KEY (ano_letivo_id) REFERENCES anos_letivos(id)
);
