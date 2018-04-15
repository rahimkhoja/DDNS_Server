#!/bin/bash
# DDNS Cron Script
# By Rahim Khoja for DDNS Server

d=$(date +%Y-%m-%d)
dt=$(date '+%d/%m/%Y %H:%M:%S');

# Database Properties
user="dyn_cron_user"
dbname="dyn_server_db"
password="password"

# Update Accounts that have Forgot Status for more than 1 Day.
mysql -u $user -D $dbname -p$password -e "UPDATE dyn_server_db.dyn_users SET forgot=0 WHERE forgot=1 AND forgot_date <= (now() - INTERVAL 1 DAY);"

# Delete Accounts that have not been enabled within 1 Day.
mysql -u $user -D $dbname -p$password -e "DELETE FROM dyn_server_db.dyn_users WHERE ( enable_date IS null OR enable_date='0000-0-0' ) AND enabled=0 AND create_date <= (now() - INTERVAL 1 DAY);"

echo "DDNS Cron Script Ran On $dt " >> /var/ddns/log/ddns-$d.log

# SELECT CONCAT(dyn_hosts.hostname, '.', dyn_domains.domain_name) AS all_hosts FROM dyn_server_db.dyn_domains, dyn_server_db.dyn_hosts WHERE dyn_hosts.domain_id = dyn_domains.id;
