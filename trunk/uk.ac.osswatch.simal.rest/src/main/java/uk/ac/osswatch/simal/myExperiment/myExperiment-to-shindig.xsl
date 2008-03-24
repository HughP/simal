<?xml version="1.0"?>
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