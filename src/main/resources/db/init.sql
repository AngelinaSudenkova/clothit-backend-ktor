-- CREATE TYPE itemCategory AS ENUM ('SUMMER', 'AUTUMN', 'WINTER','SPRING');

CREATE TABLE IF NOT EXISTS items
(
    id           SERIAL       NOT NULL PRIMARY KEY,
    category     VARCHAR(25)  NOT NULL,
    description  VARCHAR(255) NULL,
    time_created TIMESTAMP    NOT NULL
);

CREATE TABLE IF NOT EXISTS files
(
    id           SERIAL       NOT NULL PRIMARY KEY,
    name         varchar(255) NOT NULL,
    size         BIGINT       NOT NULL,
    item_id      INT          NULL,
    outfit_id    INT          NULL,
    user_id      INT          NULL,
    time_created TIMESTAMP    NOT NULL,
    time_updated TIMESTAMP    NOT NULL
);

ALTER TABLE files
    ADD CONSTRAINT fk_files_items
        FOREIGN KEY (item_id) REFERENCES items (id) ON DELETE CASCADE;


CREATE TABLE IF NOT EXISTS outfits
(
    id           SERIAL       NOT NULL PRIMARY KEY,
    season       VARCHAR(25)  NOT NULL,
    description  VARCHAR(255) NULL,
    name         VARCHAR(50)  NOT NULL,
    time_created TIMESTAMP    NOT NULL,
    time_updated TIMESTAMP    NOT NULL
);


CREATE TABLE IF NOT EXISTS items_to_outfits
(
    item_id   INT NOT NULL,
    outfit_id INT NOT NULL
);

ALTER TABLE items_to_outfits
    ADD COLUMN id SERIAL NOT NULL PRIMARY KEY;

ALTER TABLE items_to_outfits
    ADD CONSTRAINT fk_items
        FOREIGN KEY (item_id) REFERENCES items (id) ON DELETE CASCADE;

ALTER TABLE items_to_outfits
    ADD CONSTRAINT fk_outfits
        FOREIGN KEY (outfit_id) REFERENCES outfits (id) ON DELETE CASCADE;


CREATE TABLE IF NOT EXISTS users
(
    id            uuid NOT NULL PRIMARY KEY,
    email         VARCHAR(50)  NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    username      VARCHAR(50)  NOT NULL,
    register_time TIMESTAMP    NULL,
    last_login    TIMESTAMP    NULL,
    is_active     BOOLEAN      NULL

);

CREATE TABLE IF NOT EXISTS tokens
(
    id      SERIAL          NOT NULL,
    user_id uuid NOT NULL,
    token   VARCHAR(255) NOT NULL
);

ALTER TABLE tokens
    ADD CONSTRAINT fk_users
        FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE ;


CREATE TABLE IF NOT EXISTS friend
(
    id SERIAL  NOT NULL,
    user_id_owner uuid NOT NULL,
    user_id_invited uuid NOT NULL,
    invited_time TIMESTAMP NULL,
    status VARCHAR(20) NOT NULL
);

ALTER TABLE friend
    ADD CONSTRAINT fk_user_owner
        FOREIGN KEY (user_id_owner) REFERENCES users (id)  ;

ALTER TABLE friend
    ADD CONSTRAINT fk_user_invited
        FOREIGN KEY (user_id_invited) REFERENCES users (id)  ;



