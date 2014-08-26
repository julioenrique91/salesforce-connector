/**
 * Mule Salesforce Connector
 *
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 *
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */

package org.mule.modules.salesforce.bulk;

import com.sforce.soap.partner.Error;
import com.sforce.soap.partner.UpsertResult;
import com.sforce.soap.partner.sobject.SObject;
import com.sforce.ws.ConnectionException;
import com.sforce.ws.bind.TypeMapper;
import com.sforce.ws.parser.XmlInputStream;
import com.sforce.ws.parser.XmlOutputStream;

import javax.xml.namespace.QName;
import java.io.IOException;

public class EnrichedUpsertResult extends UpsertResult {

    private final UpsertResult wrapped;
    private SObject payload;

    public EnrichedUpsertResult(UpsertResult wrapped) {
        this.wrapped = wrapped;
    }

    /**
     * @return the payload
     */
    public SObject getPayload() {
        return payload;
    }

    /**
     * @param payload the payload to set
     */
    public void setPayload(SObject payload) {
        this.payload = payload;
    }

    /**
     * @param obj
     * @return
     * @see java.lang.Object#equals(java.lang.Object)
     */
    public boolean equals(Object obj) {
        return wrapped.equals(obj);
    }

    /**
     * @return
     * @see com.sforce.soap.partner.UpsertResult#getCreated()
     */
    public boolean getCreated() {
        return wrapped.getCreated();
    }

    /**
     * @param created
     * @see com.sforce.soap.partner.UpsertResult#setCreated(boolean)
     */
    public void setCreated(boolean created) {
        wrapped.setCreated(created);
    }

    /**
     * @return
     * @see com.sforce.soap.partner.UpsertResult#getErrors()
     */
    public Error[] getErrors() {
        return wrapped.getErrors();
    }

    /**
     * @param errors
     * @see com.sforce.soap.partner.UpsertResult#setErrors(com.sforce.soap.partner.Error[])
     */
    public void setErrors(Error[] errors) {
        wrapped.setErrors(errors);
    }

    /**
     * @return
     * @see com.sforce.soap.partner.UpsertResult#getId()
     */
    public String getId() {
        return wrapped.getId();
    }

    /**
     * @return
     * @see com.sforce.soap.partner.UpsertResult#getSuccess()
     */
    public boolean getSuccess() {
        return wrapped.getSuccess();
    }

    /**
     * @return
     * @see java.lang.Object#hashCode()
     */
    public int hashCode() {
        return wrapped.hashCode();
    }

    /**
     * @return
     * @see com.sforce.soap.partner.UpsertResult#isCreated()
     */
    public boolean isCreated() {
        return wrapped.isCreated();
    }

    /**
     * @param id
     * @see com.sforce.soap.partner.UpsertResult#setId(java.lang.String)
     */
    public void setId(String id) {
        wrapped.setId(id);
    }

    /**
     * @return
     * @see com.sforce.soap.partner.UpsertResult#isSuccess()
     */
    public boolean isSuccess() {
        return wrapped.isSuccess();
    }

    /**
     * @param success
     * @see com.sforce.soap.partner.UpsertResult#setSuccess(boolean)
     */
    public void setSuccess(boolean success) {
        wrapped.setSuccess(success);
    }

    /**
     * @param element
     * @param out
     * @param typeMapper
     * @throws IOException
     * @see com.sforce.soap.partner.UpsertResult#write(javax.xml.namespace.QName, com.sforce.ws.parser.XmlOutputStream, com.sforce.ws.bind.TypeMapper)
     */
    public void write(QName element, XmlOutputStream out,
                      TypeMapper typeMapper) throws IOException {
        wrapped.write(element, out, typeMapper);
    }

    /**
     * @param in
     * @param typeMapper
     * @throws IOException
     * @throws ConnectionException
     * @see com.sforce.soap.partner.UpsertResult#load(com.sforce.ws.parser.XmlInputStream, com.sforce.ws.bind.TypeMapper)
     */
    public void load(XmlInputStream in, TypeMapper typeMapper)
            throws IOException, ConnectionException {
        wrapped.load(in, typeMapper);
    }

    /**
     * @return
     * @see com.sforce.soap.partner.UpsertResult#toString()
     */
    public String toString() {
        return wrapped.toString();
    }


}
