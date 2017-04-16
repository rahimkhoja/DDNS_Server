Dynamic DNS Server
==================

An open source dynamic dns server solution. Known to work with DDNS clients including ddclient, DD-WRT, Tomato, as well as most others. The Dynamic DNS server Leverages Bind with dlz support, Tomcat, and MariaDB to provide dynamic dns services and updates.   



#### Table of Contents
* [Features](#features)
* [Community Support](#community--support)
* [Pools Using This Software](#pools-using-this-software)
* [Usage](#usage)
  * [Requirements](#requirements)
  * [Downloading & Installing](#1-downloading--installing)
  * [Configuration](#2-configuration)
  * [Configure Easyminer](#3-optional-configure-cryptonote-easy-miner-for-your-pool)
  * [Starting the Pool](#4-start-the-pool)
  * [Host the front-end](#5-host-the-front-end)
  * [Customizing your website](#6-customize-your-website)
  * [Upgrading](#upgrading)
* [JSON-RPC Commands from CLI](#json-rpc-commands-from-cli)
* [Monitoring Your Pool](#monitoring-your-pool)
* [Donations](#donations)
* [License](#license)



#### Basic features

* Am update API based on the Dyndns2 protocal
  * Makes this compatible with most DDNS clients
* Fully functional web application for managment of hosts and user profile information.
* Multiple DNS Server setup for redundancy.
* Payment processing
  * Splintered transactions to deal with max transaction size
  * Minimum payment threshold before balance will be paid out
  * Minimum denomination for truncating payment amount precision to reduce size/complexity of block transactions
* Detailed logging
* Ability to configure multiple ports - each with their own difficulty
* Variable difficulty / share limiter
* Share trust algorithm to reduce share validation hashing CPU load
* Clustering for vertical scaling
* Modular components for horizontal scaling (pool server, database, stats/API, payment processing, front-end)
* Live stats API (using AJAX long polling with CORS)
  * Currency network/block difficulty
  * Current block height
  * Network hashrate
  * Pool hashrate
  * Each miners' individual stats (hashrate, shares submitted, pending balance, total paid, etc)
  * Blocks found (pending, confirmed, and orphaned)
* An easily extendable, responsive, light-weight front-end using API to display data


Donations
---------

Many Bothans died getting this software to you. Honor them by sending me some BTC or XMR.

 * BTC: 1K4N5msYZHse6Hbxz4oWUjwqPf8wu6ducV
 * XMR: 42VxjBpfi4TS6KFjNrrKo3QLcyK7gBGfM9w7DxmGRcocYnEbJ1hhZWXfaHJtCXBxnL74DpkioPSivjRYU8qkt59s3EaHUU3


License
-------

Released under the GNU General Public License v2

 * http://www.gnu.org/licenses/gpl-2.0.html
