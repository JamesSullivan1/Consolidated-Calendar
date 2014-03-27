Consolidated-Calendar
=====================

Consolidated Calendar Application for CPSC301 - University of Calgary, W2014

<li>
  <ul>Scott Lamb</ul>
  <ul>James Sullivan</ul>
  <ul>Daniel Wehr</ul>
  <ul>Michal Widomski</ul>
</li>

------
Description
------
  Consolidated Calendar is a web-based service to pull calendar events from multiple .ics links, and push them
  to the user's Google Calendar. The application parses the .ics feeds and interacts with Google Calendar's APIs 
  to ensure that no duplicate events are added for the user. A planned feature is for the application to detect
  overlapping events and provide the user with a conflict resolution dialogue.
  
  
-----
Requirements
-----
<li>
  <ul>1) Up to date installation of JRE 7</ul>
  <ul>2) Up to date installation of Apache Tomcat7</ul>
  <ul>3) A Google account</ul>
  <ul>4) An account with any other service that provides a public link to a .ics calendar feed (For example, D2L)</ul>
</li>
-----
Setup
-----
 <li> 
  <ol>1) Ensure that Tomcat7 is configured to use port 8080 for HTTP/1.1, which can be set in $TOMCAT_HOME/conf/server.xml</ol>
  <ol>1) Download the master repository as a .zip file and extract to a local folder.</ol>
  <ol>2) Move the 'Consolidated-Cal.war' deployment file into $TOMCAT_HOME/webapps.</ol>
  <ol>3) Restart (or start) your Apache Tomcat server.</ol>
  <ol>4) Open a web browser (recommended to use Firefox or Chrome) and point to the following URL:
  <code>localhost:8080/Consolidated-Cal/</code></ol>
 </li>
-----
Use
-----
  Follow the instructions displayed on your browser to use the application. The link to the .ics file will vary based on the service that you are importing events from. As an example, the Desire2Learn service provides the user with a static .ics feed link by selecting 'Calendar' from your homepage, followed by 'Subscribe'.
