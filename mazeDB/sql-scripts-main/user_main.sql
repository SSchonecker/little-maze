CREATE USER IF NOT EXISTS 'mazewalker'@'%' IDENTIFIED BY 'runT0mrun!';
GRANT EXECUTE ON maze_safe.* TO 'mazewalker'@'%';
GRANT SELECT, INSERT, UPDATE ON maze_safe.`user` TO 'mazewalker'@'%';
GRANT SELECT, INSERT ON maze_safe.`score` TO 'mazewalker'@'%';