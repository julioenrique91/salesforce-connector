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

import java.io.IOException;

import javax.xml.namespace.QName;

import com.sforce.soap.partner.Error;
import com.sforce.soap.partner.IError;
import com.sforce.soap.partner.SaveResult;
import com.sforce.soap.partner.sobject.SObject;
import com.sforce.ws.ConnectionException;
import com.sforce.ws.bind.TypeMapper;
import com.sforce.ws.parser.XmlInputStream;
import com.sforce.ws.parser.XmlOutputStream;

public class EnrichedSaveResult extends SaveResult {

    private final SaveResult wrapped;
    private SObject payload;

    public EnrichedSaveResult(SaveResult wrapped) {
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

    public SaveResult getWrapped() {
        return wrapped;
    }

    /**
     * @param obj
     * @return
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        return wrapped.equals(obj);
    }

    /**
     * @return
     * @see com.sforce.soap.partner.SaveResult#getErrors()
     */
    @Override
    public Error[] getErrors() {
        return wrapped.getErrors();
    }

    /**
     * @param errors
     * @see com.sforce.soap.partner.SaveResult#setErrors(com.sforce.soap.partner.Error[])
     */
    @Override
    public void setErrors(IError[] errors) {
        wrapped.setErrors(errors);
    }

    /**
     * @return
     * @see com.sforce.soap.partner.SaveResult#getId()
     */
    @Override
    public String getId() {
        return wrapped.getId();
    }

    /**
     * @param id
     * @see com.sforce.soap.partner.SaveResult#setId(java.lang.String)
     */
    @Override
    public void setId(String id) {
        wrapped.setId(id);
    }

    /**
     * @return
     * @see com.sforce.soap.partner.SaveResult#getSuccess()
     */
    @Override
    public boolean getSuccess() {
        return wrapped.getSuccess();
    }

    /**
     * @return
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        return wrapped.hashCode();
    }

    /**
     * @return
     * @see com.sforce.soap.partner.SaveResult#isSuccess()
     */
    @Override
    public boolean isSuccess() {
        return wrapped.isSuccess();
    }

    /**
     * @param success
     * @see com.sforce.soap.partner.SaveResult#setSuccess(boolean)
     */
    @Override
    public void setSuccess(boolean success) {
        wrapped.setSuccess(success);
    }

    /**
     * @param element
     * @param out
     * @param typeMapper
     * @throws IOException
     * @see com.sforce.soap.partner.SaveResult#write(javax.xml.namespace.QName, com.sforce.ws.parser.XmlOutputStream, com.sforce.ws.bind.TypeMapper)
     */
    @Override
    public void write(QName element, XmlOutputStream out,
                      TypeMapper typeMapper) throws IOException {
        wrapped.write(element, out, typeMapper);
    }

    /**
     * @param in
     * @param typeMapper
     * @throws IOException
     * @throws ConnectionException
     * @see com.sforce.soap.partner.SaveResult#load(com.sforce.ws.parser.XmlInputStream, com.sforce.ws.bind.TypeMapper)
     */
    @Override
    public void load(XmlInputStream in, TypeMapper typeMapper)
            throws IOException, ConnectionException {
        wrapped.load(in, typeMapper);
    }

    /**
     * @return
     * @see com.sforce.soap.partner.SaveResult#toString()
     */
    @Override
    public String toString() {
        return wrapped.toString();
    }

}
