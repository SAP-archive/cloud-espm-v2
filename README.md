Enterprise Sales Procurement Model (ESPM) Sample Application with API Management
================================================================================



# Table of Contents

1. Introduction
2. Business Scenario
3. Demo
4. Implementation
 


# Introduction

SAP API Management allows you to expose your data and processes as API’s for Omni-channel consumption and manage the lifecycle of those API’s. 
SAP API Management is offers as a service on SAP HANA Cloud Platform. For details, refer to the documentation
Business Scenario
In ESPM we have a local OData service running when the application is deployed. The OData service provides the required data and endpoints for the application.
We can switch to the OData Service that is exposed by SAP API Management. The API end point can be configured in ESPM from the UI of the application as shown in the below demo script.
The SAP API Management exposed API is 

https://APIHostName:port/v1/espm.svc

**Note: Replace APIHostName & port with your API host name and port.**

API key - xxxxxxxxxxxxxxxxxxxxxxxxxxxx

**Note: Replace xxxxxxxxxxxxxxxxxxxxxxxxxxx with API key.**

The OData service exposed is https://espmxxxxxxxtrial.hanatrial.ondemand.com/espm-cloud-web/espm.svc/
![Usecase Diagram](/docs/images/APIMgmntFlow.JPG?raw=true)

# Demo Script
1.	Open ESPM URL in the browser. https://espmxxxxxxxtrial.hanatrial.ondemand.com/espm-cloud-web/webshop/ . Click on the settings icon in the top right corner

![ESPM1](/docs/images/ESPM1.jpg?raw=true)
2.	In the Popup window, Enter the API endpoint and the API key as provided above. Click on the Submit button

![ESPM2](/docs/images/ESPM2.jpg?raw=true)
3.	The Data URL that the ESPM application uses is now switched from the local service to API endpoint URL. The application loads data from API endpoint URL
![ESPM3](/docs/images/ESPM3.jpg?raw=true)



