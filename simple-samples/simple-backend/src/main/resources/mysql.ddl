DROP TABLE IF EXISTS CONTACT CASCADE;
DROP TABLE IF EXISTS PERSON CASCADE;
DROP TABLE IF EXISTS STATE CASCADE;

CREATE TABLE PERSON (
  ID BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  FIRST_NAME VARCHAR(100) NOT NULL,
  LAST_NAME VARCHAR(100) NOT NULL,
  DATE_OF_BIRTH DATE,
  GENDER ENUM('M','F','0') NOT NULL,
  SSN VARCHAR(100)
);

ALTER TABLE PERSON ADD COLUMN VERSION INT NOT NULL DEFAULT 0; 

CREATE  INDEX IX_PERSON_LAST_NAME ON PERSON (LAST_NAME);

CREATE TABLE STATE (
  ID BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  CODE CHAR(3) NOT NULL,
  NAME VARCHAR(100)
);

CREATE  INDEX IX_STATE_CODE ON STATE (CODE);

CREATE TABLE CONTACT (
  ID BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  PERSON_ID BIGINT NOT NULL,
  CTYPE ENUM('0','1','2'), 
  ADDRESS VARCHAR(100),
  PHONE_NUMBER VARCHAR(100),
  STATE_CODE CHAR(3) NOT NULL
);

ALTER TABLE CONTACT ADD COLUMN VERSION INT NOT NULL DEFAULT 0; 

ALTER TABLE CONTACT ADD CONSTRAINT FK_CONTACT_PERSON
	FOREIGN KEY (PERSON_ID) REFERENCES PERSON (ID) ON DELETE CASCADE;
	
ALTER TABLE CONTACT ADD CONSTRAINT FK_CONTACT_STATE
	FOREIGN KEY (STATE_CODE) REFERENCES STATE (CODE) ON DELETE CASCADE;
	
INSERT INTO STATE (ID,CODE,NAME) values (1,'CZE','Česká Republika');
INSERT INTO STATE (ID,CODE,NAME) values (2,'SVK','Slovenská Republika');
commit;