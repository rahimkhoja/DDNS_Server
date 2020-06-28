#!/bin/bash
# Dynamic DNS Server - Master Name Server Deployment
# Requires: CentOS 7.*
# Note: Incomplete
# By Rahim Khoja (rahim@khoja.ca)

echo
echo -e "\033[0;31m░░░░░░░░▀▀▀██████▄▄▄"
echo "░░░░░░▄▄▄▄▄░░█████████▄ "
echo "░░░░░▀▀▀▀█████▌░▀▐▄░▀▐█ "
echo "░░░▀▀█████▄▄░▀██████▄██ "
echo "░░░▀▄▄▄▄▄░░▀▀█▄▀█════█▀"
echo "░░░░░░░░▀▀▀▄░░▀▀███░▀░░░░░░▄▄"
echo "░░░░░▄███▀▀██▄████████▄░▄▀▀▀██▌"
echo "░░░██▀▄▄▄██▀▄███▀▀▀▀████░░░░░▀█▄"
echo "▄▀▀▀▄██▄▀▀▌█████████████░░░░▌▄▄▀"
echo "▌░░░░▐▀████▐███████████▌"
echo "▀▄░░▄▀░░░▀▀██████████▀"
echo "░░▀▀░░░░░░▀▀█████████▀"
echo "░░░░░░░░▄▄██▀██████▀█"
echo "░░░░░░▄██▀░░░░░▀▀▀░░█"
echo "░░░░░▄█░░░░░░░░░░░░░▐▌"
echo "░▄▄▄▄█▌░░░░░░░░░░░░░░▀█▄▄▄▄▀▀▄"
echo -e "▌░░░░░▐░░░░░░░░░░░░░░░░▀▀▄▄▄▀\033[0m"
echo "---Dynamic DNS Server - Master Name Server Deployment Script---"
echo "---By: Rahim Khoja (rahim@khoja.ca)---"
echo
echo " Please note, this script has only been tested with users who are under 5 foot 8 inches in height!"
echo

# Stop on Error
set -eE  # same as: `set -o errexit -o errtrace`

# Dump Vars Function
function dump_vars {
    if ! ${STATUS+false};then echo "STATUS = ${STATUS}";fi
    if ! ${LOGFILE+false};then echo "LOGFILE = ${LOGFILE}";fi
    if ! ${SCRIPTDIR+false};then echo "SCRIPTDIR = ${SCRIPTDIR}";fi
    if ! ${DEBUG+false};then echo "DEBUG = ${DEBUG}";fi
    if ! ${PUBLICIP+false};then echo "PUBLICIP = ${PUBLICIP}";fi
    if ! ${TIMESTAMP+false};then echo "TIMESTAMP = ${TIMESTAMP}";fi
    if ! ${finish+false};then echo "finish = ${finish}";fi
    if ! ${ddnsserver+false};then echo "ddnsserver = ${ddnsserver}";fi
    if ! ${ddnsmaster+false};then echo "ddnsmaster = ${ddnsmaster}";fi
    if ! ${MASTERIP+false};then echo "MASTERIP = ${MASTERIP}";fi
    if ! ${MYSQLPASSWORD+false};then echo "MYSQLPASSWORD = ${MYSQLPASSWORD}";fi
    if ! ${SLAVEID+false};then echo "SLAVEID = ${SLAVEID}";fi
}

# Failure Function
function failure() {
    local lineno=$1
    local msg=$2
    echo ""
    echo -e "\033[0;31mError at Line Number $lineno: '$msg'\033[0m"
    echo ""
    if [[ $DEBUG -eq 1 ]]; then
      dump_vars
    fi
}

# Failure Function Trap
trap 'failure ${LINENO} "$BASH_COMMAND"' ERR

# Default Variriable Declaration
LOGFILE=/var/log/DDNS-Server-Setup.log
SCRIPTDIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" >/dev/null 2>&1 && pwd )"
DEBUG=1
STATUS="Initializing"
TIMESTAMP="$( date +%s )"
#PUBLICIP="$(dig @resolver1.opendns.com ANY myip.opendns.com +short)"
finish="-1"
ddnsserver="0"
ddnsmaster="0"
middleman="0"
backupconfig="0"
getpagespeed="0"

# Check the bash shell script is being run by root
STATUS="Check - Script Run as Root user"
if [[ $EUID -ne 0 ]];
then
    echo "This script must be run as root" 1>&2
    exit 1
fi

STATUS="Starting Installation"
echo "$(date \"+%FT%T\") $STATUS" >> "${LOGFILE}"

# Prompt to Confirm Installation of DDNS Server
STATUS="Prompt - Do you Want To Install DDNS Server (Master or Slave)"
finish="-1"
while [ "$finish" = '-1' ]
do
    finish="1"
    echo
    read -p "Do you want to set this Install DDNS Server [y/n]? " answer

    if [ "$answer" = '' ];
    then
        answer=""
    else
        case $answer in
            y | Y | yes | YES ) answer="y"; ddnsserver="1";;
            n | N | no | NO ) answer="n"; ddnsserver="0"; exit 1;;
            *) finish="-1";
                echo -n 'Invalid Response\n';
        esac
    fi
done
echo "$(date \"+%FT%T\") $STATUS" >> "${LOGFILE}"

# Prompt to Confirm Setup of DDNS Master or Slave
STATUS="Prompt - Is This Master Server"
while [ "$finish" = '-1' ]
do
    finish="1"
    echo
    read -p "Do you want to set this system up as a DDNS Master Server [y/n]? " answer

    if [ "$answer" = '' ];
    then
        answer=""
    else
        case $answer in
            y | Y | yes | YES ) answer="y"; ddnsmaster="1";;
            n | N | no | NO ) answer="n"; ddnsmaster="0";;
            *) finish="-1";
                echo -n 'Invalid Response\n';
        esac
    fi
done
echo "$(date \"+%FT%T\") $STATUS" >> "${LOGFILE}"



# If Slave
   # Propt Master IP
   # Prompt Master MySQL Server Password
   # Prompt Slave ID




# Prompt - Slave Server Prompts.
STATUS="Prompts - Slave Server Inputs"
echo "$(date \"+%FT%T\") $STATUS" >> "${LOGFILE}"
if [[ "$ddnsmaster" == "0" ]]; then
    # Prompt - Master Server IP?
    STATUS="Prompt - Slave Prompt - What is Master Server IP?"
    finish="-1"
    while [ "$finish" = '-1' ]
    do
        finish="1"
        echo
        read -p "Please enter DDNS Master Server IP Address?: " MASTERIP
        MASTERIP=${MASTERIP:-""}
        echo
        read -p "DDNS Server Master IP: $MASTERIP [y/n]? " answer

        if [ "$answer" = '' ];
        then
            answer=""
        else
            case $answer in
                y | Y | yes | YES ) answer="y";;
                n | N | no | NO ) answer="n"; finish="-1"; ;
                *) finish="-1";
                echo -n 'Invalid Response\n';
            esac
        fi
    done
    echo "$(date \"+%FT%T\") $STATUS" >> "${LOGFILE}"

    # Prompt - Master Server MySQL Password?
    STATUS="Prompt - Slave Prompt - What is Master Server MySQL Password?"
    finish="-1"
    while [ "$finish" = '-1' ]
    do
        finish="1"
        echo
        read -p "Please enter DDNS Master Server IP Address?: " MYSQLPASSWORD
        MYSQLPASSWORD=${MYSQLPASSWORD:-""}
        echo
        read -p "DDNS Server Master MySQL Password: $MYSQLPASSWORD [y/n]? " answer

        if [ "$answer" = '' ];
        then
            answer=""
        else
            case $answer in
                y | Y | yes | YES ) answer="y";;
                n | N | no | NO ) answer="n"; finish="-1"; ;
                *) finish="-1";
                echo -n 'Invalid Response\n';
            esac
        fi
    done    
    echo "$(date \"+%FT%T\") $STATUS" >> "${LOGFILE}"
    
    # Prompt - Slave Server Number?
    STATUS="Prompt - Slave Prompt - What is the Slave Server ID Number?"
    finish="-1"
    while [ "$finish" = '-1' ]
    do
        finish="1"
        echo
        read -p "Please enter DDNS Slave ID Number that is being Setup?: " SLAVEID
        SLAVEID=${SLAVEID:-""}
        echo
        read -p "DDNS Server Master MySQL Password: $SLAVEID [y/n]? " answer

        if [ "$answer" = '' ];
        then
            answer=""
        else
            case $answer in
                y | Y | yes | YES ) answer="y";;
                n | N | no | NO ) answer="n"; finish="-1"; ;
                *) finish="-1";
                echo -n 'Invalid Response\n';
            esac
        fi
    done    
    echo "$(date \"+%FT%T\") $STATUS" >> "${LOGFILE}"
fi












# Prompt to DDNS Slave ID Numberup of DDNS Master or Slave
STATUS="Prompt - Is This Master Server"
while [ "$finish" = '-1' ]
do
    finish="1"
    echo
    read -p "Do you want to set this system up as a DDNS Master Server [y/n]? " answer

    if [ "$answer" = '' ];
    then
        answer=""
    else
        case $answer in
            y | Y | yes | YES ) answer="y"; ddnsmaster="1";;
            n | N | no | NO ) answer="n"; ddnsmaster="0";;
            *) finish="-1";
                echo -n 'Invalid Response\n';
        esac
    fi
done
echo "$(date \"+%FT%T\") $STATUS" >> "${LOGFILE}"










# Install Required Packages & Update System
STATUS="Install Yum Packages"
yum -y groupinstall 'Development Tools'
yum -y install mariadb mariadb-server mariadb-devel mariadb-embedded curl wget git openssl policycoreutils-python
STATUS="Update & Upgrade System"
yum -y update && yum -y upgrade


# Check if Varnish is installed.
STATUS="Check - Is Varnish installed"
if [[ -d "/etc/varnish" ]]; then
    varnish="1" 
fi

# Check if NGINX is installed.
STATUS="Check - Is Nginx installed"
if [[ -d "/etc/nginx" ]]; then
    nginx="1" 
fi






















# System Update



# Clone DDNS Repository
cd /tmp
git clone https://github.com/CanadianRepublican/DDNS_Server.git DDNS

# Bind User & Directory Creation
groupadd named
useradd -d /var/named -g named -s /bin/false named
mkdir -p /var/named/
chown -R named:named /var/named
chmod -R 755 /var/named
mkdir /var/log/named
chown named:named /var/log/named
mkdir /var/run/named
chown named:named /var/run/named
chown named:named /usr/sbin/named

# Master Database Setup


# Bind Installation
curl -o /tmp/bind-9.11.0-P3.tar.gz https://www.isc.org/downloads/file/bind-9-11-0-p3/
cd /tmp
tar -xvzf bind-9.11.0-P3.tar.gz
mv /tmp/bind-9.11.0-P3 /usr/src/
 cd /usr/src/bind-9.11.0-P3/
./configure --prefix=/usr --sysconfdir=/etc/bind --localstatedir=/var --mandir=/usr/share/man --infodir=/usr/share/info --enable-threads --enable-largefile --with-libtool --enable-shared --enable-static --with-openssl=/usr --with-gssapi=/usr --with-gnu-ld --with-dlz-postgres=no --with-dlz-mysql=yes --with-dlz-bdb=no --with-dlz-filesystem=yes --with-dlz-ldap=no --with-dlz-stub=yes --enable-ipv6 LDFLAGS="-I/usr/include/mysql -L/usr/lib64/mysql"
make
make install
ldconfig -v

semanage permissive -a named_t
setsebool -P named_write_master_zones=1

groupadd ddns
useradd -d /var/ddns -g ddns -s /bin/false ddns
mkdir -p /var/ddns/
mkdir /var/ddns/log
chown -R ddns:ddns /var/ddns
chmod -R 755 /var/ddns

crontab -l -u ddns | echo '*/5 * * * * /var/ddns/ddns-cron.sh' | crontab -u ddns -


vi /usr/lib/systemd/system/named.service

Copy Below Here 
[Unit]
Description=Berkeley Internet Name Domain (DNS) with DLZ
After=network.target

[Service]
Type=simple
Restart=always
RestartSec=60
ExecStart=/usr/sbin/named -u named -c /etc/named.conf -f -d 9

[Install]
WantedBy=multi-user.target

Copy Above Here

systemctl daemon-reload

# Database Setup
systemctl enable mariadb
systemctl start mariadb
mysql_secure_installation

mysql -u root -p -e "create database dyn_server_db;"
mysql -u root -p dyn_server_db < /tmp/DDNS/dyn_server_db.sql

# Setup Slave Databases
mysql -u root -p -e "CHANGE MASTER TO MASTER_HOST='172.16.254.40', MASTER_USER='dyn_replicator', MASTER_PASSWORD='replicator_password'; GRANT SELECT,LOCK TABLES on dyn_server_db.* to 'dyn_dns_user'@'localhost' identified by 'password'; flush privileges;"


# Setup Master Database 
# Create Bind w DLZ Database User (change IP to private network range & update password)
# This password relates to the mysql settings within /etc/named.conf
mysql -u root -p -e "GRANT SELECT,LOCK TABLES on dyn_server_db.* to 'dyn_dns_user'@'172.16.254.%' identified by 'password'; GRANT SELECT,LOCK TABLES on dyn_server_db.* to 'dyn_dns_user'@'localhost' identified by 'password';"

# Create Tomcat User (update password)
# This password relates to the mysql settings within Tomcat context file /opt/tomcat/webapps/ddns/WEB-INF/web.xml

mysql -u root -p -e "GRANT SELECT on dyn_server_db.dyn_user_roles to 'dyn_tomcat_user'@'localhost' identified by 'password'; GRANT SELECT on dyn_server_db.dyn_users to 'dyn_tomcat_user'@'localhost' identified by 'password'; GRANT SELECT on dyn_server_db.dyn_user_roles to 'dyn_tomcat_user'@'172.16.254.%' identified by 'password'; GRANT SELECT on dyn_server_db.dyn_users to 'dyn_tomcat_user'@'172.16.254.%' identified by 'password';"

GRANT SELECT,UPDATE,DELETE on dyn_server_db.dyn_users to 'dyn_cron_user'@'localhost' identified by 'password';

flush privileges;


service firewalld stop

// On Slave DNS
mysqldump -h 172.16.254.40 -u dyn_dns_user -p dyn_server_db xfr_table dyn_dns_records > dyn_server_db.sql
create database dyn_server_db;
mysql -u root -p dyn_server_db < dyn_server_db.sql
sudo systemctl restart mariadb 


cat <<EOF >>/etc/my.cnf.master
bind-address=0.0.0.0
server-id=1
log-bin
binlog-format=row
binlog-do-db=dyn_server_db

log-slave-updates
replicate-do-table=dyn_server_db.dyn_dns_records
replicate-do-table=dyn_server_db.xfr_table
EOF



slave-number=3
cat <<EOF >>/etc/my.cnf.slave-${slave-number}
server-id=${slave-number}
binlog_do_db=dyn_server_db
replicate-do-table=dyn_server_db.dyn_dns_records
replicate-do-table=dyn_server_db.xfr_table
bind-address=127.0.0.1
EOF

GRANT REPLICATION SLAVE ON *.* TO 'dyn_replicator'@'172.16.%.%' IDENTIFIED BY 'Password!';
grant all on dyn_server_db.* to 'dyn_web_app'@'172.16.254.%' identified by 'Password!';
grant all on dyn_server_db.* to 'dyn_web_app'@'localhost' identified by 'Password';



// Tomcat 9 Install
yum -y install java-1.8.0-openjdk.x86_64 java-1.8.0-openjdk-devel.x86_64
groupadd tomcat 
useradd -s /bin/false -g tomcat -d /opt/tomcat tomcat
cd /tmp
wget http://apache.mirror.globo.tech/tomcat/tomcat-9/v9.0.0.M21/bin/apache-tomcat-9.0.0.M21.tar.gz
tar -xzvf apache-tomcat-9.0.0.M21.tar.gz
mv apache-tomcat-9.0.0.M21 /opt/tomcat
cd /opt
chown -hR tomcat:tomcat tomcat


# Setup FirewallD
firewall-cmd --zone=public --permanent --add-service=http
firewall-cmd --zone=public --permanent --add-service=https
firewall-cmd --zone=public --permanent --add-service=mysql
firewall-cmd --zone=public --permanent --add-service=dns
firewall-cmd --zone=public --permanent --add-port=8080/tcp
firewall-cmd --zone=public --permanent --add-port=8080/udp
firewall-cmd --reload
firewall-cmd --zone=public --permanent --list-ports
systemctl enable firewalld
service firewalld start
