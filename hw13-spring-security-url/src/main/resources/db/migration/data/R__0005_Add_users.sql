INSERT INTO users(username, password, role, last_login, active)
VALUES ('user', '$2a$10$2QijsT.ISCurZV5vP8oHk.6L9Rg2VprJfF0cfM.J.cRfPILsVO5ZG', 'ROLE_USER', CURRENT_TIMESTAMP, TRUE),
('admin', '$2a$10$2QijsT.ISCurZV5vP8oHk.6L9Rg2VprJfF0cfM.J.cRfPILsVO5ZG', 'ROLE_ADMIN', CURRENT_TIMESTAMP, TRUE),
('guest', '$2a$10$2QijsT.ISCurZV5vP8oHk.6L9Rg2VprJfF0cfM.J.cRfPILsVO5ZG', 'ROLE_GUEST', CURRENT_TIMESTAMP, TRUE);
