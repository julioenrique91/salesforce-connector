package com.sforce.soap.partner;

/**
 * Generated by ComplexTypeCodeGenerator.java. Please do not edit.
 */
public class Upsert_element implements com.sforce.ws.bind.XMLizable {

    /**
     * Constructor
     */
    public Upsert_element() {}

    /**
     * element : externalIDFieldName of type {http://www.w3.org/2001/XMLSchema}string
     * java type: java.lang.String
     */
    private static final com.sforce.ws.bind.TypeInfo externalIDFieldName__typeInfo =
      new com.sforce.ws.bind.TypeInfo("urn:partner.soap.sforce.com","externalIDFieldName","http://www.w3.org/2001/XMLSchema","string",1,1,true);

    private boolean externalIDFieldName__is_set = false;

    private java.lang.String externalIDFieldName;

    public java.lang.String getExternalIDFieldName() {
      return externalIDFieldName;
    }

    public void setExternalIDFieldName(java.lang.String externalIDFieldName) {
      this.externalIDFieldName = externalIDFieldName;
      externalIDFieldName__is_set = true;
    }

    /**
     * element : sObjects of type {urn:sobject.partner.soap.sforce.com}sObject
     * java type: com.sforce.soap.partner.sobject.SObject[]
     */
    private static final com.sforce.ws.bind.TypeInfo sObjects__typeInfo =
      new com.sforce.ws.bind.TypeInfo("urn:partner.soap.sforce.com","sObjects","urn:sobject.partner.soap.sforce.com","sObject",0,-1,true);

    private boolean sObjects__is_set = false;

    private com.sforce.soap.partner.sobject.SObject[] sObjects = new com.sforce.soap.partner.sobject.SObject[0];

    public com.sforce.soap.partner.sobject.SObject[] getSObjects() {
      return sObjects;
    }

    public void setSObjects(com.sforce.soap.partner.sobject.SObject[] sObjects) {
      this.sObjects = sObjects;
      sObjects__is_set = true;
    }

    /**
     */
    @Override
    public void write(javax.xml.namespace.QName __element,
        com.sforce.ws.parser.XmlOutputStream __out, com.sforce.ws.bind.TypeMapper __typeMapper)
        throws java.io.IOException {
      __out.writeStartTag(__element.getNamespaceURI(), __element.getLocalPart());
      writeFields(__out, __typeMapper);
      __out.writeEndTag(__element.getNamespaceURI(), __element.getLocalPart());
    }

    protected void writeFields(com.sforce.ws.parser.XmlOutputStream __out,
         com.sforce.ws.bind.TypeMapper __typeMapper)
         throws java.io.IOException {
       __typeMapper.writeString(__out, externalIDFieldName__typeInfo, externalIDFieldName, externalIDFieldName__is_set);
       __typeMapper.writeObject(__out, sObjects__typeInfo, sObjects, sObjects__is_set);
    }

    @Override
    public void load(com.sforce.ws.parser.XmlInputStream __in,
        com.sforce.ws.bind.TypeMapper __typeMapper) throws java.io.IOException, com.sforce.ws.ConnectionException {
      __typeMapper.consumeStartTag(__in);
      loadFields(__in, __typeMapper);
      __typeMapper.consumeEndTag(__in);
    }

    protected void loadFields(com.sforce.ws.parser.XmlInputStream __in,
        com.sforce.ws.bind.TypeMapper __typeMapper) throws java.io.IOException, com.sforce.ws.ConnectionException {
        __in.peekTag();
        if (__typeMapper.verifyElement(__in, externalIDFieldName__typeInfo)) {
            setExternalIDFieldName(__typeMapper.readString(__in, externalIDFieldName__typeInfo, java.lang.String.class));
        }
        __in.peekTag();
        if (__typeMapper.isElement(__in, sObjects__typeInfo)) {
            setSObjects((com.sforce.soap.partner.sobject.SObject[])__typeMapper.readObject(__in, sObjects__typeInfo, com.sforce.soap.partner.sobject.SObject[].class));
        }
    }

    @Override
    public String toString() {
      java.lang.StringBuilder sb = new java.lang.StringBuilder();
      sb.append("[Upsert_element ");
      sb.append(" externalIDFieldName='").append(com.sforce.ws.util.Verbose.toString(externalIDFieldName)).append("'\n");
      sb.append(" sObjects='").append(com.sforce.ws.util.Verbose.toString(sObjects)).append("'\n");
      sb.append("]\n");
      return sb.toString();
    }
}
