package controller.user;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 아동 결과 추출 시 하나의 검사 유형 만을 선택한 경우, 단일 파일 추출
 */
@WebServlet("/ExportTestResultExcel")
public class ExportTestResultExcel extends HttpServlet {
	private static final long serialVersionUID = 1L;
       

    public ExportTestResultExcel() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
	}


	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
