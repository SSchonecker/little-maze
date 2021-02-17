#
# MySQL Dockerfile
#
# https://github.com/dockerfile/mysql
#

# Pull base image.
FROM mysql:8

# Set up MySQL DB.
RUN \
	mysql < dump_main.sql

# Define mountable directories.
VOLUME ["/etc/mysql", "/var/lib/mysql"]

# Define default command.
CMD ["mysqld_safe"]

# Expose ports.
EXPOSE 3306
