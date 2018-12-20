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
	//�ϴ��ļ��Ĵ洢Ŀ¼
	private static final String UPLOAD_DIRECTORY="zhangmengs_file";
	//�ϴ�����
	private static final int	MEMORY_THRESHOLD = 1024*1024*5;
	private static final int MAX_FILE_SIZE=1024*1024*5;
	private static final int MAX_REQUEST_SIZE=1024*1024*5;
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//		����Ƿ�Ϊ��ý���ϴ�
		if(!ServletFileUpload.isMultipartContent(request)){
			//���������ֹͣ
			PrintWriter writer=response.getWriter();
			writer.println("�ϴ�����!!!");
			writer.flush();
			return;
		}
		HttpSession session=request.getSession();
		//�����ϴ�����---------Create a factory for disk-based file items(Ϊ���ڴ��̵��ļ����һ������)
		DiskFileItemFactory factory=new DiskFileItemFactory();
		//�����ڴ��ٽ�ֵ
		factory.setSizeThreshold(MEMORY_THRESHOLD);
		//������ʱ�洢Ŀ¼
		factory.setRepository(new File(System.getProperty("java.io.tmpdir")));
		//����һ���µ� �ļ��ϴ�handler(������)
		ServletFileUpload upload = new ServletFileUpload(factory);
        
        // ��������ļ��ϴ�ֵ
        upload.setFileSizeMax(MAX_FILE_SIZE);
         
        // �����������ֵ (�����ļ��ͱ�����)
        upload.setSizeMax(MAX_REQUEST_SIZE);

        // ���Ĵ���
        upload.setHeaderEncoding("UTF-8"); 

        // ������ʱ·�����洢�ϴ����ļ�
        // ���·����Ե�ǰӦ�õ�Ŀ¼
        ResourceBundle bundle=ResourceBundle.getBundle("uploadpath");
		//properties�������û����ͼƬ�ļ���
        String uploadPath=bundle.getString("UPLOADPATH");
        // ���Ŀ¼�������򴴽�
        File uploadDir = new File(uploadPath);
        if (!uploadDir.exists()) {
            uploadDir.mkdir();
        }

        try {
            // ���������������ȡ�ļ����ݣ���������ֵȫ����������
            List<FileItem> formItems = upload.parseRequest(request);
            
            //��Ϊ��������
            if (formItems != null && formItems.size() > 0) {
                // ���������ݣ�����ÿ����������
                for (FileItem item : formItems) {
                    // �����ڱ��е��ֶ�-----------����ֻ�����ļ����͵�
                    if (!item.isFormField()) {
                    	session.setAttribute("item", item.getName());
                        String fileName = item.getName();
                        String filePath = uploadPath + fileName;
                        File storeFile = new File(filePath);
                        // �ڿ���̨����ļ����ϴ�·��
                        System.out.println(filePath);
                        // �����ļ���Ӳ��
                        item.write(storeFile);
                        //����ͼƬ����
                        request.getRequestDispatcher("/j2t.html").forward(request, response);
//                        String command=bundle.getString("COMMAND");������properties��������������ɾ�������ļ����е��ļ�
//                        Runtime.getRuntime().exec(command);
                    }
                }
            }
        } catch (Exception ex) {
            request.setAttribute("message","������Ϣ: " + ex.getMessage());
            request.setAttribute("msg", "�ϴ�ʧ��");
         // ��ת�� message.jsp
            request.getServletContext().getRequestDispatcher("/message.jsp").forward(request, response);
        }
        
	}
}
