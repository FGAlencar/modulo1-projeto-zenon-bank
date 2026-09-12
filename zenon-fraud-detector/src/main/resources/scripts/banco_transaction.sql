CREATE TABLE transactions (
  id                SERIAL PRIMARY KEY,
  step              INTEGER NOT NULL,
  type              VARCHAR(20) NOT NULL,
  amount            NUMERIC(20, 2) NOT NULL,
  name_orig         VARCHAR(20) NOT NULL,
  old_balance_orig   NUMERIC(20, 2) NOT NULL,
  new_balance_orig  NUMERIC(20, 2) NOT NULL,
  name_dest         VARCHAR(20) NOT NULL,
  old_balance_dest  NUMERIC(20, 2) NOT NULL,
  new_balance_dest  NUMERIC(20, 2) NOT NULL,
  is_fraud          BOOLEAN NOT NULL,
  is_flagged_fraud  BOOLEAN NOT NULL
);