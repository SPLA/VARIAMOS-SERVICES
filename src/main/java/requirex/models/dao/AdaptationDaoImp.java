package requirex.models.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import db.Pool;
import requirex.models.entitys.Adaptation;

public class AdaptationDaoImp implements IAdaptationDao {
	
	Pool pool = null;

	@Override
	public List<Adaptation> findAll() {
		List<Adaptation> list = new ArrayList<Adaptation>();
		String query = "SELECT * FROM adaptation";
		Statement st = null;
		
		try {
			pool = new Pool();
			Connection conn = pool.dataSource.getConnection();
			if (conn != null) {

				st = conn.createStatement();

				ResultSet rs = st.executeQuery(query);
				
				while (rs.next()) {
					
					Adaptation adaptation = setAdaptation(rs);
					
					list.add(adaptation);
				}
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			 try {
				st.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return list;
	}

	@Override
	public List<Adaptation> findAllByEstado(boolean estado) {
		List<Adaptation> list = new ArrayList<Adaptation>();
		String query = "SELECT * FROM application Where estado = ?";
		PreparedStatement prepStatement = null;
		
		try {
			pool = new Pool();
			Connection conn = pool.dataSource.getConnection();
			if (conn != null) {

				prepStatement = conn.prepareStatement(query);
				prepStatement.setBoolean(1, estado);

				ResultSet rs = prepStatement.executeQuery(query);
				
				while (rs.next()) {
					
					Adaptation app = setAdaptation(rs);
					
					list.add(app);
				}
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			if(prepStatement != null) {
				try {
					prepStatement.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}

		return list;
	}

	@Override
	public Adaptation findById(int id) {
		String query = "SELECT * FROM adaptation Where id = ?";
		PreparedStatement prepStatement = null;
		Adaptation adaptation = null;
		
		try {
			pool = new Pool();
			Connection conn = pool.dataSource.getConnection();
			if (conn != null) {

				prepStatement = conn.prepareStatement(query);
				prepStatement.setInt(1, id);

				ResultSet rs = prepStatement.executeQuery(query);
				
				while (rs.next()) {
					
					adaptation = setAdaptation(rs);
					
				}
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			if(prepStatement != null) {
				try {
					prepStatement.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}

		return adaptation;
	}
	

	private Adaptation setAdaptation(ResultSet rs) {
		Adaptation app = new Adaptation();
		
		try {
			app.setId(rs.getInt("id"));
			app.setRequirementNumber(rs.getString("requirementNumber"));
			app.setReqType(rs.getString("reqType"));
			app.setName(rs.getString("name"));
			app.setCondition(rs.getBoolean("condition"));
			app.setConditionDescription(rs.getString("conditionDescription"));
			app.setImperative(rs.getString("imperative"));
			app.setSystemName(rs.getString("systemName"));
			
			app.setProcessVerb(rs.getString("processVerb"));
			app.setObject(rs.getString("object"));
			app.setSystem(rs.getString("system"));
			app.setMsg(rs.getString("msg"));
			app.setEstado(rs.getBoolean("estado"));
			app.setFechaRegistro(rs.getDate("fecha_registro"));
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		return app;
	}



}
