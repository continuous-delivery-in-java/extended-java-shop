-- Some data for tests
DELETE FROM flag
INSERT INTO flag(flag_id, name, portion_in) VALUES (1, 'half-way-feature', 50)
INSERT INTO flag(flag_id, name, portion_in) VALUES (2, 'disabled-feature', 0)
INSERT INTO flag(flag_id, name, portion_in) VALUES (3, 'fully-enabled-feature', 100)

