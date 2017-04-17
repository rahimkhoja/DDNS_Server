Dynamic DNS Server
==================

An open source dynamic dns server solution. Known to work with DDNS clients including ddclient, DD-WRT, Tomato, as well as most others. The Dynamic DNS server Leverages Bind with dlz support, Tomcat, and MariaDB to provide dynamic dns services and updates.   



#### Table of Contents
* [Features](#features)
* [Support](#support)
* [Update API](#pools-using-this-software)
* [Installation Requirements](#usage)
  * [Single Server](#requirements)
  * [Single Server with Two DNS Slaves](#1-downloading--installing)
* [Installation Instructions](#usage)
  * [Single Server](#3-optional-configure-cryptonote-easy-miner-for-your-pool)
    * [Install CentOS 7 Minimal](#4-start-the-pool)
    * [Compile & Install Bind with DLZ Support](#5-host-the-front-end)
    * [Install Tomcat](#6-customize-your-website)
    * [Setup Database](#4-start-the-pool)
    * [Finalizing System Configuration](#4-start-the-pool)
    * [Starting the DDNS Server](#4-start-the-pool)
    * [Logging in to Web Admin](#4-start-the-pool)
  * [Slave Server](#upgrading)
    * [Install CentOS 7 Minimal](#4-start-the-pool)
    * [Compile & Install Bind with DLZ Support](#5-host-the-front-end)
    * [Setup Slave Database](#4-start-the-pool)
    * [Finalizing Slave System Configuration](#4-start-the-pool)
* [Registra Nameserver Configuration](#json-rpc-commands-from-cli)
* [Monitoring Your Pool](#monitoring-your-pool)
* [Donations](#donations)
* [License](#license)



#### Features
-------------

* REST Based Update API based on the Dyndns2 protocol
* Compatible with most DDNS clients
* Fully functional web application for managment of hosts and user profile information
* Designed for multiple slave DNS Servers
* IPv4 DNS Record Updates
* Basic user signup, forgot, and login functionlaity
* Built on CentOS 7 with Bind 9.1x(compiled with DLZ support), Tomcat 9, and MaridbDB


#### Support
------------

Since I am extreemly lazy I am not going to offer support. Well maybe every once n a while. This project is an Eclipse workspace folder, so it should be pretty easy for someone to import it into Eclipse and make fixes or changes to the software.

Sites running this software:

http://www.hiive.biz


#### Donations
--------------

Many Bothans died getting this software to you. Honor them by sending me some BTC or XMR.

 * BTC: 1K4N5msYZHse6Hbxz4oWUjwqPf8wu6ducV
 * XMR: 42VxjBpfi4TS6KFjNrrKo3QLcyK7gBGfM9w7DxmGRcocYnEbJ1hhZWXfaHJtCXBxnL74DpkioPSivjRYU8qkt59s3EaHUU3


#### License
------------

Released under the GNU General Public License v2

 * http://www.gnu.org/licenses/gpl-2.0.html
