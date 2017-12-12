package com.sist.controller;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.sist.model.Model;

import java.util.*; //Map(��û=> Ŭ����(��)) ��Ī���ֱ� ���� 


public class DispatcherServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	//Ŭ�����ּҰ�
	private String[] strCls={
		"com.sist.model.MovieList",
		"com.sist.model.MovieDetail"
	};
	
	//��ûŰ����
	private String[] strCmd= {
			"list","detail"
	};
	//������������  <bean id="list" class="com.sist.model.MovieList"/> class.forname���� �޸��Ҵ��� �ϰ� ���´�.
	//csv => list,com.sist.model.MovieList csv������ �޸��� �����Ѵ�.
	//Class.forName() : �̸������� �޸��Ҵ��� �Ѵ�.
	// Ŭ������ �̸��� �о �޸� �Ҵ��� �ϴ°� =========> replaction(���÷���)
	
	/*
	 * key 			value
	 * list 		new MovieList() or Class.forName()
	 * detail		new MovieDetail()
	 */
	
	//Map�� �������̽��� �̰��� �����ϰ� �ִ� ���� HashMap
	private Map clsMap=new HashMap();

	
	public void init(ServletConfig config) throws ServletException {
		//config���� web.xml�о�ü� �ִ�.
		//web.xml���� Ŭ������ ��ϵ� ������ �ִ�.
		//���� init�޼ҵ忡���� web.xml�� �о�����ν� Ŭ������ ��ϵ� ������ �о���°�.
		//init�޼ҵ�� ������ ���� ���ҷ� ȯ�漳���� �ϴ� �κ��̴�.
		//init�޼ҵ�� ó�� �����Ҷ� �ѹ��� ���ư���.
		//�ι�° ȣ�⿡�� serviece�� ȣ���ϴ� ���̴�.
		
		try
		{
			/*
			clsMap.put("list",new MovieList());
			clsMap.put("detail", new MovieDetail());
			�̷��� �ᵵ ������ ���� �������� ��� ���� for�� ������ ��
			*/
			
			for(int i=0; i<strCls.length; i++)
			{
				Class clsName=Class.forName(strCls[i]);  //Ŭ���� �̸����� �޸��Ҵ�
				Object obj=clsName.newInstance();		//��ü ����   �� ������ new MovieList()�� ����.
				System.out.println(obj);
				clsMap.put(strCmd[i], obj);				//Ű,�ּҰ��� �ؽ��ʿ� �־��ش�
														//**** singleton
				
			}
		}catch(Exception ex)
		{
			System.out.println(ex.getMessage());
		}
	}

	
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			//list.do   or   movie.do?cmd=list
			String cmd=request.getRequestURI();    //URI: ����ڰ� �ּ��Է¶��� ��û�� ����
			//http://localhost:8080/MVCProjcect1/list.do
			// URI :  /MVCProjcect1/list.do
			cmd=cmd.substring(request.getContextPath().length()+1,cmd.lastIndexOf(".") ); //����ڰ� ��û�� ���� : list
			
			//��û�� ó��=> ��Ŭ����(Ŭ����,�޼ҵ�)
			Model model=(Model)clsMap.get(cmd);
			
			//model=> ������ �� �Ŀ� ����� request�� ��ƴ޶�
			//Call by Reference => �ּҸ� �Ѱ��ְ� �ּҰ��� ä���.
			String jsp=model.execute(request);
			
			//jsp�� request,session���� ����
			RequestDispatcher rd=request.getRequestDispatcher(jsp);
			rd.forward(request, response);
			
			/*
			 *  serviece(request,response)
			 *  {
			 *  	_jspServiece(request);
			 *  }
			 */
			
			
		}catch(Exception ex)
		{
			System.out.println(ex.getMessage());
		}
	}

}
