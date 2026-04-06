CREATE TABLE users (
                       id BIGSERIAL PRIMARY KEY,
                       username varchar(50) NOT NULL UNIQUE,
                       password varchar(255) NOT NULL,
                       email varchar(255) NOT NULL,
                       is_active BOOLEAN NOT NULL DEFAULT FALSE,
                       is_verified BOOLEAN NOT NULL DEFAULT FALSE,
                       created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                       role varchar(50) NOT NULL
),

CREATE TABLE companies (
    id BIGSERIAL PRIMARY KEY ,
    name varchar(100) NOT NULL UNIQUE,
    address varchar(200) NOT NULL ,
    email varchar(100),
    website varchar(200),
    notes TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP,
    user_id BIGINT NOT NULL REFERENCES users (id)
);

CREATE TABLE applications (
    id BIGSERIAL PRIMARY KEY ,
    company_id BIGINT NOT NULL,
    position varchar(100) NOT NULL ,
    salary DECIMAL,
    notes TEXT,
    applied_date DATE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP,
    status varchar(50) NOT NULL ,
    FOREIGN KEY (company_id) REFERENCES companies(id) ON DELETE CASCADE

)


