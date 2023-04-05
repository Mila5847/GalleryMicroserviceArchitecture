USE `sculpture-db`;

create table if not exists sculptures (
                                         id INTEGER NOT NULL AUTO_INCREMENT PRIMARY KEY,
                                         sculpture_id VARCHAR(36),
    gallery_id VARCHAR(36),
    title VARCHAR(50),
    material VARCHAR(100),
    texture VARCHAR(100)
    );
