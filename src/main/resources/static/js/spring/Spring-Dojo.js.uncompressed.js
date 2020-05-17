/*
 * Copyright 2004-2008 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


/**
 * 
 * Added By Hany K Abdelsayed 
 * 
 * Purposes
 * 1. To allow the developer to send a headers object along with the Spring.addDecoration function.
 *    This headers element is used to set the headers for the Dojo Ajax request implementation.  
 * 
 * 		Usage
 * 		By Adding this feature, we can send some useful headers in the Ajax request 
 * 		such as the CSRF token, to allow the Ajax requests to pass the Spring security.
 * 
 * 2. To allow the developer to send DOJO callback functions to handle the response after AJAX submit which is:
 * 		load(response, ioArgs) - The callback that fires when the request successfully completes. 
 * 						The first argument of load is the result of the request in the format designated 
 * 						by the handleAs option. 
 * 
 * 		error(errorMessage) - The callback that fires when the request fails. The first argument is the error message, 
 * 						if available.
 * 
 */

dojo.declare("AjaxExtender", null, {
	// get the "headers" object from Spring.decorations that is declared in Spring.js file
	getHeaders: function(sourceId) {
		var headers = {};
		var decoration = Spring.decorations[sourceId];
		if(decoration && typeof(decoration) !== 'undefined') {
			headers = decoration[0].headers;			
			
			// check if the Accept property is already exist in the headers object
			var exist = false;
			for (var prop in headers) { 
				exist = (prop==="Accept");		
	        }			
			if(!exist) headers["Accept"] = "text/html;type=ajax";		
			 
		} else {
			headers["Accept"] = "text/html;type=ajax";
		}
		
		return headers;
	},
	
	// get the "load" object from Spring.decorations that is declared in Spring.js file
	handleAjaxLoad: function(sourceId, response, ioArgs) {
		var decoration = Spring.decorations[sourceId];
		if(decoration && typeof(decoration) !== 'undefined') {
			loadFunction = decoration[0].load;			
			//check a variable is of function type or not 
			if(loadFunction instanceof Function) { 
				// execute the function
				try {
					loadFunction(response, ioArgs);
				} catch(err){}							
			}
			
		} 
	}, 
	
	// get the "error" object from Spring.decorations that is declared in Spring.js file
	handleAjaxError: function(sourceId, response, ioArgs) {
		var decoration = Spring.decorations[sourceId];
		if(decoration && typeof(decoration) !== 'undefined') {
			errorFunction = decoration[0].error;			
			//check a variable is of function type or not 
			if(errorFunction instanceof Function) { 
				// execute the function
				try {
					errorFunction(response, ioArgs);
				} catch(err){}							
			}
			
		} 
	} 
	
});


dojo.declare("Spring.DefaultEquals", null, {
	equals : function(/*Object*/other){
		if (other.declaredClass && other.declaredClass == this.declaredClass) {
			return true;
		}else{
			return false;
		}
	}			
});

dojo.declare("Spring.ElementDecoration", [Spring.AbstractElementDecoration, Spring.DefaultEquals], {
	constructor : function(config) {
		this.copyFields = new Array('name', 'value', 'type', 'checked', 'selected', 'readOnly', 'disabled', 'alt', 'maxLength', 'class', 'title');
		dojo.mixin(this, config);
		if(this.widgetModule == "") {
			this.widgetModule = this.widgetType;
		}
	},
	
	apply : function(){
		if (dijit.byId(this.elementId)) {
			dijit.byId(this.elementId).destroyRecursive(false);
		}
		var element = dojo.byId(this.elementId);
		if (!element) {
			console.error("Could not apply " + this.widgetType + " decoration.  Element with id '" + this.elementId + "' not found in the DOM.");
		}
		else {
			var datePattern = this.widgetAttrs['datePattern'];
			if (datePattern && this.widgetType == 'dijit.form.DateTextBox') {
				if (!this.widgetAttrs['value']) {
					this.widgetAttrs['value'] = dojo.date.locale.parse(element.value, {selector : "date", datePattern : datePattern});
				}
				if (!this.widgetAttrs['serialize']) {
					this.widgetAttrs['serialize'] = function(d, options){
						return dojo.date.locale.format(d, {selector : "date", datePattern : datePattern});
					}
				}
			}
			for (var copyField in this.copyFields) {
				copyField = this.copyFields[copyField];
				if (!this.widgetAttrs[copyField] && element[copyField] &&
				(typeof element[copyField] != 'number' ||
				(typeof element[copyField] == 'number' && element[copyField] >= 0))) {
					this.widgetAttrs[copyField] = element[copyField];
				}
			}
			if(element['style'] && element['style'].cssText){
				this.widgetAttrs['style'] = element['style'].cssText;
			}
			dojo.require(this.widgetModule);
			var widgetConstructor = dojo.eval(this.widgetType);
			this.widget = new widgetConstructor(this.widgetAttrs, element);
			this.widget.startup();
		}
   		//return this to support method chaining
   		return this;
	},
	
	validate : function(){
		if (!this.widget.isValid) {
			// some widgets cannot be validated
			return true;
		}
		var isValid = this.widget.isValid(false);
		if (!isValid) {
			this.widget.state = "Error";
			this.widget._setStateClass();
		}
		return isValid;
	}
});

dojo.declare("Spring.ValidateAllDecoration", [Spring.AbstractValidateAllDecoration, Spring.DefaultEquals], {
	constructor : function(config) {
		this.originalHandler = null;
		this.connection = null;
		dojo.mixin(this, config);
	},
	
	apply : function() {
		var element = dojo.byId(this.elementId);
		if (!element) {
			console.error("Could not apply ValidateAll decoration.  Element with id '" + this.elementId + "' not found in the DOM.");
		} else {
			this.originalHandler = element[this.event];
			var context = this;
			element[this.event] = function(event){
				context.handleEvent(event, context);
			};
		}
		return this;
	},
	
	cleanup : function(){
		dojo.disconnect(this.connection);
	},
	
	handleEvent : function(event, context){ 
		if (!Spring.validateAll()) {
			dojo.publish(this.elementId+"/validation", [false]);
			dojo.stopEvent(event);
		} else {
			dojo.publish(this.elementId+"/validation", [true]);
			if(dojo.isFunction(context.originalHandler)) {
				var result = context.originalHandler(event);
				if (result == false) {
					dojo.stopEvent(event);
				}
			}
		}
	}
});

dojo.declare("Spring.AjaxEventDecoration", [Spring.AbstractAjaxEventDecoration, Spring.DefaultEquals], {
	constructor : function(config){
		this.validationSubscription = null;
		this.connection = null;
		this.allowed = true;
		dojo.mixin(this, config);
	},
	
	apply : function() {
		var element = dojo.byId(this.elementId);
		if (!element) {
			console.error("Could not apply AjaxEvent decoration.  Element with id '" + this.elementId + "' not found in the DOM.");
		} else {
			this.validationSubscription = dojo.subscribe(this.elementId+"/validation", this, "_handleValidation");
			this.connection = dojo.connect(element, this.event, this, "submit");
		}
		return this;	
	},
	
	cleanup : function(){
		dojo.unsubscribe(this.validationSubscription);
		dojo.disconnect(this.connection);
	},
	
	submit : function(event){
		if (this.sourceId == ""){
			this.sourceId = this.elementId;
		}
		if(this.formId == ""){
			Spring.remoting.getLinkedResource(this.sourceId, this.params, this.popup);
		} else {
			if (this.allowed){
				Spring.remoting.submitForm(this.sourceId, this.formId, this.params);
			}
		}
		dojo.stopEvent(event);
	},
	
	_handleValidation : function(success){
		if (!success) {
			this.allowed=false;
		} else {
			this.allowed=true;
		}
	}
});

dojo.declare("Spring.RemotingHandler", Spring.AbstractRemotingHandler, {
	constructor : function(){},
	
	submitForm : function(/*String */ sourceId, /*String*/formId, /*Object*/ params) {
		/*
		 * 
		 * Edited By Hany K Abdelsayed
		 * 
		 * Set a variable by current instance 
		 */ 
		var me = this;
		
		/*
		 * declare an instance from the AjaxExtender class
		 */
		var ajaxExtender = new AjaxExtender();
		
		var content = new Object();
		for (var key in params) {
			content[key] = params[key];
		}
		
		var sourceComponent = dojo.byId(sourceId);
	
	    if (sourceComponent != null){
	    	if(sourceComponent.value != undefined && sourceComponent.type && ("button,submit,reset").indexOf(sourceComponent.type) < 0) {
				content[sourceId] = sourceComponent.value;
			}
			else if(sourceComponent.name != undefined) {
	    		content[sourceComponent.name] = sourceComponent.name;
	    	} else {
	    		content[sourceId] = sourceId;
	    	}
	    }
	    
		if (!content['ajaxSource']) {
			content['ajaxSource'] = sourceId;
		}
		
		dojo.xhrPost({
			
			content: content,
			
			form: formId,
			
			handleAs: "text", 
			
			/*
			 * 
			 * Edited By Hany K Abdelsayed
			 * 
			 * Set "headers" property by the "headers" object in the Dojo's Ajax post method 
			 */ 
			/*headers: {
				"Accept" : "text/html;type=ajax"
			},*/
			headers: ajaxExtender.getHeaders(sourceId),
			
			/*
			 * 
			 * Edited By Hany K Abdelsayed
			 * 
			 * handle extra functionality on AJAX request callback functions
			 */ 
			// The LOAD function will be called on a successful response.
	        //load: this.handleResponse,
			load: function(response, ioArgs) {				
				ajaxExtender.handleAjaxLoad(sourceId, response, ioArgs);
				return me.handleResponse(response, ioArgs);
			},
			
			
	        // The ERROR function will be called in an error case.
	        //error: this.handleError
			error: function(response, ioArgs) {				
				ajaxExtender.handleAjaxError(sourceId, response, ioArgs);
				return me.handleError(response, ioArgs);
			},
        });	

	},
	
	getLinkedResource: function(/*String */ linkId, /*Object*/params, /*boolean*/ modal) {
		this.getResource(dojo.byId(linkId).href, params, modal);
	},
	
	getResource: function(/*String */ resourceUri, /*Object*/params, /*boolean*/ modal) {
		/*
		 * 
		 * Edited By Hany K Abdelsayed
		 * 
		 * Set a variable by current instance 
		 */ 
		var me = this;
		
		/*
		 * declare an instance from the AjaxExtender class
		 */
		var ajaxExtender = new AjaxExtender();
		
		dojo.xhrGet({
			
			url: resourceUri,
			
			content: params,
			
			handleAs: "text",
			
			/*
			 * 
			 * Edited By Hany K Abdelsayed
			 * 
			 * Set "headers" property by the "headers" object in the Dojo's Ajax get method 
			 */ 
			/*headers: {"Accept" : "text/html;type=ajax"},*/
			headers: ajaxExtender.getHeaders(sourceId),
						
			/*
			 * 
			 * Edited By Hany K Abdelsayed
			 * 
			 * handle extra functionality on AJAX request callback functions
			 */ 
			// The LOAD function will be called on a successful response.
	        //load: this.handleResponse,
			load: function(response, ioArgs) {				
				ajaxExtender.handleAjaxLoad(sourceId, response, ioArgs);
				return me.handleResponse(response, ioArgs);
			},		
			
			//error: this.handleError
			error: function(response, ioArgs) {				
				ajaxExtender.handleAjaxError(sourceId, response, ioArgs);
				return me.handleError(response, ioArgs);
			},
			
			modal: modal
		});
	},
	
	handleResponse: function(response, ioArgs) {
		//First check if this response should redirect
		var redirectURL = ioArgs.xhr.getResponseHeader('Spring-Redirect-URL');
		var modalViewHeader = ioArgs.xhr.getResponseHeader('Spring-Modal-View');
		var modalView = ((dojo.isString(modalViewHeader) && modalViewHeader.length > 0) || ioArgs.args.modal);
		
		if (dojo.isString(redirectURL) && redirectURL.length > 0) {
			if (modalView) {
				//render a popup with the new URL
				Spring.remoting.renderURLToModalDialog(redirectURL, ioArgs);
				return response;
			}
			else {
				if (redirectURL.indexOf("/") >= 0) {
					window.location = window.location.protocol + "//" + window.location.host + redirectURL;
				} else {
					var location = window.location.protocol + "//" + window.location.host + window.location.pathname;
					var appendIndex = location.lastIndexOf("/");
					location = location.substr(0,appendIndex+1) + redirectURL;
					if (location == window.location) {
						Spring.remoting.getResource(location, ioArgs.args.content, false);
					}
					else {
						window.location = location;
					}
				}
				return response;
			}
		}
		
		//Extract and store all <script> elements from the response
		var scriptPattern = '(?:<script(.|[\n|\r])*?>)((\n|\r|.)*?)(?:<\/script>)';
		var extractedScriptNodes = [];
		var matchAll = new RegExp(scriptPattern, 'img');
		var matchOne = new RegExp(scriptPattern, 'im');
	
		var scriptNodes = response.match(matchAll);
		if (scriptNodes != null)
		{
			for (var i=0; i<scriptNodes.length; i++)
			{
				var script = (scriptNodes[i].match(matchOne) || ['','',''])[2];
				script = script.replace(/<!--/mg,'').replace(/\/\/-->/mg,'').replace(/<!\[CDATA\[(\/\/>)*/mg,'').replace(/(<!)*\]\]>/mg,'');
				extractedScriptNodes.push(script);
			}
		}
		response = response.replace(matchAll, '');

		//Extract the new DOM nodes from the response
		var tempSpan = dojo.doc.createElement("span");
		tempSpan.id="ajaxResponse";
		tempSpan.style.visibility= "hidden";
		document.body.appendChild(tempSpan);
		tempSpan.innerHTML=response;
		var tempContainer = new dojo.NodeList(tempSpan);
		var newNodes = tempContainer.query("#ajaxResponse > *").orphan();
		tempContainer.orphan();
	
		//For a modal view, just dump the new nodes into a modal dialog
		if (modalView) {
			Spring.remoting.renderNodeListToModalDialog(newNodes);
		}
		else {
			//Insert the new DOM nodes and update the Form's action URL
			newNodes.forEach(function(item){
				if (item.id != null && item.id != "") {
					var target = dojo.byId(item.id);
					if (!target) {
						console.error("An existing DOM elment with id '" + item.id + "' could not be found for replacement.");
					} else {
						target.parentNode.replaceChild(item, target);
					}
				}
			});
		}
		
		//Evaluate any script code
		dojo.forEach(extractedScriptNodes, function(script){
			dojo.eval(script);
		});
		
		return response;
	},
	
	handleError: function(response, ioArgs) {
		dojo.require("dijit.Dialog");
		
		console.error("HTTP status code: ", ioArgs.xhr.status);
		
		if (Spring.debug && ioArgs.xhr.status != 200) {
			var dialog = new dijit.Dialog({});
			dojo.connect(dialog, "hide", dialog, function(){
				this.destroyRecursive(false);
			});
			dialog.domNode.style.width = "80%";
			dialog.domNode.style.height = "80%";
			dialog.domNode.style.textAlign = "left";
			dialog.setContent(ioArgs.xhr.responseText);
			dialog.show();
		}
		
		return response;
	},
	
	renderURLToModalDialog: function(url, ioArgs) {
		url = url + "&"+dojo.objectToQuery(ioArgs.args.content);
		
		Spring.remoting.getResource(url, {}, true);
	},
	
	renderNodeListToModalDialog: function(nodes) {
		dojo.require("dijit.Dialog");
		
		var dialog = new dijit.Dialog({});
		dialog.setContent(nodes);
		dojo.connect(dialog, "hide", dialog, function(){
			this.destroyRecursive(false);
		});
		dialog.show();
	}
});

dojo.declare("Spring.CommandLinkDecoration", [Spring.AbstractCommandLinkDecoration, Spring.DefaultEquals], {
	constructor : function(config){
		dojo.mixin(this, config);
	},
	
	apply : function(){
		var advisedNode = dojo.byId(this.elementId);
		if (!dojo.hasClass(advisedNode, "progressiveLink")) {
			//Node must be replaced
			var nodeToReplace = new dojo.NodeList(advisedNode);
			nodeToReplace.addContent(this.linkHtml, "after").orphan("*");
			//Get the new node
			advisedNode = dojo.byId(this.elementId);
		}
		advisedNode.submitFormFromLink = this.submitFormFromLink;
		//return this to support method chaining
   		return this;
	},
	
	submitFormFromLink : function(/*String*/ formId, /*String*/ sourceId, /*Array of name,value params*/ params){
		var addedNodes = [];		
		var formNode = dojo.byId(formId);
		var linkNode = document.createElement("input");
		linkNode.name = sourceId;
		linkNode.value = "submitted";
		addedNodes.push(linkNode);
		
		dojo.forEach(params, function(param){
			var paramNode = document.createElement("input");
			paramNode.name=param.name;
			paramNode.value=param.value;
			addedNodes.push(paramNode);
		});
		
		dojo.forEach(addedNodes, function(nodeToAdd){
			dojo.addClass(nodeToAdd, "SpringLinkInput");
			dojo.place(nodeToAdd, formNode, "last");
		});		
		
		if ((formNode.onsubmit ? !formNode.onsubmit() : false) || !formNode.submit()) {
			dojo.forEach(addedNodes, function(hiddenNode){
				formNode.removeChild(hiddenNode);
			});
		}
	}
});

dojo.addOnLoad(Spring.initialize);