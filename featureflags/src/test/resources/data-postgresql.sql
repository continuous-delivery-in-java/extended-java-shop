-- Some data for tests
DELETE FROM flag
INSERT INTO flag(flag_id, name, portion_in, sticky) VALUES (1, 'half-way-feature', 50, TRUE)
INSERT INTO flag(flag_id, name, portion_in, sticky) VALUES (2, 'disabled-feature', 0, FALSE)
INSERT INTO flag(flag_id, name, portion_in, sticky) VALUES (3, 'fully-enabled-feature', 100, TRUE)

