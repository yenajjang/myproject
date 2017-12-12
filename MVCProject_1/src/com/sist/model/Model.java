package com.sist.model;

import javax.servlet.http.HttpServletRequest;


/*
 * 1.8�̻� : default�޼ҵ�, static �޼ҵ�=> ������ �޼ҵ带 ���� �� �ִ�.
 * 
 * ���
 * class / interface
 * 
 * 		extends
 * class  ====> class
 * 
 *		  extends 
 * interface ====> interface
 * 
 * 			implements
 * interface =======> class
 * 			
 * 			(�Ұ���)
 * class ========> interface
 */

public interface Model {
	
	public String execute(HttpServletRequest req) throws Exception;

}
