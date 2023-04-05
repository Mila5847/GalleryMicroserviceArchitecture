USE `exhibition-db`;

create table if not exists painters (
                                        id INTEGER NOT NULL AUTO_INCREMENT PRIMARY KEY,
                                        painter_id VARCHAR(36),
    name VARCHAR(50),
    origin VARCHAR(50),
    birth_date DATE,
    death_date DATE
);

create table if not exists exhibitions (
                                        id INTEGER NOT NULL AUTO_INCREMENT PRIMARY KEY,
                                        exhibition_id VARCHAR(36),
    gallery_id VARCHAR(36),
    name VARCHAR(50),
    room_number INTEGER,
    duration INTEGER,
    start_day VARCHAR(36),
    end_day VARCHAR(36)
);


create table if not exists paintings (
                                         id INTEGER NOT NULL AUTO_INCREMENT PRIMARY KEY,
                                         painting_id VARCHAR(36),
    exhibition_id VARCHAR(36),
    gallery_id VARCHAR(36),
    title VARCHAR(50),
    year INTEGER,
    painter_id VARCHAR(36)
    );

create table if not exists sculptures (
                                         id INTEGER NOT NULL AUTO_INCREMENT PRIMARY KEY,
                                         sculpture_id VARCHAR(36),
    exhibition_id VARCHAR(36),
    gallery_id VARCHAR(36),
    title VARCHAR(50),
    material VARCHAR(100),
    texture VARCHAR(100)
    );

create table if not exists galleries (
                                         id INTEGER NOT NULL AUTO_INCREMENT PRIMARY KEY,
                                         gallery_id VARCHAR(36),
    name VARCHAR(100),
    open_from VARCHAR(100),
    open_until VARCHAR(100),
    street_address VARCHAR(100),
    city VARCHAR(100),
    province VARCHAR(100),
    country VARCHAR(100),
    postal_code VARCHAR(100)
);
