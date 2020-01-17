package requirex.models.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import db.ConnectionManager;
import requirex.models.entitys.Domain;

public class DomainDaoImp implements IDomainDao {


	@Override
	public List<Domain> findAll() {
		
		List<Domain> list = new ArrayList<>();
		Statement stmt = null;
		ResultSet rs = null;
		Connection conn = null;
		
		String sql = "Select * From domain";
		
		try{
			conn = ConnectionManager.getConnection();
			stmt = conn.createStatement();
			rs = stmt.executeQuery(sql);
			
			while (rs.next()) {
				Domain domain = setDomain(rs);
				list.add(domain);
			}
			
			stmt.close();
			
		}catch (SQLException e) {
			try {
				conn.close();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		}
		
		
		return list;
	}

	@Override
	public List<Domain> findByEstado(boolean estado) {
		List<Domain> list = new ArrayList<>();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		Connection conn = null;
		
		String sql = "Select * From domain Where estado = ?";
		
		try{
			conn = ConnectionManager.getConnection();
			stmt = conn.prepareStatement(sql);
			stmt.setBoolean(0, estado);
			rs = stmt.executeQuery(sql);
			
			while (rs.next()) {
				Domain domain = setDomain(rs);
				list.add(domain);
			}
			
			stmt.close();
			
		}catch (SQLException e) {
			try {
				conn.close();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		}
		
		return list;
	}

	@Override
	public Domain findById(int id) {
		Domain domain = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		Connection conn = null;
		
		String sql = "Select * From domain Where estado = ?";
		
		try{
			conn = ConnectionManager.getConnection();
			stmt = conn.prepareStatement(sql);
			stmt.setInt(0, id);
			rs = stmt.executeQuery(sql);
			
			while (rs.next()) {
				domain = setDomain(rs);
			}
			
			stmt.close();
			
		}catch (SQLException e) {
			try {
				conn.close();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		}
		
		return domain;
	}
	
	private Domain setDomain(ResultSet rs) {
		Domain domain = new Domain();
		
		try {
			domain.setId(rs.getInt("id"));
			domain.setRequirementNumber(rs.getString("requirementNumber"));
		    domain.setAffectedSystems(rs.getString("affectedSystems"));
		    domain.setThoseCodition(rs.getString("thoseCodition"));
		    domain.setReqType(rs.getString("reqType"));
		    domain.setName(rs.getString("name"));
		    domain.setCondition(rs.getBoolean("condition"));
		    domain.setConditionDescription(rs.getString("conditionDescription"));
		    domain.setImperative(rs.getString("imperative"));
		    domain.setSystemName(rs.getString("name"));
		    domain.setSystemActivity(rs.getString("systemActivity"));
		    domain.setUser(rs.getString("user"));
		    domain.setProcessVerb(rs.getString("processVerb"));
		    domain.setObject(rs.getString("object"));
		    domain.setSystem(rs.getString("system"));
		    domain.setFrom(rs.getString("from"));
		    domain.setSystemCondition(rs.getBoolean("systemCondition"));
		    domain.setSystemConditionDescription(rs.getString("systemConditionDescription"));
		    domain.setMsg(rs.getString("msg"));
		    domain.setEstado(rs.getBoolean("estado"));
		    domain.setFecha_registro(rs.getDate("fecha_registro"));
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return domain;
		
	}

}
