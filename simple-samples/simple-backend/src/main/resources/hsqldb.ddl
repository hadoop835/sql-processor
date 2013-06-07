DROP SEQUENCE SIMPLE_SEQUENCE IF EXISTS CASCADE;
DROP TABLE CONTACT IF EXISTS CASCADE;
DROP TABLE PERSON IF EXISTS CASCADE;
DROP TABLE STATE IF EXISTS CASCADE;

CREATE SEQUENCE SIMPLE_SEQUENCE AS BIGINT START WITH 100 INCREMENT BY 1;

CREATE TABLE PERSON
(
  ID BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL
, FIRST_NAME VARCHAR(100) NOT NULL 
, LAST_NAME VARCHAR(100) NOT NULL 
, DATE_OF_BIRTH DATE
, GENDER VARCHAR(1) NOT NULL
, SSN VARCHAR(100)
, VERSION INT DEFAULT 0 NOT NULL
);

ALTER TABLE PERSON ADD CONSTRAINT PK_PERSON
	PRIMARY KEY (ID)
;

ALTER TABLE PERSON ADD CONSTRAINT CHECK_PERSON_GENDER
    CHECK (GENDER IN ('M', 'F', '0'))
;

CREATE  INDEX IX_PERSON_LAST_NAME ON PERSON (LAST_NAME);

CREATE TABLE STATE 
(
  ID BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL
, CODE CHAR(3) NOT NULL
, NAME VARCHAR(100)
);

ALTER TABLE STATE ADD CONSTRAINT PK_STATE
	PRIMARY KEY (ID)
;

CREATE TABLE CONTACT
(
  ID BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL
, PERSON_ID BIGINT NOT NULL 
, CTYPE VARCHAR(1) NOT NULL 
, ADDRESS VARCHAR(100) NOT NULL 
, PHONE_NUMBER VARCHAR(100)
, VERSION INT DEFAULT 0 NOT NULL
, STATE_CODE CHAR(3) NOT NULL
);

ALTER TABLE CONTACT ADD CONSTRAINT PK_CONTACT
	PRIMARY KEY (ID)
;

ALTER TABLE CONTACT ADD CONSTRAINT FK_CONTACT_PERSON
	FOREIGN KEY (PERSON_ID) REFERENCES PERSON (ID) ON DELETE CASCADE
;

ALTER TABLE CONTACT ADD CONSTRAINT CHECK_CONTACT_TYPE
    CHECK (CTYPE IN ('0', '1', '2'))
;

ALTER TABLE STATE ADD CONSTRAINT FK_CONTACT_CODE
	UNIQUE (CODE)
;

ALTER TABLE CONTACT ADD CONSTRAINT FK_CONTACT_STATE
	FOREIGN KEY (STATE_CODE) REFERENCES STATE (CODE) ON DELETE CASCADE
;

INSERT INTO STATE (ID,CODE,NAME) values (1,'CZE','Česká Republika');
INSERT INTO STATE (ID,CODE,NAME) values (2,'SVK','Slovenská Republika');
commit;