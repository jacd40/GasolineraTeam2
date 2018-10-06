/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.umg.dao;

import com.umg.dto.usuarioDTO;
import java.sql.SQLException;
import java.util.List;
import javax.naming.NamingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author JACD
 */
public class usuarioDAO extends DataAccessBase<usuarioDTO> {
    private static Logger logger = LoggerFactory.getLogger(usuarioDAO.class);
    private final String SQL_EXIST_USER="select count(1) from usuario where usuario=? and password=?"; 
    private final String SQL_GET_USUARIO="select usuario, password from usuario where usuario=?";
    public boolean checkUniqueUsername(String username, String password) throws SQLException, NamingException {
		logger.debug(sql2string(SQL_EXIST_USER, username));
		return getQueryRunner().query(SQL_EXIST_USER, getBigDecimalHandler(), password).intValue() == 0;
	}
    
    public usuarioDTO getUsuario(String username) throws Exception{
        usuarioDTO usuario = new usuarioDTO();
        List<usuarioDTO> usuarios= getQueryRunner().query(getConnection(),SQL_GET_USUARIO,new Object[]{username}, getBeanListHandler());
        usuario=usuarios.get(0);
        return usuario;
    }
}
