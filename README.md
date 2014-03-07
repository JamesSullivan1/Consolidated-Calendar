Consolidated-Calendar
=====================

Consolidated Calendar Application for CPSC301 - University of Calgary, W2014

------
Description
------
  Consolidated Calendar is a web-based service to pull calendar events from multiple .ics links, and push them
  to the user's Google Calendar. The application parses the .ics feeds and interacts with Google Calendar's APIs 
  to ensure that no duplicate events are added for the user. A planned feature is for the application to detect
  overlapping events and provide the user with a conflict resolution dialogue.
  
-----
Setup
-----
  The minimal setup will require the latest version of JRE, and Apache Tomcat7. Apache Ant is reccomended for ease of setup.
 
 <li> 
  <ol>1) Clone the repository into the $CATALINA_HOME/webapps directory. Your $CATALINA_HOME can be found in the configuration files for Tomcat.</ol>
  <ol>2) Use Apache Ant to build the source code.</ol>
    ant build
  
  </li>

