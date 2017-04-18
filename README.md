Dynamic DNS Server
==================

An open source dynamic dns server solution. Known to work with DDNS clients including ddclient, DD-WRT, Tomato, as well as most others. The Dynamic DNS server Leverages Bind with dlz support, Tomcat, and MariaDB to provide dynamic dns services and updates.   



### Table of Contents
* [Features](#features)
* [Support](#support)
* [Update API](#update-api)
  * [API Parameters](#api-parameters)
  * [API Responses](#api-responses)
  * [API Examples](#api-examples)
* [Installation Requirements](#requirements)
  * [Single Server](#requirements-single)
  * [Server with Two or more DNS Slaves](#requirements-mutiple)
* [Installation Instructions](#install)
  * [Single Server](#basic-install)
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
* [Donations](#donations)
* [License](#license)



### Features
-------------

* REST Based Update API based on the Dyndns2 protocol
* Compatible with most DDNS clients
* Fully functional web application for managment of hosts and user profile information
* Designed for multiple slave DNS Servers
* IPv4 DNS Record Updates
* Basic user signup, forgot, and login functionlaity
* Built on CentOS 7 with Bind 9.1x(compiled with DLZ support), Tomcat 9, and MaridbDB


### Support
------------

Since I am extreemly lazy I am not going to offer support. Well maybe every once n a while. This project is an Eclipse workspace folder, so it should be pretty easy for someone to import it into Eclipse and make fixes or changes to the software.

Sites running this software:

http://www.hiive.biz


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



### Donations
-------------

Many Bothans died getting this software to you. Honor them by sending me some BTC or XMR.

 * BTC: 1K4N5msYZHse6Hbxz4oWUjwqPf8wu6ducV
 * XMR: 42VxjBpfi4TS6KFjNrrKo3QLcyK7gBGfM9w7DxmGRcocYnEbJ1hhZWXfaHJtCXBxnL74DpkioPSivjRYU8qkt59s3EaHUU3


### License
-----------

Released under the GNU General Public License v2

 * http://www.gnu.org/licenses/gpl-2.0.html
