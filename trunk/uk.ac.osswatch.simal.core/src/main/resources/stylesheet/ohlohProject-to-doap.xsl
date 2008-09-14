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
    xmlns:projects="http://projects.apache.org/ns/asfext#">
    <xsl:template match="response">    
      <rdf:RDF>
        <xsl:apply-templates select="//project" />
      </rdf:RDF>
    </xsl:template>

    <xsl:template match="project">
      <doap:Project>
        <xsl:attribute name="rdf:about">http://www.ohloh.net/projects/<xsl:value-of select="url_name"/></xsl:attribute>
        <xsl:apply-templates/>
        <doap:homepage>
          <xsl:attribute name="rdfs:label">Ohloh Page</xsl:attribute>
          <xsl:attribute name="rdf:resource">http://www.ohloh.net/projects/<xsl:value-of select="url_name"/></xsl:attribute>
        </doap:homepage>
      </doap:Project>
    </xsl:template>
   
    <xsl:template match="name">
      <doap:name><xsl:value-of select="."/></doap:name>
    </xsl:template>
   
    <xsl:template match="description">
      <doap:description><xsl:value-of select="."/></doap:description>
    </xsl:template>
   
    <xsl:template match="created_at">
      <doap:created><xsl:value-of select="."/></doap:created>
    </xsl:template>
   
    <xsl:template match="homepage_url">
      <doap:homepage>
        <xsl:attribute name="rdf:resource"><xsl:value-of select="."/></xsl:attribute>
      </doap:homepage>
    </xsl:template>
   
    <xsl:template match="download_url">
      <doap:download-page>
        <xsl:attribute name="rdf:resource"><xsl:value-of select="."/></xsl:attribute>
      </doap:download-page>
    </xsl:template>
   
    <xsl:template match="analysis">
      <xsl:apply-templates/>
    </xsl:template>
   
    <xsl:template match="main_language_name">
      <doap:programming-language><xsl:value-of select="."/></doap:programming-language>
    </xsl:template>
   
    <xsl:template match="*"/>
</xsl:stylesheet>
