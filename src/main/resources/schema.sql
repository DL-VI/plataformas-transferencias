-- Drop tables if they exist (with CASCADE to handle foreign keys)
DROP TABLE IF EXISTS transfers CASCADE;
DROP TABLE IF EXISTS accounts CASCADE;

-- Create accounts table
CREATE TABLE accounts (
    id SERIAL PRIMARY KEY,
    account_number VARCHAR(50) UNIQUE NOT NULL,
    owner_name VARCHAR(100) NOT NULL,
    balance DECIMAL(15, 2) NOT NULL DEFAULT 0.00,
    currency VARCHAR(10) NOT NULL DEFAULT 'COP',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT check_currency CHECK (currency = 'COP')
);

-- Create transfers table
CREATE TABLE transfers (
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

-- Insert sample data (only if not exists)
INSERT INTO accounts (account_number, owner_name, balance, currency) 
SELECT 'ACC-001', 'Juan Pérez', 5000000.00, 'COP'
WHERE NOT EXISTS (SELECT 1 FROM accounts WHERE account_number = 'ACC-001');

INSERT INTO accounts (account_number, owner_name, balance, currency) 
SELECT 'ACC-002', 'María García', 3000000.00, 'COP'
WHERE NOT EXISTS (SELECT 1 FROM accounts WHERE account_number = 'ACC-002');

INSERT INTO accounts (account_number, owner_name, balance, currency) 
SELECT 'ACC-003', 'Carlos López', 7500000.00, 'COP'
WHERE NOT EXISTS (SELECT 1 FROM accounts WHERE account_number = 'ACC-003');
