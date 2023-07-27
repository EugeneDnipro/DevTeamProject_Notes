CREATE TABLE users (
    id BIGSERIAL PRIMARY KEY
)

CREATE TABLE notes (
    id UUID PRIMARY KEY,
    title VARCHAR,
    content VARCHAR,
    privacy VARCHAR,
    user_id BIGINT,
    FOREIGN KEY(user_id) REFERENCES users(id)
)

CREATE TABLE request (
    id BIGSERIAL PRIMARY KEY,
    creation_date DATE,
    description VARCHAR,
    repairer VARCHAR,
    cost BIGINT,
    completion_status VARCHAR,
    payment_status VARCHAR
);

CREATE TABLE feedback (
    id BIGSERIAL PRIMARY KEY,
    feedback_date DATE,
    rating SMALLINT,
    description VARCHAR,
    request_id BIGINT,
    client_id VARCHAR,
    repairer_id VARCHAR
);