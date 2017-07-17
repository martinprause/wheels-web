INSERT INTO users (id, username, password, role) VALUES ('0', 'admin', '$2a$06$Oux0YaB2vq95FLcok1W8fuvtZRsqoJ6gQ8DFDo69D6SZ0eewYBXrG', 'ADMIN');
INSERT INTO access_level (id, access_level) VALUES (1, 'DeleteOrder'), (2, 'CreateOrder'), (3, 'CreateUser'), (4, 'DeleteUser'), (5, 'Reports');
INSERT INTO country (id, description) VALUES (1, 'Germany');
INSERT INTO guideline (id, description) VALUES (1, 'Polishing');
INSERT INTO manufacturer (id, description) VALUES (1, 'Manufacturer');
INSERT INTO model (id, description) VALUES (1, 'Model S');
INSERT INTO model_type (id, description) VALUES (1, 'Model Type X');
INSERT INTO valve_type (id, description) VALUES (1, 'Valve Type 3');