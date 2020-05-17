//console.log("Tasks");

$(function() {

	// for handling CSRF token
	var _csrf_token = $('meta[name="_csrf"]').attr('content');
	var _csrf_header = $('meta[name="_csrf_header"]').attr('content');
	
	// for handling toggle form action after rendering the form via AJAX
	var $toggleChkBox = $('#toggleTaskFormChkBox');
	var doToggleAction = true;
	var isHandleMessageOnToggle = true;
	
	var $formId = 'taskForm';
		
	// handle click event for close form button 	
	$.fn.doClose = function() {
		$toggleChkBox.bootstrapToggle('on');			
	};
	
	// handle click event for save task button 	
	$.fn.doSave = function() {
		//$toggleChkBox.bootstrapToggle('on');
		//operation = "save";
		$("#saveTask").click();
	};
	
	// handle click event for delete task button 
	$.fn.doDelete = function(id) {
		//$toggleChkBox.bootstrapToggle('on');
		//operation = "delete";
		$("#taskId").attr('value' , id)
		$("#deleteTask").click();
	};
	
	// handle on change event for toggle form button (check box)
	$toggleChkBox.on('change', function (e) {
    	//if(doToggleAction) $('#doAddTask').click();
    	
    	if(doToggleAction) {
    		// clear the form fields  
    		var $form = $('#taskForm');
    		$form.clear();
			
			// show/hide the form
    		var $formContainer = $('#taskFormDiv');
			if (!$(this).prop('checked')) {
				// show form
				$formContainer.removeClass('hidden');								
			} else {
				// hide form
				$formContainer.addClass('hidden');				
			}
			
			// hide global message section on toggling
			var $globalMessage = $('#globalMessage');
			if(isHandleMessageOnToggle) {
				$globalMessage.addClass('hidden');				
			}
						
    	}
    	
    	// return to default behavior
		doToggleAction = true;
		isHandleMessageOnToggle = true;
    	
    });
	
	$.fn.clear = function() {
		$(this).trigger("reset");
	};
	
	
	
	/*
	 * Spring AJAX Decorations
	 */
	
	// add AJAX spring decoration for refresh task button click event  
	Spring.addDecoration(new Spring.AjaxEventDecoration({
		formId : 'actionsTaskTableForm',
		elementId : 'doRefresh',
		event : 'onclick',
		params : {
			
		},
		headers : {
			[_csrf_header] :  _csrf_token
		},
		load: function(data) {
			// keep all actions that are binded to toggle button on change event
			doToggleAction = true;
			
			// let toggle button on change event hiding message section
			isHandleMessageOnToggle = true; 
						
			// turn on the toggle button   
			$toggleChkBox.bootstrapToggle('on');
		}
	}));
	
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

	// add AJAX spring decoration for delete task button click event 
	Spring.addDecoration(new Spring.AjaxEventDecoration({
		formId : $formId,
		elementId : 'deleteTask',
		event : 'onclick',
		headers : {
			[_csrf_header] :  _csrf_token
		},
		load: function(data) {
			// keep all actions that are binded to toggle button on change event
			doToggleAction = true;
			
			// stop toggle button on change event to hide message section
			isHandleMessageOnToggle = false; 
			
			$toggleChkBox.bootstrapToggle('on');
		}
	}));
	
	
	/*Spring.addDecoration(new Spring.AjaxEventDecoration({
		formId : 'actionsTaskTableForm',
		elementId : 'doAddTask',
		event : 'onclick',
		headers : {
			[_csrf_header] :  _csrf_token
		}
	}));*/
	
});
