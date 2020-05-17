# AjaxThymeleaf
Spring Boot - All About Thymeleaf layouts, Spring Security, MVC, Web Flow, and implementing AJAX in both Web Flow and MVC.

## Technologies used
	1- Integrating Thymeleaf, Spring Security, Spring MVC, Spring Web-flow all together and doing all CRUD operations 
	   using AJAX in both Spring MVC, and Spring Web-flow scenarios.
	2- Demonstrating all about Thymeleaf layouts. 
	3- Using bootstrap, JQuery, Dojo, and Spring-Dojo. 

## Database used
For the Database, this project using H2Database for simplicity of installation and you can build tables just by running the application and the sample data will be inserted into the created tables too.

## Why I decided to build this project?
	- After a deep long search on the internet, I found out a lack of documentation for 
	  using AJAX with both Spring Web-flow and Spring MVC.	
	- Applying best practices in all project phases such as project packaging structure, 
	  using a mapper to map between the DTO and entity models, etc. 

## Some of useful features
	1- Do all CRUD operations only using AJAX in both Spring MVC scenario and Web-flow scenario. 
	2- Using JQuery to handle AJAX requests with Spring MVC.
	3- Using Dojo and Spring-Dojo integration in addition to JQuery to handle AJAX requests with Spring Web-flow. 
	4- To get the best results from AJAX with Spring Web-flow scenario along with Spring security and handling of AJAX 
	   caLLback functions, I did some modifications and extension in the functionalities of Spring-Dojo integration file 
	   to enable the end developers to add some headers in AJAX request and to be able to do actions after the AJAX request 
	   callback. 
	
		You can find the updated file in the following path:
		"AjaxThymeleaf\thymeleaf-layouts-webflow-ajax-security\src\main\resources\static\js\spring\Spring-Dojo.js.uncompressed.js".
		
		And the usage of these new features can be found in the following path:
		"AjaxThymeleaf\thymeleaf-layouts-webflow-ajax-security\src\main\resources\static\js\task\task-list.js".
	5- Building a small Rest API using Spring best practices.
	6- Using Junit Jupiter to test services and MVC controllers, and applying best practices in organizing the test classes.

## Following is a sample code from "task-list.js" file:		
```javascript
// add AJAX spring decoration for save task button click event
Spring.addDecoration(new Spring.AjaxEventDecoration({
	formId : $formId,
	elementId : 'saveTask',
	event : 'onclick',
	headers : {
		[_csrf_header] :  _csrf_token
	},
	load: function(response) {
		// stop all actions that are binded to toggle button on change event 
		doToggleAction = false;
		
		// if the response contains any errors, hide message part
		if ($(response).find('.help-block').length > 0) {
			// turn off the toggle button
			$toggleChkBox.bootstrapToggle('off');
		} else {	
			// turn on the toggle button
			$toggleChkBox.bootstrapToggle('on');				
		}
	}
}));
```		
	