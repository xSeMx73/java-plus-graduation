CREATE TABLE IF NOT EXISTS categories
(
    id   BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name VARCHAR(50) NOT NULL,
    CONSTRAINT categories_name_ux2 UNIQUE (name)
);

CREATE TABLE IF NOT EXISTS events
(
    id                 BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    initiator_id       BIGINT                      NOT NULL,
    title              VARCHAR(120)                NOT NULL,
    confirmed_requests BIGINT,
    category_id        BIGINT REFERENCES categories(id) NOT NULL,
    event_date         TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    lat                REAL,
    lon                REAL,
    annotation         VARCHAR(2000)               NOT NULL,
    description        VARCHAR(7000)               NOT NULL,
    participant_limit  BIGINT                      NOT NULL,
    paid               BOOLEAN                     NOT NULL,
    request_moderation BOOLEAN                     NOT NULL,
    created_on         TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    published_on       TIMESTAMP WITHOUT TIME ZONE,
    state              VARCHAR(10)                 NOT NULL,
    views              BIGINT,

    CONSTRAINT state_values CHECK (state IN ('PENDING', 'PUBLISHED', 'CANCELED'))
    );



CREATE TABLE IF NOT EXISTS compilations
(
    id     BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    pinned BOOLEAN      NOT NULL,
    title  VARCHAR(255) NOT NULL,
    CONSTRAINT compilations_title_ux UNIQUE (title)
    );

CREATE TABLE IF NOT EXISTS compilations_events
(
    compilation_id BIGINT NOT NULL,
    events_id      BIGINT NOT NULL,
    CONSTRAINT compilations_events_compilation_id_fk FOREIGN KEY (compilation_id) REFERENCES compilations (id),
    CONSTRAINT compilations_events_event_id_fk FOREIGN KEY (events_id) REFERENCES events (id)
    );