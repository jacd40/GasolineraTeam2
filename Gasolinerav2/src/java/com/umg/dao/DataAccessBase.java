package com.umg.dao;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Properties;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.RowProcessor;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;

public class DataAccessBase<T> {

	// *********************
	// ***** Data part *****
	// *********************

	protected static final String RESOURCE_FTRE = "java:comp/env/jdbc/sclgtepre";

	private String defaultDataSourceName;
	private DataSource defaultDataSource;

	private Class<T> type;
	private QueryRunner queryRunner;
	private BeanHandler<T> beanHandler;
	private BeanListHandler<T> beanListHandler;
	private ScalarHandler<BigDecimal> bigDecimalHandler;

	@SuppressWarnings("unchecked")
	private Class<T> getType() {
		if (type == null) {
			Type superclass = this.getClass().getGenericSuperclass();
			if (superclass instanceof ParameterizedType) {
				type = (Class<T>) ((ParameterizedType) superclass).getActualTypeArguments()[0];
			}
		}
		return type;
	}

	protected void setConvert(RowProcessor convert) {
		this.beanHandler = new BeanHandler<>(getType(), convert);
		this.beanListHandler = new BeanListHandler<>(getType(), convert);
	}

	/**
	 * <p>
	 * Application's default data source name.
	 * </p>
	 */
	protected String getDefaultDataSourceName() {
		return defaultDataSourceName;
	}

	protected void setDefaultDataSourceName(String defaultDataSourceName) {
		this.defaultDataSourceName = defaultDataSourceName;
		this.defaultDataSource = null;
		this.queryRunner = null;
	}

	/**
	 * <p>
	 * Gets datasource from JNDI.
	 * </p>
	 * 
	 * @param dataSourceName
	 * @return
	 * @throws NamingException
	 */
	public DataSource getDataSource(String dataSourceName) throws NamingException {
		InitialContext ctx = new InitialContext();
		return (DataSource) ctx.lookup(dataSourceName);
	}

	/**
	 * <p>
	 * Gets the default data source.
	 * </p>
	 * 
	 * @return
	 * @throws NamingException
	 */
	public DataSource getDefaultDataSource() throws NamingException {
		if (defaultDataSource == null) {
			defaultDataSource = getDataSource(defaultDataSourceName);
		}
		return defaultDataSource;
	}

	/**
	 * <p>
	 * Gets connection from data source.
	 * </p>
	 * 
	 * @param dataSourceName
	 * @return
	 * @throws NamingException
	 * @throws SQLException
	 */
	public Connection getConnection(String dataSourceName) throws SQLException, NamingException {
		return getDataSource(dataSourceName).getConnection();
	}

	/**
	 * <p>
	 * Gets connection from default data source.
	 * </p>
	 * 
	 * @return
	 * @throws NamingException
	 * @throws SQLException
	 */
	public Connection getConnection() throws SQLException, NamingException, ClassNotFoundException {
            Connection con =null;
            Class.forName("com.mysql.jdbc.Driver");
            System.out.println("actualizo");
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/gasolineradb?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC", "owner", "1qaz2wsx3edc");
            
            return con;
            
	}

	/**
	 * <p>
	 * Gets the default query runner object using default data source.
	 * </p>
	 * 
	 * @return
	 * @throws NamingException
	 */
	public QueryRunner getQueryRunner() throws NamingException {
		if (queryRunner == null) {
			queryRunner = new QueryRunner();
		}
		return queryRunner;
	}

	/**
	 * <p>
	 * Gets the default bean handler.
	 * </p>
	 * 
	 * @return
	 */
	public BeanHandler<T> getBeanHandler() {
		if (beanHandler == null) {
			beanHandler = new BeanHandler<>(getType());
		}
		return beanHandler;
	}

	/**
	 * <p>
	 * Gets the default bean list handler.
	 * </p>
	 * 
	 * @return
	 */
	public BeanListHandler<T> getBeanListHandler() {
		if (beanListHandler == null) {
			beanListHandler = new BeanListHandler<>(getType());
		}
		return beanListHandler;
	}

	/**
	 * <p>
	 * Gets the default big decimal handler.
	 * </p>
	 * 
	 * @return
	 */
	public ScalarHandler<BigDecimal> getBigDecimalHandler() {
		if (bigDecimalHandler == null) {
			bigDecimalHandler = new ScalarHandler<>();
		}
		return bigDecimalHandler;
	}

	// ******************
	// ***** Utility ****
	// ******************

	public String sql2string(String sql, Object... params) {
		StringBuilder msg = new StringBuilder();

		msg.append(" Query: ");
		msg.append(sql);
		msg.append(" Parameters: ");

		if (params == null) {
			msg.append("[]");
		} else {
			msg.append(Arrays.deepToString(params));
		}

		return msg.toString();
	}

	public String sql2string(String sql, Object[][] params) {
		StringBuilder msg = new StringBuilder();

		msg.append(" Query: ");
		msg.append(sql);
		msg.append(" Parameters: ");

		for (Object[] objects : params) {
			if (objects == null) {
				msg.append("[]");
			} else {
				msg.append(Arrays.deepToString(objects));
			}
		}

		return msg.toString();
	}
}
