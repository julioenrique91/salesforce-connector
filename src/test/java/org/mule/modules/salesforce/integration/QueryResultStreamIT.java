/**
 * Mule Salesforce Connector
 * 
 * Copyright (c) MuleSoft, Inc. All rights reserved. http://www.mulesoft.com
 * 
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */

package org.mule.modules.salesforce.integration;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import junit.framework.Assert;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Before;
import org.junit.Test;
import org.mule.api.ConnectionException;
import org.mule.modules.salesforce.SalesforceConnector;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

import com.sforce.async.BatchInfo;
import com.sforce.async.BatchStateEnum;
import com.sforce.async.JobInfo;
import com.sforce.async.OperationEnum;

public class QueryResultStreamIT {

	static final Log logger = LogFactory.getLog(QueryResultStreamIT.class);
	static final private String ENTITY_ID = "Contact";
	// static final private String QUERY = "SELECT FirstName, LastName FROM " + ENTITY_ID;
	static final private String QUERY = "SELECT Id, AccountId, AssistantName, AssistantPhone, Birthdate, Boolean_Test_Field__c, Fax, Phone, Description, CreatedById, CreatedDate, Currency_Test_Field__c, Jigsaw, Date_Custom_Field__c, DateTime_Test_Field__c, IsDeleted, Department, Email, EmailBouncedDate, EmailBouncedReason, email_custom__c, FirstName, Name, HomePhone, Int_Test_Field_c__c, JigsawContactId, LastActivityDate, LastModifiedById, LastModifiedDate, LastName, LastCURequestDate, LastCUUpdateDate, LeadSource, MailingCity, MailingCountry, MailingState, MailingStreet, MailingPostalCode, MasterRecordId, MobilePhone, MultiPicklist_Test_Field__c, MyExternalFieldId__c, Nicolas_Custom_Field__c, Number_Test_Field__c, OtherCity, OtherCountry, OtherPhone, OtherState, OtherStreet, OtherPostalCode, OwnerId, Percent_Test_Field__c, Picklist_Test_Field__c, ReportsToId, Salutation, SystemModstamp, Title, URL_Test_Field__c FROM Contact";
	// static final private String QUERY_MOCK =
	// "<?xml version=\"1.0\" encoding=\"UTF-8\"?><queryResult xmlns=\"http://www.force.com/2009/06/asyncapi/dataload\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"><records xsi:type=\"sObject\"><type>Contact</type><Id xsi:nil=\"true\"/><FirstName>Jtest 1</FirstName><LastName>Lona</LastName></records><records xsi:type=\"sObject\"><type>Contact</type><Id xsi:nil=\"true\"/><FirstName>Jtest 3</FirstName><LastName>Lina SFDL 1</LastName></records><records xsi:type=\"sObject\"><type>Contact</type><Id xsi:nil=\"true\"/><FirstName>Jtest 4</FirstName><LastName>Lina SFDL 2</LastName></records><records xsi:type=\"sObject\"><type>Contact</type><Id xsi:nil=\"true\"/><FirstName>Jtest 5</FirstName><LastName>Lina SFDL 3</LastName></records><records xsi:type=\"sObject\"><type>Contact</type><Id xsi:nil=\"true\"/><FirstName>Jtest 7</FirstName><LastName>Lina SFDL 5</LastName></records><records xsi:type=\"sObject\"><type>Contact</type><Id xsi:nil=\"true\"/><FirstName>Jtest 8</FirstName><LastName>Lina SFDL 6</LastName></records><records xsi:type=\"sObject\"><type>Contact</type><Id xsi:nil=\"true\"/><FirstName>Jtest 10</FirstName><LastName>Lina SFDL 8</LastName></records><records xsi:type=\"sObject\"><type>Contact</type><Id xsi:nil=\"true\"/><FirstName>Jtest 1</FirstName><LastName>Lona</LastName></records><records xsi:type=\"sObject\"><type>Contact</type><Id xsi:nil=\"true\"/><FirstName>Jtest 3</FirstName><LastName>Lina SFDL 1</LastName></records><records xsi:type=\"sObject\"><type>Contact</type><Id xsi:nil=\"true\"/><FirstName>Jtest 4</FirstName><LastName>Lina SFDL 2</LastName></records><records xsi:type=\"sObject\"><type>Contact</type><Id xsi:nil=\"true\"/><FirstName>Jtest 5</FirstName><LastName>Lina SFDL 3</LastName></records><records xsi:type=\"sObject\"><type>Contact</type><Id xsi:nil=\"true\"/><FirstName>Jtest 7</FirstName><LastName>Lina SFDL 5</LastName></records><records xsi:type=\"sObject\"><type>Contact</type><Id xsi:nil=\"true\"/><FirstName>Jtest 8</FirstName><LastName>Lina SFDL 6</LastName></records><records xsi:type=\"sObject\"><type>Contact</type><Id xsi:nil=\"true\"/><FirstName>Jtest 10</FirstName><LastName>Lina SFDL 8</LastName></records><records xsi:type=\"sObject\"><type>Contact</type><Id xsi:nil=\"true\"/><FirstName>Jtest 1</FirstName><LastName>Lona</LastName></records><records xsi:type=\"sObject\"><type>Contact</type><Id xsi:nil=\"true\"/><FirstName>Jtest 3</FirstName><LastName>Lina SFDL 1</LastName></records><records xsi:type=\"sObject\"><type>Contact</type><Id xsi:nil=\"true\"/><FirstName>Jtest 4</FirstName><LastName>Lina SFDL 2</LastName></records><records xsi:type=\"sObject\"><type>Contact</type><Id xsi:nil=\"true\"/><FirstName>Jtest 5</FirstName><LastName>Lina SFDL 3</LastName></records><records xsi:type=\"sObject\"><type>Contact</type><Id xsi:nil=\"true\"/><FirstName>Jtest 7</FirstName><LastName>Lina SFDL 5</LastName></records><records xsi:type=\"sObject\"><type>Contact</type><Id xsi:nil=\"true\"/><FirstName>Jtest 8</FirstName><LastName>Lina SFDL 6</LastName></records><records xsi:type=\"sObject\"><type>Contact</type><Id xsi:nil=\"true\"/><FirstName>Jtest 10</FirstName><LastName>Lina SFDL 8</LastName></records><records xsi:type=\"sObject\"><type>Contact</type><Id xsi:nil=\"true\"/><FirstName>Jtest 1</FirstName><LastName>Lona</LastName></records><records xsi:type=\"sObject\"><type>Contact</type><Id xsi:nil=\"true\"/><FirstName>Jtest 3</FirstName><LastName>Lina SFDL 1</LastName></records><records xsi:type=\"sObject\"><type>Contact</type><Id xsi:nil=\"true\"/><FirstName>Jtest 4</FirstName><LastName>Lina SFDL 2</LastName></records><records xsi:type=\"sObject\"><type>Contact</type><Id xsi:nil=\"true\"/><FirstName>Jtest 5</FirstName><LastName>Lina SFDL 3</LastName></records><records xsi:type=\"sObject\"><type>Contact</type><Id xsi:nil=\"true\"/><FirstName>Jtest 7</FirstName><LastName>Lina SFDL 5</LastName></records><records xsi:type=\"sObject\"><type>Contact</type><Id xsi:nil=\"true\"/><FirstName>Jtest 8</FirstName><LastName>Lina SFDL 6</LastName></records><records xsi:type=\"sObject\"><type>Contact</type><Id xsi:nil=\"true\"/><FirstName>Jtest 10</FirstName><LastName>Lina SFDL 8</LastName></records><records xsi:type=\"sObject\"><type>Contact</type><Id xsi:nil=\"true\"/><FirstName>Jtest 1</FirstName><LastName>Lona</LastName></records><records xsi:type=\"sObject\"><type>Contact</type><Id xsi:nil=\"true\"/><FirstName>Jtest 3</FirstName><LastName>Lina SFDL 1</LastName></records><records xsi:type=\"sObject\"><type>Contact</type><Id xsi:nil=\"true\"/><FirstName>Jtest 4</FirstName><LastName>Lina SFDL 2</LastName></records><records xsi:type=\"sObject\"><type>Contact</type><Id xsi:nil=\"true\"/><FirstName>Jtest 5</FirstName><LastName>Lina SFDL 3</LastName></records><records xsi:type=\"sObject\"><type>Contact</type><Id xsi:nil=\"true\"/><FirstName>Jtest 7</FirstName><LastName>Lina SFDL 5</LastName></records><records xsi:type=\"sObject\"><type>Contact</type><Id xsi:nil=\"true\"/><FirstName>Jtest 8</FirstName><LastName>Lina SFDL 6</LastName></records><records xsi:type=\"sObject\"><type>Contact</type><Id xsi:nil=\"true\"/><FirstName>Jtest 10</FirstName><LastName>Lina SFDL 8</LastName></records><records xsi:type=\"sObject\"><type>Contact</type><Id xsi:nil=\"true\"/><FirstName>Jtest 1</FirstName><LastName>Lona</LastName></records><records xsi:type=\"sObject\"><type>Contact</type><Id xsi:nil=\"true\"/><FirstName>Jtest 3</FirstName><LastName>Lina SFDL 1</LastName></records><records xsi:type=\"sObject\"><type>Contact</type><Id xsi:nil=\"true\"/><FirstName>Jtest 4</FirstName><LastName>Lina SFDL 2</LastName></records><records xsi:type=\"sObject\"><type>Contact</type><Id xsi:nil=\"true\"/><FirstName>Jtest 5</FirstName><LastName>Lina SFDL 3</LastName></records><records xsi:type=\"sObject\"><type>Contact</type><Id xsi:nil=\"true\"/><FirstName>Jtest 7</FirstName><LastName>Lina SFDL 5</LastName></records><records xsi:type=\"sObject\"><type>Contact</type><Id xsi:nil=\"true\"/><FirstName>Jtest 8</FirstName><LastName>Lina SFDL 6</LastName></records><records xsi:type=\"sObject\"><type>Contact</type><Id xsi:nil=\"true\"/><FirstName>Jtest 10</FirstName><LastName>Lina SFDL 8</LastName></records><records xsi:type=\"sObject\"><type>Contact</type><Id xsi:nil=\"true\"/><FirstName>Jtest 1</FirstName><LastName>Lona</LastName></records><records xsi:type=\"sObject\"><type>Contact</type><Id xsi:nil=\"true\"/><FirstName>Jtest 3</FirstName><LastName>Lina SFDL 1</LastName></records><records xsi:type=\"sObject\"><type>Contact</type><Id xsi:nil=\"true\"/><FirstName>Jtest 4</FirstName><LastName>Lina SFDL 2</LastName></records><records xsi:type=\"sObject\"><type>Contact</type><Id xsi:nil=\"true\"/><FirstName>Jtest 5</FirstName><LastName>Lina SFDL 3</LastName></records><records xsi:type=\"sObject\"><type>Contact</type><Id xsi:nil=\"true\"/><FirstName>Jtest 7</FirstName><LastName>Lina SFDL 5</LastName></records><records xsi:type=\"sObject\"><type>Contact</type><Id xsi:nil=\"true\"/><FirstName>Jtest 8</FirstName><LastName>Lina SFDL 6</LastName></records><records xsi:type=\"sObject\"><type>Contact</type><Id xsi:nil=\"true\"/><FirstName>Jtest 10</FirstName><LastName>Lina SFDL 8</LastName></records><records xsi:type=\"sObject\"><type>Contact</type><Id xsi:nil=\"true\"/><FirstName>Jtest 1</FirstName><LastName>Lona</LastName></records><records xsi:type=\"sObject\"><type>Contact</type><Id xsi:nil=\"true\"/><FirstName>Jtest 3</FirstName><LastName>Lina SFDL 1</LastName></records><records xsi:type=\"sObject\"><type>Contact</type><Id xsi:nil=\"true\"/><FirstName>Jtest 4</FirstName><LastName>Lina SFDL 2</LastName></records><records xsi:type=\"sObject\"><type>Contact</type><Id xsi:nil=\"true\"/><FirstName>Jtest 5</FirstName><LastName>Lina SFDL 3</LastName></records><records xsi:type=\"sObject\"><type>Contact</type><Id xsi:nil=\"true\"/><FirstName>Jtest 7</FirstName><LastName>Lina SFDL 5</LastName></records><records xsi:type=\"sObject\"><type>Contact</type><Id xsi:nil=\"true\"/><FirstName>Jtest 8</FirstName><LastName>Lina SFDL 6</LastName></records><records xsi:type=\"sObject\"><type>Contact</type><Id xsi:nil=\"true\"/><FirstName>Jtest 10</FirstName><LastName>Lina SFDL 8</LastName></records><records xsi:type=\"sObject\"><type>Contact</type><Id xsi:nil=\"true\"/><FirstName>Jtest 1</FirstName><LastName>Lona</LastName></records><records xsi:type=\"sObject\"><type>Contact</type><Id xsi:nil=\"true\"/><FirstName>Jtest 3</FirstName><LastName>Lina SFDL 1</LastName></records><records xsi:type=\"sObject\"><type>Contact</type><Id xsi:nil=\"true\"/><FirstName>Jtest 4</FirstName><LastName>Lina SFDL 2</LastName></records><records xsi:type=\"sObject\"><type>Contact</type><Id xsi:nil=\"true\"/><FirstName>Jtest 5</FirstName><LastName>Lina SFDL 3</LastName></records><records xsi:type=\"sObject\"><type>Contact</type><Id xsi:nil=\"true\"/><FirstName>Jtest 7</FirstName><LastName>Lina SFDL 5</LastName></records><records xsi:type=\"sObject\"><type>Contact</type><Id xsi:nil=\"true\"/><FirstName>Jtest 8</FirstName><LastName>Lina SFDL 6</LastName></records><records xsi:type=\"sObject\"><type>Contact</type><Id xsi:nil=\"true\"/><FirstName>Jtest 10</FirstName><LastName>Lina SFDL 8</LastName></records><records xsi:type=\"sObject\"><type>Contact</type><Id xsi:nil=\"true\"/><FirstName>Jtest 1</FirstName><LastName>Lona</LastName></records><records xsi:type=\"sObject\"><type>Contact</type><Id xsi:nil=\"true\"/><FirstName>Jtest 3</FirstName><LastName>Lina SFDL 1</LastName></records><records xsi:type=\"sObject\"><type>Contact</type><Id xsi:nil=\"true\"/><FirstName>Jtest 4</FirstName><LastName>Lina SFDL 2</LastName></records><records xsi:type=\"sObject\"><type>Contact</type><Id xsi:nil=\"true\"/><FirstName>Jtest 5</FirstName><LastName>Lina SFDL 3</LastName></records><records xsi:type=\"sObject\"><type>Contact</type><Id xsi:nil=\"true\"/><FirstName>Jtest 7</FirstName><LastName>Lina SFDL 5</LastName></records><records xsi:type=\"sObject\"><type>Contact</type><Id xsi:nil=\"true\"/><FirstName>Jtest 8</FirstName><LastName>Lina SFDL 6</LastName></records><records xsi:type=\"sObject\"><type>Contact</type><Id xsi:nil=\"true\"/><FirstName>Jtest 7</FirstName><LastName>Lina SFDL 5</LastName></records><records xsi:type=\"sObject\"><type>Contact</type><Id xsi:nil=\"true\"/><FirstName>Jtest 8</FirstName><LastName>Lina SFDL 6</LastName></records><records xsi:type=\"sObject\"><type>Contact</type><Id xsi:nil=\"true\"/><FirstName>Jtest 10</FirstName><LastName>Lina SFDL 8</LastName></records><records xsi:type=\"sObject\"><type>Contact</type><Id xsi:nil=\"true\"/><FirstName>Jtest 10</FirstName><LastName>Lina SFDL 8</LastName></records><records xsi:type=\"sObject\"><type>Contact</type><Id xsi:nil=\"true\"/><FirstName>Jtest 1</FirstName><LastName>Lona</LastName></records><records xsi:type=\"sObject\"><type>Contact</type><Id xsi:nil=\"true\"/><FirstName>Jtest 3</FirstName><LastName>Lina SFDL 1</LastName></records><records xsi:type=\"sObject\"><type>Contact</type><Id xsi:nil=\"true\"/><FirstName>Jtest 4</FirstName><LastName>Lina SFDL 2</LastName></records><records xsi:type=\"sObject\"><type>Contact</type><Id xsi:nil=\"true\"/><FirstName>Jtest 5</FirstName><LastName>Lina SFDL 3</LastName></records><records xsi:type=\"sObject\"><type>Contact</type><Id xsi:nil=\"true\"/><FirstName>Jtest 1</FirstName><LastName>Lona</LastName></records><records xsi:type=\"sObject\"><type>Contact</type><Id xsi:nil=\"true\"/><FirstName>Jtest 3</FirstName><LastName>Lina SFDL 1</LastName></records><records xsi:type=\"sObject\"><type>Contact</type><Id xsi:nil=\"true\"/><FirstName>Jtest 4</FirstName><LastName>Lina SFDL 2</LastName></records><records xsi:type=\"sObject\"><type>Contact</type><Id xsi:nil=\"true\"/><FirstName>Jtest 5</FirstName><LastName>Lina SFDL 3</LastName></records><records xsi:type=\"sObject\"><type>Contact</type><Id xsi:nil=\"true\"/><FirstName>Jtest 7</FirstName><LastName>Lina SFDL 5</LastName></records><records xsi:type=\"sObject\"><type>Contact</type><Id xsi:nil=\"true\"/><FirstName>Jtest 8</FirstName><LastName>Lina SFDL 6</LastName></records><records xsi:type=\"sObject\"><type>Contact</type><Id xsi:nil=\"true\"/><FirstName>Jtest 10</FirstName><LastName>Lina SFDL 8</LastName></records><records xsi:type=\"sObject\"><type>Contact</type><Id xsi:nil=\"true\"/><FirstName>Jtest 1</FirstName><LastName>Lona</LastName></records><records xsi:type=\"sObject\"><type>Contact</type><Id xsi:nil=\"true\"/><FirstName>Jtest 3</FirstName><LastName>Lina SFDL 1</LastName></records><records xsi:type=\"sObject\"><type>Contact</type><Id xsi:nil=\"true\"/><FirstName>Jtest 4</FirstName><LastName>Lina SFDL 2</LastName></records><records xsi:type=\"sObject\"><type>Contact</type><Id xsi:nil=\"true\"/><FirstName>Jtest 5</FirstName><LastName>Lina SFDL 3</LastName></records><records xsi:type=\"sObject\"><type>Contact</type><Id xsi:nil=\"true\"/><FirstName>Jtest 7</FirstName><LastName>Lina SFDL 5</LastName></records><records xsi:type=\"sObject\"><type>Contact</type><Id xsi:nil=\"true\"/><FirstName>Jtest 8</FirstName><LastName>Lina SFDL 6</LastName></records><records xsi:type=\"sObject\"><type>Contact</type><Id xsi:nil=\"true\"/><FirstName>Jtest 10</FirstName><LastName>Lina SFDL 8</LastName></records><records xsi:type=\"sObject\"><type>Contact</type><Id xsi:nil=\"true\"/><FirstName>Jtest 1</FirstName><LastName>Lona</LastName></records><records xsi:type=\"sObject\"><type>Contact</type><Id xsi:nil=\"true\"/><FirstName>Jtest 3</FirstName><LastName>Lina SFDL 1</LastName></records><records xsi:type=\"sObject\"><type>Contact</type><Id xsi:nil=\"true\"/><FirstName>Jtest 4</FirstName><LastName>Lina SFDL 2</LastName></records><records xsi:type=\"sObject\"><type>Contact</type><Id xsi:nil=\"true\"/><FirstName>Jtest 5</FirstName><LastName>Lina SFDL 3</LastName></records><records xsi:type=\"sObject\"><type>Contact</type><Id xsi:nil=\"true\"/><FirstName>Jtest 7</FirstName><LastName>Lina SFDL 5</LastName></records><records xsi:type=\"sObject\"><type>Contact</type><Id xsi:nil=\"true\"/><FirstName>Jtest 8</FirstName><LastName>Lina SFDL 6</LastName></records><records xsi:type=\"sObject\"><type>Contact</type><Id xsi:nil=\"true\"/><FirstName>Jtest 10</FirstName><LastName>Lina SFDL 8</LastName></records><records xsi:type=\"sObject\"><type>Contact</type><Id xsi:nil=\"true\"/><FirstName>Jtest 4</FirstName><LastName>Lina SFDL 2</LastName></records><records xsi:type=\"sObject\"><type>Contact</type><Id xsi:nil=\"true\"/><FirstName>Jtest 5</FirstName><LastName>Lina SFDL 3</LastName></records><records xsi:type=\"sObject\"><type>Contact</type><Id xsi:nil=\"true\"/><FirstName>Jtest 7</FirstName><LastName>Lina SFDL 5</LastName></records><records xsi:type=\"sObject\"><type>Contact</type><Id xsi:nil=\"true\"/><FirstName>Jtest 8</FirstName><LastName>Lina SFDL 6</LastName></records><records xsi:type=\"sObject\"><type>Contact</type><Id xsi:nil=\"true\"/><FirstName>Jtest 10</FirstName><LastName>Lina SFDL 8</LastName></records><records xsi:type=\"sObject\"><type>Contact</type><Id xsi:nil=\"true\"/><FirstName>Jtest 1</FirstName><LastName>Lona</LastName></records><records xsi:type=\"sObject\"><type>Contact</type><Id xsi:nil=\"true\"/><FirstName>Jtest 3</FirstName><LastName>Lina SFDL 1</LastName></records><records xsi:type=\"sObject\"><type>Contact</type><Id xsi:nil=\"true\"/><FirstName>Jtest 4</FirstName><LastName>Lina SFDL 2</LastName></records><records xsi:type=\"sObject\"><type>Contact</type><Id xsi:nil=\"true\"/><FirstName>Jtest 5</FirstName><LastName>Lina SFDL 3</LastName></records><records xsi:type=\"sObject\"><type>Contact</type><Id xsi:nil=\"true\"/><FirstName>Jtest 7</FirstName><LastName>Lina SFDL 5</LastName></records><records xsi:type=\"sObject\"><type>Contact</type><Id xsi:nil=\"true\"/><FirstName>Jtest 8</FirstName><LastName>Lina SFDL 6</LastName></records><records xsi:type=\"sObject\"><type>Contact</type><Id xsi:nil=\"true\"/><FirstName>Jtest 10</FirstName><LastName>Lina SFDL 8</LastName></records><records xsi:type=\"sObject\"><type>Contact</type><Id xsi:nil=\"true\"/><FirstName>Jtest 1</FirstName><LastName>Lona</LastName></records><records xsi:type=\"sObject\"><type>Contact</type><Id xsi:nil=\"true\"/><FirstName>Jtest 3</FirstName><LastName>Lina SFDL 1</LastName></records><records xsi:type=\"sObject\"><type>Contact</type><Id xsi:nil=\"true\"/><FirstName>Jtest 1</FirstName><LastName>Lona</LastName></records><records xsi:type=\"sObject\"><type>Contact</type><Id xsi:nil=\"true\"/><FirstName>Jtest 3</FirstName><LastName>Lina SFDL 1</LastName></records><records xsi:type=\"sObject\"><type>Contact</type><Id xsi:nil=\"true\"/><FirstName>Jtest 4</FirstName><LastName>Lina SFDL 2</LastName></records><records xsi:type=\"sObject\"><type>Contact</type><Id xsi:nil=\"true\"/><FirstName>Jtest 5</FirstName><LastName>Lina SFDL 3</LastName></records><records xsi:type=\"sObject\"><type>Contact</type><Id xsi:nil=\"true\"/><FirstName>Jtest 7</FirstName><LastName>Lina SFDL 5</LastName></records><records xsi:type=\"sObject\"><type>Contact</type><Id xsi:nil=\"true\"/><FirstName>Jtest 8</FirstName><LastName>Lina SFDL 6</LastName></records><records xsi:type=\"sObject\"><type>Contact</type><Id xsi:nil=\"true\"/><FirstName>Jtest 10</FirstName><LastName>Lina SFDL 8</LastName></records><records xsi:type=\"sObject\"><type>Contact</type><Id xsi:nil=\"true\"/><FirstName>Jtest 1</FirstName><LastName>Lona</LastName></records><records xsi:type=\"sObject\"><type>Contact</type><Id xsi:nil=\"true\"/><FirstName>Jtest 3</FirstName><LastName>Lina SFDL 1</LastName></records><records xsi:type=\"sObject\"><type>Contact</type><Id xsi:nil=\"true\"/><FirstName>Jtest 4</FirstName><LastName>Lina SFDL 2</LastName></records><records xsi:type=\"sObject\"><type>Contact</type><Id xsi:nil=\"true\"/><FirstName>Jtest 5</FirstName><LastName>Lina SFDL 3</LastName></records><records xsi:type=\"sObject\"><type>Contact</type><Id xsi:nil=\"true\"/><FirstName>Jtest 7</FirstName><LastName>Lina SFDL 5</LastName></records><records xsi:type=\"sObject\"><type>Contact</type><Id xsi:nil=\"true\"/><FirstName>Jtest 8</FirstName><LastName>Lina SFDL 6</LastName></records><records xsi:type=\"sObject\"><type>Contact</type><Id xsi:nil=\"true\"/><FirstName>Jtest 10</FirstName><LastName>Lina SFDL 8</LastName></records><records xsi:type=\"sObject\"><type>Contact</type><Id xsi:nil=\"true\"/><FirstName>Jtest 1</FirstName><LastName>Lona</LastName></records><records xsi:type=\"sObject\"><type>Contact</type><Id xsi:nil=\"true\"/><FirstName>Jtest 3</FirstName><LastName>Lina SFDL 1</LastName></records><records xsi:type=\"sObject\"><type>Contact</type><Id xsi:nil=\"true\"/><FirstName>Jtest 4</FirstName><LastName>Lina SFDL 2</LastName></records><records xsi:type=\"sObject\"><type>Contact</type><Id xsi:nil=\"true\"/><FirstName>Jtest 5</FirstName><LastName>Lina SFDL 3</LastName></records><records xsi:type=\"sObject\"><type>Contact</type><Id xsi:nil=\"true\"/><FirstName>Jtest 7</FirstName><LastName>Lina SFDL 5</LastName></records><records xsi:type=\"sObject\"><type>Contact</type><Id xsi:nil=\"true\"/><FirstName>Jtest 8</FirstName><LastName>Lina SFDL 6</LastName></records><records xsi:type=\"sObject\"><type>Contact</type><Id xsi:nil=\"true\"/><FirstName>Jtest 10</FirstName><LastName>Lina SFDL 8</LastName></records><records xsi:type=\"sObject\"><type>Contact</type><Id xsi:nil=\"true\"/><FirstName>Jtest 1</FirstName><LastName>Lona</LastName></records><records xsi:type=\"sObject\"><type>Contact</type><Id xsi:nil=\"true\"/><FirstName>Jtest 3</FirstName><LastName>Lina SFDL 1</LastName></records><records xsi:type=\"sObject\"><type>Contact</type><Id xsi:nil=\"true\"/><FirstName>Jtest 4</FirstName><LastName>Lina SFDL 2</LastName></records><records xsi:type=\"sObject\"><type>Contact</type><Id xsi:nil=\"true\"/><FirstName>Jtest 5</FirstName><LastName>Lina SFDL 3</LastName></records><records xsi:type=\"sObject\"><type>Contact</type><Id xsi:nil=\"true\"/><FirstName>Jtest 7</FirstName><LastName>Lina SFDL 5</LastName></records><records xsi:type=\"sObject\"><type>Contact</type><Id xsi:nil=\"true\"/><FirstName>Jtest 8</FirstName><LastName>Lina SFDL 6</LastName></records><records xsi:type=\"sObject\"><type>Contact</type><Id xsi:nil=\"true\"/><FirstName>Jtest 10</FirstName><LastName>Lina SFDL 8</LastName></records><records xsi:type=\"sObject\"><type>Contact</type><Id xsi:nil=\"true\"/><FirstName>fsdds</FirstName><LastName>vffv</LastName></records><records xsi:type=\"sObject\"><type>Contact</type><Id xsi:nil=\"true\"/><FirstName>fdsa</FirstName><LastName>df</LastName></records><records xsi:type=\"sObject\"><type>Contact</type><Id xsi:nil=\"true\"/><FirstName>Holanombre1</FirstName><LastName>LastNUpdated1</LastName></records><records xsi:type=\"sObject\"><type>Contact</type><Id xsi:nil=\"true\"/><FirstName>name</FirstName><LastName>lname</LastName></records><records xsi:type=\"sObject\"><type>Contact</type><Id xsi:nil=\"true\"/><FirstName>name</FirstName><LastName>lname</LastName></records><records xsi:type=\"sObject\"><type>Contact</type><Id xsi:nil=\"true\"/><FirstName>name</FirstName><LastName>lname</LastName></records><records xsi:type=\"sObject\"><type>Contact</type><Id xsi:nil=\"true\"/><FirstName>name</FirstName><LastName>lname</LastName></records><records xsi:type=\"sObject\"><type>Contact</type><Id xsi:nil=\"true\"/><FirstName xsi:nil=\"true\"/><LastName>Apellido</LastName></records><records xsi:type=\"sObject\"><type>Contact</type><Id xsi:nil=\"true\"/><FirstName>name</FirstName><LastName>lname</LastName></records><records xsi:type=\"sObject\"><type>Contact</type><Id xsi:nil=\"true\"/><FirstName xsi:nil=\"true\"/><LastName>Hola4Lastname</LastName></records><records xsi:type=\"sObject\"><type>Contact</type><Id xsi:nil=\"true\"/><FirstName xsi:nil=\"true\"/><LastName>Hola5Lastname</LastName></records><records xsi:type=\"sObject\"><type>Contact</type><Id xsi:nil=\"true\"/><FirstName xsi:nil=\"true\"/><LastName>Hola6Lastname</LastName></records><records xsi:type=\"sObject\"><type>Contact</type><Id xsi:nil=\"true\"/><FirstName>First Name</FirstName><LastName>lastname</LastName></records><records xsi:type=\"sObject\"><type>Contact</type><Id xsi:nil=\"true\"/><FirstName>First Name</FirstName><LastName>lastname</LastName></records><records xsi:type=\"sObject\"><type>Contact</type><Id xsi:nil=\"true\"/><FirstName>First Name</FirstName><LastName>lastname</LastName></records><records xsi:type=\"sObject\"><type>Contact</type><Id xsi:nil=\"true\"/><FirstName>First Name</FirstName><LastName>lastname</LastName></records><records xsi:type=\"sObject\"><type>Contact</type><Id xsi:nil=\"true\"/><FirstName>First Name</FirstName><LastName>lastname</LastName></records><records xsi:type=\"sObject\"><type>Contact</type><Id xsi:nil=\"true\"/><FirstName>First Name</FirstName><LastName>lastname</LastName></records><records xsi:type=\"sObject\"><type>Contact</type><Id xsi:nil=\"true\"/><FirstName>First Name</FirstName><LastName>lastname</LastName></records><records xsi:type=\"sObject\"><type>Contact</type><Id xsi:nil=\"true\"/><FirstName>First Name</FirstName><LastName>lastname</LastName></records><records xsi:type=\"sObject\"><type>Contact</type><Id xsi:nil=\"true\"/><FirstName>First Name</FirstName><LastName>lastname</LastName></records><records xsi:type=\"sObject\"><type>Contact</type><Id xsi:nil=\"true\"/><FirstName>First Name</FirstName><LastName>lastname</LastName></records><records xsi:type=\"sObject\"><type>Contact</type><Id xsi:nil=\"true\"/><FirstName>First Name</FirstName><LastName>lastname</LastName></records><records xsi:type=\"sObject\"><type>Contact</type><Id xsi:nil=\"true\"/><FirstName xsi:nil=\"true\"/><LastName>Hola1Lastname</LastName></records><records xsi:type=\"sObject\"><type>Contact</type><Id xsi:nil=\"true\"/><FirstName xsi:nil=\"true\"/><LastName>Hola2Lastname</LastName></records><records xsi:type=\"sObject\"><type>Contact</type><Id xsi:nil=\"true\"/><FirstName xsi:nil=\"true\"/><LastName>Hola3Lastname</LastName></records><records xsi:type=\"sObject\"><type>Contact</type><Id xsi:nil=\"true\"/><FirstName xsi:nil=\"true\"/><LastName>Hola4Lastname</LastName></records><records xsi:type=\"sObject\"><type>Contact</type><Id xsi:nil=\"true\"/><FirstName xsi:nil=\"true\"/><LastName>Hola5Lastname</LastName></records><records xsi:type=\"sObject\"><type>Contact</type><Id xsi:nil=\"true\"/><FirstName xsi:nil=\"true\"/><LastName>Hola6Lastname</LastName></records><records xsi:type=\"sObject\"><type>Contact</type><Id xsi:nil=\"true\"/><FirstName xsi:nil=\"true\"/><LastName>Hola1</LastName></records><records xsi:type=\"sObject\"><type>Contact</type><Id xsi:nil=\"true\"/><FirstName xsi:nil=\"true\"/><LastName>Hola2</LastName></records><records xsi:type=\"sObject\"><type>Contact</type><Id xsi:nil=\"true\"/><FirstName>name</FirstName><LastName>lname</LastName></records><records xsi:type=\"sObject\"><type>Contact</type><Id xsi:nil=\"true\"/><FirstName xsi:nil=\"true\"/><LastName>Hola1</LastName></records><records xsi:type=\"sObject\"><type>Contact</type><Id xsi:nil=\"true\"/><FirstName xsi:nil=\"true\"/><LastName>Hola3</LastName></records><records xsi:type=\"sObject\"><type>Contact</type><Id xsi:nil=\"true\"/><FirstName xsi:nil=\"true\"/><LastName>Apellido</LastName></records><records xsi:type=\"sObject\"><type>Contact</type><Id xsi:nil=\"true\"/><FirstName>First Name</FirstName><LastName>lastname</LastName></records><records xsi:type=\"sObject\"><type>Contact</type><Id xsi:nil=\"true\"/><FirstName>name</FirstName><LastName>lname</LastName></records></queryResult>";

	static final private int BATCH_SLEEP_TIME_MS = 5000;

	private SalesforceConnector connector;
	private BatchInfo batchInfo = null;

	@Before
	public void initialize() throws IOException, ConnectionException {
		// Load the .properties
		Properties prop = new Properties();
		ClassLoader loader = Thread.currentThread().getContextClassLoader();
		InputStream stream = loader.getResourceAsStream("integration-credentials.properties");
		prop.load(stream);

		connector = new SalesforceConnector();
		connector.connect(prop.getProperty("salesforce.username"), prop.getProperty("salesforce.password"),
				prop.getProperty("salesforce.securityToken"), prop.getProperty("salesforce.url"), null, 80, null, null, null, null, 0, 0);

		if (!StringUtils.isEmpty(prop.getProperty("salesforce.jobid")) && !StringUtils.isEmpty(prop.getProperty("salesforce.batchid"))) {
			batchInfo = new BatchInfo();
			batchInfo.setJobId(prop.getProperty("salesforce.jobid"));
			batchInfo.setId(prop.getProperty("salesforce.batchid"));
		}
	}

	@Test
	public void executeJob() throws Exception {
		JobInfo jobInfo = null;

		if (batchInfo == null) {
			logger.debug("==== CREATING JOB ====");
			logger.debug("Entity: " + ENTITY_ID);
			// Create new Job - In this indicate the type of operation and the entity which will be
			// used
			jobInfo = connector.createJob(OperationEnum.query, ENTITY_ID, null, null, null);
			logger.debug("jobInfo: " + jobInfo.toString());

			logger.debug("==== CREATING BATCH ====");
			logger.debug("Query: " + QueryResultStreamIT.QUERY);
			// Create batch - This uses a job and in this case the query to execute
			batchInfo = connector.createBatchForQuery(jobInfo, QUERY);
			logger.debug("batchInfo: " + batchInfo.toString());
		} else {
			logger.debug("==== BATCH ALREADY CREATED ====");
			logger.debug("batchInfo: " + batchInfo.toString());
		}

		// Iterate through batch info until ready
		logger.debug("==== WAITING FOR BATCH COMPLETION ====");
		logger.debug("Sleep time: " + BATCH_SLEEP_TIME_MS + " ms.");
		do {
			// Wait some time until recheck job status
			Thread.sleep(BATCH_SLEEP_TIME_MS);
			batchInfo = connector.batchInfo(batchInfo);
			logger.debug("batchState: " + batchInfo.getState());
		} while (BatchStateEnum.InProgress.equals(batchInfo.getState()) || BatchStateEnum.Queued.equals(batchInfo.getState()));

		Assert.assertEquals(BatchStateEnum.Completed, batchInfo.getState());
		logger.debug("==== BATCH COMPLETED ====");

		logger.debug("=== ITERATING BATCH ====");
		// Return the query result stream
		
		InputStream inputStreamResult = connector.queryResultStream(batchInfo);

		// Sax parsing method... Memory usage improvement
		int bufferSize = 1024 * 1024; // 1MB
		byte[] buffer = new byte[bufferSize];
		
		long buffersReaded = 0;
		while (inputStreamResult.read(buffer) >= 0) {
			buffersReaded ++;
			if (buffersReaded % 10000 == 0) {
				logger.debug("Readed " + buffersReaded + " of " + bufferSize + " bytes each");
			}
		}
		
		logger.debug("==== ITERATION FINISHED ====");

		if (batchInfo == null) {
			logger.debug("==== CLOSING JOB ====");
			// Close the created job - This means that this job will not have more operations to
			// execute
			jobInfo = connector.closeJob(jobInfo.getId());
			logger.debug("jobInfo: " + jobInfo.toString());
		}

		logger.debug("==== THE END ====");
		// If we reach the end of the execution it means that the IT has been completed successfully
		// The error case is that in the second page of iteration the connector was throwing an
		// exception		

		Assert.assertTrue(true);

	}

	public int getAmountOfRecordsInXml(InputStream inputStream) throws ParserConfigurationException, SAXException, IOException {

		SAXParserFactory spf = SAXParserFactory.newInstance();
		SAXParser saxParser = spf.newSAXParser();
		XMLReader xmlReader = saxParser.getXMLReader();
		SaxParserCountRecords parser = new SaxParserCountRecords();
		xmlReader.setContentHandler(parser);
		xmlReader.parse(new InputSource(inputStream));

		return parser.getRecords();
	}

	private class SaxParserCountRecords extends DefaultHandler {

		private int records = 0;
		private long elements = 0;

		public int getRecords() {
			return records;
		}

		public void startElement(String namespaceURI, String localName, String qName, Attributes atts) throws SAXException {
			if (!StringUtils.isEmpty(qName)) {
				elements++;
				if (qName.equalsIgnoreCase("records")) {
					records++;
				}

				if (elements % 250 == 0) {
					logger.debug("Iterating throught element N. " + elements + "... Records found: " + records);
				}
			}
		}
	}
}