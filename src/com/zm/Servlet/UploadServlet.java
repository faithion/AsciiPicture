package com.zm.Servlet;

import java.io.IOException;
import java.util.List;
import java.util.ResourceBundle;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import java.io.*;

@WebServlet("/upload.html")
public class UploadServlet extends HttpServlet {
	//上传文件的存储目录
	private static final String UPLOAD_DIRECTORY="zhangmengs_file";
	//上传配置
	private static final int	MEMORY_THRESHOLD = 1024*1024*5;
	private static final int MAX_FILE_SIZE=1024*1024*5;
	private static final int MAX_REQUEST_SIZE=1024*1024*5;
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//		检查是否为多媒体上传
		if(!ServletFileUpload.isMultipartContent(request)){
			//如果不是则停止
			PrintWriter writer=response.getWriter();
			writer.println("上传错误!!!");
			writer.flush();
			return;
		}
		HttpSession session=request.getSession();
		//配置上传参数---------Create a factory for disk-based file items(为基于磁盘的文件项创建一个工厂)
		DiskFileItemFactory factory=new DiskFileItemFactory();
		//设置内存临界值
		factory.setSizeThreshold(MEMORY_THRESHOLD);
		//设置临时存储目录
		factory.setRepository(new File(System.getProperty("java.io.tmpdir")));
		//创建一个新的 文件上传handler(管理者)
		ServletFileUpload upload = new ServletFileUpload(factory);
        
        // 设置最大文件上传值
        upload.setFileSizeMax(MAX_FILE_SIZE);
         
        // 设置最大请求值 (包含文件和表单数据)
        upload.setSizeMax(MAX_REQUEST_SIZE);

        // 中文处理
        upload.setHeaderEncoding("UTF-8"); 

        // 构造临时路径来存储上传的文件
        // 这个路径相对当前应用的目录
        ResourceBundle bundle=ResourceBundle.getBundle("uploadpath");
		//properties里面设置缓存的图片文件夹
        String uploadPath=bundle.getString("UPLOADPATH");
        // 如果目录不存在则创建
        File uploadDir = new File(uploadPath);
        if (!uploadDir.exists()) {
            uploadDir.mkdir();
        }

        try {
            // 解析请求的内容提取文件数据，把整个表单值全部传过来了
            List<FileItem> formItems = upload.parseRequest(request);
            
            //表单为空则跳过
            if (formItems != null && formItems.size() > 0) {
                // 迭代表单数据，遍历每个表单数据项
                for (FileItem item : formItems) {
                    // 处理不在表单中的字段-----------即是只处理文件类型的
                    if (!item.isFormField()) {
                    	session.setAttribute("item", item.getName());
                        String fileName = item.getName();
                        String filePath = uploadPath + fileName;
                        File storeFile = new File(filePath);
                        // 在控制台输出文件的上传路径
                        System.out.println(filePath);
                        // 保存文件到硬盘
                        item.write(storeFile);
                        //进行图片处理
                        request.getRequestDispatcher("/j2t.html").forward(request, response);
//                        String command=bundle.getString("COMMAND");可以在properties里面设置命令来删除缓存文件夹中的文件
//                        Runtime.getRuntime().exec(command);
                    }
                }
            }
        } catch (Exception ex) {
            request.setAttribute("message","错误信息: " + ex.getMessage());
            request.setAttribute("msg", "上传失败");
         // 跳转到 message.jsp
            request.getServletContext().getRequestDispatcher("/message.jsp").forward(request, response);
        }
        
	}
}
