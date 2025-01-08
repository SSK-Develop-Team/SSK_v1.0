package controller.user;

import model.dto.export.SskExcelByUser;
import model.sevice.ExportChildResultExcelService;
import org.apache.poi.ss.usermodel.Workbook;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;

@WebServlet(name = "ExportChildResultExcel", value = "/ExportChildResultExcel")
public class ExportChildResultExcel extends HttpServlet {
	
	private static final long serialVersionUID = 1L;

	@Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html; charset=UTF-8");
        request.setCharacterEncoding("UTF-8");

        ServletContext sc = getServletContext();
        Connection conn= (Connection) sc.getAttribute("DBconnection");

        int childId = Integer.parseInt(request.getParameter("childId"));
        SskExcelByUser sskExcelByUser = ExportChildResultExcelService.getSskExcelByChild(conn, childId, true, true, true, true);
        Workbook wb = sskExcelByUser.getWorkBook();
        String fileName = new String(sskExcelByUser.getFileName().getBytes("KSC5601"), StandardCharsets.ISO_8859_1);//encoding

        response.setHeader("Set-Cookie", "fileDownload=true; path=/");
        response.setHeader("Content-Disposition", String.format("attachment; filename=\""+fileName+"\""));

        wb.write(response.getOutputStream());

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
