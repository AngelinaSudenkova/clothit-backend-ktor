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


CREATE TABLE IF NOT EXISTS outfits(
    id      SERIAL          NOT NULL PRIMARY KEY,
    season  VARCHAR(25)     NOT NULL,
    description  VARCHAR(255) NULL,
    name          VARCHAR(50) NOT NULL,
    time_created TIMESTAMP    NOT NULL,
    time_updated  TIMESTAMP    NOT NULL
);


CREATE TABLE IF NOT EXISTS items_to_outfits(
    item_id   INT       NOT NULL ,
    outfit_id INT       NOT NULL
);

ALTER TABLE items_to_outfits ADD COLUMN id SERIAL NOT NULL PRIMARY KEY ;

ALTER TABLE items_to_outfits ADD CONSTRAINT fk_items
    FOREIGN KEY (item_id) REFERENCES items(id) ON DELETE CASCADE;

ALTER TABLE items_to_outfits ADD CONSTRAINT fk_outfits
    FOREIGN KEY (outfit_id) REFERENCES outfits(id) ON DELETE CASCADE;






