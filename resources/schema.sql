USE paoj_proiect;

DROP TABLE IF EXISTS rezultat;
DROP TABLE IF EXISTS varianta;
DROP TABLE IF EXISTS intrebare;
DROP TABLE IF EXISTS quiz;
DROP TABLE IF EXISTS inscriere;
DROP TABLE IF EXISTS lectie;
DROP TABLE IF EXISTS curs;
DROP TABLE IF EXISTS utilizator;

CREATE TABLE utilizator (
                            id          BIGINT AUTO_INCREMENT PRIMARY KEY,
                            prenume     VARCHAR(100) NOT NULL,
                            nume        VARCHAR(100) NOT NULL,
                            email       VARCHAR(200) NOT NULL UNIQUE,
                            tip         VARCHAR(20)  NOT NULL,
                            departament VARCHAR(100)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE curs (
                      id          BIGINT AUTO_INCREMENT PRIMARY KEY,
                      nume        VARCHAR(200) NOT NULL,
                      profesor_id BIGINT       NOT NULL,
                      FOREIGN KEY (profesor_id) REFERENCES utilizator(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE lectie (
                        id       BIGINT AUTO_INCREMENT PRIMARY KEY,
                        titlu    VARCHAR(200) NOT NULL,
                        continut TEXT,
                        curs_id  BIGINT NOT NULL,
                        FOREIGN KEY (curs_id) REFERENCES curs(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE inscriere (
                           cursant_id BIGINT NOT NULL,
                           curs_id    BIGINT NOT NULL,
                           PRIMARY KEY (cursant_id, curs_id),
                           FOREIGN KEY (cursant_id) REFERENCES utilizator(id),
                           FOREIGN KEY (curs_id)    REFERENCES curs(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE quiz (
                      id          BIGINT AUTO_INCREMENT PRIMARY KEY,
                      titlu       VARCHAR(200) NOT NULL,
                      dificultate VARCHAR(20)  NOT NULL,
                      curs_id     BIGINT       NOT NULL,
                      FOREIGN KEY (curs_id) REFERENCES curs(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE intrebare (
                           id      BIGINT AUTO_INCREMENT PRIMARY KEY,
                           text    TEXT   NOT NULL,
                           punctaj INT    NOT NULL,
                           quiz_id BIGINT NOT NULL,
                           FOREIGN KEY (quiz_id) REFERENCES quiz(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE varianta (
                          id           BIGINT AUTO_INCREMENT PRIMARY KEY,
                          text         TEXT   NOT NULL,
                          corecta      TINYINT(1) NOT NULL DEFAULT 0,
                          intrebare_id BIGINT NOT NULL,
                          FOREIGN KEY (intrebare_id) REFERENCES intrebare(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE rezultat (
                          id         BIGINT AUTO_INCREMENT PRIMARY KEY,
                          cursant_id BIGINT NOT NULL,
                          quiz_id    BIGINT NOT NULL,
                          valoare    DOUBLE NOT NULL,
                          data       VARCHAR(20) NOT NULL,
                          FOREIGN KEY (cursant_id) REFERENCES utilizator(id),
                          FOREIGN KEY (quiz_id)    REFERENCES quiz(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;