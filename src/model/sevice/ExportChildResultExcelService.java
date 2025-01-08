package model.sevice;

import model.dao.*;
import model.dto.*;
import model.dto.export.*;
import model.dto.export.data.EsmExcelDTO;
import model.dto.export.data.EsmRecordExcelDTO;
import model.dto.export.data.LangExcelDTO;
import model.dto.export.data.SdqExcelDTO;
import model.dto.export.data.UserExcelDTO;

import java.io.File;
import java.io.FileOutputStream;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import org.apache.poi.ss.usermodel.Workbook;
/**
 * @author Jiwon Lee
 * export ssk excel by child - methods
 * export ssk excel by test - methods
 * export ssk excel - choose child/test
 */
public class ExportChildResultExcelService {

	/**
	 * export ssk excel by child - methods
	 *  각 데이터를 아동별 결과 excel data format에 맞는 형식으로 반환
	 *  - getChildData : 아동 정보 
	 *  - getLangDataListOfUser : 아동의 언어 발달 검사 결과 
	 *  - getSdqDataListOfUser : 아동의 SDQ 결과
	 *  - getEsmDataListOfUser : 아동의 ESM 결과
	 *  - getEsmRecordDataListOfUser : 아동의 ESM Record 결과
	 *  - getSskExcelByChild : 아동별 검사 결과 excel을 만들어서 반환
	 */
    /*아동 기본 정보*/
    public static UserExcelDTO getChildData(Connection con, int childId){
        return new UserExcelDTO(UserDAO.getUserById(con, childId));
    }

    /*아동의 언어 검사 결과*/
    public static  ArrayList<LangExcelDTO> getLangDataListOfUser(Connection con, int childId, String childName){
        ArrayList<LangExcelDTO> langDataList = new ArrayList<LangExcelDTO>();

        ArrayList<LangTestLog> langTestLogList = LangTestLogDAO.getLangTestLogByUserId(con, childId);
        for(int i=0;i<langTestLogList.size();i++){
            LangTestLog langTestLog = langTestLogList.get(i);

            LangExcelDTO langData = new LangExcelDTO();
            langData.setUserId(childId);
            langData.setUserName(childName);
            langData.setId(i+1);
            langData.setDateStr(langTestLog.getLangTestDate());
            langData.setAgeGroupStr(LangReplyDAO.getLangAgeGroupIdByLogId(con,langTestLog.getLangTestLogId()));

            ArrayList<LangReply> langReplyList = LangReplyDAO.getLangReplyListByLangTestLogId(con,langTestLog.getLangTestLogId());
            ArrayList<String> langReplyStrList = new ArrayList<String>();
            ArrayList<String> langTypeList = new ArrayList<String>();
            langTypeList.add("구문");
            langTypeList.add("문해");
            langTypeList.add("의미");
            langTypeList.add("의미1");
            langTypeList.add("의미2");
            langTypeList.add("조음");
            langTypeList.add("화용");
            
            /**
             * sort reply list by lang type
             * lang type order : 구문, 문해, 의미, 의미 1, 의미 2, 조음, 화용 (count : 7)
             * */
            Collections.sort(langReplyList, new Comparator<LangReply>() {/*sorting reply list by lang type*/
                @Override
                public int compare(LangReply o1, LangReply o2) {
                    LangQuestion q1 = LangQuestionDAO.getLangQuestionById(con, o1.getLangQuestionId());
                    LangQuestion q2 = LangQuestionDAO.getLangQuestionById(con, o2.getLangQuestionId());
                    return q1.getLangType().compareTo(q2.getLangType());
                }
            });
            int e = 0;
            for(int j=0;j < 7 ;j++){
                LangQuestion q1 = LangQuestionDAO.getLangQuestionById(con, langReplyList.get(e).getLangQuestionId());
                if(langTypeList.get(0).equals(q1.getLangType())&& e<=5){
                    langReplyStrList.add(String.valueOf(langReplyList.get(e).getLangReplyContent()));/*순서 보장 필요 -> 쿼리문으로 조정할 것(order by question id)*/
                    e++;
                }else{
                    langReplyStrList.add("-");
                }
                langTypeList.remove(0);
            }
            langData.setReplyList(langReplyStrList);
            langDataList.add(langData);
        }
        return langDataList;
    }

    /*아동의 정서행동 발달 검사 결과*/
    public static ArrayList<SdqExcelDTO> getSdqDataListOfUser(Connection con, int childId, String childName){
        ArrayList<SdqExcelDTO> sdqDataList = new ArrayList<SdqExcelDTO>();

        ArrayList<SdqTestLog> sdqTestLogList = SdqTestLogDAO.getSdqTestLogAllByUserId(con, childId);//아동의 모든 SDQ 기록 가져오기
        for(int i=0;i< sdqTestLogList.size();i++){
            SdqTestLog sdqTestLog = sdqTestLogList.get(i);

            SdqExcelDTO sdqData = new SdqExcelDTO();
            sdqData.setUserId(childId);
            sdqData.setUserName(childName);

            sdqData.setId(i+1);
            sdqData.setTarget(SdqTarget.getTypeNameKrByTypeName(SdqReplyDAO.getSdqTargetBySdqTestLogId(con, sdqTestLog.getSdqTestLogId())));
            sdqData.setDateStr(sdqTestLog.getSdqTestDate(), sdqTestLog.getSdqTestTime());
            sdqData.setReplyList(SdqReplyDAO.getSdqReplyListIntegerBySdqTestLogId(con, sdqTestLog.getSdqTestLogId()));//기록의 문항별 응답
            sdqData.setScoreList(SdqReplyDAO.getSdqResultOfTypesBySdqTestLogId(con, sdqTestLog.getSdqTestLogId()));//기록의 타입별 점수

            sdqDataList.add(sdqData);
        }

        return sdqDataList;
    }

    /*아동의 정서 반복 기록*/
    public static ArrayList<EsmExcelDTO> getEsmDataListOfUser(Connection con, int childId, String childName){
        ArrayList<EsmExcelDTO> esmDataList = new ArrayList<EsmExcelDTO>();

        ArrayList<EsmTestLog> esmTestLogList = EsmTestLogDAO.getEsmTestLogListByUserId(con, childId);
        for(int i=0;i< esmTestLogList.size();i++){
            EsmTestLog esmTestLog = esmTestLogList.get(i);

            EsmExcelDTO esmData = new EsmExcelDTO();
            esmData.setUserId(childId);
            esmData.setUserName(childName);

            esmData.setId(i+1);
            esmData.setDateStr(esmTestLog.getEsmTestDate(), esmTestLog.getEsmTestTime());
            esmData.setReplyList(EsmReplyDAO.getEsmReplyIntegerListByEsmTestLogId(con,esmTestLog.getEsmTestLogId()));
            esmData.setScoreList(EsmReplyDAO.getEsmResultByEsmTestLogId(con,esmTestLog.getEsmTestLogId()));

            esmDataList.add(esmData);
        }

        return esmDataList;
    }

    /*아동의 정서 다이어리*/
    public static ArrayList<EsmRecordExcelDTO> getEsmRecordDataListOfUser(Connection con, int childId, String childName){
        ArrayList<EsmRecordExcelDTO> esmRecordDataList = new ArrayList<EsmRecordExcelDTO>();

        ArrayList<EsmRecord> esmRecordList = EsmRecordDAO.getEsmRecordListByUser(con, childId);
        for(int i=0;i<esmRecordList.size();i++){
            EsmRecordExcelDTO esmRecordData = new EsmRecordExcelDTO();
            esmRecordData.setUserId(childId);
            esmRecordData.setUserName(childName);

            esmRecordData.setId(i+1);
            esmRecordData.setText(esmRecordList.get(i).getEsmRecordText());
            esmRecordData.setDateStr(esmRecordList.get(i).getEsmRecordDate(), esmRecordList.get(i).getEsmRecordTime());

            esmRecordDataList.add(esmRecordData);
        }

        return esmRecordDataList;
    }

    /**
	 * export ssk excel by Test - methods
	 *  각 데이터를 검사별 결과 excel data format에 맞는 형식으로 반환
	 *  - getLangDataList : 아동 집단의 언어 발달 검사 결과 
	 *  - getSdqDataList : 아동 집단의 SDQ 결과
	 *  - getEsmDataList : 아동 집단의 ESM 결과
	 *  - getEsmRecordDataList : 아동 집단의 ESM Record 결과
	 *  - getSskExcelByTest : 검사별 검사 결과 excel을 만들어서 반환
	 */
    /*해당하는 아동 리스트의 모든 언어 검사 결과 */
    public static ArrayList<LangExcelDTO> getLangDataList(Connection con, String[] childIdStrList){
    	ArrayList<LangExcelDTO> langDataList = new ArrayList<LangExcelDTO>();

        ArrayList<LangTestLog> langTestLogList = LangTestLogDAO.getLangTestLogListOfChildList(con, childIdStrList);
        for(int i=0;i<langTestLogList.size();i++){
            LangTestLog langTestLog = langTestLogList.get(i);
            User user = UserDAO.getUserById(con, langTestLog.getUserId());
            
            LangExcelDTO langData = new LangExcelDTO();
            
            langData.setUserId(user.getUserId());
            langData.setUserName(user.getUserName());
            langData.setId(i+1);
            langData.setDateStr(langTestLog.getLangTestDate());
            langData.setAgeGroupStr(LangReplyDAO.getLangAgeGroupIdByLogId(con,langTestLog.getLangTestLogId()));
           
            ArrayList<LangReply> langReplyList = LangReplyDAO.getLangReplyListByLangTestLogId(con,langTestLog.getLangTestLogId());
            ArrayList<String> langReplyStrList = new ArrayList<String>();
            ArrayList<String> langTypeList = new ArrayList<String>();
            langTypeList.add("구문");
            langTypeList.add("문해");
            langTypeList.add("의미");
            langTypeList.add("의미1");
            langTypeList.add("의미2");
            langTypeList.add("조음");
            langTypeList.add("화용");

            /**
             * sort reply list by lang type
             * lang type order : 구문, 문해, 의미, 의미 1, 의미 2, 조음, 화용 (count : 7)
             * */
            Collections.sort(langReplyList, new Comparator<LangReply>() {/*sorting reply list by lang type*/
                @Override
                public int compare(LangReply o1, LangReply o2) {
                    LangQuestion q1 = LangQuestionDAO.getLangQuestionById(con, o1.getLangQuestionId());
                    LangQuestion q2 = LangQuestionDAO.getLangQuestionById(con, o2.getLangQuestionId());
                    return q1.getLangType().compareTo(q2.getLangType());
                }
            });

            int e = 0;
            for(int j=0;j < 7 ;j++){
                LangQuestion q1 = LangQuestionDAO.getLangQuestionById(con, langReplyList.get(e).getLangQuestionId());
                if(langTypeList.get(0).equals(q1.getLangType()) && e<=5){
                    langReplyStrList.add(String.valueOf(langReplyList.get(e).getLangReplyContent()));/*순서 보장 필요 -> 쿼리문으로 조정할 것(order by question id)*/
                    e++;
                }else{
                    langReplyStrList.add("-");
                }
                langTypeList.remove(0);
            }
            langData.setReplyList(langReplyStrList);
            langDataList.add(langData);
        }
        return langDataList;
    }

    /*해당하는 아동 리스트의 모든 정서 행동 발달 검사 결과 */
    public static ArrayList<SdqExcelDTO> getSdqDataList(Connection con, String[] childIdStrList){
    	ArrayList<SdqExcelDTO> sdqDataList = new ArrayList<SdqExcelDTO>();

        ArrayList<SdqTestLog> sdqTestLogList = SdqTestLogDAO.getSdqTestLogListOfChildList(con, childIdStrList);//아동의 모든 SDQ 기록 가져오기
        for(int i=0;i< sdqTestLogList.size();i++){
            SdqTestLog sdqTestLog = sdqTestLogList.get(i);
            User user = UserDAO.getUserById(con, sdqTestLog.getUserId());

            SdqExcelDTO sdqData = new SdqExcelDTO();
            sdqData.setUserId(user.getUserId());
            sdqData.setUserName(user.getUserName());

            sdqData.setId(i+1);
            sdqData.setTarget(SdqTarget.getTypeNameKrByTypeName(SdqReplyDAO.getSdqTargetBySdqTestLogId(con, sdqTestLog.getSdqTestLogId())));
            sdqData.setDateStr(sdqTestLog.getSdqTestDate(), sdqTestLog.getSdqTestTime());
            sdqData.setReplyList(SdqReplyDAO.getSdqReplyListIntegerBySdqTestLogId(con, sdqTestLog.getSdqTestLogId()));//기록의 문항별 응답
            sdqData.setScoreList(SdqReplyDAO.getSdqResultOfTypesBySdqTestLogId(con, sdqTestLog.getSdqTestLogId()));//기록의 타입별 점수

            sdqDataList.add(sdqData);
        }

        return sdqDataList;
    }
    
    /*해당하는 아동 리스트의 모든 정서 반복 기록*/
    public static ArrayList<EsmExcelDTO> getEsmDataList(Connection con, String[] childIdStrList){
    	ArrayList<EsmExcelDTO> esmDataList = new ArrayList<EsmExcelDTO>();

        ArrayList<EsmTestLog> esmTestLogList = EsmTestLogDAO.getEsmTestLogListOfChildList(con, childIdStrList);
        for(int i=0;i< esmTestLogList.size();i++){
            EsmTestLog esmTestLog = esmTestLogList.get(i);
            User user = UserDAO.getUserById(con, esmTestLog.getUserId());

            EsmExcelDTO esmData = new EsmExcelDTO();
            esmData.setUserId(user.getUserId());
            esmData.setUserName(user.getUserName());

            esmData.setId(i+1);
            esmData.setDateStr(esmTestLog.getEsmTestDate(), esmTestLog.getEsmTestTime());
            esmData.setReplyList(EsmReplyDAO.getEsmReplyIntegerListByEsmTestLogId(con,esmTestLog.getEsmTestLogId()));
            esmData.setScoreList(EsmReplyDAO.getEsmResultByEsmTestLogId(con,esmTestLog.getEsmTestLogId()));

            esmDataList.add(esmData);
        }

        return esmDataList;
    }
    
    /*해당하는 아동 리스트의 모든 정서 다이어리*/	
    public static ArrayList<EsmRecordExcelDTO> getEsmRecordDataList(Connection con, String[] childIdStrList){
    	ArrayList<EsmRecordExcelDTO> esmRecordDataList = new ArrayList<EsmRecordExcelDTO>();

        ArrayList<EsmRecord> esmRecordList = EsmRecordDAO.getEsmRecordListOfChildList(con, childIdStrList);
        for(int i=0;i<esmRecordList.size();i++){
            EsmRecordExcelDTO esmRecordData = new EsmRecordExcelDTO();
            EsmRecord esmRecord = esmRecordList.get(i);
            User user = UserDAO.getUserById(con, esmRecord.getUserId());
            
            esmRecordData.setUserId(user.getUserId());
            esmRecordData.setUserName(user.getUserName());

            esmRecordData.setId(i+1);
            esmRecordData.setText(esmRecord.getEsmRecordText());
            esmRecordData.setDateStr(esmRecord.getEsmRecordDate(), esmRecord.getEsmRecordTime());

            esmRecordDataList.add(esmRecordData);
        }

        return esmRecordDataList;
    }
    
    
    /* export ssk excel by child*/
    public static SskExcelByUser getSskExcelByChild(Connection con, int childId, boolean lang, boolean sdq, boolean esm, boolean esmRecord){
        SskExcelByUser sskExcelByUser = new SskExcelByUser();

        UserExcelDTO userExcelDTO = getChildData(con, childId);
        sskExcelByUser.addUserData(userExcelDTO);

        if(lang==true){
        	ArrayList<LangExcelDTO> langExcelDTOS = getLangDataListOfUser(con, childId, userExcelDTO.getName());
            sskExcelByUser.addLangData(langExcelDTOS);
        }
        if(sdq==true){
            ArrayList<SdqExcelDTO> sdqExcelDTOS = getSdqDataListOfUser(con, childId, userExcelDTO.getName());
            sskExcelByUser.addSdqData(sdqExcelDTOS);
        }
        if(esm==true){
            ArrayList<EsmExcelDTO> esmExcelDTOS = getEsmDataListOfUser(con, childId, userExcelDTO.getName());
            sskExcelByUser.addEsmData(esmExcelDTOS);
        }
        if(esmRecord==true){
            ArrayList<EsmRecordExcelDTO> esmRecordExcelDTOS = getEsmRecordDataListOfUser(con, childId, userExcelDTO.getName());
            sskExcelByUser.addEsmRecordData(esmRecordExcelDTOS);
        }

        return sskExcelByUser;
    }
    
    /*export ssk excel by test*/
    public static SskExcelByTest getSskExcelByTest(Connection con, String childIdStrList[], String category){
    	SskExcelByTest sskExcelByTest = new SskExcelByTest();
    	switch(category) {
			case "lang":
				ArrayList<LangExcelDTO> langExcelDTOS = getLangDataList(con, childIdStrList);
	            sskExcelByTest.addLangData(langExcelDTOS);
				break;
			case "sdq":
				ArrayList<SdqExcelDTO> sdqExcelDTOS = getSdqDataList(con, childIdStrList);
				sskExcelByTest.addSdqData(sdqExcelDTOS);
				break;
			case "esm":
				ArrayList<EsmExcelDTO> esmExcelDTOS = getEsmDataList(con, childIdStrList);
				sskExcelByTest.addEsmData(esmExcelDTOS);
				break;
			case "esmRecord":
				ArrayList<EsmRecordExcelDTO> esmRecordExcelDTOS = getEsmRecordDataList(con, childIdStrList);
				sskExcelByTest.addEsmRecordData(esmRecordExcelDTOS);
				break;
		}
    	return sskExcelByTest;
    }
    
    /* make ssk excel by child in dir*/
    public static boolean makeSskExcelByUserInDir(Connection con, String dirPath, String childIdStrList[], String[] categoryList) {
    	boolean lang = false, sdq=false, esm = false, esmRecord = false;
    	for(String x : categoryList) {
			switch(x) {
				case "lang":
					lang=true;
					break;
				case "sdq":
					sdq=true;
					break;
				case "esm":
					esm=true;
					break;
				case "esmRecord":
					esmRecord = true;
					break;
			}
		}
    	try {
			for(String c : childIdStrList) {
				int childId = Integer.parseInt(c);
				if(childId==0) continue;
				SskExcel sskExcel = getSskExcelByChild(con, childId, lang, sdq, esm, esmRecord);
				Workbook wb = sskExcel.getWorkBook();
	
				String fileName = new String(sskExcel.getFileName().getBytes("KSC5601"), "EUC_KR");//encoding
	
				File excelFile = new File(dirPath+fileName);
				FileOutputStream fos = new FileOutputStream(excelFile);
	
				wb.write(fos);
				fos.flush();
				fos.close();
				wb.close();
			}
			return true;
    	}catch (Exception e) {
			e.printStackTrace();
			return false;
		}
    }
    
    /* make ssk excel by test in dir*/
    public static boolean makeSskExcelByTestInDir(Connection con, String dirPath, String childIdStrList[], String[] categoryList) {
    	try {
			for(String category : categoryList) {
				SskExcel sskExcel = getSskExcelByTest(con, childIdStrList, category);
				Workbook wb = sskExcel.getWorkBook();
	
				String fileName = new String(sskExcel.getFileName().getBytes("KSC5601"), "EUC_KR");//encoding
	
				File excelFile = new File(dirPath+fileName);
				FileOutputStream fos = new FileOutputStream(excelFile);
	
				wb.write(fos);
				fos.flush();
				fos.close();
				wb.close();
			}
			return true;
    	}catch (Exception e) {
			e.printStackTrace();
			return false;
		}
    }
    
    /* make ssk excel in directory - filter child/test */
    public static boolean makeSskExcelInDir(Connection con, String exportType, String dirPath, String childIdStrList[], String[] categoryList) {
    	if(exportType.equals("child")) {
    		return makeSskExcelByUserInDir(con, dirPath, childIdStrList, categoryList);
    	}else if(exportType.equals("test")) {
    		return makeSskExcelByTestInDir(con, dirPath, childIdStrList, categoryList);
    	}
    	return false;
    }
}
