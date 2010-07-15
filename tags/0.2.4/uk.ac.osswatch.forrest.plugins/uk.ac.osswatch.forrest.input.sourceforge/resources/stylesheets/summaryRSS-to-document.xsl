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
    xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
    
    <xsl:param name="groupid"/>
    
    <xsl:variable name="projectItemTitlePrefix">Project name: </xsl:variable>
    <xsl:variable name="projectItemDescriptionPrefix">Project description: </xsl:variable>
    <xsl:variable name="developerItemTitlePrefix">Developers on project: </xsl:variable>
    <xsl:variable name="activityItemTitlePrefix">Activity percentile (last week): </xsl:variable>
    <xsl:variable name="activityItemRankingDescriptionPrefix">Ranking: </xsl:variable> 
    <xsl:variable name="downloadItemTitlePrefix">Downloadable files: </xsl:variable>
    <xsl:variable name="trackerItemTitlePrefix">Tracker: </xsl:variable> 
     
    <xsl:template match="rss">
        <xsl:apply-templates select="//channel" />
    </xsl:template>

    <xsl:template match="channel">
        <document>
            <header>
                <title>
                    <xsl:call-template name="title"/>
                </title>
            </header>
            <body>
              <table>
                <caption>Summary</caption>
                <tr>
                  <th>Name</th>
                  <td><xsl:call-template name="title"/></td>
                </tr>
                <xsl:apply-templates select="item[starts-with(title, $projectItemTitlePrefix)]"/>
              </table>
              <table>
                <caption>Developers</caption>
                <xsl:apply-templates select="item[starts-with(title, $developerItemTitlePrefix)]"/>
              </table>
              <table>
                <caption>Activity</caption>
                <xsl:apply-templates select="item[starts-with(title, $activityItemTitlePrefix)]"/>
              </table>
              <table>
                <caption>Downloads</caption>
                <xsl:apply-templates select="item[starts-with(title, $downloadItemTitlePrefix)]"/>
              </table>
              <table>
                <caption>Trackers</caption>
                <xsl:apply-templates select="item[starts-with(title, $trackerItemTitlePrefix)]"/>
              </table>
            </body>
        </document>
    </xsl:template>
    
    <xsl:template name="title">
      <xsl:value-of select="substring(//item[starts-with(title, 'Project name:')]/title, string-length($projectItemTitlePrefix))"/>
    </xsl:template>

    <xsl:template match="item[starts-with(title, $projectItemTitlePrefix)]">
        <tr>
          <th>Description</th>
          <td><xsl:value-of select="substring-after(description, $projectItemDescriptionPrefix)"/></td>
        </tr>
        <tr>
          <th>Updated</th>
          <td><xsl:value-of select="pubDate"/></td>
        </tr>
    </xsl:template>

    <xsl:template match="item[starts-with(title, $developerItemTitlePrefix)]">
        <tr>
          <th>Number of Developers</th>
          <td><xsl:value-of select="substring-after(title, $developerItemTitlePrefix)"/></td>
        </tr>
        <tr>
          <th>Members List</th>
          <td>
            <a>
              <xsl:attribute name="href"><xsl:value-of select="link"/></xsl:attribute>
              <xsl:value-of select="link"/>
            </a>
          </td>
        </tr>
    </xsl:template>

    <xsl:template match="item[starts-with(title, $activityItemTitlePrefix)]">
        <tr>
          <th>Activity (during week up until <xsl:value-of select="pubDate"/>)</th>
          <td><xsl:value-of select="substring-after(title, $activityItemTitlePrefix)"/></td>
        </tr>
        <tr>
          <th>Ranking</th>
          <td><xsl:value-of select="substring-before(substring-after(description, $activityItemRankingDescriptionPrefix), ', Activity percentile:')"/></td>
        </tr>
        <tr>
          <th>Statistics</th>
          <td>
            <a>
              <xsl:attribute name="href"><xsl:value-of select="link"/></xsl:attribute>
              <xsl:value-of select="link"/>
            </a>
          </td>
        </tr>
    </xsl:template>

    <xsl:template match="item[starts-with(title, $downloadItemTitlePrefix)]">
        <tr>
          <th>Number</th>
          <td><xsl:value-of select="substring-after(title, $downloadItemTitlePrefix)"/></td>
        </tr>
        <tr>
          <th>Downloads Page</th>
          <td>
            <a>
              <xsl:attribute name="href"><xsl:value-of select="link"/></xsl:attribute>
              <xsl:value-of select="link"/>
            </a>
          </td>
        </tr>
    </xsl:template>

    <xsl:template match="item[starts-with(title, $trackerItemTitlePrefix)]">
        <tr>
          <th><xsl:value-of select="title"/></th>
          <td>
            <a>
              <xsl:attribute name="href"><xsl:value-of select="link"/></xsl:attribute>
              Browse tracker
            </a>
          </td>
        </tr>
    </xsl:template>

    <xsl:template match="*" />
</xsl:stylesheet>