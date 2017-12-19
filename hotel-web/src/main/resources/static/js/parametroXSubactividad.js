/**
 * Hoja de scripts manejados en el mantenimiento de clima
 */
$(document).ready(function() {
	
	toastr.options = {
		"closeButton" : true,
		"debug" : false,
		"newestOnTop" : false,
		"progressBar" : false,
		"positionClass" : "toast-top-center",
		"preventDuplicates" : false,
		"showDuration" : "300",
		"hideDuration" : "1000",
		"timeOut" : "5000",
		"extendedTimeOut" : "1000",
		"showEasing" : "swing",
		"hideEasing" : "linear",
		"showMethod" : "fadeIn",
		"hideMethod" : "fadeOut"
	}
	//cargando combos de Areas
	cargarComboSelectable('listAreas',"#cboAreas");
});


/*METODO PARA INSERTAR */
function insertarActividad(insertarActividad) {
	MYAPPL.blockPageLoad();
	$.ajax({
		type: 'POST',
		url: 'insertarParametroXSubactividad',
		data : dataInsertar(insertarActividad),
		success: function(result) {
			$('#id-modal-content').html(result);
		},complete: function() {
			$.unblockUI();
		}
	});
}

function removerActividad(indices) {
	MYAPPL.blockPageLoad();
	$.ajax({
		type: 'POST',
		url: 'modificarParametroXSubactividad',
		data : dataEliminar(indices),
		success: function(result) {
			$('#id-modal-content').html(result);
		},complete: function() {
			$.unblockUI();
		}
	});
}


function dataInsertar(indices){
	var data = {};
	data.descripcionAsignada = "";
	data.idMaster = $('#cboSubActividades').find(":selected").data("idmaster");
	data.idDetalle = $('#cboActPosibles').find(":selected").data("id");;
	data.estado = "1";
	data.indices = indices;
	console.log("data: " + JSON.stringify(data));
	return data;
}

function dataActualizarDescripcion() {
	var data = {};
	data.descripcionAsignada = $("#commentActPersonalizada").val();
	data.idMaster = $('#cboSubActividades').find(":selected").data("id");
	data.idDetalle = $('#cboActAsignadas').find(":selected").data("id");
	data.estado = "1";
	data.accion = "UD";
	return data;
}

function dataEliminar(indices) {
	var data = {};
	data.descripcionAsignada = "";
	data.idMaster = $('#cboSubActividades').find(":selected").data("idmaster");
	data.idDetalle = $('#cboActAsignadas').find(":selected").data("id");
	data.estado = "0";
	data.indices = indices;
	return data;
}

$(document).on("click", ".btn-limpiar", function(e) {
	e.preventDefault();
	clearSearchClima();
});


//PickList
	  $.fn.pickList = function(options) {

	    var opts = $.extend({}, $.fn.pickList.defaults, options);

	    this.fill = function() {
	      var option = '';

	      $.each(opts.data, function(key, val) {
	        option += '<option data-id=' + val.id + '>' + val.text + '</option>';
	      });
	      this.find('.pickData').append(option);
	    };
	    this.controll = function() {
	      var pickThis = this;

	      pickThis.find(".pAdd").on('click', function() {
	    	var arrayInsertar = "";
		    var p = pickThis.find("#cboActPosibles option:selected");
		    if(p.length<1){
		    	return;
		    }
		    p.each(function(index){
		        	var idSeleccionado = $(this).attr('data-id');
		        	arrayInsertar += (idSeleccionado + "#");
		    })  
	    	  
		    console.log("arrayInsertar: " + arrayInsertar)
	    	insertarActividad(arrayInsertar);
		        
	        p.clone().appendTo(pickThis.find(".pickListResult"));
	        p.remove();
	        var itemAdded = p.attr("data-id");
	        $("#commentActDefault").val("");
	        
	      });

	      pickThis.find(".pRemove").on('click', function() {
	    	
	    	var arrayModificar = "";
	        var p = pickThis.find(".pickListResult option:selected");
	        if(p.length<1){
		    	return;
		    }
	        p.each(function(index){
	        	var idSeleccionado = $(this).attr('data-id');
	        	arrayModificar += (idSeleccionado + "#");
	        })
	        
	        removerActividad(arrayModificar);
	        
	        p.clone().appendTo(pickThis.find(".pickData"));
	        p.remove();
	        $("#commentActPersonalizada").val("");
	        var itemAdded = p.attr("data-id");
	        
	        setTimeout(function(){
	        	refrescarSectorPosibles();
	        },300);
	        
	      });

	    };

	    this.getValues = function() {
	      var objResult = [];
	      this.find(".pickListResult option").each(function() {
	        objResult.push({
	          id: $(this).data('id'),
	          text: this.text
	        });
	      });
	      return objResult;
	    };

		  this.fill();
	      this.controll();
	 
	    return this;
	  };

	  $.fn.pickList.defaults = {
	    add: 'Add',
	    addAll: 'Add All',
	    remove: 'Remove',
	    removeAll: 'Remove All'
	  };

	  //cargar picklist
	var pick = $("#pickList").pickList({
	  data: {}
	});
	

	$('#cboAreas').change(function() {
		$("#cboActividades").html("");
		$("#cboActPosibles").html("");
		$("#cboActAsignadas").html("");
		$("#cboSubActividades").html("");
		
		
		var idArea = $('#cboAreas').find(":selected").data("id");
		var idAreaSeleccionada = $('#cboAreas').find(":selected").data("id");
		
		if(idAreaSeleccionada == undefined || idAreaSeleccionada == null){
			clearCombosForArea();
		}else{
			
			$("#cpDesc").val("000"+idAreaSeleccionada);
			MYAPPL.blockPageLoad();
			$.ajax({
				url : "listActividadesCbo/"+idArea,
				type : 'GET',
				dataType : "json",
				complete : function(xhr, status) {
					if(status == 'success' || status=='notmodified')
					{
						console.log("lenght: "+ xhr)
						var data =  $.parseJSON(xhr.responseText);
						var html = '';
						var len = data.length;
						if(len<1){
							$("#cboActividades").html("");
							$("#cboActPosibles").html("");
							$("#cboActAsignadas").html("");
						}else{
							html += '<option>-Seleccione-</option>';
							for ( var i = 0; i < len; i++) {
								html += '<option data-idMaster="'+ data[i].idMaster + '"  data-idDetalle="'+ data[i].idDetalle +'" value="' + data[i].value + '">'
								+ data[i].label + '</option>';
							}
						}
						html += '</option>';
						$("#cboActividades").html(html);
					}
					
					$.unblockUI();
				}
			});
			
		}
	});
	
	
	$('#cboActividades').change(function() {
		$("#cboActPosibles").html("");
		$("#cboActAsignadas").html("");
		$("#cboSubActividades").html("");
		
		
		var idActividad = $('#cboActividades').find(":selected").data("idmaster");
		
		console.log("idActividad: " + idActividad)
		if(idActividad != undefined && idActividad != null){
			
			MYAPPL.blockPageLoad();
			$.ajax({
				url : "listSubActividadesCbo/"+idActividad,
				type : 'GET',
				dataType : "json",
				complete : function(xhr, status) {
					if(status == 'success' || status=='notmodified')
					{
						console.log("lenght: "+ xhr)
						var data =  $.parseJSON(xhr.responseText);
						var html = '';
						var len = data.length;
						if(len<1){
							$("#cboSubActividades").html("");
							$("#cboActPosibles").html("");
							$("#cboActAsignadas").html("");
						}else{
							html += '<option>-Seleccione-</option>';
							for ( var i = 0; i < len; i++) {
								html += '<option data-idMaster="'+ data[i].idMaster + '"  data-idDetalle="'+ data[i].idDetalle +'" value="' + data[i].value + '">'
								+ data[i].label + '</option>';
							}
						}
						html += '</option>';
						$("#cboSubActividades").html(html);
					}
					
					$.unblockUI();
				}
			});
			
		}
	});
	
	$('#cboSubActividades').change(function() {
		var idMaster = $('#cboSubActividades').find(":selected").data("idmaster");
		
		if(idMaster==undefined || idMaster == null){
			$("#cboActPosibles").html("");
			$("#cboActAsignadas").html("");
			return;
		}
		
		var arrayAsignadas = [];
		var actPosibles;
		
		MYAPPL.blockPageLoad();
		$.getJSON("listParametros").then(function(data){
			actPosibles = data;

			$.getJSON("listParametrosAsignados/"+idMaster).then(function(data){
		 
		var actAsignadas = data;

		for(var i in actAsignadas) {
			  		arrayAsignadas.push(actAsignadas[i].id);
		};

		actPosibles = actPosibles.filter(function(currentObject,index) {
		   if(arrayAsignadas.indexOf(currentObject.id)!= -1){
		   		return false;
		   }
		   return true;
		});
		
		//CARGA DE LOS DISTRITOS EN LA BUSQUEDA
		
		armarCbo(actPosibles,"#cboActPosibles");
		armarCbo(actAsignadas,"#cboActAsignadas");
		
		
		setTimeout(function(){
			$('#cboActAsignadas option:eq(0)').attr('selected', 'selected');
			$('#cboActAsignadas option:eq(0)').click();
			
		},200)

		});
			
			$.unblockUI();
	 
		});
	});
	
	
	function unselectOption(idPickListDestinoMovido,idItemMoved) {
		  var ident = idPickListDestinoMovido + " option"
	     $.each($(ident), function() {
	    	 var id= $(this).attr('data-id');
	    	 			if(idItemMoved!=id){
	    	 				$(this).attr('selected', false);
	    	 				$(this).prop('selected', false);
	    	 			}
	                });
	   
	 }
	
	function clearCombosForArea(){
		$("#cpDesc").val("");
		$("#cboSubActividades").html("");
		$("#cboActPosibles").html("");
		$("#cboActAsignadas").html("");
	}
