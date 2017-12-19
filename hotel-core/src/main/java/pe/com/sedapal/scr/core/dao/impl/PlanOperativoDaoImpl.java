package pe.com.sedapal.scr.core.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Repository;

import oracle.jdbc.OracleTypes;
import pe.com.sedapal.common.core.beans.Paginacion;
import pe.com.sedapal.common.core.beans.Result;
import pe.com.sedapal.common.core.utils.ConstantsCommon;
import pe.com.sedapal.common.core.utils.CoreUtils;
import pe.com.sedapal.scr.core.beans.ParametroPlan;
import pe.com.sedapal.scr.core.beans.ParametroReporte;
import pe.com.sedapal.scr.core.beans.PlanOperativo;
import pe.com.sedapal.scr.core.beans.TotalesForm20;
import pe.com.sedapal.scr.core.common.Constants;
import pe.com.sedapal.scr.core.common.ConstantsLaboratorio;
import pe.com.sedapal.scr.core.dao.IPlanOperativoDao;
import pe.com.sedapal.scr.core.util.ExecuteProcedure;

@Repository
public class PlanOperativoDaoImpl implements IPlanOperativoDao {

	@Autowired
	private Environment environment;

	private JdbcTemplate template;

	private ExecuteProcedure execSp;

	@Autowired
	public void setTemplate(JdbcTemplate template) {
		this.template = template;
	}

	@Override
	public Result obtenerDatosParametrosPlan(ParametroPlan parametroPlan, Paginacion paginacion) {
		List lstRetorno = null;

		SimpleJdbcCall caller = new SimpleJdbcCall(template.getDataSource());
		caller.withCatalogName(ConstantsLaboratorio.PCK_ALC_PLANOPERATIVO)
				.withProcedureName(ConstantsLaboratorio.PRC_BUSCA_PARAMETROPLAN)
				.declareParameters(

						// parametros de búsqueda
						new SqlParameter(ConstantsLaboratorio.PAR_CODPLA, Types.INTEGER),

						new SqlOutParameter(ConstantsCommon.PAR_OUT_CURSOR, Types.INTEGER), new SqlOutParameter(
								ConstantsCommon.PAR_OUT_CURSOR, OracleTypes.CURSOR, new RowMapper<ArrayList>() {

									@SuppressWarnings("unchecked")
									@Override
									public ArrayList mapRow(ResultSet rs, int rowNum) throws SQLException {
										ArrayList record = new ArrayList();
										record.add("");
										
										// record.add(rs.getInt(2));//N_CODSXP
										// record.add(rs.getInt(3));//N_CODPLA
										// record.add(rs.getString(4));//C_TIPOPA
										// record.add(rs.getString(5));//C_CODESC
										
										record.add(rs.getInt(22));// N_CODARE 
										record.add(rs.getInt(23));// N_CODACT
										record.add(rs.getInt(24));// N_CODSUB
										
										record.add(rs.getLong(1));// N_CODVXP
										
										record.add(rs.getString(6));// C_DESCRI
										record.add(rs.getString(7));// C_UNIMED
										record.add(rs.getInt(8));// N_METAPR
										record.add(rs.getInt(9));// N_CANM01
										record.add(rs.getInt(10));// N_CANM02
										record.add(rs.getInt(11));// N_CANM03
										record.add(rs.getInt(12));// N_CANM04
										record.add(rs.getInt(13));// N_CANM05
										record.add(rs.getInt(14));// N_CANM06
										record.add(rs.getInt(15));// N_CANM07
										record.add(rs.getInt(16));// N_CANM08
										record.add(rs.getInt(17));// N_CANM09
										record.add(rs.getInt(18));// N_CANM10
										record.add(rs.getInt(19));// N_CANM11
										record.add(rs.getInt(20));// N_CANM12
										
										record.add(rs.getInt(2));// N_CODSXP
//										record.add(rs.getInt(2))

										return record;
									}
								}))
				.withSchemaName(environment.getRequiredProperty(ConstantsCommon.ORACLE_PROCEDURES_SCHEMA));

		MapSqlParameterSource params = new MapSqlParameterSource();
		params.addValue(ConstantsLaboratorio.PAR_CODPLA, parametroPlan.getCodplan());

		Map<String, Object> results = caller.execute(params);
		
		lstRetorno = (List) results.get(ConstantsCommon.PAR_OUT_CURSOR);

		Result result = new Result();
		result.setData(lstRetorno);

		return result;
	}

	@Override
	public ParametroPlan obtenerParametroPlan(Integer codsxp, Integer codplan) {
		ArrayList ret = null;
		ParametroPlan result = null;

		SimpleJdbcCall caller = new SimpleJdbcCall(template.getDataSource());
		caller.withCatalogName(ConstantsLaboratorio.PCK_ALC_PLANOPERATIVO)
				.withProcedureName(ConstantsLaboratorio.PRC_GET_PARAMETROPLAN)
				.declareParameters(
						// parametros de busqueda
						new SqlParameter(ConstantsLaboratorio.PAR_CODSXP, Types.INTEGER),
						new SqlParameter(ConstantsLaboratorio.PAR_CODPLA, Types.INTEGER),
						new SqlOutParameter(Constants.PAR_OUTQUANTITY, Types.INTEGER), new SqlOutParameter(
								ConstantsCommon.PAR_OUT_CURSOR, OracleTypes.CURSOR, new RowMapper<ParametroPlan>() {

									// N_CODVXP, N_CODSXP, N_CODPLA, C_TIPOPA,
									// C_CODESC,
									// C_DESCRI, C_UNIMED, N_METAPR, N_CANM01,
									// N_CANM02,
									// N_CANM03, N_CANM04, N_CANM05, N_CANM06,
									// N_CANM07,
									// N_CANM08, N_CANM09, N_CANM10, N_CANM11,
									// N_CANM12,
									// N_STATUS, N_CODARE, N_CODACT, N_CODSUB

									@Override
									public ParametroPlan mapRow(ResultSet rs, int rowNum) throws SQLException {
										ParametroPlan parametroPlan = new ParametroPlan();

										parametroPlan.setCodvxp(rs.getInt(1));
										parametroPlan.setCodsxp(rs.getInt(2));
										parametroPlan.setCodplan(rs.getInt(3));
										parametroPlan.setTipo(rs.getString(4));
										parametroPlan.setCodDesc(rs.getString(5));

										parametroPlan.setParametro(rs.getString(6));
										parametroPlan.setMedida(rs.getString(7));
										parametroPlan.setPropuesta(rs.getString(8));
										parametroPlan.setMes1(rs.getInt(9));
										parametroPlan.setMes2(rs.getInt(10));

										parametroPlan.setMes3(rs.getInt(11));
										parametroPlan.setMes4(rs.getInt(12));
										parametroPlan.setMes5(rs.getInt(13));
										parametroPlan.setMes6(rs.getInt(14));
										parametroPlan.setMes7(rs.getInt(15));

										parametroPlan.setMes8(rs.getInt(16));
										parametroPlan.setMes9(rs.getInt(17));
										parametroPlan.setMes10(rs.getInt(18));
										parametroPlan.setMes11(rs.getInt(19));
										parametroPlan.setMes12(rs.getInt(20));
										parametroPlan.setEstado(rs.getInt(21));

										// tablaPoissonBean.setCida
										// tablaPoissonBean.setMes12(rs.getInt(20));

										return parametroPlan;
									}
								}))
				.withSchemaName(environment.getRequiredProperty(ConstantsCommon.ORACLE_PROCEDURES_SCHEMA));

		MapSqlParameterSource params = new MapSqlParameterSource();
		params.addValue(ConstantsLaboratorio.PAR_CODSXP, codsxp);
		params.addValue(ConstantsLaboratorio.PAR_CODPLA, codplan);

		Map<String, Object> results = caller.execute(params);
		ret = (ArrayList) results.get(Constants.PAR_OUT_CURSOR);

		if(ret==null || ret.isEmpty()){
			return new ParametroPlan();
		}
		result = (ParametroPlan) ret.get(0);

		return result;
	}
	
	@Override
	public boolean existeParametroPlan(Integer codsxp, Integer codplan) {
		ParametroPlan parametroPlan = obtenerParametroPlan(codsxp, codplan);
		boolean existeParametroPlan = false;
		if(parametroPlan!=null){
				existeParametroPlan = parametroPlan.getCodvxp()!=null;
		}
		
		return existeParametroPlan;
	}
	
	@Override
	public void insertarParametroPlan(ParametroPlan parametroPlan) {

		List<SqlParameter> paramsInput = null;
		List<SqlOutParameter> paramsOutput = null;
		Map<String, Object> inputs = null;
		
		paramsInput = new ArrayList<>();
		paramsInput.add(new SqlParameter(ConstantsLaboratorio.PAR_USUCRE, OracleTypes.VARCHAR));
		paramsInput.add(new SqlParameter(ConstantsLaboratorio.PAR_NOMPRG, OracleTypes.VARCHAR));

		paramsInput.add(new SqlParameter(ConstantsLaboratorio.PAR_CODSXP, OracleTypes.INTEGER));
		paramsInput.add(new SqlParameter(ConstantsLaboratorio.PAR_CODPLA, OracleTypes.INTEGER));
		paramsInput.add(new SqlParameter(ConstantsLaboratorio.PAR_TIPOPA, OracleTypes.VARCHAR));
		paramsInput.add(new SqlParameter(ConstantsLaboratorio.PAR_CODESC, OracleTypes.VARCHAR));
		paramsInput.add(new SqlParameter(ConstantsLaboratorio.PAR_DESCRI, OracleTypes.VARCHAR));
		paramsInput.add(new SqlParameter(ConstantsLaboratorio.PAR_UNIMED, OracleTypes.VARCHAR));
		paramsInput.add(new SqlParameter(ConstantsLaboratorio.PAR_METAPR, OracleTypes.INTEGER));
		paramsInput.add(new SqlParameter(ConstantsLaboratorio.PAR_CANM01, OracleTypes.INTEGER));
		paramsInput.add(new SqlParameter(ConstantsLaboratorio.PAR_CANM02, OracleTypes.INTEGER));
		paramsInput.add(new SqlParameter(ConstantsLaboratorio.PAR_CANM03, OracleTypes.INTEGER));
		paramsInput.add(new SqlParameter(ConstantsLaboratorio.PAR_CANM04, OracleTypes.INTEGER));
		paramsInput.add(new SqlParameter(ConstantsLaboratorio.PAR_CANM05, OracleTypes.INTEGER));
		paramsInput.add(new SqlParameter(ConstantsLaboratorio.PAR_CANM06, OracleTypes.INTEGER));
		paramsInput.add(new SqlParameter(ConstantsLaboratorio.PAR_CANM07, OracleTypes.INTEGER));
		paramsInput.add(new SqlParameter(ConstantsLaboratorio.PAR_CANM08, OracleTypes.INTEGER));
		paramsInput.add(new SqlParameter(ConstantsLaboratorio.PAR_CANM09, OracleTypes.INTEGER));
		paramsInput.add(new SqlParameter(ConstantsLaboratorio.PAR_CANM10, OracleTypes.INTEGER));
		paramsInput.add(new SqlParameter(ConstantsLaboratorio.PAR_CANM11, OracleTypes.INTEGER));
		paramsInput.add(new SqlParameter(ConstantsLaboratorio.PAR_CANM12, OracleTypes.INTEGER));
		paramsInput.add(new SqlParameter(ConstantsLaboratorio.PAR_STATUS, OracleTypes.INTEGER));
		paramsInput.add(new SqlParameter(ConstantsLaboratorio.PAR_CODARE, OracleTypes.INTEGER));
		paramsInput.add(new SqlParameter(ConstantsLaboratorio.PAR_CODACT, OracleTypes.INTEGER));
		paramsInput.add(new SqlParameter(ConstantsLaboratorio.PAR_CODSUB, OracleTypes.INTEGER));

		paramsOutput = new ArrayList<>();

		this.execSp = new ExecuteProcedure(template.getDataSource(), ConstantsLaboratorio.PCK_ALC_PLANOPERATIVO
				+ Constants.P_SEPARADOR + ConstantsLaboratorio.PRC_INSERT_PARAMETROPLAN, paramsInput, paramsOutput);
		inputs = new HashMap<>();

		inputs.put(ConstantsLaboratorio.PAR_USUCRE, parametroPlan.getUsuarioCreacion());
		inputs.put(ConstantsLaboratorio.PAR_NOMPRG, parametroPlan.getPrograma());

		inputs.put(ConstantsLaboratorio.PAR_CODSXP, parametroPlan.getCodsxp());
		inputs.put(ConstantsLaboratorio.PAR_CODPLA, parametroPlan.getCodplan());
		inputs.put(ConstantsLaboratorio.PAR_TIPOPA, parametroPlan.getTipo());
		inputs.put(ConstantsLaboratorio.PAR_CODESC, parametroPlan.getCodDesc());
		inputs.put(ConstantsLaboratorio.PAR_DESCRI, parametroPlan.getParametro());
		inputs.put(ConstantsLaboratorio.PAR_UNIMED, parametroPlan.getMedida());
		inputs.put(ConstantsLaboratorio.PAR_METAPR, parametroPlan.getPropuesta());
		inputs.put(ConstantsLaboratorio.PAR_CANM01, parametroPlan.getMes1());
		inputs.put(ConstantsLaboratorio.PAR_CANM02, parametroPlan.getMes2());
		inputs.put(ConstantsLaboratorio.PAR_CANM03, parametroPlan.getMes3());
		inputs.put(ConstantsLaboratorio.PAR_CANM04, parametroPlan.getMes4());
		inputs.put(ConstantsLaboratorio.PAR_CANM05, parametroPlan.getMes5());
		inputs.put(ConstantsLaboratorio.PAR_CANM06, parametroPlan.getMes6());
		inputs.put(ConstantsLaboratorio.PAR_CANM07, parametroPlan.getMes7());
		inputs.put(ConstantsLaboratorio.PAR_CANM08, parametroPlan.getMes8());
		inputs.put(ConstantsLaboratorio.PAR_CANM09, parametroPlan.getMes9());
		inputs.put(ConstantsLaboratorio.PAR_CANM10, parametroPlan.getMes10());
		inputs.put(ConstantsLaboratorio.PAR_CANM11, parametroPlan.getMes11());
		inputs.put(ConstantsLaboratorio.PAR_CANM12, parametroPlan.getMes12());
		inputs.put(ConstantsLaboratorio.PAR_STATUS, parametroPlan.getEstado());
		inputs.put(ConstantsLaboratorio.PAR_CODARE, parametroPlan.getCodarea());
		inputs.put(ConstantsLaboratorio.PAR_CODACT, parametroPlan.getCodact());
		inputs.put(ConstantsLaboratorio.PAR_CODSUB, parametroPlan.getCodsubact());

		this.execSp.executeSp(inputs);

	}
	
	@Override
	public void actualizarParametroPlan(ParametroPlan parametroPlan) {
		
		List<SqlParameter> paramsInput = null;
		List<SqlOutParameter> paramsOutput = null;
		Map<String, Object> inputs = null;

			paramsInput = new ArrayList<>();
			paramsInput.add(new SqlParameter(ConstantsLaboratorio.PAR_USUMOD, OracleTypes.VARCHAR));
			paramsInput.add(new SqlParameter(ConstantsLaboratorio.PAR_NOMPRG, OracleTypes.VARCHAR));

			paramsInput.add(new SqlParameter(ConstantsLaboratorio.PAR_CODSXP, OracleTypes.INTEGER));
			paramsInput.add(new SqlParameter(ConstantsLaboratorio.PAR_CODPLA, OracleTypes.INTEGER));
			paramsInput.add(new SqlParameter(ConstantsLaboratorio.PAR_TIPOPA, OracleTypes.VARCHAR));
			paramsInput.add(new SqlParameter(ConstantsLaboratorio.PAR_CODESC, OracleTypes.VARCHAR));
			paramsInput.add(new SqlParameter(ConstantsLaboratorio.PAR_DESCRI, OracleTypes.VARCHAR));
			paramsInput.add(new SqlParameter(ConstantsLaboratorio.PAR_UNIMED, OracleTypes.VARCHAR));
			paramsInput.add(new SqlParameter(ConstantsLaboratorio.PAR_METAPR, OracleTypes.INTEGER));
			paramsInput.add(new SqlParameter(ConstantsLaboratorio.PAR_CANM01, OracleTypes.INTEGER));
			paramsInput.add(new SqlParameter(ConstantsLaboratorio.PAR_CANM02, OracleTypes.INTEGER));
			paramsInput.add(new SqlParameter(ConstantsLaboratorio.PAR_CANM03, OracleTypes.INTEGER));
			paramsInput.add(new SqlParameter(ConstantsLaboratorio.PAR_CANM04, OracleTypes.INTEGER));
			paramsInput.add(new SqlParameter(ConstantsLaboratorio.PAR_CANM05, OracleTypes.INTEGER));
			paramsInput.add(new SqlParameter(ConstantsLaboratorio.PAR_CANM06, OracleTypes.INTEGER));
			paramsInput.add(new SqlParameter(ConstantsLaboratorio.PAR_CANM07, OracleTypes.INTEGER));
			paramsInput.add(new SqlParameter(ConstantsLaboratorio.PAR_CANM08, OracleTypes.INTEGER));
			paramsInput.add(new SqlParameter(ConstantsLaboratorio.PAR_CANM09, OracleTypes.INTEGER));
			paramsInput.add(new SqlParameter(ConstantsLaboratorio.PAR_CANM10, OracleTypes.INTEGER));
			paramsInput.add(new SqlParameter(ConstantsLaboratorio.PAR_CANM11, OracleTypes.INTEGER));
			paramsInput.add(new SqlParameter(ConstantsLaboratorio.PAR_CANM12, OracleTypes.INTEGER));
			paramsInput.add(new SqlParameter(ConstantsLaboratorio.PAR_STATUS, OracleTypes.INTEGER));
			paramsInput.add(new SqlParameter(ConstantsLaboratorio.PAR_CODARE, OracleTypes.INTEGER));
			paramsInput.add(new SqlParameter(ConstantsLaboratorio.PAR_CODACT, OracleTypes.INTEGER));
			paramsInput.add(new SqlParameter(ConstantsLaboratorio.PAR_CODSUB, OracleTypes.INTEGER));
			
			paramsOutput = new ArrayList<>();

			this.execSp = new ExecuteProcedure(template.getDataSource(), ConstantsLaboratorio.PCK_ALC_PLANOPERATIVO+Constants.P_SEPARADOR+ConstantsLaboratorio.PRC_UPDATE_PARAMETROPLAN, paramsInput, paramsOutput);
			inputs = new HashMap<>();
			
			inputs.put(ConstantsLaboratorio.PAR_USUMOD, parametroPlan.getUsuarioCreacion());
			inputs.put(ConstantsLaboratorio.PAR_NOMPRG, parametroPlan.getPrograma());

			inputs.put(ConstantsLaboratorio.PAR_CODSXP, parametroPlan.getCodsxp());
			inputs.put(ConstantsLaboratorio.PAR_CODPLA, parametroPlan.getCodplan());
			inputs.put(ConstantsLaboratorio.PAR_TIPOPA, parametroPlan.getTipo());
			inputs.put(ConstantsLaboratorio.PAR_CODESC, parametroPlan.getCodDesc());
			inputs.put(ConstantsLaboratorio.PAR_DESCRI, parametroPlan.getParametro());
			inputs.put(ConstantsLaboratorio.PAR_UNIMED, parametroPlan.getMedida());
			inputs.put(ConstantsLaboratorio.PAR_METAPR, parametroPlan.getPropuesta());
			inputs.put(ConstantsLaboratorio.PAR_CANM01, parametroPlan.getMes1());
			inputs.put(ConstantsLaboratorio.PAR_CANM02, parametroPlan.getMes2());
			inputs.put(ConstantsLaboratorio.PAR_CANM03, parametroPlan.getMes3());
			inputs.put(ConstantsLaboratorio.PAR_CANM04, parametroPlan.getMes4());
			inputs.put(ConstantsLaboratorio.PAR_CANM05, parametroPlan.getMes5());
			inputs.put(ConstantsLaboratorio.PAR_CANM06, parametroPlan.getMes6());
			inputs.put(ConstantsLaboratorio.PAR_CANM07, parametroPlan.getMes7());
			inputs.put(ConstantsLaboratorio.PAR_CANM08, parametroPlan.getMes8());
			inputs.put(ConstantsLaboratorio.PAR_CANM09, parametroPlan.getMes9());
			inputs.put(ConstantsLaboratorio.PAR_CANM10, parametroPlan.getMes10());
			inputs.put(ConstantsLaboratorio.PAR_CANM11, parametroPlan.getMes11());
			inputs.put(ConstantsLaboratorio.PAR_CANM12, parametroPlan.getMes12());
			inputs.put(ConstantsLaboratorio.PAR_STATUS, parametroPlan.getEstado());
			inputs.put(ConstantsLaboratorio.PAR_CODARE, parametroPlan.getCodarea());
			inputs.put(ConstantsLaboratorio.PAR_CODACT, parametroPlan.getCodact());
			inputs.put(ConstantsLaboratorio.PAR_CODSUB, parametroPlan.getCodsubact());
		
			this.execSp.executeSp(inputs);
	}
	
	@Override
	public void inactivarParametroPlan(ParametroPlan parametroPlan) {
		
		List<SqlParameter> paramsInput = null;
		List<SqlOutParameter> paramsOutput = null;
		Map<String, Object> inputs = null;

		paramsInput = new ArrayList<>();
		paramsInput.add(new SqlParameter(ConstantsLaboratorio.PAR_USUMOD, OracleTypes.VARCHAR));
		paramsInput.add(new SqlParameter(ConstantsLaboratorio.PAR_NOMPRG, OracleTypes.VARCHAR));
		paramsInput.add(new SqlParameter(ConstantsLaboratorio.PAR_CODSXP, OracleTypes.INTEGER));
		paramsInput.add(new SqlParameter(ConstantsLaboratorio.PAR_CODPLA, OracleTypes.INTEGER));
		paramsInput.add(new SqlParameter(ConstantsLaboratorio.PAR_STATUS, OracleTypes.INTEGER));
		paramsOutput=new ArrayList<>();

		this.execSp=new ExecuteProcedure(template.getDataSource(),ConstantsLaboratorio.PCK_ALC_PLANOPERATIVO+Constants.P_SEPARADOR+ConstantsLaboratorio.PRC_INACTIVAR_PARAMETROPLAN,paramsInput,paramsOutput);inputs=new HashMap<>();
		inputs.put(ConstantsLaboratorio.PAR_USUMOD, parametroPlan.getUsuarioCreacion());
		inputs.put(ConstantsLaboratorio.PAR_NOMPRG, parametroPlan.getPrograma());
		inputs.put(ConstantsLaboratorio.PAR_CODSXP, parametroPlan.getCodsxp());
		inputs.put(ConstantsLaboratorio.PAR_CODPLA, parametroPlan.getCodplan());
		inputs.put(ConstantsLaboratorio.PAR_STATUS, parametroPlan.getEstado());
		
		this.execSp.executeSp(inputs);
		
	}
	
	@Override
	public void actualizarPlanOperativo(PlanOperativo planOperativo) {
		
		List<SqlParameter> paramsInput = null;
		List<SqlOutParameter> paramsOutput = null;
		Map<String, Object> inputs = null;

			paramsInput = new ArrayList<>();
			paramsInput.add(new SqlParameter(ConstantsLaboratorio.PAR_USUMOD, OracleTypes.VARCHAR));
			paramsInput.add(new SqlParameter(ConstantsLaboratorio.PAR_NOMPRG, OracleTypes.VARCHAR));

			paramsInput.add(new SqlParameter(ConstantsLaboratorio.PAR_CODPLA, OracleTypes.INTEGER));
			paramsInput.add(new SqlParameter(ConstantsLaboratorio.PAR_ESTPLA, OracleTypes.INTEGER));
			paramsInput.add(new SqlParameter(ConstantsLaboratorio.PAR_STATUS, OracleTypes.VARCHAR));
			
			paramsOutput = new ArrayList<>();

			this.execSp = new ExecuteProcedure(template.getDataSource(), ConstantsLaboratorio.PCK_ALC_PLANOPERATIVO+Constants.P_SEPARADOR+ConstantsLaboratorio.PRC_UPDATE_PLANOPERATIVO, paramsInput, paramsOutput);
			inputs = new HashMap<>();
			
			inputs.put(ConstantsLaboratorio.PAR_USUMOD, planOperativo.getUsuarioModificacion());
			inputs.put(ConstantsLaboratorio.PAR_NOMPRG, planOperativo.getPrograma());

			inputs.put(ConstantsLaboratorio.PAR_CODPLA, planOperativo.getCodigoPlan());
			inputs.put(ConstantsLaboratorio.PAR_ESTPLA, planOperativo.getEstadoPlan());
			inputs.put(ConstantsLaboratorio.PAR_STATUS, planOperativo.getActivo());
			
			this.execSp.executeSp(inputs);
	}
	
	@Override
	public PlanOperativo obtenerPlanOperativo(Integer codplan) {
		ArrayList ret = null;
		PlanOperativo result = null;

		SimpleJdbcCall caller = new SimpleJdbcCall(template.getDataSource());
		caller.withCatalogName(ConstantsLaboratorio.PCK_ALC_PLANOPERATIVO)
				.withProcedureName(ConstantsLaboratorio.PRC_GET_PLANOPERATIVO)
				.declareParameters(
						// parametros de busqueda
						new SqlParameter(ConstantsLaboratorio.PAR_CODPLA, Types.INTEGER),
						new SqlOutParameter(Constants.PAR_OUTQUANTITY, Types.INTEGER), new SqlOutParameter(
								ConstantsCommon.PAR_OUT_CURSOR, OracleTypes.CURSOR, new RowMapper<PlanOperativo>() {

									@Override
									public PlanOperativo mapRow(ResultSet rs, int rowNum) throws SQLException {
										
										PlanOperativo planOperativo = new PlanOperativo();
										planOperativo.setCodigoPlan(rs.getInt(1));
										planOperativo.setDescripcion(rs.getString(2));
										planOperativo.setCodigoDescripcionPlan(rs.getString(3));
										planOperativo.setEstadoPlan(rs.getInt(4));
										planOperativo.setActivo(rs.getInt(5));

										return planOperativo;
									}
								}))
				.withSchemaName(environment.getRequiredProperty(ConstantsCommon.ORACLE_PROCEDURES_SCHEMA));

		MapSqlParameterSource params = new MapSqlParameterSource();
		params.addValue(ConstantsLaboratorio.PAR_CODPLA, codplan);

		Map<String, Object> results = caller.execute(params);
		ret = (ArrayList) results.get(Constants.PAR_OUT_CURSOR);

		if(ret==null || ret.isEmpty()){
			return new PlanOperativo();
		}
		result = (PlanOperativo) ret.get(0);

		return result;
	}
	
	
	
	
	/**************************************************************************** 
	 ******************* METODOS PARA BANDJEA DE PLAN OPERATIVO *****************
	 ****************************************************************************/
	@Override
	public Result obtenerDatosPlanOperativo(PlanOperativo planOperativo, Paginacion paginacion) {
		List lstRetorno = null;

		SimpleJdbcCall caller = new SimpleJdbcCall(template.getDataSource());
		caller.withCatalogName(ConstantsLaboratorio.PCK_ALC_PLANOPERATIVO).withProcedureName(ConstantsLaboratorio.PRC_BUSCA_PLANOPERATIVO)
				.declareParameters(
						// parametros de bUsqueda
						// parametros de arquitectura
						new SqlParameter(ConstantsLaboratorio.PAR_CODARE, Types.INTEGER),
						new SqlParameter(Constants.PAR_COLORDERBY, Types.INTEGER),
						new SqlParameter(Constants.PAR_COLORDERBYDIR, Types.VARCHAR),
						new SqlParameter(Constants.PAR_PAGDESDE, Types.INTEGER),
						new SqlParameter(Constants.PAR_CANTIDADPAG, Types.INTEGER),
						new SqlOutParameter(Constants.PAR_OUTQUANTITY, Types.INTEGER), new SqlOutParameter(
								ConstantsCommon.PAR_OUT_CURSOR, OracleTypes.CURSOR, new RowMapper<ArrayList>() {
									@SuppressWarnings("unchecked")
									@Override
									public ArrayList mapRow(ResultSet rs, int rowNum) throws SQLException {
										ArrayList record = new ArrayList();
										record.add("");
										record.add(rs.getInt(1)); // "N_CODPLA"
										record.add(rs.getString(3)); // "C_DESPLA"
										record.add(rs.getString(2)); // "C_CODESP"
										record.add(rs.getInt(4)); // "N_ESTPLA"
										record.add(rs.getInt(5)); // "N_CODARE"
										return record;
									}
								}))
				.withSchemaName(environment.getRequiredProperty(ConstantsCommon.ORACLE_PROCEDURES_SCHEMA));

		MapSqlParameterSource params = new MapSqlParameterSource();
		Integer codare = planOperativo.getCodare();
		if(codare==null){
			codare = -1;
		}
		params.addValue(ConstantsLaboratorio.PAR_CODARE, codare);
		params.addValue(Constants.PAR_COLORDERBY, paginacion.getColorderby());
		params.addValue(Constants.PAR_COLORDERBYDIR, paginacion.getColorderbydir());
		params.addValue(Constants.PAR_PAGDESDE, paginacion.getPagdesde());
		params.addValue(Constants.PAR_CANTIDADPAG, paginacion.getCantidadpag());

		Map<String, Object> results = caller.execute(params);
		int quantity = (int) results.get(Constants.PAR_OUTQUANTITY);
		lstRetorno = (List) results.get(ConstantsCommon.PAR_OUT_CURSOR);

		Result result = new Result();
		result.setData(lstRetorno);
		result.setRecords((long) quantity);

		return result;
	}
	
	@Override
	public int grabarPlanOperativo(PlanOperativo planOperativo) {
		
		List<SqlParameter> lstParamsInput = null;
		List<SqlOutParameter> lstParamsOutput = null;
		Map<String, Object> inputs = null;

			lstParamsInput = new ArrayList<SqlParameter>();
			lstParamsInput.add(new SqlParameter(ConstantsLaboratorio.PAR_CODARE, OracleTypes.INTEGER));
			lstParamsInput.add(new SqlParameter(ConstantsLaboratorio.PAR_USUCRE, OracleTypes.VARCHAR));	
			lstParamsInput.add(new SqlParameter(ConstantsLaboratorio.PAR_NOMPRG, OracleTypes.VARCHAR));		
			lstParamsInput.add(new SqlParameter(ConstantsLaboratorio.PAR_DESPLA, OracleTypes.VARCHAR));
			lstParamsInput.add(new SqlParameter(ConstantsLaboratorio.PAR_CODESP, OracleTypes.VARCHAR));
		
			lstParamsOutput = new ArrayList<SqlOutParameter>();			
//			lstParamsOutput.add(new SqlOutParameter(Constants.PAR_OUT_RETURN, OracleTypes.INTEGER));
			
			this.execSp = new ExecuteProcedure(template.getDataSource(), 
					CoreUtils.concatenar(ConstantsLaboratorio.PCK_ALC_PLANOPERATIVO,ConstantsCommon.P_SEPARADOR,ConstantsLaboratorio.PRC_INSERT_PLANOPERATIVO), lstParamsInput, lstParamsOutput);
			inputs = new HashMap<String, Object>();
			inputs.put(ConstantsLaboratorio.PAR_CODARE, planOperativo.getCodare());
			inputs.put(ConstantsLaboratorio.PAR_USUCRE, planOperativo.getUsuarioCreacion());		
			inputs.put(ConstantsLaboratorio.PAR_NOMPRG, planOperativo.getPrograma());		
			inputs.put(ConstantsLaboratorio.PAR_DESPLA, planOperativo.getDescripcion());	
			inputs.put(ConstantsLaboratorio.PAR_CODESP, planOperativo.getCodigoDescripcionPlan());
		
			Map<String, Integer> outputs = this.execSp.executeSp(inputs);
			return 0;
	}
	

	public List<ParametroReporte> obtenerReportePlanOperativo(Integer codplan,Integer idGrupo, String mes){
		List<ParametroReporte> ret = null;

		SimpleJdbcCall caller = new SimpleJdbcCall(template.getDataSource());
		caller.withCatalogName(ConstantsLaboratorio.PCK_ALC_PLANOPERATIVO)
				.withProcedureName(ConstantsLaboratorio.PRC_BUSCA_REPORTE_PLAN)
				.declareParameters(
						// parametros de busqueda
						new SqlParameter(ConstantsLaboratorio.PAR_CODPLA, Types.INTEGER),
						new SqlParameter(ConstantsLaboratorio.PAR_MES, Types.VARCHAR),
						new SqlOutParameter(Constants.PAR_OUTQUANTITY, Types.INTEGER), new SqlOutParameter(
								ConstantsCommon.PAR_OUT_CURSOR, OracleTypes.CURSOR, new RowMapper<ParametroReporte>() {

									@Override
									public ParametroReporte mapRow(ResultSet rs, int rowNum) throws SQLException {
										ParametroReporte parametroReporte = new ParametroReporte(); 
										//ACTIVIDAD 8	39	RIMAC CUENCA  ALTA	40	Coliformes totales	45	32
										parametroReporte.setActividad(rs.getString(1));
										parametroReporte.setCodact(rs.getInt(2));
										parametroReporte.setSubactividad(rs.getString(3));
										parametroReporte.setCodsubact(rs.getInt(4));
										parametroReporte.setParametro(rs.getString(5));
										parametroReporte.setCodparam(rs.getInt(6));
										parametroReporte.setProgramado(rs.getInt(7));
										parametroReporte.setEjecutado(rs.getInt(8));
										parametroReporte.setCodpar(rs.getInt(9));
										
										return parametroReporte;
									}
								}))
				.withSchemaName(environment.getRequiredProperty(ConstantsCommon.ORACLE_PROCEDURES_SCHEMA));
		MapSqlParameterSource params = new MapSqlParameterSource();
		params.addValue(ConstantsLaboratorio.PAR_CODPLA, codplan);
		params.addValue(ConstantsLaboratorio.PAR_MES, mes);
		Map<String, Object> results = caller.execute(params);
		ret = (List<ParametroReporte>) results.get(Constants.PAR_OUT_CURSOR);
		return ret;
	}
	
	public void guardarParametroReporte(ParametroReporte parametroReporte , Integer codplan, String mes){
		
		List<SqlParameter> paramsInput = null;
		List<SqlOutParameter> paramsOutput = null;
		Map<String, Object> inputs = null;

			paramsInput = new ArrayList<>();
			paramsInput.add(new SqlParameter(ConstantsLaboratorio.PAR_USUMOD, OracleTypes.VARCHAR));
			paramsInput.add(new SqlParameter(ConstantsLaboratorio.PAR_NOMPRG, OracleTypes.VARCHAR));

			paramsInput.add(new SqlParameter(ConstantsLaboratorio.PAR_MES, OracleTypes.VARCHAR));
			paramsInput.add(new SqlParameter(ConstantsLaboratorio.PAR_CODPLA, OracleTypes.INTEGER));
			paramsInput.add(new SqlParameter(ConstantsLaboratorio.PAR_CODPAR, OracleTypes.INTEGER));
			paramsInput.add(new SqlParameter(ConstantsLaboratorio.PAR_TOTEJE, OracleTypes.INTEGER));
			
//		   --parametros
			paramsOutput = new ArrayList<>();

			this.execSp = new ExecuteProcedure(template.getDataSource(), ConstantsLaboratorio.PCK_ALC_PLANOPERATIVO + Constants.P_SEPARADOR+ConstantsLaboratorio.PRC_UPDATE_REPORTEPLAN, paramsInput, paramsOutput);
			inputs = new HashMap<>();
			
			inputs.put(ConstantsLaboratorio.PAR_USUMOD, parametroReporte.getUsuarioModificacion());
			inputs.put(ConstantsLaboratorio.PAR_NOMPRG, parametroReporte.getPrograma());

			inputs.put(ConstantsLaboratorio.PAR_MES, mes);
			inputs.put(ConstantsLaboratorio.PAR_CODPLA, codplan);
			inputs.put(ConstantsLaboratorio.PAR_CODPAR, parametroReporte.getCodparam());
			inputs.put(ConstantsLaboratorio.PAR_TOTEJE, parametroReporte.getEjecutado());
			
			System.out.println("PAR_MES " + mes);
			System.out.println("PAR_CODPLA " + codplan);
			System.out.println("PAR_TOTEJE " + parametroReporte.getEjecutado());
			System.out.println("PAR_TOTEJE " + parametroReporte.getEjecutado());
			
			this.execSp.executeSp(inputs);
	}
	
	@Override
	public TotalesForm20 obtenerTotalesForm20(String mes, Integer tipoResult) {
		ArrayList ret = null;
		TotalesForm20 result = null;

		SimpleJdbcCall caller = new SimpleJdbcCall(template.getDataSource());
		caller.withCatalogName(ConstantsLaboratorio.PCK_ALC_PLANOPERATIVO)
				.withProcedureName(ConstantsLaboratorio.PRC_BUSCA_TOTALES_FORM20)
				.declareParameters(
						// parametros de busqueda
						new SqlParameter(ConstantsLaboratorio.PAR_MES, Types.VARCHAR),
						new SqlParameter(ConstantsLaboratorio.PAR_TIPRES, Types.INTEGER),
						new SqlOutParameter(Constants.PAR_OUTQUANTITY, Types.INTEGER), new SqlOutParameter(
								ConstantsCommon.PAR_OUT_CURSOR, OracleTypes.CURSOR, new RowMapper<TotalesForm20>() {

									@Override
									public TotalesForm20 mapRow(ResultSet rs, int rowNum) throws SQLException {
										
										TotalesForm20 totalesForm20 = new TotalesForm20();
										
										totalesForm20.setTipo(rs.getInt(1));
										totalesForm20.setColiTotales(rs.getInt(2));
										totalesForm20.setColiTermoTolerantes(rs.getInt(3));
										totalesForm20.setHeterotroficas(rs.getInt(4));
										
										return totalesForm20;
									}
								}))
				.withSchemaName(environment.getRequiredProperty(ConstantsCommon.ORACLE_PROCEDURES_SCHEMA));

		MapSqlParameterSource params = new MapSqlParameterSource();
		params.addValue(ConstantsLaboratorio.PAR_MES, mes);
		params.addValue(ConstantsLaboratorio.PAR_TIPRES, tipoResult);

		Map<String, Object> results = caller.execute(params);
		ret = (ArrayList) results.get(Constants.PAR_OUT_CURSOR);

		if(ret==null || ret.isEmpty()){
			return new TotalesForm20();
		}
		result = (TotalesForm20) ret.get(0);

		return result;
	}
	

}
