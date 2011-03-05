/**
 * Copyright 2010 University of Oxford
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

String.prototype.trim = function() {
  a = this.replace(/^\s+/, '');
  return a.replace(/\s+$/, '');
};

var DOAPPersonCount = 1;
var JISCPersonCount = 1;

var MAX_NR_PERSONS = 10;
var BASE_NAMESPACE_URL = "http://registry.oss-watch.ac.uk/";
var PEOPLE_NAMESPACE_URL = BASE_NAMESPACE_URL + "people/";
var CATEGORY_NAMESPACE_URL = BASE_NAMESPACE_URL + "categories/";

// settings - should be set as preferences
var SIMAL_REST_BASE_URL = "http://localhost:8080/simal-rest"; 

var jiscQuestions;

function initForm() {
  fetchAllCategories();
  addPersons(3);
  initJiscFunded();
}

function processCategories(allCategories) {
  document.getElementById("categories_container").innerHTML = generateCategoriesHtml(allCategories.items);
}

function generateCategoriesHtml(categoriesItems) {
  categoriesHtml = "       <select id=\"categories\" multiple=\"multiple\">\n";

  for(i=0;i<categoriesItems.length;i++) {
    if(categoriesItems[i].label.indexOf("http://") != 0) {
      categoriesHtml += "         <option value=\"" + categoriesItems[i].id + "\">" + categoriesItems[i].label + "</option>\n";
    }
  }

  categoriesHtml += "       </select><br />\n";
  categoriesHtml += "Use the 'Control' or 'Shift' key to select multiple categories.";

  return categoriesHtml;
}

function isJiscFunded() {
  return eval(document.getElementById("jisc_funded").value);
}

function checkJiscFunded() {
  if (isJiscFunded()) {
    document.getElementById("jisc_questions").innerHTML = jiscQuestions;
  } else {
    document.getElementById("jisc_questions").innerHTML = "";
  }
}

function initJiscFunded() {
  jiscQuestions = document.getElementById("jisc_questions").innerHTML;
  document.getElementById("jisc_questions").innerHTML = "";
}

function generateDOAPPersonOptions() {
  personHtml  = "         <option value=\"maintainer\">Maintainer</option>\n";
  personHtml += "         <option value=\"developer\">Developer</option>\n";
  personHtml += "         <option value=\"documenter\">Documenter</option>\n";
  personHtml += "         <option value=\"tester\">Tester</option>\n";
  personHtml += "         <option value=\"helper\">Helper</option>\n";
  personHtml += "         <option value=\"translator\">Translator</option>\n";
  
  return personHtml;
}

function generateJISCPersonOptions() {
  personHtml  = "         <option value=\"simal:project-director\">Project director</option>\n";
  personHtml += "         <option value=\"simal:project-manager\">Project manager</option>\n";
  personHtml += "         <option value=\"simal:administrative-assistant\">Administrative</option>\n";
  personHtml += "         <option value=\"simal:research-officer\">Research officer</option>\n";
  
  return personHtml;
}

function generatePersonHtml(personIndex, DOAPpersons) {
  personHtml = "";
  personHtml += "				<select id=\"person_type_" + personIndex + "\">\n";
  
  if(DOAPpersons) {
    personHtml += generateDOAPPersonOptions();
  } else {
    personHtml += generateJISCPersonOptions();
  }
  personHtml += "				</select>\n";
  personHtml += "			<input type=\"text\" id=\"person_name_" + personIndex
      + "\" class=\"short\"/>\n";
  personHtml += "			<input type=\"text\" id=\"person_email_" + personIndex
      + "\" class=\"short\"/>\n";
  personHtml += "			<input type=\"text\" id=\"person_url_" + personIndex
      + "\" class=\"short\"/>\n";
  personHtml += "			<input type=\"text\" id=\"person_pic_" + personIndex
      + "\" class=\"short\"/><br />\n";
  return personHtml;
}

function checkMaxpersonCount(personCount) {
  if (personCount > MAX_NR_PERSONS) {
    alert("You can add a maximum of " + MAX_NR_PERSONS + " people.");
    throw new Error("You can add a maximum of " + MAX_NR_PERSONS + " people.");
  }
}

function addJISCPerson() {
  checkMaxpersonCount(JISCPersonCount);
  document.getElementById("people_container_"
      + (JISCPersonCount + MAX_NR_PERSONS)).innerHTML = 
        addPerson((JISCPersonCount++ + MAX_NR_PERSONS), false);
}

function addDOAPPerson() {
  checkMaxpersonCount(DOAPPersonCount);
  document.getElementById("people_container_" + DOAPPersonCount).innerHTML = 
    addPerson(DOAPPersonCount++, true);
} 


function addPerson(personCount, DOAPPersons) {
  return generatePersonHtml(personCount, DOAPPersons);
}

function addPersons(count) {
  for (i = 1; i <= count; i++) {
    addJISCPerson();
    addDOAPPerson();
  }
}

function exists(obj) {
  return typeof (obj) != 'undefined';
}

function processPerson(personIndex) {
  if (!exists(document.getElementById("person_name_" + personIndex)))
    return "";

  var person_type = document.getElementById("person_type_" + personIndex).value;
  var person_name = document.getElementById("person_name_" + personIndex).value;
  var person_email = checkEmail("person_email_" + personIndex);
  var person_url = checkUrl("person_url_" + personIndex);
  var person_pic = checkUrl("person_pic_" + personIndex);
  var person_about = person_url;

  if (person_name == "" || person_email == "")
    return "";

  if (person_about == "") {
    person_about = PEOPLE_NAMESPACE_URL + hex_sha1(person_email);
  }

  peopleDoap = " <" + person_type + ">\n";
  peopleDoap += "  <foaf:Person rdf:about=\"" + person_about + "\">\n";
  peopleDoap += "   <foaf:name>" + person_name + "</foaf:name>\n";
  if (person_url != "") {
    peopleDoap += "   <foaf:homepage rdf:resource=\"" + person_url + "/\"/>\n";
  }
  if (person_pic != "") {
    peopleDoap += "   <foaf:depiction rdf:resource=\"" + person_pic + "/\"/>\n";
  }
  peopleDoap += "   <foaf:mbox rdf:resource=\"mailto:" + person_email
      + "\" />\n";
  peopleDoap += "   <foaf:mbox_sha1sum>" + hex_sha1(person_email)
      + "</foaf:mbox_sha1sum>\n";
  peopleDoap += "  </foaf:Person>\n";
  peopleDoap += " </" + person_type + ">\n";

  return peopleDoap;
}

function processPeople() {
  var peopleDoap = "";

  for (i = 1; i < DOAPPersonCount; i++) {
    peopleDoap += processPerson(i);
  }

  for (i = 1; i < JISCPersonCount; i++) {
    peopleDoap += processPerson(i);
  }

  return peopleDoap;
}

function allMandatoryFieldsOK() {
  var allMandatoryFieldIds = [ "shortname", "name", "shortdesc", "description",
      "homepage", "person_email_1", "person_name_1" ];
  var allMandatoryJiscFieldIds = [ "name", "shortname", "shortdesc", "description",
                               "blog", "homepage", "blogfeed", "projectemail", "person_email_1",
                               "person_name_1", "person_pic_1", "lead_institution", "department",
                               "dept_postcode", "resp_reports_name", "resp_reports_email", "licence",
                               "cont_licence", "jiscmanager" ];

  var allNotNoneFields = [ "primary_product", "secondary_product",
      "primary_audience" ];

  for (i = 0; i < allMandatoryFieldIds.length; i++) {
    if (!exists(document.getElementById(allMandatoryFieldIds[i])))
      alert("What do you mean, " + allMandatoryFieldIds[i]);
    if (document.getElementById(allMandatoryFieldIds[i]).value == "") {
      alert("Please fill in all mandatory fields as marked with a '*'");
      document.getElementById(allMandatoryFieldIds[i]).focus();
      return false;
    }
  }

  if (isJiscFunded()) {
    for (i = 0; i < allMandatoryJiscFieldIds.length; i++) {
      if (!exists(document.getElementById(allMandatoryJiscFieldIds[i])))
        alert("What do you mean, " + allMandatoryJiscFieldIds[i]);
      if (document.getElementById(allMandatoryJiscFieldIds[i]).value == "") {
        alert("This field is mandatory for JISC-funded projects.");
        document.getElementById(allMandatoryJiscFieldIds[i]).focus();
        return false;
      }
    }

    for (i = 0; i < allNotNoneFields.length; i++) {
      if (document.getElementById(allNotNoneFields[i]).value == "none") {
        alert("Please select an answer from the drop-down box.");
        document.getElementById(allNotNoneFields[i]).focus();
        return false;
      }
    }

    if (document.getElementById("resp_reports_phone").value == ""
        && document.getElementById("resp_reports_skype").value == "") {
      alert("Please provide a valid phone number or a valid Skype ID.");
      document.getElementById("resp_reports_phone").focus();
      return false;
    }
  }
  
  if (document.getElementById("shortname").value.indexOf(' ') != -1) {
    alert("The unique project tag cannot contain spaces.");
    document.getElementById("shortname").focus();
    return false;
  }
  return true;
}

function isUrl(s) {
  var regexp = /^(([\w]+:)?\/\/)?(([\d\w]|%[a-fA-f\d]{2,2})+(:([\d\w]|%[a-fA-f\d]{2,2})+)?@)?([\d\w][-\d\w]{0,253}[\d\w]\.)+[\w]{2,4}(:[\d]+)?(\/([-+_~.\d\w]|%[a-fA-f\d]{2,2})*)*(\?(&?([-+_~.\d\w]|%[a-fA-f\d]{2,2})=?)*)?(#([-+_~.\d\w]|%[a-fA-f\d]{2,2})*)?$/;
  return regexp.test(s);
}

function isEmailAddress(s) {
  var regexp = /^((([a-z]|[0-9]|!|#|$|%|&|'|\*|\+|\-|\/|=|\?|\^|_|`|\{|\||\}|~)+(\.([a-z]|[0-9]|!|#|$|%|&|'|\*|\+|\-|\/|=|\?|\^|_|`|\{|\||\}|~)+)*)@((((([a-z]|[0-9])([a-z]|[0-9]|\-){0,61}([a-z]|[0-9])\.))*([a-z]|[0-9])([a-z]|[0-9]|\-){0,61}([a-z]|[0-9])\.)[\w]{2,4}|(((([0-9]){1,3}\.){3}([0-9]){1,3}))|(\[((([0-9]){1,3}\.){3}([0-9]){1,3})\])))$/;
  return regexp.test(s);
}

function checkEmail(fieldName) {
  fieldValue = document.getElementById(fieldName).value;

  if (fieldValue != "") {
    if (!isEmailAddress(fieldValue.toLowerCase())) {
      document.getElementById(fieldName).focus();
      alert("'" + fieldValue + "' is not a valid email address.");
      throw new Error("Invalid email detected.");
    }

  }
  return fieldValue;
}

function escapeXML(oldValue) {
  var re = new RegExp("&+(?!amp;)", "g");
  return oldValue.replace(re, "&amp;");
}

function checkUrl(fieldName) {
  fieldValue = document.getElementById(fieldName).value;

  if (fieldValue != "") {
    origFieldValue = fieldValue;

    if (fieldValue.indexOf("http://") != 0
        && fieldValue.indexOf("https://") != 0) {
      fieldValue = "http://" + fieldValue;
    }

    if (!isUrl(fieldValue)) {
      document.getElementById(fieldName).focus();
      alert("'" + origFieldValue + "' is not a valid URL.");
      throw new Error("Invalid URL detected.");
    }

  }
  return escapeXML(fieldValue);
}

function generate() {

  if (!allMandatoryFieldsOK())
    return false;

  try {
    var today = new Date();
    var name = document.getElementById("name").value;
    var id = document.getElementById("shortname").value;
    var homepage = checkUrl("homepage");
    var blog = checkUrl("blog");
    var blogfeed = checkUrl("blogfeed");
    var bug_database = checkUrl("bug_database");
    var mailing_list_1 = checkUrl("mailing_list_1");
    var mailing_list_2 = checkUrl("mailing_list_2");
    var repo = checkUrl("repo");
    var repo_browse = checkUrl("repo_browse");

  } catch (e) {
    return;
  }
  var licence = document.getElementById("licence").value;
  var repo_type = document.getElementById("repo_type").value;
  var shortdesc = document.getElementById("shortdesc").value;
  var description = document.getElementById("description").value;
  
  var langs = document.getElementById("langs").value;
  var categoriesOptions = document.getElementById("categories").options;

  var doap = "<rdf:RDF xmlns:rdf=\"http://www.w3.org/1999/02/22-rdf-syntax-ns#\"\n";
  doap += "     xmlns:rdfs=\"http://www.w3.org/2000/01/rdf-schema#\"\n";
  doap += "     xmlns=\"http://usefulinc.com/ns/doap#\"\n";
  doap += "     xmlns:foaf=\"http://xmlns.com/foaf/0.1/\"\n";
  doap += "     xmlns:v=\"http://www.w3.org/2006/vcard/ns#\"\n";
  doap += "     xmlns:dc=\"http://purl.org/dc/core/\"\n";
  doap += "     xmlns:simal=\"http://oss-watch.ac.uk/ns/0.2/simal#\"\n";
  doap += ">\n";
  doap += "<Project rdf:about=\"" + BASE_NAMESPACE_URL + "projects/" + id
      + "\">\n";
  doap += " <created>" + today.getFullYear() + "-" + (today.getMonth() + 1)
      + "-" + today.getDate() + "</created>\n";
  doap += " <name>" + name + "</name>\n";
  doap += " <shortname>" + id + "</shortname>\n";
  doap += " <shortdesc xml:lang=\"en\">" + shortdesc + "</shortdesc>\n";
  doap += " <description xml:lang=\"en\">" + description + "</description>\n";
  
  if (homepage != "") {
    doap += " <homepage rdf:resource=\"" + homepage + "\"/>\n";
  }

  if (blog != "") {
    doap += " <blog>\n";
    doap += "  <foaf:Document rdf:about=\"" + blog + "\"/>\n";
    doap += " </blog>\n";
  }

  if (blogfeed != "") {
    doap += " <blog>\n";
    doap += "  <foaf:Document rdf:about=\"" + blogfeed + "\">\n";
    doap += "   <dc:format>application/rss+xml</dc:format>\n";
    doap += "  </foaf:Document>\n";
    doap += " </blog>\n";
  }

  for (i=0;i<categoriesOptions.length; i++) {
    if(categoriesOptions[i].selected) {
      doap += " <category rdf:resource=\"" + categoriesOptions[i].value + "\"/>\n";
    }
  }

  if (langs != "") {
    var langsArray = langs.split(",");
    for ( var i = 0; i < langsArray.length; i++) {
      doap += " <programming-language>" + langsArray[i].trim()
          + "</programming-language>\n";
    }
  }

  doap += processPeople();

  if (repo != "") {
    doap += " <repository>\n";
    doap += "  <" + repo_type + ">\n";
    doap += "    <location rdf:resource=\"" + repo + "\"/>\n";
    if (repo_browse != "") {
      doap += "    <browse rdf:resource=\"" + repo_browse + "\"/>\n";
    }
    doap += "  </" + repo_type + ">\n";
    doap += " </repository>\n";
  }

  if (bug_database != "") {
    doap += " <bug-database rdf:resource=\"" + bug_database + "\"/>\n";
  }

  if (mailing_list_1 != "") {
    doap += " <mailing_list rdf:resource=\"" + mailing_list_1 + "\"/>\n";
  }

  if (mailing_list_2 != "") {
    doap += " <mailing_list rdf:resource=\"" + mailing_list_2 + "\"/>\n";
  }

  if (licence != "") {
    doap += " <license rdf:resource=\"http://usefulinc.com/doap/licenses/"
        + licence + "\"/>\n";
  }

  if (isJiscFunded()) {
    doap += " <foaf:fundedBy>\n";
    doap += "   <foaf:Organization rdf:about=\"http://www.jisc.ac.uk\">\n";
    doap += "     <foaf:name>JISC</foaf:name>\n";
    doap += "   </foaf:Organization>\n";
    doap += " </foaf:fundedBy>\n";

    doap += getJiscFundedFields();
  }

  doap += "</Project>\n";
  doap += "</rdf:RDF>\n";

  commitDoapFile(doap);

  var doap_div = document.getElementById("doap");
  doap_div.textContent = doap;
}

function getJiscFundedFields() {
  var project_partners = document.getElementById("project_partners").value;
  var resp_reports_name = document.getElementById("resp_reports_name").value;
  var projectemail = checkEmail("projectemail");
  var resp_reports_email = checkEmail("resp_reports_email");
  var resp_reports_phone = document.getElementById("resp_reports_phone").value;
  var resp_reports_skype = document.getElementById("resp_reports_skype").value;
  var cont_licence = document.getElementById("cont_licence").value;
  var cont_licence_juris = document.getElementById("cont_licence_juris").value;
  var lead_institution = document.getElementById("lead_institution").value;
  var department = document.getElementById("department").value;
  var dept_postcode = document.getElementById("dept_postcode").value;

  var primary_product = document.getElementById("primary_product").value;
  var secondary_product = document.getElementById("secondary_product").value;
  var primary_audience = document.getElementById("primary_audience").value;
  var secondary_audience = document.getElementById("secondary_audience").value;
  var jiscmanager = document.getElementById("jiscmanager").value;

  var jiscdoap = '';
  
  jiscdoap += " <simal:reporter>\n";
  jiscdoap += "  <foaf:Person rdf:about=\"" + PEOPLE_NAMESPACE_URL
      + hex_sha1(resp_reports_email) + "\">\n";
  jiscdoap += "   <foaf:name>" + resp_reports_name + "</foaf:name>\n";
  jiscdoap += "   <foaf:mbox rdf:resource=\"mailto:" + resp_reports_email
      + "\" />\n";
  jiscdoap += "   <foaf:mbox_sha1sum>" + hex_sha1(resp_reports_email)
      + "</foaf:mbox_sha1sum>\n";
  if (resp_reports_phone != "") {
    jiscdoap += "   <foaf:phone>" + resp_reports_phone + "</foaf:phone>\n";
  }
  if (resp_reports_skype != "") {
    jiscdoap += "   <foaf:skypeID>" + resp_reports_skype + "</foaf:skypeID>\n";
  }
  jiscdoap += "  </foaf:Person>\n";
  jiscdoap += " </simal:reporter>\n";

  jiscdoap += " <simal:reporter>\n";
  jiscdoap += "  <foaf:Person rdfs:label=\"Project account\" rdf:about=\""
      + PEOPLE_NAMESPACE_URL + hex_sha1(projectemail) + "\">\n";
  jiscdoap += "   <foaf:mbox rdf:resource=\"mailto:" + projectemail + "\" />\n";
  jiscdoap += "  </foaf:Person>\n";
  jiscdoap += " </simal:reporter>\n";

  if (lead_institution != "") {
    jiscdoap += "<vendor><foaf:Organization dc:title=\"Lead institution\"><foaf:name>"
        + lead_institution + "</foaf:name>";
    if (department != "") {
      jiscdoap += "<foaf:member><foaf:Organization dc:title=\"Department\"><foaf:name>"
          + department + "</foaf:name>\n";
      if (dept_postcode != "") {
        jiscdoap += "<foaf:based_near><v:Address><v:postal-code>"
            + dept_postcode + "</v:postal-code></v:Address></foaf:based_near>";
      }
      jiscdoap += "</foaf:Organization></foaf:member>\n";
    }

    if (project_partners != "") {
      var partnersArray = project_partners.split(",");
      for ( var i = 0; i < partnersArray.length; i++) {
        jiscdoap += " <simal:projectPartner><foaf:Organization><foaf:name>"
            + partnersArray[i].trim();
        jiscdoap += "</foaf:name></foaf:Organization></simal:projectPartner>\n";
      }
    }
    jiscdoap += "</foaf:Organization></vendor>\n";
  }

  if (cont_licence != "") {
    if (cont_licence.indexOf("CC") == 0 && cont_licence_juris != "") {
      cont_licence += "-" + cont_licence_juris;
    }
    jiscdoap += " <license rdf:resource=\"http://usefulinc.com/doap/licenses/"
        + cont_licence + "\"/>\n";
  }

  if (primary_product != "none") {
    jiscdoap += " <category rdf:resource=\"" + CATEGORY_NAMESPACE_URL;
    jiscdoap += primary_product.split(' ').join('').toLowerCase()
        + "\" rdfs:label=\"" + primary_product + "\" />\n";
  }

  if (secondary_product != "none") {
    jiscdoap += " <category rdf:resource=\"" + CATEGORY_NAMESPACE_URL;
    jiscdoap += secondary_product.split(' ').join('').toLowerCase()
        + "\" rdfs:label=\"" + secondary_product + "\" />\n";
  }

  if (primary_audience != "") {
    jiscdoap += " <audience>" + primary_audience + "</audience>\n";
  }

  if (secondary_audience != "none") {
    jiscdoap += " <audience>" + secondary_audience + "</audience>\n";
  }

  if (jiscmanager != "") {
    jiscdoap += " <simal:fundingManager>" + jiscmanager
        + "</simal:fundingManager>\n";
  }

  return jiscdoap;
}

function fetchAllCategories() {
  var loc = SIMAL_REST_BASE_URL + "/allCategories/json";
  loc = Widget.proxify(loc);
  var xml_request = new XMLHttpRequest();

  xml_request.open("GET", loc, true);
  xml_request.setRequestHeader("Cache-Control", "no-cache");
  
  xml_request.onreadystatechange = function() {
    if (xml_request.readyState == 4) {
      if(xml_request.status == 200) {
        processCategories(JSON.parse(xml_request.responseText));
      } else {
        reportError(xml_request);
      }
    }
  };
  
  xml_request.send("");
}

function commitDoapFile(doap) {
  var loc = SIMAL_REST_BASE_URL + "/addProject";
  loc = Widget.proxify(loc);
  var xml_request = new XMLHttpRequest();

  xml_request.open("POST", loc, true);
  
  xml_request.onreadystatechange = function() {
    if (xml_request.readyState == 4 && xml_request.status == 200) {
      if (xml_request.responseText.indexOf("http") == 0) {
        alert('Project successfully saved to the registry.\n' + 'Visit the project here: ' + xml_request.responseText + 'Please keep the contents for your record.');
      } else {
        reportError(xml_request);
      }
    } else if (xml_request.readyState == 4 && xml_request.status == 201) {
      if (xml_request.responseText.indexOf("http") == 0) {
        alert('Project successfully saved to the registry.\n' + 'Visit the project here: ' + xml_request.responseText + 'Please keep the contents for your record.');
      } else {
          reportError(xml_request);
      }
    } else if (xml_request.readyState == 4 && (xml_request.status == 500 || xml_request.status == 403)) {
        reportError(xml_request);
    }
  };
  
  xml_request.setRequestHeader("Cache-Control", "no-cache");
  xml_request.setRequestHeader("Content-Type", "text/xml");
  xml_request.send(doap);
  alert("Please copy and paste the green code (xml) and paste into a local file.\n"
      + "I.e. copy and paste into a .txt file and then save on the 'about' page of your project blog.\n\n");
}

function reportError(xml_request) {
	alert('It seems that saving the project remotely did not succeed. The status code is ' + xml_request.status + ' The message from the remote server is :\n\'' + xml_request.responseText + '\'\nPlease report this message and submit DOAP to http://code.google.com/p/simal/issues/list');
}
