DROP TABLE reset_tokens;
DROP TABLE confirm_tokens;
DROP TABLE contestant;

CREATE TABLE contestant (
    contestant_id serial primary key,
    email character varying(50) NOT NULL UNIQUE,
    password character varying(72) NOT NULL,
    contestantfn character varying(30) NOT NULL,
    contestantln character varying(30) NOT NULL,
    contact character varying(15) NOT NULL UNIQUE,
    country character varying(15) NOT NULL,
    countrycode character varying(4) NOT NULL,
    school character varying(50) NOT NULL,
    lecturerfn character varying(30),
    lecturerln character varying(30),
    lectureremail character varying(50),
    language character varying(40) NOT NULL,
    verified boolean DEFAULT false NOT NULL,
    status boolean DEFAULT true NOT NULL,
    questions int[] DEFAULT '{}',
    timeslot bigint DEFAULT 0,
    create_date timestamp DEFAULT current_timestamp,
    last_modified timestamp
);

CREATE TABLE timeslot_list (
    timeslot_id serial primary key,
    timeslot bigint  NOT NULL UNIQUE,
);

INSERT INTO timeslot_list(timeslot, timeslot_id) VALUES (1382490000000, 1);
INSERT INTO timeslot_list(timeslot, timeslot_id) VALUES (1382494500000, 2);
INSERT INTO timeslot_list(timeslot, timeslot_id) VALUES (1382499000000, 3);
INSERT INTO timeslot_list(timeslot, timeslot_id) VALUES (1382503500000, 4);
INSERT INTO timeslot_list(timeslot, timeslot_id) VALUES (1382508000000, 5);
INSERT INTO timeslot_list(timeslot, timeslot_id) VALUES (1382512500000, 6);
INSERT INTO timeslot_list(timeslot, timeslot_id) VALUES (1382517000000, 7);
INSERT INTO timeslot_list(timeslot, timeslot_id) VALUES (1382521500000, 8);
INSERT INTO timeslot_list(timeslot, timeslot_id) VALUES (1382526000000, 9);
INSERT INTO timeslot_list(timeslot, timeslot_id) VALUES (1382530500000, 10);
INSERT INTO timeslot_list(timeslot, timeslot_id) VALUES (1382594400000, 11);
INSERT INTO timeslot_list(timeslot, timeslot_id) VALUES (1382601600000, 12);

CREATE OR REPLACE FUNCTION update_last_modified_column()
  RETURNS TRIGGER AS $$
  BEGIN
    NEW.last_modified = current_timestamp;
    RETURN NEW;
  END;

  $$ LANGUAGE 'plpgsql';


CREATE TRIGGER update_last_modified BEFORE UPDATE
  ON contestant FOR EACH ROW EXECUTE PROCEDURE
  update_last_modified_column();


CREATE TABLE reset_tokens (
    email character varying(50) NOT NULL references contestant(email) ON DELETE CASCADE ON UPDATE CASCADE,
    token character varying(50) NOT NULL primary key
);

CREATE TABLE confirm_tokens (
    email character varying(50) NOT NULL references contestant(email) ON DELETE CASCADE ON UPDATE CASCADE,
    token character varying(50) NOT NULL primary key
);
