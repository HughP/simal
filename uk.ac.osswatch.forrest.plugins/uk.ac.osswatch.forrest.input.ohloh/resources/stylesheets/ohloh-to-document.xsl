<?xml version="1.0"?>
<xsl:stylesheet version="1.0"
    xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
    <xsl:template match="response">
        <xsl:apply-templates select="//result" />
    </xsl:template>

    <xsl:template match="result">

        <document>
            <header>
                <title>
                    <xsl:choose>
                        <xsl:when test="project/name">
                            <xsl:value-of select="project/name" />
                        </xsl:when>
                        <xsl:otherwise>
                            Project statistics summary
                        </xsl:otherwise>
                    </xsl:choose>
                </title>
            </header>
            <body>
                <xsl:apply-templates />
            </body>
        </document>
    </xsl:template>

    <xsl:template match="project">
      <table>
        <caption>Summary</caption>
        <xsl:apply-templates />
      </table>
    </xsl:template>

    <xsl:template match="analysis">
        <table>
            <caption>Analysis</caption>
            <xsl:apply-templates />
        </table>
    </xsl:template>

    <xsl:template match="name">
        <tr>
            <th>Name</th>
            <td>
                <xsl:value-of select="." />
            </td>
        </tr>
    </xsl:template>

    <xsl:template match="description">
        <tr>
            <th>Description</th>
            <td>
                <xsl:value-of select="." />
            </td>
        </tr>
    </xsl:template>

    <xsl:template match="created_at">
        <tr>
            <th>Created</th>
            <td>
                <xsl:value-of select="." />
            </td>
        </tr>
    </xsl:template>

    <xsl:template match="homepage_url">
        <tr>
            <th>Homepage</th>
            <td>
                <a>
                    <xsl:attribute name="href">
                        <xsl:value-of select="." />
                    </xsl:attribute>
                    <xsl:value-of select="." />
                </a>
            </td>
        </tr>
    </xsl:template>

    <xsl:template match="download_url">
        <tr>
            <th>Download</th>
            <td>
                <a>
                    <xsl:attribute name="href">
                        <xsl:value-of select="." />
                    </xsl:attribute>
                    <xsl:value-of select="." />
                </a>
            </td>
        </tr>
    </xsl:template>

    <xsl:template match="main_language_name">
        <tr>
            <th>Main Language</th>
            <td><xsl:value-of select="." /></td>
        </tr>
    </xsl:template>
    
    <xsl:template match="updated_at">
      <tr>
        <th>Last Updated</th>
        <td><xsl:value-of select="."/></td>
      </tr>
    </xsl:template>
    
    <xsl:template match="logged_at">
      <tr>
        <th>Last log check</th>
        <td><xsl:value-of select="."/></td>
      </tr>
    </xsl:template>
    
    <xsl:template match="min_month">
      <tr>
        <th>First active month</th>
        <td><xsl:value-of select="."/></td>
      </tr>
    </xsl:template>
    
    <xsl:template match="max_month">
      <tr>
        <th>Last active month</th>
        <td><xsl:value-of select="."/></td>
      </tr>
    </xsl:template>
    
    <xsl:template match="twelve_month_contributor_count">
      <tr>
        <th>Contributors in last twelve months</th>
        <td><xsl:value-of select="."/></td>
      </tr>
    </xsl:template>
    
    <xsl:template match="total_code_lines">
      <tr>
        <th>Total code lines</th>
        <td><xsl:value-of select="."/></td>
      </tr>
    </xsl:template>

    <xsl:template match="*" />
</xsl:stylesheet>