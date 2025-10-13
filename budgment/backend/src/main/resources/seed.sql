PRAGMA foreign_keys = ON;

-- USERS
CREATE TABLE IF NOT EXISTS users (
    id TEXT PRIMARY KEY,
    name TEXT,
    username TEXT UNIQUE,
    password_hash TEXT,
    created_at DATETIME DEFAULT (CURRENT_TIMESTAMP),
    updated_at DATETIME
);

-- ACCOUNTS
CREATE TABLE IF NOT EXISTS accounts (
    id TEXT PRIMARY KEY,
    user_id TEXT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    name TEXT NOT NULL,
    currency CHAR(3) NOT NULL,                -- ISO-4217
    balance_cache_encrypted BLOB,             -- ciphertext (app)
    balance_iv BLOB,                          -- IV usado para cifrado (app)
    balance_enc_version INTEGER DEFAULT 1,    -- versión para rotación de clave/algoritmo
    created_at DATETIME DEFAULT (CURRENT_TIMESTAMP),
    updated_at DATETIME,
    deleted_at DATETIME
);

-- CATEGORIES
CREATE TABLE IF NOT EXISTS categories (
    id TEXT PRIMARY KEY,
    user_id TEXT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    name TEXT NOT NULL,
    type TEXT NOT NULL CHECK (type IN ('Gasto','Ingreso')),
    created_at DATETIME DEFAULT (CURRENT_TIMESTAMP),
    updated_at DATETIME,
    deleted_at DATETIME
);

-- RECURRING TRANSACTIONS (para generar transacciones periódicas)
CREATE TABLE IF NOT EXISTS recurring_transactions (
    id TEXT PRIMARY KEY,
    user_id TEXT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    account_id TEXT NOT NULL REFERENCES accounts(id) ON DELETE RESTRICT,
    category_id TEXT REFERENCES categories(id) ON DELETE SET NULL,
    base_amount INTEGER NOT NULL,                -- en centavos
    currency CHAR(3),                             -- opcional
    period_type INTEGER NOT NULL,                 -- mapping (1=WEEKLY,...,99=CUSTOM)
    period_interval INTEGER DEFAULT 1,            -- cada N unidades
    period_unit TEXT CHECK (period_unit IN ('DAY','WEEK','MONTH','YEAR')),
    anchor_date DATE,                             -- fecha base para calcular recurrencias
    next_occurrence DATE,                         -- próxima fecha a crear
    end_date DATE,
    active INTEGER DEFAULT 1,                     -- 0=false, 1=true
    created_at DATETIME DEFAULT (CURRENT_TIMESTAMP),
    updated_at DATETIME
);

-- TRANSACTIONS
CREATE TABLE IF NOT EXISTS transactions (
    id TEXT PRIMARY KEY,
    user_id TEXT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    account_id TEXT NOT NULL REFERENCES accounts(id) ON DELETE RESTRICT,
    category_id TEXT REFERENCES categories(id) ON DELETE SET NULL,
    amount INTEGER NOT NULL CHECK (amount != 0),   -- centavos; signed (ej. -12345 = -123.45)
    currency CHAR(3),                              -- si NULL, se asume currency de la cuenta
    exchange_rate_at_transaction REAL,             -- opcional
    description TEXT,
    date DATE NOT NULL,                             -- fecha efectiva YYYY-MM-DD
    created_at DATETIME DEFAULT (CURRENT_TIMESTAMP),
    updated_at DATETIME,
    transfer_id TEXT,                               -- id común para pares de transferencias
    recurring_id TEXT REFERENCES recurring_transactions(id) ON DELETE SET NULL
);

-- BUDGETS
CREATE TABLE IF NOT EXISTS budgets (
    id TEXT PRIMARY KEY,
    user_id TEXT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    name TEXT NOT NULL,
    amount_limit INTEGER NOT NULL,                  -- centavos
    scope TEXT NOT NULL CHECK (scope IN ('Global','Category')),
    period_type INTEGER NOT NULL,                   -- mapping: 1=WEEKLY,2=BIWEEKLY,...,8=ANNUAL,99=CUSTOM
    period_interval INTEGER DEFAULT 1,              -- cada N unidades
    period_unit TEXT CHECK (period_unit IN ('DAY','WEEK','MONTH','YEAR')),
    custom_rrule TEXT,                              -- opcional: "FREQ=MONTH;INTERVAL=3" tipo rrule simplificado
    start_date DATE NOT NULL,
    end_date DATE NOT NULL,
    auto_create_recurring INTEGER DEFAULT 0,        -- 0=false, 1=true
    created_at DATETIME DEFAULT (CURRENT_TIMESTAMP),
    updated_at DATETIME,
    CHECK (start_date <= end_date)
);

-- BUDGET_CATEGORIES (mapeo n:N entre budgets y categories)
CREATE TABLE IF NOT EXISTS budget_categories (
    id TEXT PRIMARY KEY,
    budget_id TEXT NOT NULL REFERENCES budgets(id) ON DELETE CASCADE,
    category_id TEXT NOT NULL REFERENCES categories(id) ON DELETE CASCADE,
    created_at DATETIME DEFAULT (CURRENT_TIMESTAMP),
    UNIQUE (budget_id, category_id)
);

-- INDICES RECOMENDADOS (mejoran consultas/reportes)
CREATE INDEX IF NOT EXISTS idx_tx_user_date ON transactions(user_id, date);
CREATE INDEX IF NOT EXISTS idx_tx_account_date ON transactions(account_id, date);
CREATE INDEX IF NOT EXISTS idx_tx_category_date ON transactions(category_id, date);
CREATE INDEX IF NOT EXISTS idx_tx_transfer_id ON transactions(transfer_id);
CREATE INDEX IF NOT EXISTS idx_accounts_user ON accounts(user_id);
CREATE INDEX IF NOT EXISTS idx_categories_user ON categories(user_id);
CREATE INDEX IF NOT EXISTS idx_budgets_user_start_end ON budgets(user_id, start_date, end_date);
CREATE INDEX IF NOT EXISTS idx_recurring_next ON recurring_transactions(next_occurrence);
CREATE INDEX IF NOT EXISTS idx_recurring_user ON recurring_transactions(user_id);