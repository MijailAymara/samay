
$(document).ready(function() {
	//MANEJO DEL FORMULARIO DE BUSQUEDA
//	$('#idBtnBuscarFormulario').click(function () {
//		 $('#myTableFormulario314').DataTable().ajax.reload();  //PARA LLAMAR AL DATATABLE PRINCIPAL A TRAVES DEL ID
//	});
	
	$('#periodo').datetimepicker({
		format: 'YYYYMM',
	    ignoreReadonly: false,
	    date: new Date(),
	    maxDate: new Date(),
	    locale: "es"
	});
	
	cargarCombo("listTipoEnsayo","#cboTipoEnsayo");

});

$('#modal-registra-detalle').on('shown.bs.modal', function() {
	$($.fn.dataTable.tables(true)).DataTable().columns.adjust();
})

//MANEJO DEL FORMULARIO DE BUSQUEDA
	$('#idBtnBuscarFormulario').click(function () {
		 $('#myTableFormulario314').DataTable().ajax.reload();  //PARA LLAMAR AL DATATABLE PRINCIPAL A TRAVES DEL ID
	});

function modificarFormulario(id) {
//	id= -1 Boton Agregar
	MYAPPL.blockPageLoad();
	var accion;
	
	$.ajax({
		type: 'GET',
		url: 'formulario314Get/'+id, //LLAMA AL CONTROLER Y DEVUELVE UNA PAGINA(PAGUNA HEADER)
		success: function(result) {
			$('#id-modal-content').replaceWith(result); //SE INYECTA EN LA PAGINA PRINCIPAL EL HEADER
			
			cargarCombo("listEspecialistas","#cboAnalista");
			cargarCombo("listTipoEnsayo","#cboTipoEnsayo2");
			
			if(id != -1){ //EDITAR
				setTimeout(function(){

					$('#cboTipoEnsayo2').val($('#cboTipoEnsayoHidden').val());
					$('#cboAnalista').val($('#cboAnalistaHidden').val());
				}, 500);
				
				accion = 'U';
				$('#opcion').val(accion);
				
				$('#strFecha').datetimepicker({   // -->se desabilita la fecha xq falla al recuperar de la bd
			        format: 'DD/MM/YYYY',         // --> debido new date
			        ignoreReadonly: false,      
			        //maxDate: new Date(),        
			        locale: "es"
			    });
				
//				$("#strFecha").attr('disabled', 'disabled');
				
				$('#idModalTitle').html($('#idValEditModalTitle').html()); 

			}else{
				
				accion = 'S'
				$('#opcion').val(accion);
				
				$('#strFecha').datetimepicker({
			        format: 'DD/MM/YYYY',
			        ignoreReadonly: false,
			        date: new Date(),
			        maxDate: new Date(),
			        locale: "es"
			    });
				
				$('#idModalTitle').html($('#idValCrearModalTitle').html());
			}
			
			enableValidationRules();
			
		},complete: function() {
			
			$('#modal-registra-formularioHeader').modal('show'); //MUESTRA EN LA PANTALLA PRINCIPAL FORMULARIO HEADER
			$.unblockUI();
		}
	});
}



//FUNCION PARA INACTIVAR UN REGISTRO
function inactivarDetalle(ids) {
	$.confirm({
	    title: 'Confirmaci\u00f3n',
	    content: 'Confirma inactivar Registro ?',
	    buttons: {
	    	Aceptar: { 
	        	btnClass: 'btn-blue',
	        	action: function () {
		        	$('#inacId').val(ids); 
				    $.ajax({
			    	   type: 'POST',
			    	   url: $('#form-inactiva-formulario314').attr('action'),
			    	   data: $('#form-inactiva-formulario314').serialize(),  
			    	   success: function(result){  		   
			    	       $('.container_save').html(result);		    	      
				    	   },
							complete: function() {
								$.unblockUI();
							}
				    	}); 
		        	}
		        },
		        Cancelar: {
		        }
		    }
		});	   
}


function enableValidationRules() {

	var validator = $("#form-edit-formulario314").bootstrapValidator({   //id del formulario que se va abrir (POPUP A LEVANTAR)
		message: 'El valor no es v\u00e1lido',
		feedbackIcons : {
			valid : "glyphicon glyphicon-ok",
			invalid : "glyphicon glyphico-remove",
			validating : "glyphicon glyphicon-refresh"
		},
		fields : {
			strFecha: {
					validators : {
						
						notEmpty: {
	                        message: 'Ingrese la Fecha'
	                    }
					}
			},
			
			codMuestra: {
				validators : {
							
					stringLength : {
						max : 8,
						message : "El valor ha de ser como m\u00e1ximo 8 caracteres"
					}		
				}
			},
			
			resultado1: {
				validators : {
							
					stringLength : {
						max : 8,
						message : "El valor ha de ser como m\u00e1ximo 8 caracteres"
					},			
					regexp: {
						 
						 regexp: /^[0-9]+([.][0-9]+)?$/, 
	 
						 message: 'Valor incorrecto'
	 
					}
				}
			},
			
			resultado2: {
				validators : {
							
					stringLength : {
						max : 8,
						message : "El valor ha de ser como m\u00e1ximo 8 caracteres"
					},			
					regexp: {
						 
						 regexp: /^[0-9]+([.][0-9]+)?$/, 
	 
						 message: 'Valor incorrecto'
	 
					}
				}
			},
				
		}
	})	
	.on('success.form.bv', function(e, data) {
		$("#guardarButton").html('Guardando...');
		$("#guardarButton").attr('disabled', 'disabled');
		e.preventDefault();
		var $formularioValidator314 = $(e.target);
		$.ajax({
			type: 'POST',
			url: $('#form-edit-formulario314').attr('action'), //id del formulario que se va abrir (POPUP A LEVANTAR)
			data: $('#form-edit-formulario314').serialize(),
			success: function(result){
				$formularioValidator314.bootstrapValidator('resetForm', true);
				$formularioValidator314[0].reset();
				$('.container_save').html(result);
			},
			complete: function() {
				$("#guardarButton").html('Guardar');
				$('#guardarButton').removeAttr("disabled");
			}
		});
	});	
}



function showToastFormulario314(mensajeTipo, mensajeError) {
	//Mensajes despues de grabar
	if(mensajeTipo==='grabadoOk'){
		 toastr["success"](mensajeError, "Registro correctamente guardado");
		 $('#modal-registra-formularioHeader').modal('hide');
		 $('#idBtnBuscarFormulario').click();
	 }
	 if(mensajeTipo==='grabadoNoOk'){
		 toastr["error"](mensajeError, "Hubo un error en el servidor, el registro no fue guardado");
	 }
	 //Mensajes despues de eliminar
	 if(mensajeTipo==='inactivadoOk'){
		 toastr["success"](mensajeError, "Registro correctamente inactivado");
//		 $('#modal-registra-formularioHeader').modal('hide');
		 $('#idBtnBuscarFormulario').click();
	 }
	 if(mensajeTipo==='inactivadoNoOk'){
		 toastr["error"](mensajeError, "Hubo un error en el servidor, el registro no fue inactivado");
	 }
	 if(mensajeTipo==='RegDuplicado'){
		 toastr["success"](mensajeError, "Registro ya existe en la Tabla");
		 $('#modal-registra-formularioHeader').modal('hide');
		 //$('#idBtnBuscarTurbidimetroDigital').click();
	 }
	 if(mensajeTipo==='grabadoOkSubForm'){
		 toastr["success"](mensajeError, "Registro agregado correctamente");
		 $('#modal-registra-detalle-edit').modal('hide');
//		 $('#idCodigoVerifEquipo').val($('#intCodigo').val());
		 $('#myTableDetalle222').DataTable().ajax.reload();   //LLAMA AL DATATABLE DETALLE A TRAVES DE SU ID
	 }
	 if(mensajeTipo==='inactivadoOkSubForm'){

		 toastr["success"](mensajeError, "Registro correctamente inactivado");
//		 $('#idCodigoVerifEquipo').val($('#intCodigo').val());
		 $('#myTableDetalle222').DataTable().ajax.reload();
	 }
}

