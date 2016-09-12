Enterprise Sales Procurement Model (ESPM) Sample Application With Twitter Integration
=====================================================================================


# Table of Contents

1. Introduction
2. Business Scenario
3. Demo
4. Implementation















# Introduction

OAuth 2.0 is an open protocol that enable applications to obtain limited access to an HTTP service in a simple and standard method. E.g. of providers: Facebook, GitHub, Twitter etc. 
It works by delegating user authentication to the service that hosts the user account, and authorizing third-party applications to access the user account. OAuth provides authorization flows for web and desktop applications, and mobile devices.

 To authenticate users for your web application, you need to do the following steps:-
 
1.	Register your application with the service before using OAuth 2.0. This can be done by registering in the developer or API portion of service’s website by providing few details about your application like Application Name, Application Website, and Redirected URI/Callback URL ( The Redirect URI is where the service will redirect the user after they authorize or deny your application and therefore the part of your application that will handle authorization codes or access tokens)

2.	Once Application is registered, the service will issue “client credentials” in the form of Client ID and Client Secret Key. The Client ID is a publicly exposed string that is used by the service API to identify the application and is also used to build authorization URLs that are presented to users. The Client Secret is used to authenticate the identity of the application to the service API when the application requests to access a user's account and must be kept private between the application and the API.

3.	Upon RequestToken retrieval, pass a Callback URL.

4.	Get the AccessToken with the oauth_verifier parameter which will be added to the Callback URL upon callback.








# Business Scenario:-

Implement Sign In with Twitter in ESPM Webshop Application using OAuth 2.0.
In this Scenario Customer logs in with Twitter Account to the ESPM Webshop Application. Using Twitter ID he can shop and Tweet about the products he bought.

![Usecase Diagram](/docs/images/TwitterUseCase.jpg?raw=true)




# DEMO

1.Login via Twitter, As a call back ESPM Webshop application loads

![Usecase Diagram](/docs/images/TwitterLoginPage.jpg?raw=true)
2.	Go via normal flow, Browse and Select products

![ESPM1](/docs/images/ESPM1.jpg?raw=true)
3.	Enter your details along with card details

![ESPM2](/docs/images/ESPM2.jpg?raw=true)
4.	Click on Place an order 

![ESPM3](/docs/images/ESPM3.jpg?raw=true)
5. Window with sales order id loads, Using the TWEET button on bottom right, you can tweet in Twitter

![ESPM4](/docs/images/ESPM4.jpg?raw=true)
6. Your message will be tweeted in Twitter.

![ESPM5](/docs/images/ESPM5.jpg?raw=true)





# Implementation

For Implementing Sign in with Twitter in ESPM Webshop Application, use Twitter4j Library.  

1.	Register ESPM Webshop application in the developer or API portion of Twitter website by providing few details like Application Name, Application Website, and Redirected URI/Callback URL. 
2.	Once ESPM Webshop Application is registered, the service will issue “client credentials” in the form of Consumer Key and Consumer Secret Key.
3.	Create Twitter Login Service Pass the Consumer Key and Consumer Secret Key as parameters to the Twitter API . For Twitter Login Service refer this [LINK](/espm-cloud-web/src/main/java/com/sap/espm/model/oAuth/web/TwitterLoginWs.java).
4.	Enter the login details in the Twitter Login Page in the browser, As a response from the Twitter Login page get the OAuth Token.
5.	Create another service Twitter Call Back Service for Access Token keys. Get the AccessToken with the oauth_verifier parameter which will be added to the Callback URL. Refer this [LINK](/espm-cloud-web/src/main/java/com/sap/espm/model/oAuth/web/TwitterCallBackWs.java).
6.	Using Access Token Keys from session, tweet in Twitter. Twitter Update service is for Tweeting in twitter. For code refer this [LINK](/espm-cloud-web/src/main/java/com/sap/espm/model/oAuth/web/TwitterUpdateWs.java).



### Twitter Integration Source Code Packages

Following are the packages for the twitter OAuth Implementation:

1. Src/main/java
   - com.sap.espm.model.oAuth.impl > Twitter Login and Twitter Status Update Classes.
   - com.sap.espm.model.oAuth.web > Twitter login, Callback and StatusUpdate Web Servlets.
   
2. Src/main/resources
   - Config.properties > To store consumerApplicationKey and ConsumerApplicationSecret.
