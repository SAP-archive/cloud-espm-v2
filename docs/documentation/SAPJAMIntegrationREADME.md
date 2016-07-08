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

The “annotations.xml” file used by SAP Jam to render the data is created for you and available within the ESPM application. https://espmxxxxxxxtrial.hanatrial.ondemand.com/espm-cloud-web/webshop/reviews_annotations.xml

For understanding the annotations.xml file tags, please refer the [documentation](http://help.sap.com/download/documentation/sapjam/developer/index.html#intguide/concepts/DevelopAnODataAnnotationsFileToDisplayBusinessRecords.html)

With the “annotations.xml” file and the data exposed via OData available, we can create an external application in SAP Jam that brings in products and product reviews data from the ESPM application into SAP Jam. Then create SAP Jam group for each product and its associated product reviews and collaborate within SAP Jam on the business data.
Setting up ESPM application integration with SAP Jam
Setting up the integration of ESPM application with SAP Jam has the below steps
1.	Create an External Application in SAP Jam
2.	Import the ESPM Web Shop Records into SAP Jam
3.	Import the ESPM Reviews Group Template into SAP Jam
4.	Create a new ESPM Reviews group in SAP Jam from an ESPM Web Shop Record
5.	Reconfigure the ESPM Reviews Group Widgets


###	Create an External Application in SAP Jam

An External Application in SAP Jam defines the connection between SAP Jam and an external application to access the data in that application exposed via its API. Use the following steps to create an External Application in SAP Jam:

  - Launch SAP Jam service from the HCP cockpit -> Services -> SAP Jam -> Go to Service
  - In SAP Jam, click on the "cog" settings icon and select Admin. The SAP Jam Admin page will appear.
  - Select Integrations > External Applications from the sidebar menu. The External Applications page will appear.
  - Click Add Application and select SAP HANA Cloud Platform from the dropdown menu.
  - In the Name text box, enter ESPM
  - Click on the Select Authentication Type drop-down menu, and select Common User.
  - Leave the User Name and Password fields blank.
  - Click Save to create the External Application.
  - You will see the ESPM External Application you created in the External Applications list.

###	Import the ESPM Web Shop Records into SAP Jam

Record types in SAP Jam define how the data from the external application will be displayed within SAP Jam. Use the following steps to import the ESPM Web Shop records into SAP Jam

  - In the external applications page, for the ESPM application select Action -> Manage Record types
  - Click on Add Record Type button
  - In the Name text box, enter Products
  - In the External Type text box, enterhttps://espm<account>.hanatrial.ondemand.com/espm-cloud-web /espm.svc/$metadata#Products 
  - In the Annotations URL text field, enter  https://espmxxxxxxxtrial.hanatrial.ondemand.com/espm-cloud-web /webshop/reviews_annotations.xml 
  - Click on Import External resources button. You will be display with a success page. Click on Done button
  - Click on the Create button
  - Products has been created as an entry in Manage ESPM Record Types page.
  - repeat step no b to g again, but changing the value in step c. to CustomerReviews and  the value for External Type text box in step d. to  https://espmxxxxxxxtrial.hanatrial.ondemand.com/espm-cloud-web /espm.svc/$metadata#CustomerReviews 
  - CustomerReviews has been created as an entry in Manage ESPM Record Types page 
  
  
### Create Filters for Products and CustomerReviews Record Types

We need to create the filters for the CustomerReviews Record Types so that we are able to analyze the top and bottom rated products based on the customer reviews and ratings

  - In the Manage ESPM Record Types, Against the CustomerReviews record,Click on the Filters link
  - Click on the New Filter button
  - In the Name text field, enter Top rated customer reviews
  - In the Filter text area, enter Rating gt 3
  - Select the Checkbox “enabled”
  - Click on the Submit button
  - Repeat steps b to f again with Name text field as bottom rated customer reviews and Filter text area as Rating le 2

### Import the ESPM Reviews Group Template into SAP Jam

SAP Jam groups act as the collaborative workspaces for work patterns. Group templates organize the content in a way that provides guidance to users on the best way to approach the work required to support a particular repeatable business process. Use the following steps to import the ESPM Reviews group template into SAP Jam:

  - On the Admin page select Product Setup > Group Templates from the sidebar menu. The Group Templates screen will appear.
  - Download the group template zip file from the following URL https://sapjamsamplecode.github.io/GroupTemplates/ESPM_Reviews-Products.zip  
  - Click the Import a template button.
  - Navigate to the group template zip file.
  - Click the Open button.
  - Click the Import button.
  - Once the template has been imported an Import a template dialog will be displayed. Click the OK button.
  - Refresh the web page and you will see a new group template titled ESPM Reviews - Products. If you do not see the new group template, wait 30 seconds and try again.
  - Click the Slider button next to this group template and set it to Enabled (shown as a blue checkmark). This group template is now active.

### Create a new ESPM Reviews group in SAP Jam from an ESPM Web Shop Record

Groups are a membership of users who can upload, create, or reference material specific to a department, project, or team. Group members can participate in discussions, forums and much more. Use the following steps to create a new ESPM Reviews Group in SAP Jam for an ESPM Web Shop Record:

  - Select Business Records from the top menu-bar. The Business Records screen will appear.
  - Select ESPM in the Name column.
  - Select Products in the Type column. A list of IDs from ESPM will be displayed.
  - Copy the ID value next to "Notebook Basic 15" in the ID column.
  - Hover over "Notebook Basic 15" in the Product Name column and a quick view panel of the product will appear.
  - Click the Create Group button in the quick view panel. The Create a Group dialog appears
  - Click the No Template drop-down list and select the group template ESPM Reviews - Products. 
  - In the Type a Group Name text box, enter “ESPM Product Reviews - ” and paste the ID value.
  - Set the Group Permissions by selecting Public.
  - Select the Activate this group now checkbox.
  - Click the "Create" button. The new group has now been created
  
 ### Reconfigure the ESPM Reviews Group Widgets
	
1. Select Overview. The Overview screen appears.
2. Select Edit.
3. Configure the "Customer Reviews" widget: 
     - Hover the mouse cursor over the top right corner of the "Customer Reviews" widget. The Edit and Remove menu appears.
     - Select Edit.
     - Select ESPM: Customer Reviews from the "Update Widget" drop-down list.
     - Select Related from the "Show" drop-down list.
     - Click the OK button. The "Customer Reviews" widget information appears.
4.	Configure the "Top Rated Customer Reviews" widget: 
     - Hover the mouse cursor over the top right corner of the "Top Rated Customer Reviews" widget. The Edit and Remove menu appears.
     - Select Edit.
     - Select ESPM:Customer Reviews from the "Update Widget" drop-down list.
     - Select Related from the "Show" drop-down list.
     - Select Top Rated Reviews from the "Filter by" drop-down list.
     - Select Rating from the "Sort by" drop-down list.
     - Select the Descending radio button.
     - Click the OK button. The "Top Rated Customer Reviews" widget information appears.
5. Configure the "Bottom Rated Customer Reviews" widget: 
     - Hover the mouse cursor over the top right corner of the "Bottom Rated Customer Reviews" widget. The Edit and Remove menu appears.
     - Select Edit.
     - Select ESPM:Customer Reviews from the "Update Widget" drop-down list.
     - Select Related from the "Show" drop-down list.
     - Select Bottom Rated Reviews from the "Filter by" drop-down list.
     - Select Rating from the "Sort by" drop-down list.
     - Select the Ascending radio button.
     - Click the OK button. The "Bottom Rated Customer Reviews" widget information appears.
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



