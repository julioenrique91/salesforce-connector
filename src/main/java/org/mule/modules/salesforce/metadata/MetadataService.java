/**
 * Mule Salesforce Connector
 *
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 *
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */

/**
 * 
 */
package org.mule.modules.salesforce.metadata;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.commons.beanutils.ConvertUtilsBean;
import org.apache.commons.beanutils.PropertyUtilsBean;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.mule.modules.salesforce.connection.CustomMetadataConnection;
import org.mule.modules.salesforce.metadata.type.MetadataOperationType;
import org.mule.modules.salesforce.metadata.type.MetadataType;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.sforce.soap.metadata.AsyncResult;
import com.sforce.soap.metadata.CodeCoverageWarning;
import com.sforce.soap.metadata.DeleteResult;
import com.sforce.soap.metadata.DeployDetails;
import com.sforce.soap.metadata.DeployMessage;
import com.sforce.soap.metadata.DeployOptions;
import com.sforce.soap.metadata.DeployResult;
import com.sforce.soap.metadata.Metadata;
import com.sforce.soap.metadata.PackageTypeMembers;
import com.sforce.soap.metadata.ReadResult;
import com.sforce.soap.metadata.RetrieveMessage;
import com.sforce.soap.metadata.RetrieveRequest;
import com.sforce.soap.metadata.RetrieveResult;
import com.sforce.soap.metadata.RetrieveStatus;
import com.sforce.soap.metadata.RunTestFailure;
import com.sforce.soap.metadata.RunTestsResult;
import com.sforce.soap.metadata.SaveResult;
import com.sforce.soap.metadata.UpsertResult;

/**
 * @author cristian.ambrozie
 * 
 */
@SuppressWarnings({"unchecked","rawtypes"})
public class MetadataService {
	
	private static final Logger logger = Logger.getLogger(MetadataService.class);
	
	// one second in milliseconds
	private static final long ONE_SECOND = 1000;
	// maximum number of attempts to deploy the zip file
	private static final int MAX_NUM_POLL_REQUESTS = 50;
	private static final double API_VERSION = 31.0;
	
	private MetadataService() {
	}

	public static List<SaveResult> callCreateUpdateService(
			CustomMetadataConnection connection, String type,
			List<Map<String, Object>> request,
			MetadataOperationType metadataOperation) throws Exception {

			List<SaveResult> results;
			MetadataType metadataType = MetadataType.valueOf(type);
			switch (metadataOperation) {
			case CREATE:
				results = Arrays.asList(connection
						.createMetadata(getMetadataObjects(metadataType,
								request)));
				break;
			case UPDATE:
				results = Arrays.asList(connection
						.updateMetadata(getMetadataObjects(metadataType,
								request)));
				break;
			default:
				results = new ArrayList<SaveResult>();
			}
			return results;
	}

	public static List<UpsertResult> callUpsertService(
			CustomMetadataConnection connection, String type,
			List<Map<String, Object>> request) throws Exception {

			MetadataType metadataType = MetadataType.valueOf(type);
			return Arrays.asList(connection.upsertMetadata(getMetadataObjects(
					metadataType, request)));
	}

	public static List<DeleteResult> callDeleteService(
			CustomMetadataConnection connection, String type, List<String> fullNames)
			throws Exception {

			MetadataType metadataType = MetadataType.valueOf(type);
			return Arrays.asList(connection.deleteMetadata(
					metadataType.getDisplayName(),
					fullNames.toArray(new String[fullNames.size()])));
	}

	public static ReadResult callReadService(CustomMetadataConnection connection,
			String type, List<String> fullNames) throws Exception {

			MetadataType metadataType = MetadataType.valueOf(type);
			return connection.readMetadata(metadataType.getDisplayName(),
					fullNames.toArray(new String[fullNames.size()]));
	}

	public static SaveResult callRenameService(CustomMetadataConnection connection,
			String type, String oldFullName, String newFullName)
			throws Exception {

			MetadataType metadataType = MetadataType.valueOf(type);
			return connection.renameMetadata(metadataType.getDisplayName(),
					oldFullName, newFullName);
	}
	
	public static String callDeployService(CustomMetadataConnection connection, InputStream stream) throws Exception {
		byte zipBytes[] = IOUtils.toByteArray(stream);
		DeployOptions deployOptions = new DeployOptions();
		deployOptions.setPerformRetrieve(false);
		deployOptions.setRollbackOnError(true);
		AsyncResult asyncResult = connection.deploy(zipBytes, deployOptions);
		String asyncResultId = asyncResult.getId();
		
		StringBuilder errorMessageBuilder = new StringBuilder();
		
		// Wait for the deploy to complete
		int poll = 0;
		long waitTimeMilliSecs = ONE_SECOND;
		DeployResult deployResult = null;
		boolean fetchDetails;
		
		do {
			Thread.sleep(waitTimeMilliSecs);
			// double the wait time for the next iteration
			waitTimeMilliSecs *= 2;
			if (poll++ > MAX_NUM_POLL_REQUESTS) {
				throw new Exception("Request timed out. If this is a large set of metadata components, check that the time allowed by MAX_NUM_POLL_REQUESTS is sufficient.");
			}
			// Fetch in-progress details once for every 3 polls
			fetchDetails = (poll % 3 == 0);
			deployResult = connection.checkDeployStatus(asyncResultId, fetchDetails);
			
			// save if any errors on the way
			if (!deployResult.isDone() && fetchDetails) {
				String error = getErrors(deployResult, "Failures for deployment in progress:\n");
				if(!StringUtils.EMPTY.equals(error)) {
					errorMessageBuilder.append(error);
				}
			}
		} while (!deployResult.isDone());
		
		if (!deployResult.isSuccess() && deployResult.getErrorStatusCode() != null) {
			throw new Exception(deployResult.getErrorStatusCode() + " msg: " + deployResult.getErrorMessage());
		}
		
		if (!fetchDetails) {
			// Get the final result with details if we didn't do it in the last attempt.
			deployResult = connection.checkDeployStatus(asyncResultId, true);
		}
		
		if (!deployResult.isSuccess()) {
			String error = getErrors(deployResult, "Final list of failures:\n");
			errorMessageBuilder.append(error);
			throw new Exception("The files were not successfully deployed\n" + errorMessageBuilder.toString());
		}
		
		return "The files were successfully deployed";
	}
	
	public static InputStream callRetrieveService(CustomMetadataConnection connection, List<String> packageNames, List<String> specificFiles, String unpackaged) throws Exception {
		RetrieveRequest retrieveRequest = new RetrieveRequest();
		// The version in package.xml overrides the version in RetrieveRequest
		retrieveRequest.setApiVersion(API_VERSION);
		
		if(specificFiles != null && specificFiles.size() > 0) {
			if(StringUtils.isNotBlank(unpackaged)) {
				throw new Exception("If a value is specified for specific files, packageNames must be set to null");
			}
			retrieveRequest.setSinglePackage(true);
			retrieveRequest.setSpecificFiles(specificFiles.toArray(new String[specificFiles.size()]));
		}
		if(StringUtils.isNotBlank(unpackaged)) {
			setUnpackaged(retrieveRequest, unpackaged);
		}
		
		// Start the retrieve operation
		AsyncResult asyncResult = connection.retrieve(retrieveRequest);
		String asyncResultId = asyncResult.getId();
		// Wait for the retrieve to complete
		int poll = 0;
		long waitTimeMilliSecs = ONE_SECOND;
		RetrieveResult result = null;
		do {
			Thread.sleep(waitTimeMilliSecs);
			// Double the wait time for the next iteration
			waitTimeMilliSecs *= 2;
			if (poll++ > MAX_NUM_POLL_REQUESTS) {
				throw new Exception("Request timed out. If this is a large set of metadata components, check that the time allowed "
						+ "by MAX_NUM_POLL_REQUESTS is sufficient.");
			}
			result = connection.checkRetrieveStatus(asyncResultId);
		} while (!result.isDone());
		
		if (result.getStatus() == RetrieveStatus.Failed) {
			throw new Exception(result.getErrorStatusCode() + " msg: " + result.getErrorMessage());
		} else if (result.getStatus() == RetrieveStatus.Succeeded) {
			// Print out any warning messages
			StringBuilder buf = new StringBuilder();
			if (result.getMessages() != null) {
				for (RetrieveMessage rm : result.getMessages()) {
					buf.append(rm.getFileName() + " - " + rm.getProblem());
				}
			}
			if (buf.length() > 0) {
				logger.debug("Retrieve warnings:\n" + buf);
			}
		}
		return new ByteArrayInputStream(result.getZipFile());
	}
	
	private static void setUnpackaged(RetrieveRequest request, String file) throws Exception {
		File unpackedManifest = new File(file);
		if (!unpackedManifest.exists() || !unpackedManifest.isFile())
			throw new Exception("Should provide a valid retrieve manifest for unpackaged content. Looking for "
					+ unpackedManifest.getAbsolutePath());
		// Note that we populate the _package object by parsing a manifest file here.
		// You could populate the _package based on any source for your particular application.
		com.sforce.soap.metadata.Package p = parsePackage(unpackedManifest);
		request.setUnpackaged(p);
	}
	
	private static com.sforce.soap.metadata.Package parsePackage(File file) throws Exception {
		try {
			InputStream is = new FileInputStream(file);
			List<PackageTypeMembers> pd = new ArrayList<PackageTypeMembers>();
			DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			Element d = db.parse(is).getDocumentElement();
			for (Node c = d.getFirstChild(); c != null; c = c.getNextSibling()) {
				if (c instanceof Element) {
					Element ce = (Element) c;
					//
					NodeList namee = ce.getElementsByTagName("name");
					if (namee.getLength() == 0) {
						// not
						continue;
					}
					String name = namee.item(0).getTextContent();
					NodeList m = ce.getElementsByTagName("members");
					List<String> members = new ArrayList<String>();
					for (int i = 0; i < m.getLength(); i++) {
						Node mm = m.item(i);
						members.add(mm.getTextContent());
					}
					PackageTypeMembers pdi = new PackageTypeMembers();
					pdi.setName(name);
					pdi.setMembers(members.toArray(new String[members.size()]));
					pd.add(pdi);
				}
			}
			com.sforce.soap.metadata.Package r = new com.sforce.soap.metadata.Package();
			r.setTypes(pd.toArray(new PackageTypeMembers[pd.size()]));
			r.setVersion(API_VERSION + "");
			return r;
		} catch (ParserConfigurationException pce) {
			throw new Exception("Cannot create XML parser", pce);
		} catch (IOException ioe) {
			throw new Exception(ioe);
		} catch (SAXException se) {
			throw new Exception(se);
		}
	}

	/**
	* Print out any errors, if any, related to the deploy.
	* @param result - DeployResult
	*/
	private static String getErrors(DeployResult result, String messageHeader) {
		DeployDetails deployDetails = result.getDetails();
		StringBuilder errorMessageBuilder = new StringBuilder();
		if (deployDetails != null) {
			DeployMessage[] componentFailures = deployDetails.getComponentFailures();
			for (DeployMessage message : componentFailures) {
				String loc = (message.getLineNumber() == 0 ? "" : ("(" + message.getLineNumber() + "," + message.getColumnNumber() + ")"));
				if (loc.length() == 0 && !message.getFileName().equals(message.getFullName())) {
					loc = "(" + message.getFullName() + ")";
				}
				errorMessageBuilder.append(message.getFileName() + loc + ":" + message.getProblem()).append('\n');
			}
			RunTestsResult rtr = deployDetails.getRunTestResult();
			if (rtr.getFailures() != null) {
				for (RunTestFailure failure : rtr.getFailures()) {
					String n = (failure.getNamespace() == null ? "" : (failure.getNamespace() + ".")) + failure.getName();
					errorMessageBuilder.append("Test failure, method: " + n + "." + failure.getMethodName() + " -- " + failure.getMessage()
							+ " stack " + failure.getStackTrace() + "\n\n");
				}
			}
			if (rtr.getCodeCoverageWarnings() != null) {
				for (CodeCoverageWarning ccw : rtr.getCodeCoverageWarnings()) {
					errorMessageBuilder.append("Code coverage issue");
					if (ccw.getName() != null) {
						String n = (ccw.getNamespace() == null ? "" : (ccw.getNamespace() + ".")) + ccw.getName();
						errorMessageBuilder.append(", class: " + n);
					}
					errorMessageBuilder.append(" -- " + ccw.getMessage() + "\n");
				}
			}
		}
		if (errorMessageBuilder.length() > 0) {
			errorMessageBuilder.insert(0, messageHeader);
			return errorMessageBuilder.toString();
		} else {
			return StringUtils.EMPTY;
		}
	}

    public static Metadata[] getMetadataObjects(MetadataType metadataType, List<Map<String, Object>> objects) throws Exception {
    	
    	BeanUtilsBean beanUtilsBean = new BeanUtilsBean(new ConvertUtilsBean() {
			@Override
			public Object convert(String value, Class clazz) {
				if (clazz.isEnum()) {
					return Enum.valueOf(clazz, value);
				} 
				else {
					return super.convert(value, clazz);
				}
			}
		});
    	
        Metadata[] mobjects = new Metadata[objects.size()];
        int s = 0;
        for (Map<String, Object> map : objects) {
        	mobjects[s] = toMetadataObject(beanUtilsBean, metadataType, map);
            s++;
        }
        return mobjects;
    }
    
	private static <T> Metadata toMetadataObject(BeanUtilsBean beanUtilsBean, MetadataType metadataType, Map<String, Object> map) throws Exception {
    	Metadata metadataObject = metadataType.getMetadataObject();
        for (Map.Entry<String, Object> entry : map.entrySet()) {
           String key = entry.getKey();
           
           // if the object is a map, transform to object
           if (entry.getValue() instanceof Map) {
        	   PropertyUtilsBean propertyUtilsBean = new PropertyUtilsBean();
        	   Class childClass = propertyUtilsBean.getPropertyType(metadataObject, key);
        	   MetadataType childType = MetadataType.getByClass(childClass);
        	   if (childType != null){
        	   		beanUtilsBean.setProperty(metadataObject, key, toMetadataObject(beanUtilsBean, childType, toMObjectMap((Map) entry.getValue())));
        	   }
           }
           
           //if the object is a list, transform to array
           else if (entry.getValue() instanceof List) {
        	   List<Object> objectsList = (List) entry.getValue();
        	   Metadata[] mobjects = new Metadata[objectsList.size()];
        	   int s=0;
        	   Class metadataClass = null;
        	   for (Object objectEntry : objectsList ) {
        		   if (objectEntry instanceof Map) {
        			   PropertyUtilsBean propertyUtilsBean = new PropertyUtilsBean();
                	   Class childClass = propertyUtilsBean.getPropertyType(metadataObject, key);
                	   if (childClass != null && childClass.isArray()) {
                		   childClass = childClass.getComponentType();
                	   }
                	   if (metadataClass == null) {
                		   metadataClass = childClass;
                	   }
                	   MetadataType childType = MetadataType.getByClass(childClass);
                	   mobjects[s] = toMetadataObject(beanUtilsBean, childType, toMObjectMap((Map) objectEntry));
                	   s++;
        		   }
        	   }
        	   if (metadataClass != null) {
        		   Object[] metadataChildObject = (T[]) Array.newInstance(metadataClass, mobjects.length);
        		   for (int i=0;i<mobjects.length;i++) {
        			   metadataChildObject[i] = metadataClass.cast(mobjects[i]);
        		   }
        		   beanUtilsBean.setProperty(metadataObject, key, metadataChildObject);
        	   }
           }
            else {
            	beanUtilsBean.setProperty(metadataObject, key, entry.getValue());
           }
        }
        return metadataObject;
    }
    
    public static Map<String, Object> toMObjectMap(Map<Object, Object> map) {
        Map<String, Object> mObjectMap = new HashMap<String, Object>();
        for (Map.Entry<Object, Object> entry : map.entrySet()) {
        	mObjectMap.put(entry.getKey().toString(), entry.getValue());
        }
        return mObjectMap;
    }
}
