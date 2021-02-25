#
# MySQL Dockerfile for test DB
#
# https://github.com/dockerfile/mysql
#

# Pull base image.
FROM mysql:8

# Set root pass
ENV MYSQL_ROOT_PASSWORD password

COPY ./sql-scripts-test/ /docker-entrypoint-initdb.d/

# Expose ports.
EXPOSE 3306
