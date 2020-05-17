//console.log("Task");

$(function() {
	// for handling CSRF token
	var _csrf_token = $('meta[name="_csrf"]').attr('content');
	var _csrf_header = $('meta[name="_csrf_header"]').attr('content');
	if ((_csrf_token != undefined && _csrf_header != undefined)
			&& (_csrf_token.length > 0 && _csrf_header.length > 0)) {
		// set the token header for the ajax request
		$(document).ajaxSend(function(e, xhr, options) {
			xhr.setRequestHeader(_csrf_header, _csrf_token);
		});
	}
	
	// handle click event for refresh button 
    $('#doRefresh').on('click', function (e) {
       $.ajax({
            url: 'task',
            cache: false
        }).done(function (html) {
            $('#taskListTable').replaceWith(html);
        });
    });
    
    
    // handle click event for delete task button 
    $.fn.doDelete = function(id) {
		$.ajax({
            url: 'task/' + id,
            type: 'delete'
    	}).done(function (html) {
    		$('#taskListTable').replaceWith(html);
        });
	};
    
	// handle click event for save task button 	
	$.fn.doSave = function() {
		var $form = $('#addTaskForm');
		var $successMessage = $('#successMessageDiv');
		//$(document.body).append($form);
		
		// declare submit action for the form
		$form.on('submit', function(e) {
	    	e.preventDefault();
	    	$.ajax({
	    		url: $form.attr('action'),
	    		type: 'post',
	    		data: $form.serialize(),
	    		success: function(response) {    	
	    			// replace the form
	    			$form.replaceWith(response);
	    			
	    			// if the response contains any errors, hide message part
	    			if ($(response).find('.has-error').length > 0) {
	    				// hide the success message
	    				$successMessage.addClass('hidden');
	    				$successMessage.hide();
	    			} else {
	    				// redirect to task list
	    				/*var pathname = window.location.pathname;
	    				pathname = pathname.substring(0, pathname.lastIndexOf('/')+1);
	    				window.location.replace(pathname + "task");*/

	    				// show the success message
	    				$successMessage.removeClass('hidden');
	    				$successMessage.show();	    				
	    			}
	    		}
	    	});
	    });
		
		$form.submit();
	};  
	
	$.fn.clearForm = function() {
		// clear the form 
		var $form = $('#addTaskForm');
		$form.clear();
		
		// hide the success message
		var $successMessage = $('#successMessageDiv');
		$successMessage.addClass('hidden');
		$successMessage.hide();
	};
	$.fn.clear = function() {
		$(this).trigger("reset");
	    /*$(this).find('input')
	            .filter(':text, :password, :file').val('')
	            .end()
	            .filter(':checkbox, :radio')
	                .removeAttr('checked')	                
	            .end()
	        .end()
	    .find('textarea').val('')
	        .end()	    
	    .find('select').prop("selectedIndex", -1)
	        .find('option:selected').removeAttr('selected')
	    ;*/
	    
	    //$('input[type=date]').val('');
	    
	    /*$('input[type=text]').val('');
	    $('input[type=password]').val('');
	    $('input[type=file]').val('');
	    
	    
	    $('textarea').val('');
	    
	    $('input[type=checkbox]').removeAttr('checked');
	    $('input[type=radio]').removeAttr('checked');
	    
	    $('select').prop("selectedIndex", -1);*/	    
	};
	
    	
});