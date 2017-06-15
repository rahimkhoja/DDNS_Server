
# System Update
yum -y update && yum -y upgrade
yum -y groupinstall 'Development Tools'
yum -y install mariadb mariadb-server mariadb-devel mariadb-embedded curl wget git openssl policycoreutils-python

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

sudo firewall-cmd --zone=public --permanent --add-port=80/tcp
sudo firewall-cmd --zone=public --permanent --add-port=80/udp
sudo firewall-cmd --zone=public --permanent --add-port=443/udp
sudo firewall-cmd --zone=public --permanent --add-port=443/tcp
sudo firewall-cmd --zone=public --permanent --add-port=8080/tcp
sudo firewall-cmd --zone=public --permanent --add-port=8080/udp

sudo firewall-cmd --zone=public --permanent --add-port=3306/tcp
sudo firewall-cmd --zone=public --permanent --add-port=3306/udp

sudo systemctl enable firewalld
sudo firewall-cmd --zone=public --permanent --add-port=53/udp
sudo firewall-cmd --zone=public --permanent --add-port=53/tcp
sudo firewall-cmd --reload
sudo firewall-cmd --zone=public --permanent --list-ports


// On Slave DNS
mysqldump -h 172.16.254.40 -u dyn_dns_user -p dyn_server_db xfr_table dyn_dns_records > dyn_server_db.sql
create database dyn_server_db;
mysql -u root -p dyn_server_db < dyn_server_db.sql
sudo systemctl restart mariadb 


Start Master my.cnf
bind-address = 0.0.0.0
server-id=1
log-bin
binlog-format=row
binlog-do-db=dyn_server_db

log-slave-updates
replicate-do-table=dyn_server_db.dyn_dns_records
replicate-do-table=dyn_server_db.xfr_table

end my.cnf


Start Slave my.cnf
server-id=3
binlog_do_db=dyn_server_db
replicate-do-table=dyn_server_db.dyn_dns_records
replicate-do-table=dyn_server_db.xfr_table
bind-address = 127.0.0.1
end my.cnf

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

systemctl enable firewalld
