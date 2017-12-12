package com.sist.controller;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.sist.model.Model;

import java.util.*; //Map(요청=> 클래스(모델)) 매칭해주기 위해 


public class DispatcherServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	//클래스주소값
	private String[] strCls={
		"com.sist.model.MovieList",
		"com.sist.model.MovieDetail"
	};
	
	//요청키워드
	private String[] strCmd= {
			"list","detail"
	};
	//스프링에서는  <bean id="list" class="com.sist.model.MovieList"/> class.forname으로 메모리할당을 하고 들어온다.
	//csv => list,com.sist.model.MovieList csv파일은 콤마로 구분한다.
	//Class.forName() : 이름만으로 메모리할당을 한다.
	// 클래스의 이름을 읽어서 메모리 할당을 하는것 =========> replaction(리플렉션)
	
	/*
	 * key 			value
	 * list 		new MovieList() or Class.forName()
	 * detail		new MovieDetail()
	 */
	
	//Map은 인터페이스고 이것을 구현하고 있는 것이 HashMap
	private Map clsMap=new HashMap();

	
	public void init(ServletConfig config) throws ServletException {
		//config에서 web.xml읽어올수 있다.
		//web.xml에는 클래스에 등록된 파일이 있다.
		//따라서 init메소드에서는 web.xml을 읽어옴으로써 클래스에 등록된 파일을 읽어오는것.
		//init메소드는 생성자 같은 역할로 환경설정을 하는 부분이다.
		//init메소드는 처음 실행할때 한번만 돌아간다.
		//두번째 호출에는 serviece만 호출하는 것이다.
		
		try
		{
			/*
			clsMap.put("list",new MovieList());
			clsMap.put("detail", new MovieDetail());
			이렇게 써도 되지만 양이 많아지면 곤란 따라서 for문 돌리는 것
			*/
			
			for(int i=0; i<strCls.length; i++)
			{
				Class clsName=Class.forName(strCls[i]);  //클래스 이름으로 메모리할당
				Object obj=clsName.newInstance();		//객체 생성   이 두줄이 new MovieList()와 같다.
				System.out.println(obj);
				clsMap.put(strCmd[i], obj);				//키,주소값을 해쉬맵에 넣어준다
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
			String cmd=request.getRequestURI();    //URI: 사용자가 주소입력란에 요청한 파일
			//http://localhost:8080/MVCProjcect1/list.do
			// URI :  /MVCProjcect1/list.do
			cmd=cmd.substring(request.getContextPath().length()+1,cmd.lastIndexOf(".") ); //사용자가 요청한 내용 : list
			
			//요청을 처리=> 모델클래스(클래스,메소드)
			Model model=(Model)clsMap.get(cmd);
			
			//model=> 실행을 한 후에 결과를 request에 담아달라
			//Call by Reference => 주소를 넘겨주고 주소값을 채운다.
			String jsp=model.execute(request);
			
			//jsp에 request,session값을 전송
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
