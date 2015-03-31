This document describes the Schema currently in use within Simal as well as outlining some proposals for future data requirements.

# Namespaces #

| **Prefix** | **Name** | **URI** | **Notes** |
|:-----------|:---------|:--------|:----------|
| asf | Apache Software Foundation | http://projects.apache.org/ns/asfext# |  |
| dc | Dublin Core | http://purl.org/dc/elements/1.1/ |  |
| doap | Description of a Project | http://usefulinc.com/ns/doap# |  |
| foaf | Friend of a Friend | http://xmlns.com/foaf/0.1/ |  |
| rdf | Resource Description Framework | http://www.w3.org/1999/02/22-rdf-syntax-ns# |  |
| rdfs | Resource Description Framework | http://www.w3.org/2000/01/rdf-schema# |  |
| simal | Simal | http://oss-watch.ac.uk/ns/0.2/simal# |  |

# Currently Supported #

## Doap Namespace ##

DOAP is used to capture key details about a project.

| **Class** | **Description** | **Since** | **Example** |
|:----------|:----------------|:----------|:------------|
|doap:Project|A project.|0.2|<doap:Project rdf:about="http://foo.org/projectX#">...

&lt;doap:Project&gt;

|


| **Property** | **Description** | **Range** | **Since** | **Current implementation** | **Example** |
|:-------------|:----------------|:----------|:----------|:---------------------------|:------------|
|doap:name|A name of something.| Literal |0.2| String | 

&lt;doap:name&gt;

Project X

&lt;/doap:name&gt;

|
|doap:homepage|URL of a project's homepage, associated with exactly one project. (subProperty of foaf:homepage) | foaf:Document <br> (inherited) <table><thead><th>0.2</th><th>IDoapHomepage</th><th><doap:homepage rdf:resource="<a href='http://www.foo.org'>http://www.foo.org</a>"/></th></thead><tbody>
<tr><td>doap:old-homepage</td><td>URL of a project's past homepage, associated with exactly one project. (subProperty of foaf:homepage) </td><td> foaf:Document <br> (inherited) </td><td>0.2</td><td>IDoapHomepage</td><td><doap:old-homepage rdf:resource="<a href='http://www.bar.org/foo'>http://www.bar.org/foo</a>"/></td></tr>
<tr><td>doap:created</td><td>Date when something was created, in YYYY-MM-DD form. e.g. 2004-04-05</td><td> Literal </td><td>0.2</td><td>String</td><td>

<doap:created>

2004-04-05<br>
<br>
</doap:created><br>
<br>
</td></tr>
<tr><td>doap:shortdesc</td><td>Short plain text description of a project.</td><td> Literal  </td><td>0.2</td><td> String</td><td>

<doap:shortdesc>

A short description<br>
<br>
</doap:shortdesc><br>
<br>
</td></tr>
<tr><td>doap:description</td><td>Plain text description of a project.</td><td> Literal </td><td>0.2</td><td> String</td><td>

<doap:description>

A complete description with all the words you could hope to see.<br>
<br>
</doap:description><br>
<br>
</td></tr>
<tr><td>doap:release</td><td>A project release.</td><td> doap:Version </td><td>0.2</td><td>IDoapRelease</td><td><doap:release rdf:about="<a href='http://foo.org/project/release/0.1>'>http://foo.org/project/release/0.1&gt;</a>...<br>
<br>
Unknown end tag for </release><br>
<br>
</td></tr>
<tr><td>doap:mailing-list</td><td>Mailing list home page or email address.</td><td> Unspecified</td><td>0.2</td><td>IDoapMailingList</td></tr>
<tr><td>doap:category</td><td>A category of project.</td><td> Unspecified </td><td>0.2</td><td>IDoapCategory</td><td>  </td></tr>
<tr><td>doap:license</td><td>The URI of an RDF description of the license outputs are distributed under.</td><td> Unspecified </td><td>0.2</td><td>IDoapLicence</td><td>  </td></tr>
<tr><td>doap:repository</td><td>Data repository.</td><td> doap:Repository </td><td>0.2</td><td>IDoapRepository</td><td>  </td></tr>
<tr><td>doap:download-page</td><td>Web page from which the project outputs can be downloaded.</td><td> Unspecified</td><td>0.2</td><td>IDoapDownloadPage</td><td>  </td></tr>
<tr><td>doap:download-mirror</td><td>Mirror of download web page.</td><td> Unspecified </td><td>0.2</td><td>IDoapDownloadMirror</td><td>  </td></tr>
<tr><td>doap:wiki</td><td>URL of Wiki for collaborative discussion of project.</td><td> Unspecified </td><td>0.2</td><td>IDoapWiki</td><td>  </td></tr>
<tr><td>doap:bug-database</td><td>issue tracker for a project.</td><td> Unspecified </td><td>0.2</td><td>IDoapBugDatabase</td><td>  </td></tr>
<tr><td>doap:screenshots</td><td>Web page with screenshots of project.</td><td> Unspecified </td><td>0.2</td><td>IDoapScreenshot</td><td>  </td></tr>
<tr><td>doap:maintainer</td><td>Maintainer of a project, a project leader.</td><td> foaf:Person </td><td>0.2</td><td>IPerson</td><td>  </td></tr>
<tr><td>doap:developer</td><td>Developer of software for the project.</td><td> foaf:Person </td><td>0.2</td><td>IPerson</td><td>  </td></tr>
<tr><td>doap:documenter</td><td>Contributor of documentation to the project.</td><td> foaf:Person </td><td>0.2</td><td>IPerson</td><td>  </td></tr>
<tr><td>doap:translator</td><td>Contributor of translations to the project.</td><td> foaf:Person </td><td>0.2</td><td>IPerson</td><td>  </td></tr>
<tr><td>doap:tester</td><td>A tester or other quality control contributor.</td><td> foaf:Person </td><td>0.2</td><td>IPerson</td><td>  </td></tr>
<tr><td>doap:helper</td><td>Project contributor.</td><td> foaf:Person </td><td>0.2</td><td>IPerson</td><td>  </td></tr>
<tr><td>doap:programming-language</td><td>Programming language a project is implemented in or intended for use with.</td><td> Literal </td><td>0.2</td><td>String</td><td>  </td></tr>
<tr><td>doap:os</td><td>Operating system that a project is limited to. Omit this property if the project is not OS-specific.</td><td> Literal </td><td>0.2</td><td>String</td><td>  </td></tr></tbody></table>


<table><thead><th> <b>Domain</b> </th><th> <b>Property</b> </th><th> <b>Description</b> </th><th> <b>Range</b> </th><th> <b>Since</b> </th><th> <b>Current</b><br />Implementation</th><th> <b>Example</b> </th></thead><tbody>
<tr><td>doap:Repository</td><td>doap:browse</td><td>Web browser interface to repository. </td><td> Unspecified </td><td> 0.2 </td><td> IDoapLocation </td><td>  </td></tr>
<tr><td>doap:Repository</td><td>doap:location</td><td>Location of a repository. </td><td> Unspecified </td><td> 0.2 </td><td> IDoapLocation </td><td>  </td></tr>
<tr><td> doap:Repository </td><td> doap:anon-root</td><td> Repository for anonymous access. </td><td> Literal</td><td> 0.2 </td><td> String </td><td>  </td></tr>
<tr><td> doap:CVSRepository <br /> doap:ArchRepository <br /> doap:BKRepository </td><td> doap:module </td><td> Module name of a repository. </td><td>  Unspecified </td><td> 0.2 </td><td> String </td><td>  </td></tr>
<tr><td>doap:Version</td><td>doap:revision</td><td>Revision identifier of an output</td><td> Literal </td><td>0.2</td><td>String</td><td>  </td></tr></tbody></table>



<h3>Not yet supported</h3>
<table><thead><th> <b>Property</b> </th><th> <b>Description</b> </th><th> <b>Domain</b> </th><th> <b>Range</b> </th></thead><tbody>
<tr><td> doap:file-release </td><td> URI of download associated with this release. </td><td> doap:Version </td><td> Unspecified </td></tr>
<tr><td> doap:implements </td><td> Implements specification </td><td> doap:Project </td><td> doap:Specification </td></tr>
<tr><td> doap:serivce-endpoint </td><td> The URI of a web service endpoint where software as a service may be accessed. </td><td> doap:Project </td><td> rdfs:Resource </td></tr>
<tr><td> doap:language </td><td> ISO language code a project has been translated into </td><td> doap:Project </td><td> Literal </td></tr>
<tr><td> doap:vendor </td><td> Vendor organization: commercial, free or otherwise </td><td> doap:Project </td><td> foaf:Organization </td></tr>
<tr><td> doap:platform </td><td> Indicator of software platform (non-OS specific), e.g. Java, Firefox, ECMA CLR </td><td> doap:Project <br /> doap:Version </td><td> Literal </td></tr>
<tr><td> doap:audience </td><td> Description of target user base </td><td> doap:Project </td><td> Literal </td></tr>
<tr><td> doap:blog </td><td> URI of a blog related to a project </td><td> doap:Project </td><td> rdfs:Resource </td></tr></tbody></table>

<h2>FOAF Namespace</h2>

FOAF is used to capture key details about a person.<br>
<br>
<h2>RDFS Namespace</h2>

RDFS is used to capture information about a resource.<br>
<br>
<table><thead><th> <b>Name</b> </th><th> <b>Since</b> </th><th> <b>Description</b> </th><th> <b>Example</b> </th></thead><tbody>
<tr><td> rdfs:label </td><td> 0.2 </td><td> A human readable label for a resource </td><td> <br>
<br>
<rdfs:label><br>
<br>
Human Readable Label<br>
<br>
</rdfs:label><br>
<br>
 </td></tr>
<tr><td> rdfs:seeAlso </td><td> 0.2 </td><td> A reference to another, related resource </td><td> <rdfs:seeAlso rdf:resource="<a href='http://jisc.ac.uk/project#17'>http://jisc.ac.uk/project#17</a>" /> </td></tr></tbody></table>

<h2>Simal Namespace</h2>

Simal is used to capture details specific to Simal held in the Simal repository.<br>
<br>
<table><thead><th> <b>Name</b> </th><th> <b>Since</b> </th><th> <b>Description</b> </th><th> <b>Example</b> </th></thead><tbody>
<tr><td>simal:Project</td><td>A simal representation of a project. This extends a doap:Project and allows some additional information to be provided.</td><td>0.3</td><td><simal:Project rdf:about="<a href='http://foo.org/projectX'>http://foo.org/projectX</a>"></td></tr>
<tr><td>simal:Person</td><td>A simal representation of a project. This extends a foaf:Person and allows some additional information to be provided.</td><td>0.3</td><td><simal:Person rdf:about="<a href='http://foo.org/personX'>http://foo.org/personX</a>"></td></tr>
<tr><td>simal:personId</td><td>A unique identifier for a person stored within Simal.</td><td>0.2</td><td>

<simal:personId>

this-is-a-world-unique-id<br>
<br>
</simal:personId><br>
<br>
</td></tr>
<tr><td>simal:projectId</td><td>A unique identifier for a project stored within Simal.</td><td>0.2</td><td>

<simal:projectId>

this-is-a-world-unique-id<br>
<br>
</simal:projectId><br>
<br>
</td></tr></tbody></table>

<h1>Proposed Additions</h1>

<a href='http://groups.google.com/group/simal-users/browse_thread/thread/81c6443d4ebb4252'>Discussion</a> of some proposed additions.<br>
<br>
<table><thead><th> <b>Data</b> </th><th> <b>Description</b> </th><th> <b>Suggested example Implementation</b> </th></thead><tbody>
<tr><td>simal:collected</td><td>The date a fact was collected</td><td>

<simal:collected>

YYYY-MM-DD<br>
<br>
</simal:collected><br>
<br>
</td></tr>
<tr><td>simal:source</td><td>The source of the document </td><td>

<simal:source>

JISC<br>
<br>
</simal:source><br>
<br>
</td></tr></tbody></table>

<h1>Relationship between schemas</h1>

For each project that is newly imported into Simal, a new <code>simal:Project</code> is created and a <code>simal:ProjectId</code> is generated. The DOAP file is kept as is and is referenced from the <code>simal:Project</code> via an <code>rdfs:seeAlso</code>. If the imported project has an <code>rdf:about</code> URI this will be used, for the reference, otherwise one will be generated that is specific to the source of the import.<br>
<br>
Multiple DOAP files can be imported for the same project. See the figure below:<br>
<br>
<img src='http://simal.googlecode.com/svn/wiki/Schema.attach/simalDoapProjects.png' />

The same is true for entities of type <code>foaf:Person</code>. A <code>simal:Person</code> is created for a <code>foaf:Person</code> with an <code>rdfs:seeAlso</code> reference to the original <code>foaf:Person</code>. This has no effect on the <code>doap:Project</code> which will still reference to the original <code>foaf:Person</code>.