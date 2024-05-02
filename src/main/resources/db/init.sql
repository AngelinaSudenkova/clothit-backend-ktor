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
    time_updated  TIMESTAMP    NOT NULL
);

ALTER TABLE files ADD CONSTRAINT fk_files_items
    FOREIGN KEY (item_id) REFERENCES items(id) ON DELETE CASCADE;


