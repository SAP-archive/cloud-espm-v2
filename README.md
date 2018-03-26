# Enterprise Sales Procurement Model (ESPM) Application

## Description
The ESPM (Enterprise Sales & Procurement Model)application is a reference application which demonstrates how to build applications on SAP Cloud Platform (HCP) with the Java runtime.The application also consumes and showcases services like the Persistence Service, Document Service, SAP Jam Collaboration, and API Management Service which are offered by the platform. The application User Interface (UI) is built with the SAPUI5 framework after the SAP FIORI Fiori design principles. 

### Business Scenario
The business scenario is that of an eCommerce site that sells electronic products. 

- Customers give reviews and ratings to the products they have purchased.
- Retailers would like to analyze this data to better sell their products
- Retailers post their questions on SAP Jam, and a “Bot” answers them.
	Eg:  What are my top rated products?
	What products are preferred by youngsters?

![datamodel](/docs/images/ML_DataModel.png?raw=true)

# Requirements

  - Make sure that you have enabled SAP Jam service in account.hanatrial.com as shown in the below screen shot

![Services](/docs/documentation/SAPJAMIntegrationImages/EnableJamService.png?raw=true)

# Download and Installation

## 1.Clone Git repository and import Maven project
   - Open https://github.com/SAP/cloud-espm-v2/ with your web browser.
   - Click on the Copy to clipboard so that the Git repository URL of the opened GitHub repository is copied to your clipboard.

![gitclonenew](/docs/images/gitclonenew.PNG?raw=true)

   - In Eclipse, open the Git perspective. From the Eclipse IDE main menu, choose Window > Open Perspective > Other.... Select Git and choose Ok to open the Git perspective. 
   - In this perspective you have the Git Repositories view on the left. As long as there is no Git Repository defined you will see 3 links (as shown here) to add a repository to the view.

![Git Clone](/docs/images/GitClone.PNG?raw=true)

   - In the corresponding menu (top-right of the view), click on the Clone a Git repository link.
   - Because you copied before the ESPM repository URL to your clipboard, the fields (URI, Host, Repository path and Protocol) of the opened dialog are filled automatically with the correct values.
   - Do not change anything, just click Next >.
   - On this wizard page, select the branch as ‘ML’ and click again on Next >.
   - On the last wizard page you can adjust the location of the local Git Repository, but for the scope of this tutorial we'll just leave the default as-is.
   - Click on Finish so that the remote ESPM repository (source code) is cloned to the local location specified on the last wizard page.
   - In Eclipse, open File->Import->Existing Maven projects and import the Maven project.

## 2.Update dependencies and build Maven project

- Instruction to run update dependencies for the Maven project
 - Right click on the web project in ESPM > and choose Maven > Click on Update Project
 ![Maven Update](/docs/images/MavenUpdate.PNG?raw=true)
- Note! If you face errors you need to modify the parent pom.xml for certain property values depending on your environment:
 - local.server.proxy.settings - delete this if you are not behind a proxy server, else update your proxy settings here
 - browser.proxy.settings - delete this if your browser is not using a proxy, else update your browser proxy settings here
 - The SAP Cloud Platform SDK for Java Web Tomcat 8 version that you intend to run the application with,is version 3.20.3.1
 - olingo.version - The Apache Olingo version that you intend the application to run with, the minimum version supported is 2.0.7
    
 The application can be built with the maven command to the parent pom.xml :

    clean install
   
 After building the application update the Maven Project :
 
    Right click on the web project in ESPM > and choose Maven > Click on Update Project
    Check if two wars (espm-cloud-jambott.war and espm-cloud-web.war) are created in the folder espmv2.0\deploy
    
**The unit tests and the integration tests are run by default when building the project with goal "clean install"**

## 3.Deploy the application 

### i)Run the application in SAP CP 

- Download neo sdk console client from https://tools.hana.ondemand.com/#cloud and unzip it.
- Go to the tools folder and run the following command using your HCP account credentials
	```
	neo deploy -a <account_name> -b espm -s <Git_location>\espmv2.0\deploy -h hanatrial.ondemand.com --password <password> -u <username>
	```
- Go to you hanatrial cockpit and open Java applications. ESPM application should be available there.
- Start the application. Two links should be available.
![application_links](/docs/images/Application_Links.png?raw=true)
- The first link references ESPM Jam Bot application. This application has a model that can classify the questions submitted via SAP JAM and give appropriate responses to the Customers. The second link references the ESPM application itself.

### ii)Add an oAuth Client

- Go to your SAP Jam Service in your hanatrial account (Services> SAP Jam) and Click on the link 'Go to Service'. In case of error, check Pre-requisites.
- Go to the SAP Jam Admin console in developer.sapjam.com and select  Integrations  >OAuth Clients  from the left side navigation. The OAuth Clients page displays.
![sap_jam_admin](/docs/images/SAP_Jam_Admin.PNG?raw=true)
![oauth_client](/docs/images/OAuthClient.png?raw=true)
- This page presents a catalog of previously configured OAuth Clients, with UI controls that allow you to View, Edit, or Delete existing OAuth Clients, or to add a new OAuth client (Add OAuth Client).
- To add an OAuth client, click Add OAuth Client at the upper right corner of the page. The Register a new OAuth Client page displays.
![new_oauth_client](/docs/images/NewOAuthClient.png?raw=true)
- In the Name field, enter a meaningful name that will allow company administrators to recognize what the client is.
- (Optional) From the Feed Filtering drop-down menu, select either none or SAP CRM.
- In the Integration URL field, enter any URL (Eg: example.com).
- (Optional) In the Callback URL field, enter the callback URL for the client application's API calls.
- (Optional) In the Support URL field, enter the support URL for the client application's API.
- (Optional) Select the Can Suppress Notifications checkbox to allow the suppression of notifications from external data sources that use this OAuth client. It is up to the developer of this external application integration whether they will disable notifications or not, but this setting determines whether notification suppression will be permitted from this external application.
- (Optional) In the X509 Certificate (Base64) text box, enter the Transport Layer Security (TLS; supercedes SSL) public key certificate string for the client application's API access.
- (Optional) The Administrative Area drop-down menu allows you to select the area in which you want this OAuth Client configuration to be available. The default is "Company", which will make it available to all groups and areas. Selecting a specific area will limit the scope of the OAuth Client configuration and will limit the management of that configuration to either area administrators assigned responsibility for that area or to company administrators.


When all of the above settings are complete, click Save to save the record and establish the trust relationship with the OAuth client application.
You are returned to the OAuth Clients page, with the OAuth client record that you just added listed in the catalog.

### iii)Create an Alias User
- In SAP Jam, click on the "cog" settings icon and select Admin. The SAP Jam Admin page will appear.
- Select Users > Alias Accounts.
- Click New Alias Account.
- Create an Alias Account Name in the Alias Account Name field.
- Click Add an OAuth2 Access Token. Select an OAuth Client from the drop-down menu and click OK.
- Click Save changes.
- Copy the new OAuth2 Access Token and paste it as the JAM_OAUTH_TOKEN in espmv2.0\espm-cloud-jambott\src\main\resources\utils\util.properties
![alias_account](/docs/images/Alias_Account.png?raw=true)
![oauth2_access_token](/docs/images/OAuth2_Access_Token.png?raw=true)
### iv)Create a new Push Notification Subscription on Jam
With an alias user created, we could create a new push notification subscription that triggers webhook calls whenever the alias user is @mentioned.

- In the admin settings page select Integrations > Push Notifications.
- Click +Push Notification Subscription.
- Create a name for the Push Notification in the Name field.
- Paste the URL of the ESPM Jam Bot Application you just deployed on SAP Cloud Platform into the Callback URL field [https://espm<account_name>.hanatrial.ondemand.com/espm-cloud-jambott].
- Set the Scope to Alias Account and enter the alias user we just created in the Alias Account field.
- Enable all the Alias Account Notifications in Event List.
- Copy the Token field and paste it as the JAM_WEBHOOK_VERIFICATION_TOKEN in espmv2.0\espm-cloud-jambott\src\main\resources\utils\util.properties 
- Click Save.
![push_notification1](/docs/images/Push_Notification1.png?raw=true)
![push_notification2](/docs/images/Push_Notification2.png?raw=true)

### v)Redeploy the application
- Paste the oData endpoint of the ESPM Web Application you just deployed on SAP Cloud Platform (https:// espm<>.hanatrial.ondemand.com/espm-cloud-web/ espm.svc/) as espmendpoint in espmv2.0\espm-cloud-jambott\src\main\resources\utils\util.properties 
- Using the steps 2 to 7 in i)Run the application in SAP CP, redeploy your application.
### vi)Enable Your Webhook in Jam
- Now go back to Jam and select Integrations > Push Notifications.
- Click on the slider to enable your webhook.
- Your webhook servlet is now enabled.
### vii)Trigger Webhook Callbacks
Your webhook servlet should be ready to receive webhook calls. Try creating @mentions at the Alias User you've created. The webhook server should be triggered and automatically post a reply comment to the @mention on Jam.
![sample_query](/docs/images/Sample_Query.png?raw=true)

## 4.Regenerating the Model with New or Updated data (Optional)
- Go to the java class espm-cloud-jambott\src\main\java\com\sap\espm\services\TextLearner.java and update the path to your new data set
![text_learner](/docs/images/TextLearner.JPG?raw=true)

- Run TextLearner.java as 'Java Application'. New model 'classifier.dat' will be saved in the root folder.
- Replace the 'classifier.dat' in the utils folder with that of the newly generated one(in the root folder).
- Build the application with `mvn clean install` command from the root folder of your application.
- Redeploy the application to your SAP Cloud Platform account.

# Configurations Used
In the current scenario, ESPM has used machine learning library - weka to categorize the questions a user posts in SAP Jam and then reply accordingly. First step to use any machine learning algorithm is to prepare your training data. Here, our training data can be found in the path ‘espmv2.0\espm-cloud-jambott\src\main\resources\utils\espmdataset.arff’. The file has the following details: 

- Possible categories (Attribute Class)
- Datatype of the data to be classified (Attribute Text)
- Data (Category and Query)

The categories used in this case are:

- TR_middle-aged – Top rated products for the age group ‘Middle aged’
- BR_middle-aged – Bottom rated products for the age group ‘Middle aged’
- TR_young – Top rated products for the age group ‘Young’
- BR_young – Bottom rated products for the age group ‘Young’
- TR_old – Top rated products for the age group ‘Old’
- BR_old – Bottom rated products for the age group ‘Old’
- top-rated – Top rated products for all age groups
- bottom-rated – Bottom rated products for all age groups
- None – Uncategorizable Query.

This training data  is fed to the ‘Classifier’ which is an instance of the class weka.classifiers.meta.FilteredClassifier. The classifier now forms inferences from the training data and creates a machine learning model. This model is checked for accuracy. If accuracy is as per expectations, it is put to productive use. Every question user posts, is passed to the model created and is thus classified to one of the above mentioned categories. Each category has a corresponding query (or filter) attached to it and using this, we hit the backend via a rest api and get the appropriate results. This result formatted and sent to the user as reply to his question.

# How to obtain support
In case you find a bug/need support, please create github issues
 
# Copyright and License
Copyright 2016 SAP SE

Licensed under the Apache License, Version 2.0 (the "License"); you may not use this work except in compliance with the License. You may obtain a copy of the License in the LICENSE file, or at:
[http://www.apache.org/licenses/LICENSE-2.0](http://www.apache.org/licenses/LICENSE-2.0)
Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
