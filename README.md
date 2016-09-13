# Enterprise Sales Procurement Model (ESPM) Application

ESPM (Enterprise Sales & Procurement Model) is reference application which demonstrates how to build applications on SAP HANA Cloud Platform (HCP) with the Java runtime. It also consumes application services like Persistence Service, Document Service, SAP Jam Service, API Management Service which is offered by the platform. The application User Interface (UI) is built with SAPUI5 framework and has SAP FIORI look and feel. 

# Business Scenario
The business scenario is that of an eCommerce site that sells electronic products. 

- The Customers can order products and provide review on the products
- The Retailer can then accept the sales orders created against orders created by customers. The Retailer can also update the product stock information.

![Usecase Diagram](/docs/images/ESPMUseCase.png?raw=true)

# Get the Source Code
Clone the Git [repository](https://github.com/SAP/cloud-espm-v2.git) or download the latest release.

# 1. Quick start guide
### Setting up the developer environment
1. Install [SAP JVM 7.*](https://tools.hana.ondemand.com/#cloud) or Java JDK 1.7 and setup the JAVA_HOME and PATH environment variables in your local machine
2. Install [Eclipse](https://help.hana.ondemand.com/help/frameset.htm?761374e5711e1014839a8273b0e91070.html). Please download Eclipse Mars
3. [Install SAP Development Tools for Eclipse](https://help.hana.ondemand.com/help/frameset.htm?76137a37711e1014839a8273b0e91070.html)
4. Install the [SAP HANA Cloud SDK](https://help.hana.ondemand.com/help/frameset.htm?7613843c711e1014839a8273b0e91070.html). Please download Java EE 6 Web Profile SDK
5. [Setup the Runtime Environment](https://help.hana.ondemand.com/help/frameset.htm?7613f000711e1014839a8273b0e91070.html). please use the Java EE 6 Web Profile section in the above document
6. Signup for [HCP Trial account](https://hcp.sap.com/developers.html#section_4) 

### Build the application and deploy
1.Git configuration in Eclipse
   - From the Eclipse IDE main menu, choose Window > Preferences
   - Enter git in the filter field in the top-left corner.
   - Navigate to Team > Git > Configuration and select the Configuration node and add the following configuration

![EGit Configuration](/docs/images/EGitConfig.PNG?raw=true)

**Note! For most people the proxy value doesn’t need to be set but if you are working behind a proxy, then it should be set as per you environment**
 
2.Maven configuration
   - From the Eclipse IDE main menu, choose Window > Preferences
   - Enter maven in the filter field in the top-left corner
   - Navigate to Maven > User Settings and select the User Settings node
   - If you are using Maven for the first time you need to create a settings.xml file at location C:/Users/<your-user-name>/.m2/settings.xml. The contents of the settings .xml file should looks like below snippet:
   
![Maven Settings Configuration](/docs/images/MavenSettings.PNG?raw=true)
   - Note: If you are not working behind a proxy firewall you can remove the entire proxy section from the snippet
   - Information:  If you have already installed Maven before you can click open file link and add e.g. the proxy configuration to your settings.xml if not already there.

    ```sh
        <settings xmlns="http://maven.apache.org/SETTINGS/1.0.0"
        	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
        	xsi:schemaLocation="http://maven.apache.org/SETTINGS/1.0.0 http://maven.apache.org/xsd/settings-1.0.0.xsd">
        	<localRepository>${user.home}/.m2/repository</localRepository>
        	<profiles>
        		<profile>
        			<id>development</id>
        			<activation>
        				<activeByDefault>true</activeByDefault>
        			</activation>
        			<properties>
        			</properties>
        		</profile>
        	</profiles>
        	<proxies>
        	<proxy>
        		<active>true</active>
        		<protocol>http</protocol>
        		<host>proxy</host>
        		<port>8080</port>
        	</proxy>
        </proxies>
        </settings>
    ```


3.Clone Git repository and import Maven project
   - Open https://github.com/SAP/cloud-espm-v2 with your web browser
   - Click on the Copy to clipboard so that the Git repository URL of the opened cloud-basecamp GitHub repository is copied to your clipboard.

![Repo URL](/docs/images/repoclone.png?raw=true)
   - In Eclipse, open the Git perspective. From the Eclipse IDE main menu, choose Window > Open Perspective > Other.... Select Git and choose Ok to open the Git perspective. 
   - In this perspective you have the Git Repositories view on the left. As long as there is no Git Repository defined you will see 3 links (as shown here) to add a repository to the view.

![Git Clone](/docs/images/GitClone.PNG?raw=true)
   - In the corresponding menu (top-right of the view), click on the Clone a Git repository link.
   - Because you copied before the cloud-basecamp Git repository URL to your clipboard, the fields (URI, Host, Repository path and Protocol) of the opened dialog are filled automatically with the correct values.
   - Do not change anything, just click Next >
   - On this wizard page check that the master branch is selected and click again on Next >
   - On the last wizard page you can adjust the location of the local Git Repository, but for the scope of this tutorial we'll just leave the default as-is
   - Click on Finish so that the remote cloud-basecamp Git repository (source code) is cloned to the local location specified on the last wizard page.
   - In Eclipse, open File->Import->Existing Maven projects.

4.Update dependencies and build Maven project

- Instruction to run update dependencies for the Maven project
 - Right click on the web project in ESPM > and choose Maven > Click on Update Project
- Note! you need to modify the parent pom.xml for certain property values depending on your environment:
 - local.server.proxy.settings - comment this out if you are not behind a proxy server. Else update your proxy settings here
 - browser.proxy.settings - comment this out if your browser is not using a proxy. Else update your browser proxy settings here
 - sap.cloud.sdk.version - The SAP HANA Cloud Platform SDK for Java EE 6 Web Profile version that you intend to run the application with. the minimum version supported is 2.87.10
 - olingo.version - The Apache Olingo version that you intend the application to run with. The minimum version supported is 2.0.6
    
 The application can be built with the maven command to the parent pom.xml

    ```sh
    clean install
    ```

**The unit tests and the integration tests are run by default when building the project with goal "clean install"**

5.Deploy the application on local Cloud Runtime

i)Run the application in HCP Java EE 6 Web Profile Server

- Right click on the web project in ESPM > and choose the Run on Server option
       
![Run ESPM Locally](/docs/images/RunESPM1.png?raw=true)
- Make sure that Manually define a new server is selected and choose SAP > Java EE 6 Web Profile Server as server type. Leave all other settings unchanged and choose Finish

![Run ESPM Finish](/docs/images/RunESPM2.png?raw=true)
- Now a local server is started that has your espm application deployed.

ii)Create Users and Assign Role

- To enable local users to access the Retailer UI, you need to define user IDs in the local user store and assigned the role “Retailer” to this user. 
       
  Create a user with the below information
            
            		ID		Name	Password	Role
            		ret   	ret		123			Retailer

![Create User locally](/docs/images/localuser.png?raw=true)
- The eCommerce site can be accessed via the URL: https://localhost:\<port\>/espm-cloud-web/webshop
- The Retailer UI can be accessed via the URL: https://localhost:\<port\>/espm-cloud-web/retailer

6.Deploy the application on SAP HCP via the cockpit

 **Note! The application name must be "espm", else the above URL will change based on the application name given during deployment**
 
   - Deploy the application in your SAP HANA Cloud Platform Trial account. 
     - Note! If you deploy with the console client, make sure to specify the --java-version parameter with value 7. Note! The application name must be espm.
   - Configure the application role assignments from the [cockpit](https://help.hana.ondemand.com/help/frameset.htm?db8175b9d976101484e6fa303b108acd.html). You basically need to add the "Retailer" role to your SAP HANA Cloud Platform user to access the Retailer UI

    You can access the application from the URL
    
    * The eCommerce site can be accessed via the URL: https://espm\<account\>.hanatrial.ondemand.com/espm-cloud-web/webshop
    * The Retailer UI can be accessed via the URL: https://espm\<account\>.hanatrial.ondemand.com/espm-cloud-web/retailer

    **Note! The application name must be "espm", else the above URL will change bsaed on the application name given during deployment**


7.Bind the database to espm application and start espm application

Below is the process to bind the database to the java application in HCP trial account using a Shared HANA database

   - In the cockpit, select an account and choose Persistence -> Databases & Schemas -> in the navigation area.
   - Click on the new button
   - In the popup window, enter the below information
   	```sh
	Schema ID: espm
	Database System: HANA (<shared>)
	Version: 1.00*
	Click on Save button
	```
	
   - In the cockpit, select an account and choose Applications -> Java Application -> Click on the name of the espm application that you deployed
   - In the navigation area in the cockpit, select Configuration -> Data Source Bindings
   - Click "New Binding" button in detail plane
   - In the popup window, enter the below information
   	```sh
	Data Source - <default> 
	DB/Schema ID - select espm( the one that you created above)
	Click on Save button
	```

   - Now you need to restart your espm application ( stop(if already started) and start the application) from the cockpit. 
   
   [help information on Binding applications to Database on HCP - Link 1](https://help.hana.ondemand.com/help/frameset.htm?1742986c3cfa47099442aee0cf8df5e9.html).
   
   [help information on Binding applications to Database on HCP - Link 2](https://help.hana.ondemand.com/help/frameset.htm?216cef2158cc419fade9a8247d5008fa.html).


### Demo script for [ESPM Webshop](/docs/demoscript/WebshopREADME.md) 
### Demo script for [ESPM Retailer-SalesorderApproval](/docs/demoscript/Retailer_SalesOrderApprovalREADME.md)
### Demo script for [ESPM Retailer-StockUpdate](/docs/demoscript/Retailer_StockUpdateREADME.md)
### Documentation for [Document Service](/docs/documentation/DocumentServiceREADME.md)
### Documentation for [SAP JAM Integration](/docs/documentation/SAPJAMIntegrationREADME.md)
# 2.Deep-dive guide
### Architecture Overview
The following diagram provides an overview of the ESPM Sample application architecture: 

![Architecture Diagram](/docs/images/architecture.jpg?raw=true)

Reading the above architecture diagram from top to bottom, we have the following components

 - **SAPUI5 layer** - All the front-end code of the application is written in SAPUI5. The end user accesses the application using web browser (preferably latest Google Chrome)
 - **OData layer** - The ESPM application services are implemented in OData version 2.0. We use Apache Olingo for transforming JPA Models into OData Services. For more information on Apache Olingo JPA Models transformation to OData services, see [Details](http://olingo.apache.org/doc/odata2/tutorials/CreateWebApp.html) 
 - **JPA Layer** - The persistence of the application is implemented using Java JPA. We use EclipseLink as the persistence provider
 - **Persistence** - The persistence used for the application is SAP HANA DB in Cloud (HCP). All the tables modelled in Java JPA are created in the HANA DB

The ESPM Sample Application is a Maven based project which has a parent pom.xml file and 2 sub projects as below 

 - **espm-cloud-jpa** - This project contains the source code implementation for the backend/JPA along with unit tests. Below is the JPA Class diagram.
 - **espm-cloud-web** - This project contains the source code implementation for generating the odata services for JPA entities implemented in "espm-cloud-jpa" and front end of the application created using SAPUI5. The OData integration tests are also implemented in this package

### The JPA Class diagram

![JPA Class Diagram](/docs/images/espm-cloud-jpa.png?raw=true)
### ESPM Source Code packages
**The espm-cloud-jpa has the following packages:**

* src/main/java
	* com.sap.espm.model - JPA Model classes for persistence
	* com.sap.espm.model.data - Data loader classes to populate the master data for the eCommerce site like Product information etc
	* com.sap.espm.model.util - The utility classses

* src/main/resources
	* com.sap.espm.model.data - XML files containing master data for the eCommerce site like Products, Product Categories etc

* src/test/java
	* com.sap.espm.model - The Unit tests for the database entities
	* com.sap.espm.model.util - The test factory class

**espm-cloud-web has the following packages:**

* src/main/java
	* com.sap.espm.model.function.impl - The classes for function import implementation for odata services using Apache Olingo
	* com.sap.espm.model.web - the Startup servlet and Apache Olingo OData service implementation classes

* src/test/java
	* com.sap.espm.model.web - The OData integration tests classes
	* com.sap.espm.model.web.util - The utility classes for the tests

* src/test/resources
	* com.sap.espm.model.web - The XML files for the OData Integration tests

**The UI is located in webapps folder. The frontend is implemented in SAPUI5.**
### Securing OData Services

* The ESPM application consists of 2 parts - **ESPM Webshop** and **ESPM Retailer**. The application is split into 2 parts by a secure URL and non-secure URL.

        Secure URL:
        http://<appname><accountname>.hana.ondemand.com/espm-cloud-web/espm.svc/secure
        
        Non secure URL
        http://<appname><accountname>.hana.ondemand.com/espm-cloud-web/espm.svc/

* The WebShop application uses non secure URL as any user without any logon credentials can create a sales order. The Retailer application uses secure URL as only a user with Java EE Web role Retailer can access it. To implement security we use a mix of Java EE Web roles and Servlet Filter

![Web.xml for security](/docs/images/webxml.png?raw=true)
* Any access to the secure entity and respective HTTP method via secure url (../espm.svc/secure/)is secured by Java EE Web role Retailer. The form authentication is specified for this Retailer and url-pattern /espm.svc/secure is restricted to this role. This is translated to SAML authentication on deploying in cloud

![Secure URL](/docs/images/secureurl.png?raw=true)
* Any access to secure entity and respective HTTP via non-secure url is restricted by a servlet filter. The servlet filter is defined under the package com.sap.espm.model.web as EspmServiceFactoryFilter class and specified in the web.xml. The logic for restricting access to secure entities via non-secure url is implemented in isPathRestricted()

![Secure URL](/docs/images/servletfilter.png?raw=true)

After deploying the application in HCP, assign the Retailer role to the user who will act as the retailer of the eCommerce site. Please refer to documentation of SAP HANA Cloud Platform on how to assign roles to users. See [Details](https://help.hana.ondemand.com/help/frameset.htm?db8175b9d976101484e6fa303b108acd.html)

# Important Disclaimers on Security and Legal Aspects
This document is for informational purposes only. Its content is subject to change without notice, and SAP does not warrant that it is error-free. SAP MAKES NO WARRANTIES, EXPRESS OR IMPLIED, OR OF MERCHANTABILITY, OR FITNESS FOR A PARTICULAR PURPOSE.

# Coding Samples
Any software coding and/or code lines / strings ("Code") included in this documentation are only examples and are not intended to be used in a productive system environment. The Code is only intended to better explain and visualize the syntax and phrasing rules of certain coding. SAP does not warrant the correctness and completeness of the Code given herein, and SAP shall not be liable for errors or damages caused by the usage of the Code, unless damages were caused by SAP intentionally or by SAP's gross negligence.

# Copyright and License
Copyright 2016 SAP SE

Licensed under the Apache License, Version 2.0 (the "License"); you may not use this work except in compliance with the License. You may obtain a copy of the License in the LICENSE file, or at:
[http://www.apache.org/licenses/LICENSE-2.0](http://www.apache.org/licenses/LICENSE-2.0)
Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
