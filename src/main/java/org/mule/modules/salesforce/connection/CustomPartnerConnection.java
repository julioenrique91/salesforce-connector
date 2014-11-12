package org.mule.modules.salesforce.connection;

import javax.xml.namespace.QName;

import com.sforce.soap.partner.PartnerConnection;
import com.sforce.ws.ConnectorConfig;
import com.sforce.ws.bind.XMLizable;

public class CustomPartnerConnection {

	private PartnerConnection connection;

	public CustomPartnerConnection() {
	}

	public PartnerConnection getConnection() {
		return connection;
	}

	public void setConnection(PartnerConnection connection) {
		this.connection = connection;
	}

	public ConnectorConfig getConfig() {
		return this.connection.getConfig();
	}

	public void setLimitInfoHeader(com.sforce.soap.partner.LimitInfo[] limitInfo) {
		this.connection.setLimitInfoHeader(limitInfo);
	}

	public void clearLimitInfoHeader() {
		this.connection.clearLimitInfoHeader();
	}

	public com.sforce.soap.partner.LimitInfoHeader_element getLimitInfoHeader() {
		return this.connection.getLimitInfoHeader();
	}

	public void __setLimitInfoHeader(
			com.sforce.soap.partner.LimitInfoHeader_element __header) {
		this.connection.__setLimitInfoHeader(__header);
	}

	public void setStreamingEnabledHeader(boolean streamingEnabled) {
		this.connection.setStreamingEnabledHeader(streamingEnabled);
	}

	public void clearStreamingEnabledHeader() {
		this.connection.clearStreamingEnabledHeader();
	}

	public com.sforce.soap.partner.StreamingEnabledHeader_element getStreamingEnabledHeader() {
		return this.connection.getStreamingEnabledHeader();
	}

	public void __setStreamingEnabledHeader(
			com.sforce.soap.partner.StreamingEnabledHeader_element __header) {
		this.connection.__setStreamingEnabledHeader(__header);
	}

	public void setMruHeader(boolean updateMru) {
		this.connection.setMruHeader(updateMru);
	}

	public void clearMruHeader() {
		this.connection.clearMruHeader();
	}

	public com.sforce.soap.partner.MruHeader_element getMruHeader() {
		return this.connection.getMruHeader();
	}

	public void __setMruHeader(
			com.sforce.soap.partner.MruHeader_element __header) {
		this.connection.__setMruHeader(__header);
	}

	public void setCallOptions(java.lang.String client,
			java.lang.String defaultNamespace) {
		this.connection.setCallOptions(client, defaultNamespace);
	}

	public void clearCallOptions() {
		this.connection.clearCallOptions();
	}

	public com.sforce.soap.partner.CallOptions_element getCallOptions() {
		return this.connection.getCallOptions();
	}

	public void __setCallOptions(
			com.sforce.soap.partner.CallOptions_element __header) {
		this.connection.__setCallOptions(__header);
	}

	public void setPackageVersionHeader(
			com.sforce.soap.partner.PackageVersion[] packageVersions) {
		this.connection.setPackageVersionHeader(packageVersions);
	}

	public void clearPackageVersionHeader() {
		this.connection.clearPackageVersionHeader();
	}

	public com.sforce.soap.partner.PackageVersionHeader_element getPackageVersionHeader() {
		return this.connection.getPackageVersionHeader();
	}

	public void __setPackageVersionHeader(
			com.sforce.soap.partner.PackageVersionHeader_element __header) {
		this.connection.__setPackageVersionHeader(__header);
	}

	public void setEmailHeader(boolean triggerAutoResponseEmail,
			boolean triggerOtherEmail, boolean triggerUserEmail) {
		this.connection.setEmailHeader(triggerAutoResponseEmail,
				triggerOtherEmail, triggerUserEmail);
	}

	public void clearEmailHeader() {
		this.connection.clearEmailHeader();
	}

	public com.sforce.soap.partner.EmailHeader_element getEmailHeader() {
		return this.connection.getEmailHeader();
	}

	public void __setEmailHeader(
			com.sforce.soap.partner.EmailHeader_element __header) {
		this.connection.__setEmailHeader(__header);
	}

	public void setLocaleOptions(java.lang.String language,
			boolean localizeErrors) {
		this.connection.setLocaleOptions(language, localizeErrors);
	}

	public void clearLocaleOptions() {
		this.connection.clearLocaleOptions();
	}

	public com.sforce.soap.partner.LocaleOptions_element getLocaleOptions() {
		return this.connection.getLocaleOptions();
	}

	public void __setLocaleOptions(
			com.sforce.soap.partner.LocaleOptions_element __header) {
		this.connection.__setLocaleOptions(__header);
	}

	public void setOwnerChangeOptions(boolean transferAttachments,
			boolean transferOpenActivities) {
		this.connection.setOwnerChangeOptions(transferAttachments,
				transferOpenActivities);
	}

	public void clearOwnerChangeOptions() {
		this.connection.clearOwnerChangeOptions();
	}

	public com.sforce.soap.partner.OwnerChangeOptions_element getOwnerChangeOptions() {
		return this.connection.getOwnerChangeOptions();
	}

	public void __setOwnerChangeOptions(
			com.sforce.soap.partner.OwnerChangeOptions_element __header) {
		this.connection.__setOwnerChangeOptions(__header);
	}

	public void setDebuggingInfo(java.lang.String debugLog) {
		this.connection.setDebuggingInfo(debugLog);
	}

	public void clearDebuggingInfo() {
		this.connection.clearDebuggingInfo();
	}

	public com.sforce.soap.partner.DebuggingInfo_element getDebuggingInfo() {
		return this.connection.getDebuggingInfo();
	}

	public void __setDebuggingInfo(
			com.sforce.soap.partner.DebuggingInfo_element __header) {
		this.connection.__setDebuggingInfo(__header);
	}

	public void setSessionHeader(java.lang.String sessionId) {
		this.connection.setSessionHeader(sessionId);
	}

	public void clearSessionHeader() {
		this.connection.clearSessionHeader();
	}

	public com.sforce.soap.partner.SessionHeader_element getSessionHeader() {
		return this.connection.getSessionHeader();
	}

	public void __setSessionHeader(
			com.sforce.soap.partner.SessionHeader_element __header) {
		this.connection.__setSessionHeader(__header);
	}

	public void setDebuggingHeader(com.sforce.soap.partner.DebugLevel debugLevel) {
		this.connection.setDebuggingHeader(debugLevel);
	}

	public void clearDebuggingHeader() {
		this.connection.clearDebuggingHeader();
	}

	public com.sforce.soap.partner.DebuggingHeader_element getDebuggingHeader() {
		return this.connection.getDebuggingHeader();
	}

	public void __setDebuggingHeader(
			com.sforce.soap.partner.DebuggingHeader_element __header) {
		this.connection.__setDebuggingHeader(__header);
	}

	public void setLoginScopeHeader(java.lang.String organizationId,
			java.lang.String portalId) {
		this.connection.setLoginScopeHeader(organizationId, portalId);
	}

	public void clearLoginScopeHeader() {
		this.connection.clearLoginScopeHeader();
	}

	public com.sforce.soap.partner.LoginScopeHeader_element getLoginScopeHeader() {
		return this.connection.getLoginScopeHeader();
	}

	public void __setLoginScopeHeader(
			com.sforce.soap.partner.LoginScopeHeader_element __header) {
		this.connection.__setLoginScopeHeader(__header);
	}

	public void setDisableFeedTrackingHeader(boolean disableFeedTracking) {
		this.connection.setDisableFeedTrackingHeader(disableFeedTracking);
	}

	public void clearDisableFeedTrackingHeader() {
		this.connection.clearDisableFeedTrackingHeader();
	}

	public com.sforce.soap.partner.DisableFeedTrackingHeader_element getDisableFeedTrackingHeader() {
		return this.connection.getDisableFeedTrackingHeader();
	}

	public void __setDisableFeedTrackingHeader(
			com.sforce.soap.partner.DisableFeedTrackingHeader_element __header) {
		this.connection.__setDisableFeedTrackingHeader(__header);
	}

	public void setUserTerritoryDeleteHeader(java.lang.String transferToUserId) {
		this.connection.setUserTerritoryDeleteHeader(transferToUserId);
	}

	public void clearUserTerritoryDeleteHeader() {
		this.connection.clearUserTerritoryDeleteHeader();
	}

	public com.sforce.soap.partner.UserTerritoryDeleteHeader_element getUserTerritoryDeleteHeader() {
		return this.connection.getUserTerritoryDeleteHeader();
	}

	public void __setUserTerritoryDeleteHeader(
			com.sforce.soap.partner.UserTerritoryDeleteHeader_element __header) {
		this.connection.__setUserTerritoryDeleteHeader(__header);
	}

	public void setAllowFieldTruncationHeader(boolean allowFieldTruncation) {
		this.connection.setAllowFieldTruncationHeader(allowFieldTruncation);
	}

	public void clearAllowFieldTruncationHeader() {
		this.connection.clearAllowFieldTruncationHeader();
	}

	public com.sforce.soap.partner.AllowFieldTruncationHeader_element getAllowFieldTruncationHeader() {
		return this.connection.getAllowFieldTruncationHeader();
	}

	public void __setAllowFieldTruncationHeader(
			com.sforce.soap.partner.AllowFieldTruncationHeader_element __header) {
		this.connection.__setAllowFieldTruncationHeader(__header);
	}

	public void setQueryOptions(int batchSize) {
		this.connection.setQueryOptions(batchSize);
	}

	public void clearQueryOptions() {
		this.connection.clearQueryOptions();
	}

	public com.sforce.soap.partner.QueryOptions_element getQueryOptions() {
		return this.connection.getQueryOptions();
	}

	public void __setQueryOptions(
			com.sforce.soap.partner.QueryOptions_element __header) {
		this.connection.__setQueryOptions(__header);
	}

	public void setAssignmentRuleHeader(java.lang.String assignmentRuleId,
			java.lang.Boolean useDefaultRule) {
		this.connection.setAssignmentRuleHeader(assignmentRuleId,
				useDefaultRule);
	}

	public void clearAssignmentRuleHeader() {
		this.connection.clearAssignmentRuleHeader();
	}

	public com.sforce.soap.partner.AssignmentRuleHeader_element getAssignmentRuleHeader() {
		return this.connection.getAssignmentRuleHeader();
	}

	public void __setAssignmentRuleHeader(
			com.sforce.soap.partner.AssignmentRuleHeader_element __header) {
		this.connection.__setAssignmentRuleHeader(__header);
	}

	public void setAllOrNoneHeader(boolean allOrNone) {
		this.connection.setAllOrNoneHeader(allOrNone);
	}

	public void clearAllOrNoneHeader() {
		this.connection.clearAllOrNoneHeader();
	}

	public com.sforce.soap.partner.AllOrNoneHeader_element getAllOrNoneHeader() {
		return this.connection.getAllOrNoneHeader();
	}

	public void __setAllOrNoneHeader(
			com.sforce.soap.partner.AllOrNoneHeader_element __header) {
		this.connection.__setAllOrNoneHeader(__header);
	}

	public void setConditionalRequestHeader(
			java.util.Calendar ifModifiedBefore,
			java.util.Calendar ifModifiedSince) {
		this.connection.setConditionalRequestHeader(ifModifiedBefore,
				ifModifiedSince);
	}

	public void clearConditionalRequestHeader() {
		this.connection.clearConditionalRequestHeader();
	}

	public com.sforce.soap.partner.ConditionalRequestHeader_element getConditionalRequestHeader() {
		return this.connection.getConditionalRequestHeader();
	}

	public void __setConditionalRequestHeader(
			com.sforce.soap.partner.ConditionalRequestHeader_element __header) {
		this.connection.__setConditionalRequestHeader(__header);
	}

	public com.sforce.soap.partner.DescribeSoqlListViewResult describeSoqlListViews(
			com.sforce.soap.partner.DescribeSoqlListViewsRequest request)
			throws com.sforce.ws.ConnectionException {
		return this.connection.describeSoqlListViews(request);
	}

	public com.sforce.soap.partner.DescribeCompactLayoutsResult describeCompactLayouts(
			java.lang.String sObjectType, java.lang.String[] recordTypeIds)
			throws com.sforce.ws.ConnectionException {
		return this.connection.describeCompactLayouts(sObjectType,
				recordTypeIds);
	}

	public com.sforce.soap.partner.DescribeGlobalResult describeGlobal()
			throws com.sforce.ws.ConnectionException {
		return this.connection.describeGlobal();
	}

	public com.sforce.soap.partner.sobject.SObject[] retrieve(
			java.lang.String fieldList, java.lang.String sObjectType,
			java.lang.String[] ids) throws com.sforce.ws.ConnectionException {
		return this.connection.retrieve(fieldList, sObjectType, ids);
	}

	public com.sforce.soap.partner.DescribeTab[] describeAllTabs()
			throws com.sforce.ws.ConnectionException {
		return this.connection.describeAllTabs();
	}

	public com.sforce.soap.partner.DescribeLayoutResult describeLayout(
			java.lang.String sObjectType, java.lang.String layoutName,
			java.lang.String[] recordTypeIds)
			throws com.sforce.ws.ConnectionException {
		return this.connection.describeLayout(sObjectType, layoutName,
				recordTypeIds);
	}

	public com.sforce.soap.partner.EmptyRecycleBinResult[] emptyRecycleBin(
			java.lang.String[] ids) throws com.sforce.ws.ConnectionException {
		return this.connection.emptyRecycleBin(ids);
	}

	public com.sforce.soap.partner.DescribeAppMenuResult describeAppMenu(
			com.sforce.soap.partner.AppMenuType appMenuType)
			throws com.sforce.ws.ConnectionException {
		return this.connection.describeAppMenu(appMenuType);
	}

	public com.sforce.soap.partner.ProcessResult[] process(
			com.sforce.soap.partner.ProcessRequest[] actions)
			throws com.sforce.ws.ConnectionException {
		return this.connection.process(actions);
	}

	public void logout() throws com.sforce.ws.ConnectionException {
		this.connection.logout();
	}

	public com.sforce.soap.partner.InvalidateSessionsResult[] invalidateSessions(
			java.lang.String[] sessionIds)
			throws com.sforce.ws.ConnectionException {
		return this.connection.invalidateSessions(sessionIds);
	}

	public com.sforce.soap.partner.DescribeListViewResult[] describeListViews(
			java.lang.String[] sObjectType)
			throws com.sforce.ws.ConnectionException {
		return this.connection.describeListViews(sObjectType);
	}

	public com.sforce.soap.partner.KnowledgeSettings describeKnowledgeSettings()
			throws com.sforce.ws.ConnectionException {
		return this.connection.describeKnowledgeSettings();
	}

	public com.sforce.soap.partner.DescribeCompactLayout[] describePrimaryCompactLayouts(
			java.lang.String[] sObjectTypes)
			throws com.sforce.ws.ConnectionException {
		return this.connection.describePrimaryCompactLayouts(sObjectTypes);
	}

	public com.sforce.soap.partner.GetUserInfoResult getUserInfo()
			throws com.sforce.ws.ConnectionException {
		return this.connection.getUserInfo();
	}

	public com.sforce.soap.partner.GetServerTimestampResult getServerTimestamp()
			throws com.sforce.ws.ConnectionException {
		return this.connection.getServerTimestamp();
	}

	public com.sforce.soap.partner.DescribeSearchLayoutResult[] describeSearchLayouts(
			java.lang.String[] sObjectType)
			throws com.sforce.ws.ConnectionException {
		return this.connection.describeSearchLayouts(sObjectType);
	}

	public com.sforce.soap.partner.DescribeFlexiPageResult[] describeFlexiPages(
			java.lang.String[] flexiPages)
			throws com.sforce.ws.ConnectionException {
		return this.connection.describeFlexiPages(flexiPages);
	}

	public com.sforce.soap.partner.SetPasswordResult setPassword(
			java.lang.String userId, java.lang.String password)
			throws com.sforce.ws.ConnectionException {
		return this.connection.setPassword(userId, password);
	}

	public com.sforce.soap.partner.SendEmailResult[] sendEmailMessage(
			java.lang.String[] ids) throws com.sforce.ws.ConnectionException {
		return this.connection.sendEmailMessage(ids);
	}

	public com.sforce.soap.partner.DeleteResult[] delete(java.lang.String[] ids)
			throws com.sforce.ws.ConnectionException {
		return this.connection.delete(ids);
	}

	public com.sforce.soap.partner.DescribeTabSetResult[] describeTabs()
			throws com.sforce.ws.ConnectionException {
		return this.connection.describeTabs();
	}

	public com.sforce.soap.partner.ExecuteListViewResult executeListView(
			com.sforce.soap.partner.ExecuteListViewRequest request)
			throws com.sforce.ws.ConnectionException {
		return this.connection.executeListView(request);
	}

	public com.sforce.soap.partner.DescribeSoftphoneLayoutResult describeSoftphoneLayout()
			throws com.sforce.ws.ConnectionException {
		return this.connection.describeSoftphoneLayout();
	}

	public com.sforce.soap.partner.DescribeSObjectResult describeSObject(
			java.lang.String sObjectType)
			throws com.sforce.ws.ConnectionException {
		return this.connection.describeSObject(sObjectType);
	}

	public com.sforce.soap.partner.DescribeSObjectResult[] describeSObjects(
			java.lang.String[] sObjectType)
			throws com.sforce.ws.ConnectionException {
		return this.connection.describeSObjects(sObjectType);
	}

	public com.sforce.soap.partner.GetUpdatedResult getUpdated(
			java.lang.String sObjectType, java.util.Calendar startDate,
			java.util.Calendar endDate)
			throws com.sforce.ws.ConnectionException {
		return this.connection.getUpdated(sObjectType, startDate, endDate);
	}

	public com.sforce.soap.partner.QueryResult queryAll(
			java.lang.String queryString)
			throws com.sforce.ws.ConnectionException {
		return this.connection.queryAll(queryString);
	}

	public com.sforce.soap.partner.DescribeApprovalLayoutResult describeApprovalLayout(
			java.lang.String sObjectType,
			java.lang.String[] approvalProcessNames)
			throws com.sforce.ws.ConnectionException {
		return this.connection.describeApprovalLayout(sObjectType,
				approvalProcessNames);
	}

	public com.sforce.soap.partner.ResetPasswordResult resetPassword(
			java.lang.String userId) throws com.sforce.ws.ConnectionException {
		return this.connection.resetPassword(userId);
	}

	public com.sforce.soap.partner.MergeResult[] merge(
			com.sforce.soap.partner.MergeRequest[] request)
			throws com.sforce.ws.ConnectionException {
		return this.connection.merge(request);
	}

	public com.sforce.soap.partner.QueryResult query(
			java.lang.String queryString)
			throws com.sforce.ws.ConnectionException {
		return this.connection.query(queryString);
	}

	public com.sforce.soap.partner.SaveResult[] update(
			com.sforce.soap.partner.sobject.SObject[] sObjects)
			throws com.sforce.ws.ConnectionException {
		return this.connection.update(sObjects);
	}

	public com.sforce.soap.partner.UndeleteResult[] undelete(
			java.lang.String[] ids) throws com.sforce.ws.ConnectionException {
		return this.connection.undelete(ids);
	}

	public com.sforce.soap.partner.LeadConvertResult[] convertLead(
			com.sforce.soap.partner.LeadConvert[] leadConverts)
			throws com.sforce.ws.ConnectionException {
		return this.connection.convertLead(leadConverts);
	}

	public com.sforce.soap.partner.DescribeGlobalTheme describeGlobalTheme()
			throws com.sforce.ws.ConnectionException {
		return this.connection.describeGlobalTheme();
	}

	public com.sforce.soap.partner.DescribeDataCategoryGroupStructureResult[] describeDataCategoryGroupStructures(
			com.sforce.soap.partner.DataCategoryGroupSobjectTypePair[] pairs,
			boolean topCategoriesOnly) throws com.sforce.ws.ConnectionException {
		return this.connection.describeDataCategoryGroupStructures(pairs,
				topCategoriesOnly);
	}

	public com.sforce.soap.partner.SendEmailResult[] sendEmail(
			com.sforce.soap.partner.Email[] messages)
			throws com.sforce.ws.ConnectionException {
		return this.connection.sendEmail(messages);
	}

	public com.sforce.soap.partner.DescribeAvailableQuickActionResult[] describeAvailableQuickActions(
			java.lang.String contextType)
			throws com.sforce.ws.ConnectionException {
		return this.connection.describeAvailableQuickActions(contextType);
	}

	public com.sforce.soap.partner.SaveResult[] create(
			com.sforce.soap.partner.sobject.SObject[] sObjects)
			throws com.sforce.ws.ConnectionException {
		return this.connection.create(sObjects);
	}

	public com.sforce.soap.partner.GetDeletedResult getDeleted(
			java.lang.String sObjectType, java.util.Calendar startDate,
			java.util.Calendar endDate)
			throws com.sforce.ws.ConnectionException {
		return this.connection.getDeleted(sObjectType, startDate, endDate);
	}

	public com.sforce.soap.partner.DescribeQuickActionResult[] describeQuickActions(
			java.lang.String[] quickActions)
			throws com.sforce.ws.ConnectionException {
		return this.connection.describeQuickActions(quickActions);
	}

	public com.sforce.soap.partner.QuickActionTemplateResult[] retrieveQuickActionTemplates(
			java.lang.String[] quickActionNames, java.lang.String contextId)
			throws com.sforce.ws.ConnectionException {
		return this.connection.retrieveQuickActionTemplates(quickActionNames,
				contextId);
	}

	public com.sforce.soap.partner.PerformQuickActionResult[] performQuickActions(
			com.sforce.soap.partner.PerformQuickActionRequest[] quickActions)
			throws com.sforce.ws.ConnectionException {
		return this.connection.performQuickActions(quickActions);
	}

	public com.sforce.soap.partner.LoginResult login(java.lang.String username,
			java.lang.String password) throws com.sforce.ws.ConnectionException {
		return this.connection.login(username, password);
	}

	public com.sforce.soap.partner.DescribeSearchScopeOrderResult[] describeSearchScopeOrder()
			throws com.sforce.ws.ConnectionException {
		return this.connection.describeSearchScopeOrder();
	}

	public com.sforce.soap.partner.DescribeLookupLayoutResult[] describeLookupLayouts(
			java.lang.String[] sObjectType)
			throws com.sforce.ws.ConnectionException {
		return this.connection.describeLookupLayouts(sObjectType);
	}

	public com.sforce.soap.partner.DescribeDataCategoryGroupResult[] describeDataCategoryGroups(
			java.lang.String[] sObjectType)
			throws com.sforce.ws.ConnectionException {
		return this.connection.describeDataCategoryGroups(sObjectType);
	}

	public com.sforce.soap.partner.QueryResult queryMore(
			java.lang.String queryLocator)
			throws com.sforce.ws.ConnectionException {
		return this.connection.queryMore(queryLocator);
	}

	public com.sforce.soap.partner.SearchResult search(
			java.lang.String searchString)
			throws com.sforce.ws.ConnectionException {
		return this.connection.search(searchString);
	}

	public com.sforce.soap.partner.DescribeThemeResult describeTheme(
			java.lang.String[] sobjectType)
			throws com.sforce.ws.ConnectionException {
		return this.connection.describeTheme(sobjectType);
	}

	public com.sforce.soap.partner.UpsertResult[] upsert(
			java.lang.String externalIDFieldName,
			com.sforce.soap.partner.sobject.SObject[] sObjects)
			throws com.sforce.ws.ConnectionException {
		return this.connection.upsert(externalIDFieldName, sObjects);
	}

	public void addExtraHeader(QName __headerName, XMLizable __value) {
		this.connection.addExtraHeader(__headerName, __value);
	}

	public void removeExtraHeader(QName __headerName) {
		this.connection.removeExtraHeader(__headerName);
	}

	public XMLizable getExtraHeader(QName __headerName) {
		return this.connection.getExtraHeader(__headerName);
	}

	public void clearExtraHeaders() {
		this.connection.clearExtraHeaders();
	}
}
