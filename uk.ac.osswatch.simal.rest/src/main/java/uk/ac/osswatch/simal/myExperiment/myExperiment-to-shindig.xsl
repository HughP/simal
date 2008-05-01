<?xml version="1.0"?>
<!--
 
Copyright 2008 University of Oxford 
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

<xsl:stylesheet xmlns:xsl='http://www.w3.org/1999/XSL/Transform' version='1.0'>
  <xsl:template match='/'>
    <xsl:apply-templates/>
  </xsl:template>
  
  <xsl:template match="user">
    <container>
      <people>
        <person>
          <xsl:attribute name="id"><xsl:value-of select="id"/></xsl:attribute>
          <xsl:attribute name="name"><xsl:value-of select="name"/></xsl:attribute>
          <xsl:for-each select="friends/friend">
            <friend><xsl:value-of select="@uri"/></friend>
          </xsl:for-each>
        </person>
        <xsl:apply-templates select="friends/friend"/>
      </people>
    </container>
  </xsl:template>
  
  <xsl:template match="friend">
    <person>
      <xsl:attribute name="id"><xsl:value-of select="@uri"/></xsl:attribute>
      <xsl:attribute name="name"><xsl:value-of select="."/></xsl:attribute>
    </person>
  </xsl:template>
  
  <xsl:template match="*"/>
</xsl:stylesheet>
