CREATE TABLE IF NOT EXISTS refresh_token
(
    refresh_token_id BIGINT       NOT NULL AUTO_INCREMENT,
    crew_id          BIGINT       NOT NULL UNIQUE,
    token            VARCHAR(512) NOT NULL,
    expires_at       DATETIME     NOT NULL,
    PRIMARY KEY (refresh_token_id),
    FOREIGN KEY (crew_id) REFERENCES crew (crew_id)
);
