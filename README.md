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

### Protecting from Cross-Site Request Forgery

In ESPM, we use **Custom header approach** for CSRF protection. For details on the same, please refer [SAP HANA Cloud Platform documentation](https://help.hana.ondemand.com/help/frameset.htm?1f5f34e31ec64af8b5fef1796ea07c0a.html)

In Web.xml file in ESPM espm-cloud-web project(path src/main/webapp/WEB-INF/web.xml), we have added the below tags to enable CSRF protection for the secure odata service (http://<appname><accountname>.hana.ondemand.com/espm-cloud-web/espm.svc/secure)
```sh
	<!-- CSRF protection for the REST API for retailer scenario -->
		<filter>
		   <filter-name>RestCSRF</filter-name>
		   <filter-class>org.apache.catalina.filters.RestCsrfPreventionFilter</filter-class>
	 	</filter>
	 <filter-mapping>
	   	<filter-name>RestCSRF</filter-name>
	    <!--modifying REST APIs  -->
		<url-pattern>/espm.svc/secure/*</url-pattern>	    
	 </filter-mapping>	
```

Note that the CSRF protection is performed only for modifying HTTP requests (different from GET|HEAD or OPTIONS).
All CSRF protected resources should be protected with an authentication mechanism.

In ESPM, the Retailer scenario (https://localhost:\<port\>/espm-cloud-web/retailer) is protected with authentication. The Sales Order Approval and Stock Update scenario is protected with CSRF protection. 

The modifing HTTP requests to the secure service will be sent with header **X-CSRF-Token: <token_value>**

Prior to sending a modifing HTTTP request, an HTTP GET request should be sent to a non-modifing HTTP request with the header **X-CSRF-Token: Fetch**. This will fetch the **<token_value>** required for the modifing request.
 





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

 
1.Go to Cockpit -> Enable Beta Feature

![BetaFeature](/docs/images/BetaFeature.jpg?raw=true)

2.Navigate to services -> Go to oAuth 2.0 Services -> Platform API Beta-> create a Client ID and Client Secret. Save them for the next steps.

3.Create a destination as shown in the below screen shot.
	Name - oAuths
	URL - https://api.hanatrial.ondemand.com/oauth2/apitoken/v1?grant_type=client_credentials 
	User and Password has to be given the values ClientId and ClientSecret from Step 1.

![OAuthDestination](/docs/images/OAuthDestination.jpg?raw=true)

4.In espm-cloud-web/src/main/resources/Config.Properties -> add AppName (your app name), AccountName ( your account name of SAP HCP) and LandscapeHost ( for hana trial account, the value is hanatrial.ondemand.com) , OAuthDestinationName (this is the HCP destination name -  oAuths )

5.Go to Cockpit -> Navigate to application -> Navigate to Security -> Go to Roles -> Grant role "Retailer" in HCP to your user.

![Security](/docs/images/Security.jpg?raw=true)

6.Build the applciation in Eclipse and deploy the same to HCP.

### Authorization API Source code packages:-

Packages related to Authorization API Implementation on ESPM:-

1. Src/main/java
     - com.sap.espm.model.AuthorizationManagement- AuthorizationApiRoleManagement.java, AuthorizationApiUserManagementWs.java
     - com.sap.espm.model.AuthorizationManagement.Handler- AuthApiHandler.java, OAuthHandler.java
     - com.sap.espm.model.keystore- passwordKeyStore.java
     - com.sap.espm.model.util- HttpClientUtil.java, Util.java

2. src/main/resources
     - config.properties

#### Code Details

1.	First we check Access Token is available in password Store or not?  if it is not available or the access Token which is obtained from Password Store is invalid then make a POST request to OAuth API for Access Token. For Sample code Click on this [LINK](/espm-cloud-web/src/main/java/com/sap/espm/model/AuthorizationManagement/AuthorizationApiUserManagementWs.java).
2.	On obtaining the Access Token proceed with operation with Authorization API. Click on the [LINK](/espm-cloud-web/src/main/java/com/sap/espm/model/AuthorizationManagement/Handler/AuthApiHandler.java) for sample code.





