Enterprise Sales Procurement Model (ESPM) Sample Application With SAP JAM Integration
==================================================================================

# Pre-requisite

  - Make sure that you have enabled SAP Jam service in HCP as shown in the below screen shot
  ![Services](/docs/documentation/SAPJAMIntegrationImages/EnableJamService.png?raw=true)
  - You have running ESPM application and deployed to SAP HCP as described in master branch
  - In the below guide, replace account information with your trial account. The application name is “espm” during deployment.





# Overview of External Application Integration in SAP Jam

SAP Jam is capable of incorporating business data in SAP Jam groups without the need to build an OpenSocial gadget. As long as the business data is exposed to SAP Jam via an OData service, SAP Jam can render the business data as part of a SAP Jam group.
SAP Jam uses a generic mechanism to render data exposed via OData services. Therefore, a developer only has limited influence on how the data is rendered inside of SAP Jam via an "annotations.xml" file.





# Business Scenario

Now, Imagine the business case where you have an

  - E-commerce application that sells various IT products
  - Customers can write reviews about the products
  - If you could analyze this massive customer reviews of the products and use that information to make business decisions on product sales, promotions etc. All this in a social manner where product managers and sales team collaborate on product reviews and analysis.

This is the story what ESPM – SAP Jam Integration showcases.
ESPM is a web shopping application running on SAP HANA Cloud platform with HANA as the underlying database.
Customers can write reviews on the products
The product reviews information from within SAP HANA Cloud Platform can be fetched into SAP Jam and you can analyze on the reviews for each product like Top Rated Products, Bottom rated products etc. to make business decisions.





# Implementation

The business data that is exposed in this scenario is the product reviews written by customers for products in ESPM application. The data is exposed as OData.

https://espmxxxxxxxtrial.hanatrial.ondemand.com/espm-cloud-web/espm.svc/Products 

https://espmxxxxxxxtrial.hanatrial.ondemand.com/espm-cloud-web/espm.svc/CustomerReviews 

The “annotations.xml” file used by SAP Jam to render the data is created for you and available within the ESPM application.

https://espmxxxxxxxtrial.hanatrial.ondemand.com/espm-cloud-web/webshop/reviews_annotations.xml

For understanding the annotations.xml file tags, please refer the [documentation](http://help.sap.com/download/documentation/sapjam/developer/index.html#intguide/concepts/DevelopAnODataAnnotationsFileToDisplayBusinessRecords.html)

With the “annotations.xml” file and the data exposed via OData available, we can create an external application in SAP Jam that brings in products and product reviews data from the ESPM application into SAP Jam. Then create SAP Jam group for each product and its associated product reviews and collaborate within SAP Jam on the business data.

#####Setting up ESPM application integration with SAP Jam

Setting up the integration of ESPM application with SAP Jam has the below steps <br/ >
1.	Create an External Application in SAP Jam <br/ >
2.	Import the ESPM Web Shop Records into SAP Jam <br/ >
3.	Import the ESPM Reviews Group Template into SAP Jam <br/ >
4.	Create a new ESPM Reviews group in SAP Jam from an ESPM Web Shop Record <br/ >
5.	Reconfigure the ESPM Reviews Group Widgets <br/ >


###	Create an External Application in SAP Jam

An External Application in SAP Jam defines the connection between SAP Jam and an external application to access the data in that application exposed via its API. Use the following steps to create an External Application in SAP Jam:

  a.	Launch SAP Jam service from the HCP cockpit -> Services -> SAP Jam -> Go to Service <br/ >
  b	In SAP Jam, click on the "cog" settings icon and select Admin. The SAP Jam Admin page will appear. <br/ >
  c.	Select Integrations > External Applications from the sidebar menu. The External Applications page will appear. <br/ >
  d.	Click Add Application and select SAP HANA Cloud Platform from the dropdown menu. <br/ >
  e.	In the Name text box, enter ESPM <br/ >
  f.	Click on the Select Authentication Type drop-down menu, and select Common User. <br/ >
  g.	Leave the User Name and Password fields blank. <br/ >
  h.	Click Save to create the External Application. <br/ >
  i.	You will see the ESPM External Application you created in the External Applications list. <br/ >

###	Import the ESPM Web Shop Records into SAP Jam

Record types in SAP Jam define how the data from the external application will be displayed within SAP Jam. Use the following steps to import the ESPM Web Shop records into SAP Jam

  a.	In the external applications page, for the ESPM application select Action -> Manage Record types <br />
  b.	Click on Add Record Type button <br />
  c.	In the Name text box, enter Products <br />
  d. 	In the External Type text box, enter <br/ >
  https://espm<account>.hanatrial.ondemand.com/espm-cloud-web/espm.svc/$metadata#Products <br/ >
  e.	In the Annotations URL text field, enter <br/ >
        https://espmxxxxxxxtrial.hanatrial.ondemand.com/espm-cloud-web/webshop/reviews_annotations.xml <br/>
  f. 	Click on Import External resources button. You will be display with a success page. Click on Done button. <br/>
  g.	Click on the Create button. <br/>
  h.	Products has been created as an entry in Manage ESPM Record Types page. <br/>
  i.	repeat step no b to g again, but changing the value in step c. to CustomerReviews and  the value for External Type text box in step d. to <br/ >
   https://espmxxxxxxxtrial.hanatrial.ondemand.com/espm-cloud-web/espm.svc/$metadata#CustomerReviews <br/> 
  j.	CustomerReviews has been created as an entry in Manage ESPM Record Types page
  
  
### Create Filters for Products and CustomerReviews Record Types

We need to create the filters for the CustomerReviews Record Types so that we are able to analyze the top and bottom rated products based on the customer reviews and ratings

  a.	In the Manage ESPM Record Types, Against the CustomerReviews record,Click on the Filters link <br/ >
  b.	Click on the New Filter button <br/ >
  c.	In the Name text field, enter Top rated customer reviews <br/ >
  d.	In the Filter text area, enter Rating gt 3 <br/ >
  e.	Select the Checkbox “enabled” <br/ >
  f.	Click on the Submit button <br/ >
  g.	Repeat steps b to f again with Name text field as bottom rated customer reviews and Filter text area as Rating le 2 <br/ >

### Import the ESPM Reviews Group Template into SAP Jam

SAP Jam groups act as the collaborative workspaces for work patterns. Group templates organize the content in a way that provides guidance to users on the best way to approach the work required to support a particular repeatable business process. Use the following steps to import the ESPM Reviews group template into SAP Jam:

  a.	On the Admin page select Product Setup > Group Templates from the sidebar menu. The Group Templates screen will appear. <br/ >
  b.	Download the group template zip file from the following URL <br/ > https://sapjamsamplecode.github.io/GroupTemplates/ESPM_Reviews-Products.zip <br/ >
  c.	Click the Import a template button. <br/ >
  d.	Navigate to the group template zip file. <br/ >
  e.	Click the Open button. <br/ >
  f.	Click the Import button. <br/ >
  g.	Once the template has been imported an Import a template dialog will be displayed. Click the OK button. <br/ >
  h.	Refresh the web page and you will see a new group template titled ESPM Reviews - Products. If you do not see the new group template, wait 30 seconds and try again. <br/ >
  i.	Click the Slider button next to this group template and set it to Enabled (shown as a blue checkmark). This group template is now active. <br/ >

### Create a new ESPM Reviews group in SAP Jam from an ESPM Web Shop Record

Groups are a membership of users who can upload, create, or reference material specific to a department, project, or team. Group members can participate in discussions, forums and much more. Use the following steps to create a new ESPM Reviews Group in SAP Jam for an ESPM Web Shop Record: <br/ >

  a.	Select Business Records from the top menu-bar. The Business Records screen will appear. <br/ >
  b.	Select ESPM in the Name column. <br/ >
  c.	Select Products in the Type column. A list of IDs from ESPM will be displayed. <br/ >
  d.	Copy the ID value next to "Notebook Basic 15" in the ID column. <br/ >
  e.	Hover over "Notebook Basic 15" in the Product Name column and a quick view panel of the product will appear. <br/ >
  f.	Click the Create Group button in the quick view panel. The Create a Group dialog appears. <br/ >
  g.	Click the No Template drop-down list and select the group template ESPM Reviews - Products. <br/ >
  h.	In the Type a Group Name text box, enter “ESPM Product Reviews - ” and paste the ID value. <br/ >
  i.	Set the Group Permissions by selecting Public. <br/ >
  j.	Select the Activate this group now checkbox. <br/ >
  k.	Click the "Create" button. The new group has now been created <br/ >
  
### Reconfigure the ESPM Reviews Group Widgets
	
1. Select Overview. The Overview screen appears.
2. Select Edit.
3. Configure the "Customer Reviews" widget: <br/ >
     a.	Hover the mouse cursor over the top right corner of the "Customer Reviews" widget. The Edit and Remove menu appears. <br/ >
     b.	Select Edit. <br/ >
     c.	Select ESPM: Customer Reviews from the "Update Widget" drop-down list. <br/ >
     d.	Select Related from the "Show" drop-down list. <br/ >
     e.	Click the OK button. The "Customer Reviews" widget information appears. <br/ >
4. Configure the "Top Rated Customer Reviews" widget: <br/ > 
     a.	Hover the mouse cursor over the top right corner of the "Top Rated Customer Reviews" widget. The Edit and Remove menu appears. <br/ >
     b.	Select Edit. <br/ >
     c.	Select ESPM:Customer Reviews from the "Update Widget" drop-down list. <br/ >
     d.	Select Related from the "Show" drop-down list. <br/ >
     e.	Select Top Rated Reviews from the "Filter by" drop-down list. <br/ >
     f.	Select Rating from the "Sort by" drop-down list. <br/ >
     g.	Select the Descending radio button. <br/ >
     h.	Click the OK button. The "Top Rated Customer Reviews" widget information appears. <br/ >
5. Configure the "Bottom Rated Customer Reviews" widget: <br/ > 
     a.	Hover the mouse cursor over the top right corner of the "Bottom Rated Customer Reviews" widget. The Edit and Remove menu appears. <br/ >
     b.	Select Edit. <br/ >
     c.	Select ESPM:Customer Reviews from the "Update Widget" drop-down list. <br/ >
     d.	Select Related from the "Show" drop-down list. <br/ >
     e.	Select Bottom Rated Reviews from the "Filter by" drop-down list. <br/ >
     f.	Select Rating from the "Sort by" drop-down list. <br/ >
     g.	Select the Ascending radio button. <br/ >
     h.	Click the OK button. The "Bottom Rated Customer Reviews" widget information appears. <br/ >
6. Click the Publish button.
7. Click the Publish button. 
8. The Overview page will now display rich information about the product and its reviews. 
9. Start collaborating with other users on this ESPM Review group! Try creating other sales collaboration groups for other items from the ESPM web shop business records as well.

# Demo

The demo can be split into 2 steps

1. Creation of the Product Reviews in ESPM
2. Viewing the Product Reviews in SAP Jam 

###Creation of the Product Reviews in ESPM

1. Open ESPM “webshop” URL in the browser.
![ESPM1](/docs/documentation/SAPJAMIntegrationImages/ESPMURL.png?raw=true)

2. Enter “Notebook Basic 15” in the product Search field.
![ESPM1](/docs/documentation/SAPJAMIntegrationImages/NotebookBasic15Search.png?raw=true)

3. Press the “Enter” key to view the Product “Notebook Basic 15” in the product list.
![ESPM1](/docs/documentation/SAPJAMIntegrationImages/NotebookBasic15SearchList.png?raw=true)

4. Select “Notebook Basic 15” item from the product list to view its details.
![ESPM1](/docs/documentation/SAPJAMIntegrationImages/NotebookBasic15Selection.png?raw=true)

5. Click on “Write a Review” button.
![ESPM1](/docs/documentation/SAPJAMIntegrationImages/WriteProductReview.png?raw=true)

6. In the popup window, enter the product review information and click on “Submit” button.
![ESPM1](/docs/documentation/SAPJAMIntegrationImages/SubmitProductReview.png?raw=true)

### Viewing the Product Reviews in SAP Jam

1. Open SAP Jam URL from HCP Service cockpit by clicking on “Go to Service” URL
![ESPM1](/docs/documentation/SAPJAMIntegrationImages/JamURL.png?raw=true)

2. Select the Groups dropdown “ESPM Product Reviews – HT-1000”
![ESPM1](/docs/documentation/SAPJAMIntegrationImages/ESPMProductGroup.png?raw=true)

3. You will be able to see the top rated and bottom rated reviews for the product “HT-1000”
![ESPM1](/docs/documentation/SAPJAMIntegrationImages/ESPMProductReviews.png?raw=true)

4. Click on link “View all 14 Items” to see product review information that was created earlier
![ESPM1](/docs/documentation/SAPJAMIntegrationImages/viewReviewdetails.png?raw=true)



