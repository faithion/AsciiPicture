package com.zm.Servlet;

import java.io.IOException;
import java.util.ResourceBundle;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.zm.services.AsciiPictureServices;


@WebServlet("/j2t.html")
public class PictureServlet extends HttpServlet {
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session=request.getSession();
		if(session.getAttribute("item")==null){
			request.setAttribute("msg", "´íÎó");
			request.setAttribute("message", "ÄãÎ´Ñ¡ÔñÍ¼Æ¬");
			request.getRequestDispatcher("message.jsp").forward(request, response);
		}else{
			String picturename=session.getAttribute("item").toString();
			try {
				ResourceBundle bundle=ResourceBundle.getBundle("uploadpath");
				String uploadpath=bundle.getString("UPLOADPATH");
				AsciiPictureServices picture=new AsciiPictureServices(uploadpath+picturename);
				String asciiPictureTxt=picture.processing();
				request.setAttribute("asciiPictureTxt", asciiPictureTxt);
				request.getRequestDispatcher("picture.jsp").forward(request, response);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		this.doGet(request, response);
	}

}
