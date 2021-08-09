DROP TABLE IF EXISTS account;
  
CREATE TABLE account (
  id INT AUTO_INCREMENT  PRIMARY KEY,
  number INT NOT NULL,
  balance NUMERIC NOT NULL,
  current_transfer_operations INT NOT NULL,
  last_transfer_date date
);