$(document).ready(function() {
	$('#enviarButton').click(function () {		 
		MYAPPL.blockPageLoad();
		$.ajax({
	 	   type: 'POST',
	 	   url: "correoGerencia/ejecutar",
	 	   success: function(result){  	
	 		  toastr.success(result);
		    },
		    error: function( jqXHR, textStatus, errorThrown ) {
		    	toastr.error(jqXHR.responseText);
		    },
			complete: function() {
				$.unblockUI();
			}
		}); 
	});
});