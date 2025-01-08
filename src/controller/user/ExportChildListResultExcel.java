package controller.user;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletOutputStream;

import model.sevice.ExportChildResultExcelService;
import util.process.ZipUtils;



/**
 * @author Jiwon Lee
 * 아동 결과 엑셀 파일 export
 * export type = 아동 별(child) / 검사별(test)
 */
@WebServlet("/ExportChildListResultExcel")
public class ExportChildListResultExcel extends HttpServlet {
	private static final long serialVersionUID = 1L;
	public static final String FILE_SEPARATOR = System.getProperty("file.separator");

	
    public ExportChildListResultExcel() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html; charset=UTF-8");
		request.setCharacterEncoding("UTF-8");

		ServletContext sc = getServletContext();
		Connection conn= (Connection) sc.getAttribute("DBconnection");

		
		String exportType = request.getParameter("exportType"); // child, test
		String[] childIdStrList = request.getParameterValues("childId");
		String[] categoryList = request.getParameterValues("category");
		
		
		try {
			String dirPath = getServletContext().getRealPath("ssk/");
			File directory = new File(dirPath);
			
			if(!directory.exists()) directory.mkdirs();
			
			/*해당하는 .xlsx 파일을 directory에 삽입*/
			if(!ExportChildResultExcelService.makeSskExcelInDir(conn, exportType, dirPath, childIdStrList, categoryList)) {
				PrintWriter out = response.getWriter();
				out.println("<script>alert('결과 파일 추출에 실패했습니다. 관리자에게 문의하세요.'); hitory.go(-1);</script>");
				out.flush();
			}
			
			String[] files = directory.list();
			
			if(files != null && files.length > 0) {
				byte[] zip = ZipUtils.zipFiles(directory);
				ServletOutputStream sos = response.getOutputStream();
	            response.setContentType("application/zip;charset=utf-8");
	            String headerKey = "Content-Disposition";
	            
	            String filename = new String(("export by "+exportType).getBytes("UTF-8"),StandardCharsets.ISO_8859_1);
	            String headerValue = String.format("attachment; filename=\"" + filename + ".zip");
	            response.setHeader(headerKey, headerValue);
	            
	            sos.write(zip);
	            sos.flush();
	            
	            boolean isDel = ZipUtils.deleteFiles(directory);
	            
	            if(!isDel)
	            	throw new Exception("Fail to delete files in server.");
			}
		
		}catch (Exception e) {
			e.printStackTrace();
		}
		/*response.setHeader("Set-Cookie", "fileDownload=true; path=/");
		response.setHeader("Content-Disposition", String.format("attachment; filename=\""+fileName+"\""));

		wb.write(response.getOutputStream());
		 */
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}
	
	

}
