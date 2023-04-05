USE `gallery-db`;

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
