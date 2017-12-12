package com.sist.manager;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.sist.dao.MovieDAO;
import com.sist.dao.MovieVO;

public class MovieManager {

	public static void main(String[] args) {
		MovieManager m = new MovieManager();
		//m.movieLinkData();
		m.movieDetailData();
		System.out.println("저장완료!!!");
		

	}
	public List<String> movieLinkData()
	{
		//1페이지 ==> http://movie.naver.com/movie/sdb/rank/rmovie.nhn?sel=pnt&date=20171205
		//2페이지 ==> http://movie.naver.com/movie/sdb/rank/rmovie.nhn?sel=pnt&date=20171206&page=2
		//영화마다 고유의 code번호가 있으므로 code 번호를 모두 긁는다.
		
		 
//		<td class="title">
//		<div class="tit5">
//			<a href="/movie/bi/mi/basic.nhn?code=17421" title="쇼생크 탈출">쇼생크 탈출</a>
//		</div>
//	</td>

		
		List<String> list = new ArrayList<String>();
		try
		{
			Date date=new Date();
			SimpleDateFormat sdf=new SimpleDateFormat("yyyyMMdd");
			
			int k=1;
			for(int i=1; i<=40; i++)
			{
				Document doc=Jsoup.connect("http://movie.naver.com/movie/sdb/rank/rmovie.nhn?sel=pnt&date="+sdf.format(date)+"&page="+i).get();
				Elements elem=doc.select("td.title div.tit5 a");
				for(int j=0; j<elem.size(); j++)
				{
					Element code=elem.get(j);
					//System.out.println(k+":"+code.attr("href"));
					list.add("http://movie.naver.com"+code.attr("href"));
					k++;
				}
			}
			
			
		}catch(Exception ex)
		{
			System.out.println(ex.getMessage());
		}
		return list;		
	}
	
	
	public List<MovieVO> movieDetailData(){
	      List<MovieVO> list = new ArrayList<MovieVO>();
	      try {
	    	  
	    	  //같은 클래스 안에 MovieLinkData(),movieDetailData()가 있으므로 서로 호출할 수 있음
	         List<String> linkList=movieLinkData();
	         MovieDAO dao=new MovieDAO();
	         
	         for(int i=0; i<linkList.size(); i++)            
	         {
	        	 try {
	            String link=linkList.get(i);
	            Document doc=Jsoup.connect(link).get();
	            
	            //select 함수는 return형이 Elements로 여러개의 값을 가져와야 한다.
	            //하지만 우리가 받을 변수는 Element 따라서 first()로 하나만 받아오거나 get(0)을 써줘야 한다. 꼭
	            Element title=doc.select("div.mv_info h3.h_movie a").first();
	            Element director=doc.select("div.info_spec2 dl.step1 dd a").first();
	            Element actor=doc.select("div.info_spec2 dl.step2 dd a").first();
	            
	            //genre, grade, time, regdate
	            Elements temp=doc.select("p.info_spec span");
	            Element genre=temp.get(0); 
	            Element time=temp.get(2);
	            Element regdate=temp.get(3);
	            Element grade=temp.get(4);
	            
	            Element poster=doc.select("div.poster a img").first();
	            Element story=doc.selectFirst("div.story_area p.con_tx");
	            
	            //System.out.println(story.text());
	            System.out.println((i+1)+":"+title.text());
	            
	            MovieVO vo=new MovieVO();
	            vo.setMno(i+1);
	            vo.setTitle(title.text());
	            vo.setDirector(director.text());
	            vo.setActor(actor.text());
	            vo.setPoster(poster.attr("src"));
	            vo.setGenre(genre.text());
	            vo.setGrade(grade.text());
	            vo.setTime(time.text());
	            vo.setRegdate(regdate.text());
	            String s=story.text();
	            s=s.replace("'", "");
	            s=s.replace("\"", "");
	            vo.setStory(s);
	            dao.movieInsert(vo);
	            
	            list.add(vo);
	            
	        	 }catch(Exception e) {}
	         }
	      	}catch(Exception ex) {
	         System.out.println(ex.getMessage());
	         
	      }
	      return list;
	   }
	
	
}

