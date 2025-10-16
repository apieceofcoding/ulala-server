CREATE TABLE members (
    id               NUMBER(19,0)       NOT NULL,
    username         VARCHAR2(30 CHAR)  NOT NULL,
    display_name     VARCHAR2(30 CHAR),
    image_url        VARCHAR2(255 CHAR),
    member_level     VARCHAR2(255 CHAR) NOT NULL,
    provider         VARCHAR2(255 CHAR) CHECK (provider IN ('KAKAO', 'ULALA')),
    provider_user_id VARCHAR2(255 CHAR),
    deleted          NUMBER(1,0)        NOT NULL CHECK (deleted IN (0, 1)),
    deleted_at       TIMESTAMP(6),
    created_at       TIMESTAMP(6)       NOT NULL,
    created_by       VARCHAR2(255 CHAR) NOT NULL,
    modified_at      TIMESTAMP(6)       NOT NULL,
    modified_by      VARCHAR2(255 CHAR) NOT NULL,
    PRIMARY KEY (id)
);

CREATE INDEX idx_members_username ON members (username);
CREATE INDEX idx_members_provider_user_id ON members (provider, provider_user_id);
