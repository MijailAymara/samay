package pe.com.sedapal.scr.core.dao;

import pe.com.sedapal.common.core.beans.Paginacion;
import pe.com.sedapal.common.core.beans.Result;
import pe.com.sedapal.scr.core.beans.PhmetroDigitalBean;
import pe.com.sedapal.scr.core.beans.PhmetroDigitalDetalleBean;
import pe.com.sedapal.scr.core.beans.PhmetroDigitalBean;
import pe.com.sedapal.scr.core.beans.TurbidimetroDigitalVerificacionBean;

public interface IPhmetroDigitalDao {

	/*
	 * Método que permite obtener el listado de equipos
	 * @param phmetroDigitalBean Contiene el bean que representa la búsqueda
	 * @param paginacion Representa la página solicitada
	 * @Return Objeto de tipo Result que contiene los resultados
	 * @throws Exception Excepción que puede ser lanzada
	 * */
	Result obtenerDatosEquipos(PhmetroDigitalBean phmetroDigitalBean, Paginacion paginacion) throws Exception;
	
	/* 
	 * Obtiene Información de equipo por identificador
	 * @param nid Identificador del registro
	 * @throws Exception Excepción que puede ser lanzada
	 * */	
	PhmetroDigitalBean obtenerPhmetroDigital(Integer id) throws Exception;
	
	
	/* 
	 * Realiza el registro de un registro de la tabla de PHmetro Digital
	 * @param phmetroDigitalBean objeto del tipo PhmetroDigitalBean que contiene el registro  
	 * @throws Exception Excepción que puede ser lanzada
	 * */	
	int grabarPhmetroDigital(PhmetroDigitalBean phmetroDigitalBean) throws Exception;
	
	
	/* 
	 * Realiza la modificación de un registro de la tabla de PHmetro Digital
	 * @param phmetroDigitalBean objeto del tipo PhmetroDigitalBean que contiene el registro 
	 * @throws Exception Excepción que puede ser lanzada
	 * */
	void actualizarPhmetroDigital(PhmetroDigitalBean phmetroDigitalBean) throws Exception;
	
	/* 
	 * Realiza el cambío de estado de un registro de la tabla de PHmetro Digital
	 * @param phmetroDigitalBean objeto del tipo PhmetroDigitalBean que contiene el registro 
	 * @throws Exception Excepción que puede ser lanzada
	 * */
	void inactivarPhmetroDigital(PhmetroDigitalBean phmetroDigitalBean) throws Exception;
	
	/*
	 * Método que permite obtener el detalle de la tabla  PHmetro Digital
	 * @param phmetroDigitalDetalleBean Contiene el bean que representa la búsqueda
	 * @param paginacion Representa la página solicitada
	 * @Return Objeto de tipo Result que contiene los resultados
	 * @throws Exception Excepción que puede ser lanzada
	 * */
	public Result obtenerDatosEquiposDetalle(PhmetroDigitalDetalleBean phmetroDigitalDetalleBean, Paginacion paginacion) throws Exception;
	
	/* 
	 * Realiza el registro de un registro de la tabla de detalle de PHmetro Digital
	 * @param phmetroDigitalDetalleBean objeto del tipo PhmetroDigitalDetalleBean que contiene el registro  
	 * @throws Exception Excepción que puede ser lanzada
	 * */
	public int grabarPhmetroDigitalDetalle(PhmetroDigitalDetalleBean phmetroDigitalDetalleBean) throws Exception;
	
	/* 
	 * Realiza el cambío de estado de un registro de la tabla detalle de PHmetro Digital
	 * @param phmetroDigitalDetalleBean objeto del tipo PhmetroDigitalDetalleBean que contiene el registro 
	 * @throws Exception Excepción que puede ser lanzada
	 * */
	public void inactivarPhmetroDigitalDetalle(PhmetroDigitalDetalleBean phmetroDigitalDetalleBean) throws Exception;
	
	
}
