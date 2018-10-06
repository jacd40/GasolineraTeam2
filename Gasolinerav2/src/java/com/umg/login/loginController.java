/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.umg.login;

import com.umg.dao.usuarioDAO;
import com.umg.dto.usuarioDTO;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Button;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

public class loginController extends SelectorComposer<Window>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 5175750085136634621L;
	private static Logger logger = LoggerFactory.getLogger(loginController.class);
	usuarioDAO dao = new usuarioDAO();
	/*WIRE*/
	/*****************************/
	@Wire
	private Window wdwLogin;
	@Wire
	private Textbox txtUser,txtKey;
	@Wire
	private Button btnLogin;
	/*****************************/
	
	/*VARS*/
	/*****************************/
	DateFormat dateFormat = new SimpleDateFormat("dd/MM/yy");
	int index = 0;

	/*GETTERS AND SETTERS*/
	/*****************************/
	public Window getWdwLogin() {
		return wdwLogin;
	}

	public void setWdwLogin(Window wdwLogin) {
		this.wdwLogin = wdwLogin;
	}
	/*****************************/

	/*COMPOSER*/
	/*****************************/
	public void doAfterCompose(Window comp) throws Exception {
		super.doAfterCompose(comp);
		txtUser.setFocus(true);
	}
	/*****************************/

	/*BUTTONS*/
	/*****************************/
	@Listen("onOK = #txtKey, #txtUser")
	public void onEnterPressed() {
		onClick$btnLogin();
	}
	
	@Listen ("onClick = #btnLogin")
	public void onClick$btnLogin(){
		String user = txtUser.getValue();
		String pass = txtKey.getValue();
		usuarioDTO usuario = new usuarioDTO();
                if(user!=null && pass!=null){
                    try{
                    usuario=dao.getUsuario(user);
                    if(usuario.getPassword().equals(pass)){
                        Executions.sendRedirect("/dashboard/dashboard.zul");
                    }else{
                        notifyError("password incorrecto, intente de nuevo");
                    }
                    }catch(Exception e){
                     logger.error("ocurrio un error al validar el usuario");
                     e.printStackTrace();
                    }
                }
	}
        
        private void notifyError(String message) {
		message = message.replace("'", "\'");
                Clients.showNotification(message,
            Clients.NOTIFICATION_TYPE_INFO, null, "top_center", 4100);
		/*Clients.evalJavaScript("$.notify({"
				// options
				+ "icon: 'fa fa-warning'," + "message: '" + message + "' " + "},{"
				// settings
				+ "type: 'danger'," + "mouse_over: 'pause'" + "})");*/
	}
	/*****************************/

	/*METHODS*/
	/*****************************/

	/*****************************/	
}

