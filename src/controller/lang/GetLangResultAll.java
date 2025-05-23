package controller.lang;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.Collections;
import java.util.Comparator;
import java.util.NoSuchElementException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import model.dao.LangQuestionDAO;
import model.dao.LangReplyDAO;
import model.dao.LangTestLogDAO;
import model.dao.LangResultAnalysisDAO;
import model.dao.UserDAO;
import model.dto.*;
import util.process.UserInfoProcessor;

/**
 * 
 * 언어 평가 전체 결과 호출
 * 
 * 세션 정리 필요 
 */
@WebServlet("/GetLangResultAll")
public class GetLangResultAll extends HttpServlet {
	private static final long serialVersionUID = 1L;

    public GetLangResultAll() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html; charset=UTF-8");
	    request.setCharacterEncoding("UTF-8");
	    
	    HttpSession session = request.getSession(true);
		
		ServletContext sc = getServletContext();
		Connection conn= (Connection)sc.getAttribute("DBconnection");
		
		User focusUser;
		int curAge = 0;
	 	
	 	if(request.getParameter("childId")==null) {//아동이 로그인한 상태 : 자신의 아이디를 parameter로 보내지 않았을 때
			 focusUser = (User)session.getAttribute("currUser");
			 AgeGroup currAgeGroup = (AgeGroup)session.getAttribute("currAgeGroup");
			 curAge = currAgeGroup.getAgeGroupId();
		}else{
			int childId = Integer.parseInt(request.getParameter("childId"));
		 	focusUser  = UserDAO.getUserById(conn, childId);
		 	curAge = UserInfoProcessor.getUserBirthToCurrAge(focusUser.getUserBirth());
		}
	 	
	 	int userId = focusUser.getUserId();
	 	
		// 모든 lang test log
		List<LangTestLog> langTestLogList = LangTestLogDAO.getLangTestLogByUserId(conn, userId);
		
		if(langTestLogList.size() == 0) {
			PrintWriter out = response.getWriter();
			out.println("<script>location.href='langTestMain.jsp';alert('언어 평가 기록이 없습니다.');history.go(-1);</script>");
 			out.flush();
		}else {
			System.out.println("focusUser : " + focusUser.getUserId());			
			
			// 사용자의 모든 로그 리스트 가져오기 -> langTestLogList
			// 모든 로그의 로그 아이디 리스트
			ArrayList<Integer> langTestLogIDList = new ArrayList<Integer>();
			
			for(int i = 0 ;i < langTestLogList.size(); i++) {
				langTestLogIDList.add(langTestLogList.get(i).getLangTestLogId());
			}
			
			// 각 로그의 모든 응답 객체 리스트
			ArrayList<ArrayList<LangReply>> allLangReplyList = new ArrayList<ArrayList<LangReply>>();
			for(int j=0; j< langTestLogList.size(); j++) {
				ArrayList<LangReply> langReplyElement = new ArrayList<>(LangReplyDAO.getLangReplyListByLangTestLogId(conn, langTestLogIDList.get(j)));
				allLangReplyList.add(langReplyElement);
			}
			
			// 수행한 모든 연령 그룹 id 리스트 & 중복 없는 리스트
			ArrayList<Integer> allAgeGroupIDList = new ArrayList<Integer>();
			ArrayList<Integer> ageGroupSet = new ArrayList<Integer>();
			
			for(int k=0; k< langTestLogList.size(); k++) {
				int ageGroupIDElement = LangReplyDAO.getLangAgeGroupIdByLogId(conn, langTestLogIDList.get(k));	
				if(ageGroupIDElement !=-1) {
					allAgeGroupIDList.add(ageGroupIDElement);
					if(!ageGroupSet.contains(ageGroupIDElement)) {
						ageGroupSet.add(ageGroupIDElement);
					}
				}
			}
			/*
			 * langTestLogIDList : [369, 370, 371] -> 테스트 아이디
			 * allAgeGroupIDList : [10, 0, 0] -> 연령 아이디
			 * ageGroupSet : [10, 0]
			 * allLangReplyList : [<369응답 객체 리스트>, <370응답 객체 리스트>, <371응답 객체 리스트>]
			 * 
			 * */
			
			// 선택한 테스트 로그 정보 가져오기
			// if (선택한 것이 없다면) : 가장 최근에 수행한 검사 기록 가져오기
			// else (선택한 것이 있다면) : 선택한 테스트 로그 가져오기
			// 검사 직후인 경우

			
			// 연령 그룹 선택
			@SuppressWarnings("unchecked")
				
			List<Integer> langLogIdListByUser = null;	// 현재 선택한 연령의 로그 id 리스트(근데 처음에 접속하면 null 값이나 이전 사용자거 나옴)
			ArrayList<LangTestLog> langLogListByUser = new ArrayList<LangTestLog>();	// 현재 선택한 연령 로그의 LangTesetLog 객체 리스트 (테스트로그id, UserId, TestDate, TestTime)
			ArrayList<ArrayList<LangReply>> langReplyContentListByUser = new ArrayList<ArrayList<LangReply>>(); // LangReply 객체 리스트 (ReplyId, TestLogId, QuestionId, replyContent)
			
			
			if (session.getAttribute("langLogUserId") != null && // 사용자가 AgeGroup 선택한 경우
				session.getAttribute("langLogIdListByUser") != null &&
			    (int)session.getAttribute("langLogUserId") == focusUser.getUserId()) {
				
			    langLogIdListByUser = (List<Integer>)session.getAttribute("langLogIdListByUser");
			    
			} else { // 현재 사용자가 아니거나 아직 선택 안한 경우
			    // 세션이 이전 사용자 데이터면 무시
			    // 현재 사용자로 langLogIdListByUser 할당필요
			    List<Integer> tmpLogIdList = LangReplyDAO.getLangTestLogIdByAgeGroup(conn, allAgeGroupIDList.get(allAgeGroupIDList.size()-1), userId);
			    langLogIdListByUser = new ArrayList<>();
			    for(int i=0; i<tmpLogIdList.size(); i++) {
					if(! langLogIdListByUser.contains(tmpLogIdList.get(i))|| langLogIdListByUser.isEmpty()) {
						langLogIdListByUser.add(tmpLogIdList.get(i));
					}
				}
			}
			for(int i=0; i<langLogIdListByUser.size(); i++) {
				langLogListByUser.add(LangTestLogDAO.getLangTestLogById(conn, langLogIdListByUser.get(i)));
				ArrayList<LangReply> langReplyElement = new ArrayList<>(LangReplyDAO.getLangReplyListByLangTestLogId(conn, langLogIdListByUser.get(i)));
				langReplyContentListByUser.add(langReplyElement);
			}
			
			// 연령 그룹 하위 날짜 로그 선택
			LangTestLog selectedLangTestLog = null;
			int selectIndex = -1;
			boolean isTesting = false;

			if (request.getAttribute("selectIndex") != null) {
				selectIndex = (int)request.getAttribute("selectIndex");
			}
			
			if (request.getParameter("isTesting") != null) {
			    isTesting = true;
			    // 최근 검사 기준
			    selectedLangTestLog = langTestLogList.stream()
			        .max(Comparator.comparingInt(LangTestLog::getLangTestLogId))
			        .orElseThrow(NoSuchElementException::new);
			} else if (langLogIdListByUser != null && !langLogIdListByUser.isEmpty()) {
			    // 사용자 연령 선택 후 검사 목록 존재
			    if (selectIndex >= 0 && selectIndex < langLogListByUser.size()) {
			        selectedLangTestLog = langLogListByUser.get(selectIndex);
			        request.setAttribute("selectIndex", selectIndex);
			    } else {
			    	selectIndex = langLogListByUser.size()-1;
			    	request.setAttribute("selectIndex", selectIndex);
			        selectedLangTestLog = langLogListByUser.get(selectIndex); // fallback
			    }
			} else if (allAgeGroupIDList.contains(curAge)) {
			    // 기본 선택: 현재 나이에 해당하는 가장 마지막 검사
			    int ageGroupIndex = allAgeGroupIDList.lastIndexOf(curAge);
			    if (ageGroupIndex >= 0 && ageGroupIndex < langTestLogList.size()) {
			        selectedLangTestLog = langTestLogList.get(ageGroupIndex);
			    }
			}

			// 최종적으로 fallback: 아무 조건도 만족 못했을 경우 최근 검사
			if (selectedLangTestLog == null && !langTestLogList.isEmpty()) {
			    selectedLangTestLog = langTestLogList.get(langTestLogList.size() - 1);
			}
			
			// 결과값 가져온 후 lang type 이름순으로 정렬
			int langTestAgeGroupId = (int)LangReplyDAO.getLangAgeGroupIdByLogId(conn, selectedLangTestLog.getLangTestLogId());		
			ArrayList<LangReply> selectLangReplyList = new ArrayList<>(LangReplyDAO.getLangReplyListByLangTestLogId(conn, selectedLangTestLog.getLangTestLogId()));
			ArrayList<LangQuestion> selectLangQuestionList = new ArrayList<LangQuestion>(LangQuestionDAO.getLangQuestionListByAgeGroupId(conn, langTestAgeGroupId));


			Collections.sort(selectLangQuestionList, new Comparator<LangQuestion>() {/*sorting question list by lang type*/
				@Override
				public int compare(LangQuestion o1, LangQuestion o2) {
					return ((java.lang.String) o1.getLangType()).compareTo((java.lang.String) o2.getLangType());
				}
			});

			Collections.sort(selectLangReplyList, new Comparator<LangReply>() {/*sorting reply list by lang type*/
				@Override
				public int compare(LangReply o1, LangReply o2) {
					LangQuestion q1 = LangQuestionDAO.getLangQuestionById(conn, o1.getLangQuestionId());
					LangQuestion q2 = LangQuestionDAO.getLangQuestionById(conn, o2.getLangQuestionId());
					return q1.getLangType().compareTo(q2.getLangType());
				}
			});
			
			// 결과 분석(현재 연령 결과분석)
 			List<LangResultAnalysis> langResultAnalysisList = new ArrayList<LangResultAnalysis>();
 			langResultAnalysisList.addAll(LangResultAnalysisDAO.findLangResultAnalysisByAge(conn, langTestAgeGroupId));
 			 			
 			int currentAgeGroupId = langResultAnalysisList.get(0).getLangReportAgeGroupId();
 			

 			if (currentAgeGroupId != 0) {
 				langResultAnalysisList.addAll(0, LangResultAnalysisDAO.findLangResultAnalysisByReportAge(conn, currentAgeGroupId-1));
 			}
 			
 			if (currentAgeGroupId != 7) {
 				langResultAnalysisList.addAll(LangResultAnalysisDAO.findLangResultAnalysisByReportAge(conn, currentAgeGroupId+1));
 			}
 			
 			request.setAttribute("focusUser", focusUser);
			
			request.setAttribute("langTestLogIDList", langTestLogIDList);
			request.setAttribute("allLangReplyList",  allLangReplyList);
			request.setAttribute("allAgeGroupIDList",  allAgeGroupIDList);
			request.setAttribute("ageGroupSet",  ageGroupSet);
			
			request.setAttribute("langLogListByUser", langLogListByUser);
			request.setAttribute("langReplyContentListByUser", langReplyContentListByUser);
			
			request.setAttribute("langTestLogList", langTestLogList);
			request.setAttribute("selectedLangTestLog", selectedLangTestLog);
			request.setAttribute("isTesting", isTesting);
			
			request.setAttribute("selectAgeGroupId", langTestAgeGroupId);
			request.setAttribute("selectLangReplyList",  selectLangReplyList); // lang reply list sorted by question type
			request.setAttribute("selectLangQuestionList",  selectLangQuestionList); // lang question sorted by question type
			
			request.setAttribute("langResultAnalysisList", langResultAnalysisList);
			
			RequestDispatcher rd = request.getRequestDispatcher("/langTestResult.jsp");
			rd.forward(request, response);
		}

	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
