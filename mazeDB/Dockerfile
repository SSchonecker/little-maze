#
# MySQL Dockerfile for main DB
#
# https://github.com/dockerfile/mysql
#

# Pull base image.
FROM mysql:8

# Set root pass
ENV MYSQL_ROOT_PASSWORD password

COPY ./sql-scripts-main/ /docker-entrypoint-initdb.d/

# Expose ports.
EXPOSE 3306
