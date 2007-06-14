/*
* Copyright 2007 University of Oxford
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
*     http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/
String.prototype.trim = function() {
    a = this.replace(/^\s+/, '');
    return a.replace(/\s+$/, '');
}

function generateDOAP() {
    var today = new Date();
    var id = document.getElementById("id").value;
    var name = document.getElementById("name").value;
    var shortdesc = document.getElementById("shortdesc").value;
    var description = document.getElementById("description").value;
    var homePage = document.getElementById("homePage").value;
    var oses = document.getElementById("oses").value;
    var licenses = document.getElementById("license[]");
    var pi_name = document.getElementById("pi_name").value;
    var pi_email = document.getElementById("pi_email").value;
    var pi_url = document.getElementById("pi_url").value;

    var doap = "<rdf:RDF xmlns:rdf=\"http://www.w3.org/1999/02/22-rdf-syntax-ns#\"\n"
    doap += "     xmlns=\"http://usefulinc.com/ns/doap#\"\n";
    doap += "     xmlns:foaf=\"http://xmlns.com/foaf/0.1/\"\n";
    doap += "     xmlns:ossw=\"http://www.jisc.ac.uk/ns/jsfext#\"\n";
    doap += "<Project rdf:about=\"" + homePage + "\">\n";
    doap += " <name>" + name + "</name>\n";
    doap += " <shortname>" + id + "</shortname>\n";
    doap += " <shortdesc xml:lang=\"en\">" + shortdesc + "</shortdesc>\n";
    doap += " <description xml:lang=\"en\">" + description + "</description>\n";
    doap += " <homepage rdf:resource=\"" + homePage + "\"/>\n";

    var issueTracker = document.getElementById("issueTracker").value;
    doap += " <bug-database rdf:resource=\"" + issueTracker +
    "\"/>\n";

    var list = document.getElementById("mailingList").value;
    doap += " <mailing-list rdf:resource=\"" + list + "\"/>\n";

    var download = document.getElementById("downloadPage").value;
    doap += " <download-page rdf:resource=\"" + download + "\"/>\n";

    var wiki = document.getElementById("wiki").value;
    doap += " <wiki rdf:resource=\"" + wiki + "\"/>\n";

    for (i=0; i < licenses.length; i++) {
      if (licenses[i].selected) {
        doap += " <license rdf:resource=\"http://usefulinc.com/doap/licenses/" + licenses[i].value + "\"/>\n";
      }
    }

    doap += " <created>" + today.getFullYear() + "-" + (today.getMonth() + 1) + "-" + today.getDate() + "</created>\n";

    doap += " <maintainer>\n";
    doap += "  <foaf:Person rdf:about=\"" + pi_url + "#me\">\n";
    doap += "   <foaf:name>" + pi_name + "</foaf:name>\n";
    doap += "   <foaf:homepage rdf:resource=\"" + pi_url + "\"/>\n";
    doap += "   <foaf:mbox_sha1sum>" + hex_sha1(pi_email) + "</foaf:mbox_sha1sum>\n";
    doap += "  </foaf:Person>\n";
    doap += " </maintainer>\n";

    var repoType = document.getElementById("repos").value;
    if (repoType != "") {
      doap += " <repository>\n"; 
     doap += "  <" + repoType +">\n";
      doap += "    <location rdf:resource=\"http://svn.apache.org/repos/asf/labs/" + id + "/\"/>\n";
      doap += "    <browse rdf:resource=\"http://svn.apache.org/viewvc/labs/" + id + "/\"/>\n";
      doap += "  </" + repoType + ">\n";
      doap += " </repository>\n";
    }

    var cats = document.getElementById("category[]");
    for(i=0; i< cats.length; i++) {
      if (cats[i].selected) {
        doap += " <category>" + cats[i].value + "</category>\n";
      }
    }

    var langs = document.getElementById("language[]");
    for(i=0; i< langs.length; i++) {
      if (langs[i].selected) {
        doap += " <programming-language>" + langs[i].value + "</programming-language>\n";
      }
    }

    if (oses != "") {
        var osesArray = oses.split(",");
        for (var i = 0; i < osesArray.length; i++) {
            doap += " <os>" + osesArray[i].trim() + "</os>\n";
        }
    }

    // ================================================
    //                OSS Watch Extensions
    //   ================================================

    var startDate = document.getElementById("startDate").value;
    doap += " <osw:startDate>" + startDate + "</osw:startDate>\n";

    var endDate = document.getElementById("endDate").value;
    doap += " <osw:endDate>" + startDate + "</osw:endDate>\n";

    var programme = document.getElementById("programme").value;
    doap += " <osw:programme rdf:resource=\"http://www.jisc.ac.uk/whatwedo/programmes/" + programme + ".aspx\" />\n";

    var theme = document.getElementById("theme").value;
    doap += " <osw:theme rdf:resource=\"http://www.jisc.ac.uk/whatwedo/themes/" + theme + ".aspx\" />\n";

    var jiscProjectPage  = document.getElementById("jiscProjectPage").value;
    doap += " <osw:communityStrength>" + jiscProjectPage + " </communityStrength>\n";

    var communityStrength = document.getElementById("communityStrength").value;
    doap += " <osw:communityStrength>" + communityStrength + "</communityStrength>\n";

    doap += "</Project>\n";
    doap += "</rdf:RDF>\n";

    var doap_div = document.getElementById("doap");
    doap_div.textContent = doap;
}
