#
# MySQL Dockerfile
#
# https://github.com/dockerfile/mysql
#

# Pull base image.
FROM mysql:8

# Set root pass
ENV MYSQL_ROOT_PASSWORD password

COPY . .

# Set up MySQL DB.
RUN mysqld start && mysql -u root -p password < dumb_test.sql && mysql -u root -p password < user_test.sql

# Define mountable directories.
# VOLUME ["/etc/mysql", "/var/lib/mysql"]

# Define default command.
CMD ["mysql_safe"]

# Expose ports.
EXPOSE 3306
