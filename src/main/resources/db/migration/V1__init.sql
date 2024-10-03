CREATE TABLE account
(
    account_id     VARCHAR(255)     NOT NULL,
    balance        DOUBLE PRECISION NOT NULL,
    account_name   VARCHAR(255),
    account_number BIGINT           NOT NULL,
    currency       VARCHAR(255),
    code           VARCHAR(255),
    label          VARCHAR(255),
    symbol         CHAR             NOT NULL,
    updated_at     TIMESTAMP WITHOUT TIME ZONE,
    created_at     TIMESTAMP WITHOUT TIME ZONE,
    owner_id       VARCHAR(255),
    CONSTRAINT pk_account PRIMARY KEY (account_id)
);

CREATE TABLE bank_users
(
    uid        VARCHAR(255) NOT NULL,
    firstname  VARCHAR(255),
    lastname   VARCHAR(255),
    username   VARCHAR(255) NOT NULL,
    dob        TIMESTAMP WITHOUT TIME ZONE,
    tel        BIGINT       NOT NULL,
    tag        VARCHAR(255),
    password   VARCHAR(255),
    gender     VARCHAR(255),
    created_at TIMESTAMP WITHOUT TIME ZONE,
    updated_at TIMESTAMP WITHOUT TIME ZONE,
    roles      TEXT[],
    CONSTRAINT pk_bank_users PRIMARY KEY (uid)
);

CREATE TABLE cards
(
    card_id          VARCHAR(255) NOT NULL,
    card_number      BIGINT       NOT NULL,
    card_holder_name VARCHAR(255),
    card_type        VARCHAR(255),
    cvv              VARCHAR(255),
    balance          DOUBLE PRECISION,
    pin              VARCHAR(255),
    billing_address  VARCHAR(255),
    iss              date,
    updated_at       TIMESTAMP WITHOUT TIME ZONE,
    card_expiry_date TIMESTAMP WITHOUT TIME ZONE,
    owner_id         VARCHAR(255),
    CONSTRAINT pk_cards PRIMARY KEY (card_id)
);

CREATE TABLE transactions
(
    tx_id      VARCHAR(255) NOT NULL,
    amount     DOUBLE PRECISION,
    tx_fee     DOUBLE PRECISION,
    sender     VARCHAR(255),
    receiver   VARCHAR(255),
    updated_at TIMESTAMP WITHOUT TIME ZONE,
    created_at TIMESTAMP WITHOUT TIME ZONE,
    status     VARCHAR(255),
    type       VARCHAR(255),
    card_id    VARCHAR(255),
    owner_id   VARCHAR(255),
    account_id VARCHAR(255),
    CONSTRAINT pk_transactions PRIMARY KEY (tx_id)
);

ALTER TABLE account
    ADD CONSTRAINT uc_account_accountnumber UNIQUE (account_number);

ALTER TABLE bank_users
    ADD CONSTRAINT uc_bank_users_username UNIQUE (username);

ALTER TABLE cards
    ADD CONSTRAINT uc_cards_cardnumber UNIQUE (card_number);

ALTER TABLE cards
    ADD CONSTRAINT uc_cards_owner UNIQUE (owner_id);

ALTER TABLE account
    ADD CONSTRAINT FK_ACCOUNT_ON_OWNER FOREIGN KEY (owner_id) REFERENCES bank_users (uid);

ALTER TABLE cards
    ADD CONSTRAINT FK_CARDS_ON_OWNER FOREIGN KEY (owner_id) REFERENCES bank_users (uid);

ALTER TABLE transactions
    ADD CONSTRAINT FK_TRANSACTIONS_ON_ACCOUNT FOREIGN KEY (account_id) REFERENCES account (account_id);

ALTER TABLE transactions
    ADD CONSTRAINT FK_TRANSACTIONS_ON_CARD FOREIGN KEY (card_id) REFERENCES cards (card_id);

ALTER TABLE transactions
    ADD CONSTRAINT FK_TRANSACTIONS_ON_OWNER FOREIGN KEY (owner_id) REFERENCES bank_users (uid);