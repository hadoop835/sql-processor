DROP SEQUENCE SIMPLE_SEQUENCE IF EXISTS CASCADE;
DROP TABLE CONTACT IF EXISTS CASCADE;
DROP TABLE PERSON IF EXISTS CASCADE;
DROP PROCEDURE NEW_PERSON IF EXISTS CASCADE;
DROP PROCEDURE NEW_PERSON_RET IF EXISTS CASCADE;
DROP FUNCTION AN_HOUR_BEFORE IF EXISTS CASCADE;

CREATE SEQUENCE SIMPLE_SEQUENCE AS BIGINT START WITH 100 INCREMENT BY 1;

CREATE TABLE PERSON
(
  ID BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL
, FIRST_NAME VARCHAR(100) NOT NULL 
, LAST_NAME VARCHAR(100) NOT NULL 
, DATE_OF_BIRTH DATE
, SSN VARCHAR(100) 
);

ALTER TABLE PERSON ADD CONSTRAINT PK_PERSON
	PRIMARY KEY (ID)
;

CREATE  INDEX IX_PERSON_LAST_NAME ON PERSON (LAST_NAME);

CREATE TABLE CONTACT
(
  ID BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL
, PERSON_ID BIGINT NOT NULL 
, ADDRESS VARCHAR(100) NOT NULL 
, PHONE_NUMBER VARCHAR(100)
);

ALTER TABLE CONTACT ADD CONSTRAINT PK_CONTACT
	PRIMARY KEY (ID)
;

ALTER TABLE CONTACT ADD CONSTRAINT FK_CONTACT_PERSON
	FOREIGN KEY (PERSON_ID) REFERENCES PERSON (ID) ON DELETE CASCADE
;

CREATE PROCEDURE NEW_PERSON(OUT newid INT, IN date_of_birth DATE, IN ssn VARCHAR(20), IN first_name VARCHAR(100), IN last_name VARCHAR(100))
  MODIFIES SQL DATA
  BEGIN ATOMIC
    INSERT INTO PERSON VALUES (DEFAULT, first_name, last_name, date_of_birth, ssn);
    SET newid = IDENTITY();
  END
    

CREATE PROCEDURE NEW_PERSON_RET_RS(IN date_of_birth DATE, IN ssn VARCHAR(20), IN first_name VARCHAR(100), IN last_name VARCHAR(100))
  MODIFIES SQL DATA DYNAMIC RESULT SETS 1
  BEGIN ATOMIC
    DECLARE result CURSOR FOR SELECT * FROM PERSON WHERE ID = IDENTITY();
    INSERT INTO PERSON VALUES (DEFAULT, first_name, last_name, date_of_birth, ssn);
    OPEN result;
  END
    
CREATE FUNCTION AN_HOUR_BEFORE(t TIMESTAMP)
  RETURNS TIMESTAMP
  RETURN t - 1 HOUR
