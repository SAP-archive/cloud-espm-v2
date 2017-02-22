Enterprise Sales Procurement Model (ESPM) Sample Application With Document Service
==================================================================================

# Table of Contents

1. Introduction
2. Business Scenario
3. Demo
4. Implementation
     - Connects to the document service Repository and creates the Repository if it does not yet exist
     - Access the root folder of the repository
     - Create new folder or file/document as per your requirement
     - Use Streaming API’s to download invoices





#	Introduction

The SAP HANA Cloud Platform Document Service provides an on-demand content repository for unstructured or semi-structured content. 
Applications access it using the OASIS standard protocol Content Management Interoperability Services (CMIS). The CMIS standard defines the protocol level (SOAP, AtomPub, and JSON based protocols). 
The SAP HANA Cloud Platform provides a document service client API on top of this protocol for easier consumption, for creating a folder or a document in a repository. This API is the Open Source library OpenCMIS provided by the Apache Chemistry Project. 
To manage documents in the SAP HANA Cloud Platform document service, you need to connect an application to a repository of the document service. A repository is the document store for your application. It has a unique name with which it can later be accessed, and it is secured using a key provided by the application. Only applications that provide this key are allowed to connect to this repository.

![Document Service Logic](/docs/documentation/DocumentServiceImages/DocumenrServiceLogic.jpg?raw=true)

For more information, visit 

https://help.hana.ondemand.com/help/frameset.htm?e60b7e45bb57101487a881c7c5487778.html 





# Business Scenario

In ESPM Webshop application, we have implemented Document Service of HANA cloud platform. Once customer creates sales order, we store a pdf version of the same in the Document Service Repository of HANA cloud platform, which the customers can download. 

![Use Case](/docs/documentation/DocumentServiceImages/DocumentServiceUseCase.jpg?raw=true)


  


#	Demo

1. Click on the “View My Sales Orders” button.
![ESPM1](/docs/documentation/DocumentServiceImages/ESPM1.jpg?raw=true)

2. Enter the registered email id.
![ESPM2](/docs/documentation/DocumentServiceImages/ESPM2.jpg?raw=true)

3. Click on Get sales orders list, for getting the list of orders.
![ESPM3](/docs/documentation/DocumentServiceImages/ESPM3.jpg?raw=true)

4. After clicking on “Get Sales Order List” button, List of invoices come to the list.
![ESPM4](/docs/documentation/DocumentServiceImages/ESPM4.jpg?raw=true)

5. Click on sales order from the list, detail information about customer and product displayed on the right side of the screen.
![ESPM5](/docs/documentation/DocumentServiceImages/ESPM5.jpg?raw=true)

6. Download the pdf by clicking on “Download pdf” icon in the bottom right corner.
![ESPM6](/docs/documentation/DocumentServiceImages/ESPM6.jpg?raw=true) 




# Implementation

For implementing Document service in ESPM as per the business scenario, following are the implementation steps:
     -	Connect your application with the Document Service repository. 
     - Access the root folder of the repository
     - Create new folder or file/document as per your requirement
     - Use Streaming API’s to download invoices

### Connects to the document service Repository and creates the repository if it does not yet exist.

For connecting ESPM Webshop application to Hana cloud platform document repository, you can use document service client library. 
     - First create a connection with the document repository of ESPM. If repository does not exist, then create a repository for ESPM.  
     - Document service repository will have a unique name and will be secured by a key. Unique name and key will be used to access the document service repository. 

Below is the code snippet used for connection with the repository and creating repository if it does not exists.
```sh
     //Use a unique name for repository. Further accessed by this name.
	String uniqueName = “ESPM.Repository";
      // Use a secret key only known to your application (min. 10 chars)
      String secretKey = "my_super_secret_key_123";
      Session openCmisSession = null;
        InitialContext ctx = new InitialContext();
        String lookupName = "java:comp/env/" + "EcmService";
        EcmService ecmSvc = (EcmService) ctx.lookup(lookupName);
      try {
        // connect to my repository
        openCmisSession = ecmSvc.connect(uniqueName, secretKey);
      }
      catch (CmisObjectNotFoundException e) {
        // repository does not exist, so try to create it
        RepositoryOptions options = new RepositoryOptions();
        options.setUniqueName(uniqueName);
        options.setRepositoryKey(secretKey);
        options.setVisibility(Visibility.PROTECTED);
        ecmSvc.createRepository(options);
        // should be created now, so connect to it
        openCmisSession = ecmSvc.connect(uniqueName, secretKey);
      }
```

### Access the root folder of the repository

Get access to the root folder of the application document repository. Further folder and files/documents will be created under the root folder.

      Folder root = openCmisSession.getRootFolder();

### Create new folder or file/document as per your requirement
Create sales order as pdf files and export/store pdfs in the document service repository. You can use PDFBox library for creating pdfs. For code, click on the [LINK](/espm-cloud-web/src/main/java/com/sap/espm/model/documentservice/InvoiceBuilder.java).

### Use Streaming API’s to download invoices.

Customer can download sales order invoices by entering the email id. For downloading invoices use Streaming APIs. Refer this [LINK](/espm-cloud-web/src/main/java/com/sap/espm/model/pdf/generator/CmisRead.java) for code snippet.



