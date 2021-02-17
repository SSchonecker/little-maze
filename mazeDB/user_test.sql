CREATE USER IF NOT EXISTS 'mazewalker'@'%' IDENTIFIED BY 'runT0mrun!';
GRANT EXECUTE ON * TO 'mazewalker'@'%';
GRANT SELECT, INSERT, UPDATE, DELETE ON `user` TO 'mazewalker'@'%';
GRANT SELECT, INSERT ON `score` TO 'mazewalker'@'%';