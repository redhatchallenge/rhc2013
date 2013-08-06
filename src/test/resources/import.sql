insert into test.contestant (contestant_id, email, password, contestantfn, contestantln, contact, country, countrycode, school, lecturerfn, lecturerln, lectureremail, language, verified, status) values (10, 'zx@zx.com', '$2a$10$aN0pMIb2pLDA8FACRCAkYeoA3deAYCCZq8WgQvRgY3b9bc8A/xV9K', 'zx', 'z', '95681265', 'Singapore', '65', 'TP','James', 'Bond', 'jbond@tp.edu.sg', 'English', FALSE, TRUE)
insert into test.reset_tokens (email, token) values ('zx@zx.com', 'asdf')
insert into test.confirm_tokens (email, token) values ('zx@zx.com', 'qwerty')
