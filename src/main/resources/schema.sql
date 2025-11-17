-- Drop tables if they exist
DROP TABLE IF EXISTS transfers;
DROP TABLE IF EXISTS accounts;

-- Create accounts table
CREATE TABLE IF NOT EXISTS accounts (
    id SERIAL PRIMARY KEY,
    account_number VARCHAR(50) UNIQUE NOT NULL,
    owner_name VARCHAR(100) NOT NULL,
    balance DECIMAL(15, 2) NOT NULL DEFAULT 0.00,
    currency VARCHAR(10) NOT NULL DEFAULT 'COP',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT check_currency CHECK (currency = 'COP')
);

-- Create transfers table
CREATE TABLE IF NOT EXISTS transfers (
    id SERIAL PRIMARY KEY,
    source_account_id BIGINT NOT NULL,
    destination_account_id BIGINT NOT NULL,
    amount DECIMAL(15, 2) NOT NULL,
    description TEXT,
    status VARCHAR(20) NOT NULL DEFAULT 'PENDING',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (source_account_id) REFERENCES accounts(id),
    FOREIGN KEY (destination_account_id) REFERENCES accounts(id)
);

-- Insert sample data
INSERT INTO accounts (account_number, owner_name, balance, currency) VALUES
    ('ACC-001', 'Juan Pérez', 5000000.00, 'COP'),
    ('ACC-002', 'María García', 3000000.00, 'COP'),
    ('ACC-003', 'Carlos López', 7500000.00, 'COP');