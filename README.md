Dynamic DNS Server  - Please note the Readme is a Work in Progress
==================

An open source dns server solution with web management application. Offering Dynamic DNS functaionalty that is known to work with DDNS clients including ddclient, DD-WRT, Tomato, as well as most others. The DNS server leverages Bind with dlz support, Tomcat, MariaDB(or MySQL), and NGINX, to a provide web managed, highly avaialbe, dns server.   





### Table of Contents
---------------------

* [Features](#features)
* [Requirements](#requirements)
* [Support](#support)
* [Update API](#update-api)
  * [API Parameters](#api-parameters)
  * [API Responses](#api-responses)
  * [API Examples](#api-examples)
   * [Server with Two or more DNS Slaves](#requirements-mutiple)
* [Installation Instructions](#install)
  * [Master Server](#basic-install)
    * [Install CentOS 7 Minimal](#4-start-the-pool)
    * [Compile & Install Bind with DLZ Support](#5-host-the-front-end)
    * [Install Tomcat](#6-customize-your-website)
    * [Setup Database](#4-start-the-pool)
    * [Finalizing System Configuration](#4-start-the-pool)
    * [Starting the DDNS Server](#4-start-the-pool)
    * [Logging in to Web Admin](#4-start-the-pool)
  * [Slave Server](#slave-install)
    * [Install CentOS 7 Minimal](#4-start-the-pool)
    * [Compile & Install Bind with DLZ Support](#5-host-the-front-end)
    * [Setup Slave Database](#4-start-the-pool)
    * [Finalizing Slave System Configuration](#4-start-the-pool)
* [Registra Nameserver Configuration](#registra)
* [Useful Links](#useful-links)
* [Donations](#donations)
* [License](#license)



### Features
------------

* REST Based Update API based on the Dyndns2 protocol.
* Compatible with most DDNS clients.
* Fully functional web application for managment of hosts and user profile information.
* Designed for multiple slave DNS Servers.
* IPv4 DNS Record Updates.
* Basic user signup, forgot, and login functionlaity.
* Built on CentOS 7 with Bind 9.1x(compiled with DLZ support), Tomcat 9, and MaridbDB.
* Comming Soon - Many new DNS record types will be added. Ultimatly this will become a fully managed high availablity DNS server.
* Comming Soon - Ansible Deployment Scripts for Master and Slaves.
* Comming Soon - NGINX Slave Load Balanceing. 
* Comming Soon - New User Authentication Methods. (FreeIPA?, LDAP?)



### Requirements
-------------

* At least 3 CentOS 7.* Virtual Machines or Servers. 
* SSH Access to all DDNS Servers. 
* If publically accessable, Port 53 (UDP) must be open from each slave DNS server to (and from) the internet.





### Support
-----------

Since I am extremely lazy I am not going to offer any support. Well maybe every once-n-a while. It really depends on my mood. 

That being said, time was spent documenting each code section in the DNS Server. This should allow the scripts to be easily understood and modified if needed. 



### Update API
------------

The update API was designed to mimic the dyndns2 protocol in order to make it compatible most DDNS clients. Once the Dynamic DNS Server is installed and running the API is avaialble. To access the API you will need credentials from the web application in order to use the API's Http Basic Authentication. 

#### API Stucture:

http://{username}:{password}@{yourdomain.tld or ip address}/ddns/update?&hostname={hostname}&myip={IP Address}


  #### API Parameters:

  > **username** [REQUIRED]
  > Your Hiive.biz account username.


  > **password** [REQUIRED]
  > Your Hiive.biz account password.


  > **hostname** [REQUIRED]
  > Comma separated list of your Hiive.biz hostnames to update (up to 10 hostnames per request).


  > **myip** [Optional]
  > The IP v4 Address that your Hiive.biz hostname(s) will be updated to. If no IP Address is specified the Hiive.biz system will attempt to obtain the IP Address.



#### API Responses:


**good**

IP Address for specified Hosts have been updated.

**nochg**

There is no update or change required. (No need to update more than once an hour without an IP change)

**nohost**

One or more Hosts are invalid.

**numhost**

More than the maximum of ten Hosts specified.

**badaddress**

IP Address form is invalid.


#### API Examples:



### Included Files
------------------

* Bitcoin Cash daemon deployment Bash script for CentOS 7.
* Bitcoin Cash daemon systemd service file. 
* Bitcoin Cash logrotate configuration file.
* Bitcoin Cash configuration file.



### Quick Deployment Instrcutions (Work in Progress)
-----------

For quick deployment please ensure:

* Root access to an updated CentOS 7 server. 


#### As Root Type
```bash
yum install -y git
cd ~
git clone https://github.com/CanadianRepublican/BitcoinCash-Daemon-Deployment-CentOS7.git BCH-Deploy
cd BCH-Deploy
bash deploy-bch-daemon.sh

```




### Useful Links
-----------------

*Bitcoin Cash GitHub:*

https://github.com/Bitcoin-ABC/bitcoin-abc


*Bitcoin Core Conf File Example:*

https://github.com/bitcoin/bitcoin/blob/master/contrib/debian/examples/bitcoin.conf



### Donations
-------------

Many Bothans died getting this DNS server to you, honor them by sending me some Bitcoin(BTC), or Ethereum(ETH).

 * BTC: 1K4N5msYZHse6Hbxz4oWUjwqPf8wu6ducV
 * ETH: 0x76AB557F159a5048fA944566dbb18C834228d4e7




### License
-----------

Released under the GNU General Public License v3. (Not sure this is even valid)

 * http://www.gnu.org/licenses/gpl-3.0.html

