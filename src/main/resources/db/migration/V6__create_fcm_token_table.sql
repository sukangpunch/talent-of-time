CREATE TABLE fcm_token (
    fcm_token_id BIGINT NOT NULL AUTO_INCREMENT,
    crew_id      BIGINT       NOT NULL,
    token        VARCHAR(255) NOT NULL,
    PRIMARY KEY (fcm_token_id),
    UNIQUE KEY uk_fcm_token_token (token),
    CONSTRAINT fk_fcm_token_crew FOREIGN KEY (crew_id) REFERENCES crew (crew_id)
);

ALTER TABLE crew
    DROP COLUMN fcm_token;
