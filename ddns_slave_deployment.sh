#!/bin/sh
# DDNS Slave DNS Server Deployment
# for CentOS 7 
# By Rahim Khoja (rahimk@khojacorp.com)

# Default Properties
defaultslave=2
defaultmasterhost=172.16.254.40
defaultmasterpass=password
defaultmysqlpass=password
defaultnamedpass=password

# Get Slave Server Number
finish="-1"
while [ "$finish" = '-1' ]
  do
    finish="1"
    read -p "Please Enter Slave Server Number [$defaultslave]: " SLAVENUM
    SLAVENUM=${SLAVENUM:-$defaultslave}
    echo
    read -p "Slave Number is: $SLAVENUM [y/n]? " answer

    if [ "$answer" = '' ];
    then
      answer=""
    else
      case $answer in
        y | Y | yes | YES ) answer="y";;
        n | N | no | NO ) answer="n"; finish="-1";;
        *) finish="-1";
           echo -n 'Invalid Response\n';
       esac
    fi
done
echo

# Get Master Server Host
finish="-1"
while [ "$finish" = '-1' ]
  do
    finish="1"
    read -p "Please Enter Master MySQL Server IP or Hostname [$defaultmasterhost]: " MASTERHOST
    MASTERHOST=${MASTERHOST:-$defaultmasterhost}
    echo
    read -p "Master Server Host is: $MASTERHOST [y/n]? " answer

    if [ "$answer" = '' ];
    then
      answer=""
    else
      case $answer in
        y | Y | yes | YES ) answer="y";;
        n | N | no | NO ) answer="n"; finish="-1";;
        *) finish="-1";
           echo -n 'Invalid Response\n';
       esac
    fi
done
echo

# Get Master MySQL Replication Server Password
finish="-1"
while [ "$finish" = '-1' ]
  do
    finish="1"
    read -p "Please Enter Master MySQL Server Replication Password [$defaultmasterpass]: " MASTERPASS
    MASTERPASS=${MASTERPASS:-$defaultmasterpass}
    echo
    read -p "Master Server MySQL Replication Password is: $MASTERPASS [y/n]? " answer

    if [ "$answer" = '' ];
    then
      answer=""
    else
      case $answer in
        y | Y | yes | YES ) answer="y";;
        n | N | no | NO ) answer="n"; finish="-1";;
        *) finish="-1";
           echo -n 'Invalid Response\n';
       esac
    fi
done
echo


# CentOS 7 System Update & Dependency Installation
yum -y update && yum -y upgrade
yum -y groupinstall 'Development Tools'
yum -y install mariadb mariadb-server mariadb-devel mariadb-embedded curl wget git openssl policycoreutils-python

# Clone DDNS Repository
cd /tmp
git clone https://github.com/CanadianRepublican/DDNS_Server.git DDNS

# Bind with DLZ Support Installation
rm -rf /usr/src/bind-9.11.0-P3
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

# Setup Conf Files for Bind, Mysql, and Systemd
cp /tmp/DDNS/utils/usr/lib/systemd/system/named.service /usr/lib/systemd/system/named.service
cp /tmp/DDNS/utils/etc/named.conf /etc/named.conf
cp /tmp/DDNS/utils/etc/my.cnf.slave my.cnf

# Database Setup
systemctl enable mariadb
systemctl start mariadb
mysql_secure_installation <<EOF

y
password
password
y
y
y
y
EOF

mysql -u root -ppassword -e "create database dyn_server_db;"
mysql -u root -ppassword dyn_server_db < /tmp/DDNS/dyn_server_db.sql

# Setup Slave Databases
mysql -u root -ppassword -e "CHANGE MASTER TO MASTER_HOST='172.16.254.40', MASTER_USER='dyn_replicator', MASTER_PASSWORD='password'; GRANT SELECT,LOCK TABLES on dyn_server_db.* to 'dyn_dns_user'@'localhost' identified by 'password'; flush privileges;"

# Firewall Configuration
sudo systemctl enable firewalld
sudo systemctl start firewalld
sudo firewall-cmd --zone=public --permanent --add-port=53/udp
sudo firewall-cmd --zone=public --permanent --add-port=53/tcp
sudo firewall-cmd --reload

# Start Named with DLZ Support
systemctl daemon-reload
systemctl enable named
systemctl start named
