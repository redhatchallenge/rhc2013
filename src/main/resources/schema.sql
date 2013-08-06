CREATE TABLE contestant (
    contestant_id serial primary key,
    email character varying(50) NOT NULL UNIQUE,
    password character varying(72),
    contestantfn character varying(30),
    contestantln character varying(30),
    contact character varying(15) UNIQUE,
    country character varying(15),
    countrycode character varying(4),
    school character varying(50),
    lecturerfn character varying(30),
    lecturerln character varying(30),
    lectureremail character varying(50),
    language character varying(40),
    verified boolean DEFAULT false NOT NULL,
    status boolean DEFAULT true NOT NULL,
    questions int[] DEFAULT '{}'
);

CREATE TABLE reset_tokens (
    email character varying(50) NOT NULL references contestant(email),
    token character varying(50) NOT NULL primary key
);

CREATE TABLE confirm_tokens (
    email character varying(50) NOT NULL references contestant(email),
    token character varying(50) NOT NULL primary key
);