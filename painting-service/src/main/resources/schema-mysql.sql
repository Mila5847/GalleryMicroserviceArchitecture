USE `painting-db`;

create table if not exists painters (
                                        id INTEGER NOT NULL AUTO_INCREMENT PRIMARY KEY,
                                        painter_id VARCHAR(36),
    name VARCHAR(50),
    origin VARCHAR(50),
    birth_date DATE,
    death_date DATE
);

create table if not exists paintings (
                                         id INTEGER NOT NULL AUTO_INCREMENT PRIMARY KEY,
                                         painting_id VARCHAR(36),
    gallery_id VARCHAR(36),
    title VARCHAR(50),
    year_created INTEGER,
    painter_id VARCHAR(36)
    );
