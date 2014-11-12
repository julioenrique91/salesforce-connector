package org.mule.modules.salesforce.connection;

import javax.xml.namespace.QName;

import com.sforce.soap.metadata.MetadataConnection;
import com.sforce.ws.ConnectorConfig;
import com.sforce.ws.bind.XMLizable;

public class CustomMetadataConnection {

	public CustomMetadataConnection() {
	}

	private MetadataConnection connection;
	
	public MetadataConnection getConnection() {
		return this.connection;
	}

	public void setConnection(MetadataConnection connection) {
		this.connection = connection;
	}

	public ConnectorConfig getConfig() {
		return this.connection.getConfig();
	}

	public void setSessionHeader(java.lang.String sessionId) {
		this.connection.setSessionHeader(sessionId);
	}

	public void clearSessionHeader() {
		this.connection.clearSessionHeader();
	}

	public com.sforce.soap.metadata.SessionHeader_element getSessionHeader() {
		return this.connection.getSessionHeader();
	}

	public void __setSessionHeader(
			com.sforce.soap.metadata.SessionHeader_element __header) {
		this.connection.__setSessionHeader(__header);
	}

	public void setDebuggingInfo(java.lang.String debugLog) {
		this.connection.setDebuggingInfo(debugLog);
	}

	public void clearDebuggingInfo() {
		this.connection.clearDebuggingInfo();
	}

	public com.sforce.soap.metadata.DebuggingInfo_element getDebuggingInfo() {
		return this.connection.getDebuggingInfo();
	}

	public void __setDebuggingInfo(
			com.sforce.soap.metadata.DebuggingInfo_element __header) {
		this.connection.__setDebuggingInfo(__header);
	}

	public void setCallOptions(java.lang.String client) {
		this.connection.setCallOptions(client);
	}

	public void clearCallOptions() {
		this.connection.clearCallOptions();
	}

	public com.sforce.soap.metadata.CallOptions_element getCallOptions() {
		return this.connection.getCallOptions();
	}

	public void __setCallOptions(
			com.sforce.soap.metadata.CallOptions_element __header) {
		this.connection.__setCallOptions(__header);
	}

	public void setDebuggingHeader(
			com.sforce.soap.metadata.LogInfo[] categories,
			com.sforce.soap.metadata.LogType debugLevel) {
		this.connection.setDebuggingHeader(categories, debugLevel);
	}

	public void clearDebuggingHeader() {
		this.connection.clearDebuggingHeader();
	}

	public com.sforce.soap.metadata.DebuggingHeader_element getDebuggingHeader() {
		return this.connection.getDebuggingHeader();
	}

	public void __setDebuggingHeader(
			com.sforce.soap.metadata.DebuggingHeader_element __header) {
		this.connection.__setDebuggingHeader(__header);
	}

	public com.sforce.soap.metadata.SaveResult[] updateMetadata(
			com.sforce.soap.metadata.Metadata[] metadata)
			throws com.sforce.ws.ConnectionException {
		return this.connection.updateMetadata(metadata);
	}

	public com.sforce.soap.metadata.AsyncResult retrieve(
			com.sforce.soap.metadata.RetrieveRequest retrieveRequest)
			throws com.sforce.ws.ConnectionException {
		return this.connection.retrieve(retrieveRequest);
	}

	public com.sforce.soap.metadata.DeployResult checkDeployStatus(
			java.lang.String asyncProcessId, boolean includeDetails)
			throws com.sforce.ws.ConnectionException {
		return this.connection
				.checkDeployStatus(asyncProcessId, includeDetails);
	}

	public com.sforce.soap.metadata.SaveResult renameMetadata(
			java.lang.String type, java.lang.String oldFullName,
			java.lang.String newFullName)
			throws com.sforce.ws.ConnectionException {
		return this.connection.renameMetadata(type, oldFullName, newFullName);
	}

	public com.sforce.soap.metadata.CancelDeployResult cancelDeploy(
			java.lang.String String) throws com.sforce.ws.ConnectionException {
		return this.connection.cancelDeploy(String);
	}

	public com.sforce.soap.metadata.FileProperties[] listMetadata(
			com.sforce.soap.metadata.ListMetadataQuery[] queries,
			double asOfVersion) throws com.sforce.ws.ConnectionException {
		return this.connection.listMetadata(queries, asOfVersion);
	}

	public com.sforce.soap.metadata.DeleteResult[] deleteMetadata(
			java.lang.String type, java.lang.String[] fullNames)
			throws com.sforce.ws.ConnectionException {
		return this.connection.deleteMetadata(type, fullNames);
	}

	public com.sforce.soap.metadata.UpsertResult[] upsertMetadata(
			com.sforce.soap.metadata.Metadata[] metadata)
			throws com.sforce.ws.ConnectionException {
		return this.connection.upsertMetadata(metadata);
	}

	public com.sforce.soap.metadata.SaveResult[] createMetadata(
			com.sforce.soap.metadata.Metadata[] metadata)
			throws com.sforce.ws.ConnectionException {
		return this.connection.createMetadata(metadata);
	}

	public com.sforce.soap.metadata.RetrieveResult checkRetrieveStatus(
			java.lang.String asyncProcessId)
			throws com.sforce.ws.ConnectionException {
		return this.connection.checkRetrieveStatus(asyncProcessId);
	}

	public com.sforce.soap.metadata.ReadResult readMetadata(
			java.lang.String type, java.lang.String[] fullNames)
			throws com.sforce.ws.ConnectionException {
		return this.connection.readMetadata(type, fullNames);
	}

	public com.sforce.soap.metadata.DescribeMetadataResult describeMetadata(
			double asOfVersion) throws com.sforce.ws.ConnectionException {
		return this.connection.describeMetadata(asOfVersion);
	}

	public com.sforce.soap.metadata.AsyncResult deploy(byte[] ZipFile,
			com.sforce.soap.metadata.DeployOptions DeployOptions)
			throws com.sforce.ws.ConnectionException {
		return this.connection.deploy(ZipFile, DeployOptions);
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
