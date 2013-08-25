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
