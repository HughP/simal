<?xml version="1.0"?>
<!--
  Copyright 2007 University of Oxford

  Licensed under the Apache License, Version 2.0 (the "License");
  you may not use this file except in compliance with the License.
  You may obtain a copy of the License at

      http://www.apache.org/licenses/LICENSE-2.0

  Unless required by applicable law or agreed to in writing, software
  distributed under the License is distributed on an "AS IS" BASIS,
  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  See the License for the specific language governing permissions and
  limitations under the License.
-->
<xsl:stylesheet version="1.0"
    xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#"
    xmlns:doap="http://usefulinc.com/ns/doap#"
    xmlns:foaf="http://xmlns.com/foaf/0.1/"
    xmlns:labs="http://labs.apache.org/doap-ext/1.0#"
    xmlns:dc="http://purl.org/dc/elements/1.1/"
    xmlns:projects="http://projects.apache.org/ns/asfext#">

    <xsl:import href="summaryRSS-to-document.xsl"/>
    
    <xsl:template match="rss">
        <xsl:apply-templates select="//channel" />
    </xsl:template>

    <xsl:template match="channel">
        <rdf:RDF>
          <doap:Project>
            <xsl:attribute name="rdf:about"><xsl:value-of select="link"/></xsl:attribute>
            <doap:homepage>
              <xsl:attribute name="rdf:resource"><xsl:value-of select="link"/></xsl:attribute>
            </doap:homepage>
            <xsl:apply-templates />
            <doap:screenshots><xsl:attribute name="rdf:resource">http://sourceforge.net/project/screenshots.php?group_id=<xsl:value-of select="$groupid"/></xsl:attribute></doap:screenshots>
          </doap:Project>
        </rdf:RDF>
    </xsl:template>
    
    <xsl:template match="item[starts-with(title, $projectItemTitlePrefix)]">
        <doap:name><xsl:value-of select="title"/></doap:name>
        <doap:description><xsl:value-of select="substring-after(description, $projectItemDescriptionPrefix)"/></doap:description>
        <doap:created><xsl:value-of select="pubDate"/></doap:created>
    </xsl:template>

    <xsl:template match="item[starts-with(title, $downloadItemTitlePrefix)]">
      <doap:download-page>
        <xsl:attribute name="rdf:resource"><xsl:value-of select="link"/></xsl:attribute>
      </doap:download-page>
    </xsl:template>
    
    <xsl:template match="*" />
</xsl:stylesheet>