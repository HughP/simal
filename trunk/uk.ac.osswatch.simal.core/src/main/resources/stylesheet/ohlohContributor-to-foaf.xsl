<?xml version="1.0"?>
<!--
 
 Copyright 2007 University of Oxford
 
 Licensed under the Apache License, Version 2.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at
 
   http://www.apache.org/licenses/LICENSE-2.0
 
 Unless required by applicable law or agreed to in writing,
 software distributed under the License is distributed on an
 "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 KIND, either express or implied.  See the License for the
 specific language governing permissions and limitations
 under the License.
 
-->

<xsl:stylesheet version="1.0"
    xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#"
    xmlns:rdfs="http://www.w3.org/2000/01/rdf-schema#"
    xmlns:doap="http://usefulinc.com/ns/doap#"
    xmlns:foaf="http://xmlns.com/foaf/0.1/"
    xmlns:labs="http://labs.apache.org/doap-ext/1.0#"
    xmlns:dc="http://purl.org/dc/elements/1.1/"
    xmlns:geo="http://www.w3.org/2003/01/geo/wgs84_pos#"
    xmlns:projects="http://projects.apache.org/ns/asfext#">
    
    <xsl:template match="response">    
      <rdf:RDFa>
        <xsl:apply-templates select="//contributor_fact" />
      </rdf:RDFa>
    </xsl:template>

    <xsl:template match="contributor_fact">
      <foaf:Person>
        <xsl:attribute name="rdf:about">http://www.ohloh.net/accounts/<xsl:value-of select="account_id"/></xsl:attribute>
        <xsl:choose>
          <xsl:when test="response/result/accout/name">
            <foaf:name><xsl:value-of select="response/result/account/name"/></foaf:name>
          </xsl:when>
          <xsl:when test="account_name">
            <foaf:name><xsl:value-of select="account_name"/></foaf:name>
          </xsl:when>
          <xsl:otherwise>
            <foaf:name><xsl:value-of select="contributor_name"/></foaf:name>
          </xsl:otherwise>
        </xsl:choose>
        
        <xsl:apply-templates select="response/result/account"/>
        
        <foaf:homepage>
          <xsl:attribute name="rdfs:label">Ohloh Page</xsl:attribute>
          <xsl:attribute name="rdf:resource">http://www.ohloh.net/account/<xsl:value-of select="contributor_name"/></xsl:attribute>
        </foaf:homepage>
        
        <foaf:holdsAccount>
          <foaf:OnlineAccount>
            <foaf:accountServiceHomepage rdf:resource="http://www.ohloh.net"/>
            <foaf:accountName>
              <xsl:choose>
                <xsl:when test="response/result/accout/name">
                  <foaf:name><xsl:value-of select="response/result/account/name"/></foaf:name>
                </xsl:when>
                <xsl:when test="account_name">
                  <foaf:name><xsl:value-of select="account_name"/></foaf:name>
                </xsl:when>
                <xsl:otherwise>
                  <foaf:name><xsl:value-of select="account_id"/></foaf:name>
                </xsl:otherwise>
              </xsl:choose>
            </foaf:accountName>
          </foaf:OnlineAccount>
        </foaf:holdsAccount>
      </foaf:Person>
    </xsl:template>
    
    <xsl:template match="account">
      <xsl:apply-templates/>
      
      <foaf:based_near>
        <geo:Point>
          <geo:lat><xsl:value-of select="latitude"/></geo:lat>
          <geo:long><xsl:value-of select="longitude"/></geo:long>
        </geo:Point>
      </foaf:based_near>
    </xsl:template>
    
    <xsl:template match="homepage_url">
      <foaf:homepage>
        <xsl:attribute name="rdfs:label">Homepage</xsl:attribute>
        <xsl:attribute name="rdf:resource"><xsl:value-of select="."/></xsl:attribute>
      </foaf:homepage>        
    </xsl:template>
    
    <xsl:template match="avatar_url">
      <foaf:depiction>
        <xsl:attribute name="rdf:resource"><xsl:value-of select="."/></xsl:attribute>
      </foaf:depiction>        
    </xsl:template>
    
    <xsl:template match="email_sha1">
      <foaf:mbox_sha1sum><xsl:value-of select="."/></foaf:mbox_sha1sum>
    </xsl:template>
   
    <xsl:template match="*"/>
</xsl:stylesheet>
