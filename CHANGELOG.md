Salesforce Connector Release Notes
==================================

Date: 14-MAY-2014

Version: 5.3.7

Supported API versions
------------------------
Salesforce v28 API. 

Supported Mule Runtime Versions
--------------------------------
3.4.x

New Features and Functionality
------------------------------
5.3.7
-----
 - Added new exception handling for REST API
 - Minor exception handling fixes for SOAP API

5.3.6
-----
 - Added Salesforce SOAP API headers support
 - Added new exception handling for SOAP API

5.3.5
-----
 - Minor fixes in source code

5.3.4
-----
 - Added getServerTimestamp operation

5.3.3
-----
 - Fixed issues with proxies

5.3.2
-----
 - Moved force-wsc as a dependency
 - Added support for API v28
 - Added support for recursive SObjects
 - Added sessionId and serviceUrl parameters on connection
 - Fixes for streaming API reconnection
 - Added support for reference types that differ from the field name 
 
5.3.1
-----
 - Upgraded DevKit to 3.4.0
 - Added support for streaming with OAuth
 - Fixes for streaming API

5.3.0
-----
 - Upgraded DevKit to 3.4.0-RC1
 - Added Mule 3.4 metadata support

5.2.0
-----
 - Upgraded to API v26
 - Added setPassword operation
 - Fixed documentation issues

5.1.3
-----
 - Upgraded to DevKit 3.3.2
 - Added paging capabilities with paginated-query method
 - Added support for Object Search queries using SOSL
 - Fixed SessionTimedOutException INVALID_SESSION_ID when doing several concurrent request with a timed out session
 - Fixed get-deleted and get-updated operations
 - Fixed blocking account when trying to disconnect if credentials are wrong
 - Updated documentation and fixed documentation issues 

5.1.2
-----
 - Fixed issue related to ObjectStoreManager injection and get-updated-objects

5.1.1
-----
 - Fixed issue related to URL transformer with DevKit 3.3.1

