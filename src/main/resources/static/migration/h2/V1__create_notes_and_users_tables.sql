CREATE TABLE users (
    id IDENTITY PRIMARY KEY
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
    id IDENTITY PRIMARY KEY,
    creation_date DATE,
    description VARCHAR,
    repairer VARCHAR,
    cost BIGINT,
    completion_status VARCHAR,
    payment_status VARCHAR
);

CREATE TABLE feedback (
    id IDENTITY PRIMARY KEY,
    feedback_date DATE,
    rating SMALLINT,
    description VARCHAR,
    request_id BIGINT,
    client_id VARCHAR,
    repairer_id VARCHAR
);