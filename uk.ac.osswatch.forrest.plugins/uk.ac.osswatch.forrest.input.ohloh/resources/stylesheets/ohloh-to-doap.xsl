<?xml version="1.0"?>
<xsl:stylesheet version="1.0"
    xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#"
    xmlns:doap="http://usefulinc.com/ns/doap#"
    xmlns:foaf="http://xmlns.com/foaf/0.1/"
    xmlns:labs="http://labs.apache.org/doap-ext/1.0#"
    xmlns:dc="http://purl.org/dc/elements/1.1/"
    xmlns:projects="http://projects.apache.org/ns/asfext#">
    <xsl:template match="response">
        <xsl:apply-templates select="//project" />
    </xsl:template>

    <xsl:template match="project">
        <rdf:RDF>
          <doap:Project>
            <xsl:attribute name="rdf:about">http://www.ohloh.net/projects/<xsl:value-of select="id"/></xsl:attribute>
            <xsl:apply-templates/>
          </doap:Project>
        </rdf:RDF>
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
      <doap:doanload-page>
        <xsl:attribute name="rdf:resource"><xsl:value-of select="."/></xsl:attribute>
      </doap:doanload-page>
    </xsl:template>
    
    <xsl:template match="*"/>
</xsl:stylesheet>