Enterprise Sales Procurement Model (ESPM) Sample Application With Authorization API
============================================================

# Table of Contents

1. Introduction
2. Business Scenario
3. Demo
4. Implementation






# Introduction

The Authorization Management API is a REST API that allows you to manage role and group assignments of users for Java and HTML5 applications and subscriptions. The authorization management REST API provides functionality to manage authorization entities such as roles, groups and their assignments. Roles can be assigned to users and groups. Groups can be assigned to users and roles. A user can be assigned to roles and groups. Roles can be provided within the web.xml or web-fragment.xml and will be extracted during the deployment of the application. Roles deployed with the application are visible for all subscriber accounts unless their shared flag is marked to false. Roles can also be created on subscription level. Assignments for those roles can be established only in the same subscription. Groups are created on account level.





# Business Scenario

In ESPM Cloud Web application, retailer logs into the application. SAP HANA Cloud checks the authorization to see if he is a retailer. Retailer accepts/reject the sales orders created by customer. He also monitors his stock and if any products stock falls below target he raises a purchase order with his supplier.
Assume that a new user is joining as a retailer and needs to access the ESPM Retailer portal. Using Authorization API Retailer can assign retailer role to new user through ESPM Webshop application.
 





# DEMO


1.	Open ESPM Cloud Retailer application.

 ![ESPMRetailer1](/docs/images/ESPMRetailer1.jpg?raw=true)

2.	Click on Assign role icon on top right.

 ![ESPMRetailer2](/docs/images/ESPMRetailer2.jpg?raw=true)

3. Enter user id in Grant role to user window.

 ![ESPMRetailer3](/docs/images/ESPMRetailer3.jpg?raw=true)

4.	Click on Grant role button.

 ![ESPMRetailer4](/docs/images/ESPMRetailer4.jpg?raw=true)





# Implementation

For Implementing Authorization API to assign Role to user from ESPM Cloud Retailer Application.

 ![AuthAPIImpl](/docs/images/AuthAPIImpl.jpg?raw=true)

 
1.	Register ESPM Application in Hana Cloud Platform Cockpit, get Client ID and Client Secret.
2.	Using Destination fill the required fields

 ![OAuthDestination](/docs/images/OAuthDestination.jpg?raw=true)
3.	In Config.Properties add AppName, AccountName and LandscapeHost 
4.	In web.xml add this piece of code for password store
5.	First we check Access Token is available in password Store or not?  if it is not available or the access Token which is obtained from Password Store is invalid then make a POST request to OAuth API for Access Token. For Sample code Click on this [LINK](/espm-cloud-web/src/main/java/com/sap/espm/model/AuthorizationManagement/AuthorizationApiUserManagementWs.java).
6.	On obtaining the Access Token proceed with operation with Authorization API. Click on the [LINK](/espm-cloud-web/src/main/java/com/sap/espm/model/AuthorizationManagement/Handler/AuthApiHandler.java) for sample code.


### Authorization API Source code packages:-

Packages related to Authorization API Implementation on ESPM:-

1. Src/main/java
     - com.sap.espm.model.AuthorizationManagement- AuthorizationApiRoleManagement.java, AuthorizationApiUserManagementWs.java
     - com.sap.espm.model.AuthorizationManagement.Handler- AuthApiHandler.java, OAuthHandler.java
     - com.sap.espm.model.keystore- passwordKeyStore.java
     - com.sap.espm.model.util- HttpClientUtil.java, Util.java

2. src/main/resources
     - config.properties







