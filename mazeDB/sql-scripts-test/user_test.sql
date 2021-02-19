CREATE USER IF NOT EXISTS 'mazewalker'@'%' IDENTIFIED BY 'runT0mrun!';
GRANT EXECUTE ON test_maze.* TO 'mazewalker'@'%';
GRANT SELECT, INSERT, UPDATE, DELETE ON test_maze.`user` TO 'mazewalker'@'%';
GRANT SELECT, INSERT ON test_maze.`score` TO 'mazewalker'@'%';