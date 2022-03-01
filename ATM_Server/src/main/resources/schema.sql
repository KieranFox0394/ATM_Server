CREATE TABLE TBL_ACCOUNTS (
  accountid int  PRIMARY KEY,
  pin VARCHAR(4) NOT NULL,
  balance int NOT NULL,
  overdraft int );
  
  CREATE TABLE TBL_CASH (
  denomination	int PRIMARY KEY,
  quantity	int );