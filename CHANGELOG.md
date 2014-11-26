Salesforce Connector Release Notes
==================================

Date: DEC-2014

Version: 5.5.0

Supported API versions
------------------------
Salesforce v32 API. 

Supported Mule Runtime Versions
--------------------------------
3.5.1

New Features and Functionality
------------------------------
5.5.0
=====
 - Added Salesforce Metadata API support
 - Bug fixing
 - Devkit 3.5.2
 
5.4.7
=====
 - Added Salesforce SOAP API headers support
 - Added new exception handling for REST and SOAP API
 - Removed Paginated Query operation (since AutoPaging for Query and QueryAll)
 - Devkit 3.5.0

5.4.6
=====
 - Added NonPaginatedQuery operation for backwards compatibility since Query uses Mule 3.5 AutoPaging
 - Added batchSize for queries
 - Devkit 3.5.0-M4

5.4.4
=====
 - Fixed new Jetty dependencies for Mule 3.5.0
 - Fixes for authorizationUrl and accessTokenUrl in OAuth
 - Devkit 3.5.0-cascade

5.4.3
=====
 - Added compatibility with new Mule Batch module
 - Added configurable for MAX_DEPTH for BULK API
 - Devkit 3.5.0-cascade

5.4.2
=====
 - Added getServerTimestamp operation
 - New exception handling for reconnection strategy
 - Fixed issues with proxies

5.4.1
=====
 - Added Mule 3.5 AutoPaging support for Query and QueryAll
 - Added DSQL support
 - Added operation to retrieve JobInfo
 - Added a way to allow empty security tokens
 - Fixed Date values for Nested SObjects in Bulk API
 - Fixed QueryResultStream issue when service return more than one page
 - Devkit 3.5.0-bighorn

5.4.0
=====
 - Moved force-wsc as a dependency
 - Added support for API v28
 - Added support for recursive SObjects
 - Added sessionId and serviceUrl parameters on connection
 - Fixes for streaming API reconnection
 - Added support for reference types that differ from the field name 
 - Devkit 3.5.0-andes
 
5.3.1
=====
 - Upgraded DevKit to 3.4.0
 - Added support for streaming with OAuth
 - Fixes for streaming API

5.3.0
=====
 - Upgraded DevKit to 3.4.0-RC1
 - Added Mule 3.4 metadata support

5.2.0
=====
 - Upgraded to API v26
 - Added setPassword operation
 - Fixed documentation issues

5.1.3
=====
 - Upgraded to DevKit 3.3.2
 - Added paging capabilities with paginated-query method
 - Added support for Object Search queries using SOSL
 - Fixed SessionTimedOutException INVALID_SESSION_ID when doing several concurrent request with a timed out session
 - Fixed get-deleted and get-updated operations
 - Fixed blocking account when trying to disconnect if credentials are wrong
 - Updated documentation and fixed documentation issues 

5.1.2
=====
 - Fixed issue related to ObjectStoreManager injection and get-updated-objects

5.1.1
=====
 - Fixed issue related to URL transformer with DevKit 3.3.1
