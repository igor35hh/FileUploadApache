package servlet;
 
import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
 
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
 
//@WebServlet("/FileUploadApache")
public class Upload extends HttpServlet {	
	
	private String stringEqual = new String(".jpg");
	
	private Random random = new Random();
	
	public void doGet(HttpServletRequest reg, HttpServletResponse resp) throws ServletException, IOException {
	
		getServletContext().getRequestDispatcher("/request.html").forward(reg, resp);
		
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		//��������� �������� �� ���������� ������ multipart/form-data
		boolean isMultipart = ServletFileUpload.isMultipartContent(request);
		if (!isMultipart) {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST);
			return;
		}
 
		// ������ ����� ������� 
		DiskFileItemFactory factory = new DiskFileItemFactory();
 
		// ������������ ������ ������ � ������,
		// ��� ��� ���������� ������ ������ ������������ �� ���� �� ��������� ����������
		// ������������� ���� ��������
		factory.setSizeThreshold(1024*1024);
		
		// ������������� ��������� ����������
		File tempDir = (File)getServletContext().getAttribute("javax.servlet.context.tempdir");
		factory.setRepository(tempDir);
 
		//������ ��� ���������
		ServletFileUpload upload = new ServletFileUpload(factory);
		
		//������������ ������ ������ ������� ��������� ��������� � ������
		//�� ��������� -1, ��� �����������. ������������� 10 ��������. 
		upload.setSizeMax(1024 * 1024 * 1000);
 
		try {
			List items = upload.parseRequest(request);
			Iterator iter = items.iterator();
			
			while (iter.hasNext()) {
			    FileItem item = (FileItem) iter.next();
 
			    if (item.isFormField()) {
			    	//���� ����������� ����� ������ �������� ����� �����			    	
			        processFormField(item);
			    } else {
			    	//� ��������� ������ ������������� ��� ����
			        processUploadedFile(item);
			    }
			}			
		} catch (Exception e) {
			e.printStackTrace();
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			return;
		}		
	}
	
	/**
	 * ��������� ���� �� �������, � ����� upload.
	 * ���� ����� ������ ���� ��� �������. 
	 * 
	 * @param item
	 * @throws Exception
	 */
	private void processUploadedFile(FileItem item) throws Exception {
		File uploadetFile = null;
		//�������� ����� ��� ���� �� ����� ���������
		
		String pathFile = item.getName();
		
		if (pathFile.isEmpty()) {  
			
		} else {	
				
			String pathFile1 = Matcher.quoteReplacement(pathFile);
				
			String pathFile2 = pathFile1.replace("\\", "/");
			
			String finalRealFileName = "";
				
			for (String retval: pathFile2.split("//")) {
			      finalRealFileName = retval;
			}
			
			//System.out.println(finalRealFileName.contains(".jpg"));
			
			String lastSimbol = finalRealFileName.substring(finalRealFileName.length()-4); 
			
			if (lastSimbol.equals(stringEqual)) {
				
				String path = "D:/uploadFiles/"+finalRealFileName; 

				//System.out.println(path); 
				
				uploadetFile = new File(path);
				
				if (uploadetFile.exists()) {
					
				} else {
				
					//������ ����
					uploadetFile.createNewFile();
					//���������� � ���� ������
					item.write(uploadetFile);
				}
				
			} else {
				
			}

		}
		
	}
 
	/**
	 * ������� �� ������� ��� ��������� � ��������
	 * @param item
	 */
	private void processFormField(FileItem item) {
		//System.out.println(item.getFieldName()+"="+item.getString());		
	}
}
