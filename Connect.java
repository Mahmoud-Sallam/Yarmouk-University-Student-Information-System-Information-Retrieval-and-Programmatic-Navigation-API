package sallam.mahmoud.com.asynctasktest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import org.jsoup.Jsoup;
import org.jsoup.Connection.Method;
import org.jsoup.Connection.Response;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

//Created by: Mahmoud Sallam, Computer Science Graduate - Faculty of Information Technology and Computer Science, Yarmouk University.
public class Connect {
	
	private  static String baseLink = "https://sis.yu.edu.jo/pls/yuapps/";
	private  static String googleServicesLink = "134:31"; //����� ����
	private  static String mainMenuLink = "f?p=134:14"; //���� ������� ������
	private  static String changePasswordLink = "f?p=134:11"; // ����� ���� ����
	private  static String linkRegisteredClasses = "f?p=134:1";
	private  static String studentInfoLink = "f?p=134:6";
	private  static String planLink = "f?p=134:3";
	private  static String academicRecordsLink = "f?p=134:8";
	private  static String linkAdder;
	private  static String[] loginParamsNames = new String[14];
	private  static String[] loginParamsValues = new String[14];
	private  static String[] CPParamsNames = new String[16];
	private  static String[] CPParamsValues = new String[16];



	private static Map<String, String> cookies_Sis0_LoginPage, cookies_Sis1_HomePage, cookies_Sis2_MainMenuPage;
	private static Document doc_Sis0_LoginPage, doc_Sis1_HomePage, doc_Sis2_MainMenuPage, doc_Sis3_StudentInfoPage;


	private static	ArrayList<ArrayList<String>> Current = new ArrayList<ArrayList<String>>();
	private static	ArrayList<ArrayList<String>> Summer = new ArrayList<ArrayList<String>>();
	private static	ArrayList<ArrayList<String>> Second = new ArrayList<ArrayList<String>>();
	private static	ArrayList<ArrayList<String>> First = new ArrayList<ArrayList<String>>();

	private static ArrayList<Pair<String, String>> acSummary = new ArrayList<Pair<String, String>>();
	public static ArrayList<String> detailsLinksArray = new ArrayList<String>();
	public static ArrayList<ArrayList <String>> studentClassesAndMarks = new ArrayList<ArrayList <String>>();
	private static ArrayList<Pair<String, String>> listStudentInfo = new ArrayList<Pair<String,String>>();



	public static ArrayList<ArrayList<Pair<String,String>>> getSelectedDetails(String detailsLink) {
		ArrayList<ArrayList<Pair<String,String>>> materialsInPlan = new ArrayList<ArrayList<Pair<String, String>>>();
		String detailsURL = baseLink + detailsLink;
		try {
			Response responseDetails = Jsoup.connect(detailsURL)
					.method(Method.GET)
					.userAgent("Mozilla/5.0")
					.timeout(10*10000)
					.cookies(cookies_Sis1_HomePage)
					.execute();
			Document doc_Details = responseDetails.parse();
			//	Document Whatever = Document.createShell("Whatever");
			Elements materials = doc_Details.select("td[headers=TAKEN],td[headers=studied_equivalent],td[headers=REPEATABLE],td[headers=V_CRE_CODE],td[headers=V_CRE_NO],td[headers=V_CRE_DSCP],td[headers=V_CRE_HRS],td[headers=NOTES],td[headers=prereq]");
			int index = 0;

			materialsInPlan.add(new ArrayList<Pair<String, String>>());
			for(Element material: materials) {
				if(material.attr("headers").equals("prereq")) {
					materialsInPlan.get(index).add(Pair.createPair(material.text(), " "));
					materialsInPlan.add(new ArrayList<Pair<String, String>>());
					index++;
				}
				else {

					materialsInPlan.get(index).add(Pair.createPair(material.text(), " "));
				}
			}
			System.out.println("indx is = " + materialsInPlan.size());
			for(int i = 0 ; i < materialsInPlan.size(); i ++) {
				System.out.println("=======================================");
				for(int b = 0; b <materialsInPlan.get(i).size(); b++) {
					switch(b) {

						case 0:
							materialsInPlan.get(i).get(b).setSecond("درستها");
							break;
						case 1:
							materialsInPlan.get(i).get(b).setSecond("درس المساق المكافئ");
							break;
						case 2:
							materialsInPlan.get(i).get(b).setSecond("يمكن إعادة دراستها");
							break;
						case 3:
							materialsInPlan.get(i).get(b).setSecond("رمز المساق");
							break;
						case 4:
							materialsInPlan.get(i).get(b).setSecond("رقم المساق");
							break;
						case 5:
							materialsInPlan.get(i).get(b).setSecond("اسم المساق");
							break;
						case 6:
							materialsInPlan.get(i).get(b).setSecond("عدد الساعات");
							break;
						case 7:
							materialsInPlan.get(i).get(b).setSecond("المساق المكافئ");
							break;
						case 8:
							materialsInPlan.get(i).get(b).setSecond("المتطلب السابق");
							break;
					}
					System.out.println(materialsInPlan.get(i).get(b).getElement0()
							+ " : " +materialsInPlan.get(i).get(b).getElement1());
				}
			}
			//	System.out.println("===========\n" + Whatever);
		}
		catch(Exception e) {
			e.printStackTrace();
			System.out.println("Error happened while connecting to a single detail link");
		}
		return materialsInPlan;
	}
	public static void connectToPlan() {
		String planURL = baseLink + planLink + linkAdder;
		try {
			Response responsePlan = Jsoup.connect(planURL)
					.method(Method.GET)
					.userAgent("Mozilla/5.0")
					.timeout(10*10000)
					.cookies(cookies_Sis1_HomePage)
					.execute();
			Document doc_Plan = responsePlan.parse();

			Elements detailsLinks = doc_Plan.select("a[href]:contains(تفاصيل)");
			for(Element detailsLink : detailsLinks) {
				detailsLinksArray.add(detailsLink.attr("href"));
			}
			getSelectedDetails(detailsLinksArray.get(1));
		}
		catch (Exception e) {
			System.out.println("Something went wrong while trying to get the plan");
		}

	}
	public static ArrayList <ArrayList<String>> getAcademicRecords() {
		//This method does 2 things :
		// 1) Assigns values to the acSummary array
		// 2) Returns an array of arrays where each array represents a semester
		// and each sub-array represents a records data.
		ArrayList <ArrayList<String>> academicRecords = new ArrayList <ArrayList<String>>();
		String arURL = baseLink + academicRecordsLink + linkAdder;
		try {
			Response responseDetails = Jsoup.connect(arURL)
					.method(Method.GET)
					.userAgent("Mozilla/5.0")
					.timeout(10*10000)
					.cookies(cookies_Sis1_HomePage)
					.execute();
			Document doc_academicRecord = responseDetails.parse();

			Elements summaries = doc_academicRecord.select("th.t10ReportHeader, td.t10data");

			// getting the academic record summary and storing them in the private arraylist of type pair<string,string>
			int index = 0;
			int ctr = 0;
			for(Element summary: summaries) {
				if(ctr == 0) {
					acSummary.add(Pair.createPair(summary.text(), " "));
					ctr = 1;
				}
				else {
					acSummary.get(index).setSecond(summary.text());
					index++;
					ctr = 0;
				}
			}
			Elements records = doc_academicRecord.select("tr.highlight-row, td.apex_report_break");
			academicRecords.add(new ArrayList<String>());
			int index2 = 0;
			for(Element record : records) {
				if(record.hasClass("apex_report_break")) {
					//System.out.println(record.text());
					academicRecords.get(index2).add(record.text());
					academicRecords.add(new ArrayList<String>());
					index2++;
				}
				else {
					Elements children = record.children();
					for(Element child: children) {
						//	System.out.println(child.text());
						academicRecords.get(index2-1).add(child.text());
					}

				}
			}
			for(int i = 0 ; i < academicRecords.size();i++) {
				System.out.println("==========");
				for(int j = 0 ; j < academicRecords.get(i).size(); j++) {
					System.out.println(academicRecords.get(i).get(j));
				}
			}

		}
		catch(Exception e) {
			e.printStackTrace();
			System.out.println("Error happened while getting the academic record");
		}
		return academicRecords;
	}
	public static ArrayList<Pair<String, String>> getStudentInfo() {
		return listStudentInfo;
	}
	public static ArrayList<ArrayList <String>> getClassesAndMarks() {
		return studentClassesAndMarks;
	}
	public static void clearClassesAndMarksArrayList() {
		studentClassesAndMarks.clear();
	}
	public static void setLoginCredentials(String user, String pass) {
		loginParamsValues[10] = user;
		loginParamsValues[12] = pass;
		//this function MUST be performed RIGHT AFTER calling connectToSis0_LoginPage method.
		
	}
	public static String getUsername() {
		return loginParamsValues[10];
	}
	private static boolean checkPassword(String password) {
			if(!password.matches(".*[a-zA-Z]+.*"))
				return false;
			if(password.length() < 8 || password.length() > 20)
			return false;
			boolean found = false;
			for(int i = 0; i < password.length(); i++) {
				if(Character.isDigit(password.charAt(i)))
						found = true;
			}
		return found;
	}
	public static boolean validateCredentials(){
		Elements invalidPass = doc_Sis1_HomePage.select("div.t10notification");
		for (Element x : invalidPass) {
			if(x.text().equals("Invalid Login Credentials"))
				return false;
		}
		return true;
	}
	public static void changePassword(String oldPassword, String newPassword, String verifiedPassword) {
		String changePasswordURL = baseLink + changePasswordLink + linkAdder;
		if(!checkPassword(newPassword)) {
			// do something to inform user the restrictions on passwords.
		}
		if(!oldPassword.equals(loginParamsValues[12])) {
			// do something to inform user that the old password is not correct.
		}
		if(!newPassword.equals(verifiedPassword)) {
			// do something to inform user that the passwords do not match.
		}
		if(newPassword.equals(oldPassword)) {
			//do something to inform user that the old and new passwords must not match.
		}
		try {
			Response responseChangePassword = Jsoup.connect(changePasswordURL)
					.method(Method.GET)
					.userAgent("Mozilla/5.0")
					.timeout(10*10000)
					.cookies(cookies_Sis1_HomePage)
					.execute();
			Document docChangePW = responseChangePassword.parse();
			getAndSetPasswordFormInputs(docChangePW, oldPassword, newPassword, verifiedPassword);

		
			String URL = "https://sis.yu.edu.jo/pls/yuapps/wwv_flow.accept";
			Response responseSubmitChangePwForm = Jsoup.connect(URL)
					.method(Method.POST)
					.userAgent("Mozilla/5.0")
					.timeout(10*10000)
					.cookies(cookies_Sis1_HomePage)
					.data(CPParamsNames[0], CPParamsValues[0])
					.data(CPParamsNames[1], CPParamsValues[1])
					.data(CPParamsNames[2], CPParamsValues[2])
					.data(CPParamsNames[3], CPParamsValues[3])
					.data(CPParamsNames[4], "Go")
					.data(CPParamsNames[5], CPParamsValues[5])
					.data(CPParamsNames[6], CPParamsValues[6])
					.data(CPParamsNames[7], CPParamsValues[7])
					.data(CPParamsNames[8], CPParamsValues[8])
					.data(CPParamsNames[9], CPParamsValues[9])
					.data(CPParamsNames[10], CPParamsValues[10])
					.data(CPParamsNames[11], CPParamsValues[11])
					.data(CPParamsNames[12], CPParamsValues[12])
					.data(CPParamsNames[13], CPParamsValues[13])
					.data(CPParamsNames[14], CPParamsValues[14])
					.data(CPParamsNames[15], CPParamsValues[15])
					.execute();
			Document alright = responseSubmitChangePwForm.parse();
			
		} catch (IOException e) {
			System.out.println("Error occured while attempting to change password.");
			e.printStackTrace();
		}
	}
	private static void createClassesAndMarks(String studentClassesURL, String Semester) {

		try {
			Response responseMarks = Jsoup.connect(studentClassesURL)
					 .method(Method.GET)
				   .userAgent("Mozilla/5.0")
				   .timeout(10*10000)
				   .cookies(cookies_Sis1_HomePage)
				   .execute();
			
			Document marks = responseMarks.parse();
			Elements classNames = marks.select("td[headers]");
			int index = 0;
			studentClassesAndMarks.add(new ArrayList<String>());
        	for(Element className : classNames) {
        		//System.out.println(className.text());
        		if(className.attr("headers").equals("1")) { // ���� ���
        			studentClassesAndMarks.get(index).add(className.text());
        			studentClassesAndMarks.add(new ArrayList<String>());
        			index++;
        			continue;
        		}
        		else {
        			if(!className.attr("headers").equals("ID"))
					studentClassesAndMarks.get(index).add(className.text());
				}
        	}
         //  System.out.println("Cookies === " + cookies_Sis0_LoginPage +"\n" +  cookies_Sis1_HomePage+"\n" +   cookies_Sis2_MainMenuPage);
			// p_request: index 4  value = P0_TERM_LOV // p_t01 = index 6 value = 8356
		/*	switch (Semester) {
				case "CurrentSemester":
					Current =
					break;
				case "SummerSemester":
					break;
				case "SecondSemester":
					break;
				case "FirstSemester":
					break;

			}*/
		}
		catch (IOException e) {
			System.out.println("Error occured while trying to get " + Semester + " semester's classes");
			e.printStackTrace();
		}
	}

	public static ArrayList<Pair<String, String>> getAndSetPasswordFormInputs(Document doc, String oldPassword, String newPassword, String verifiedPassword) {
		ArrayList<Pair<String, String>> formInputs = new ArrayList<>();
		Elements inputs = doc.select("input");
		for(Element input: inputs) {
			if(input.attr("name").equals("p_t03") || input.attr("name").equals("p_t04") ||
			input.attr("name").equals("p_t05")) {continue;}
			formInputs.add(Pair.createPair(input.attr("name"), input.attr("value")));

		}
		/*  p_t02: 2015901115
			p_t03: oldPassword
			p_t04: newPassword
			p_t05: verifiedPassword
		*/
			formInputs.add(Pair.createPair("p_t01", "8359"));
			formInputs.add(Pair.createPair("p_t03", oldPassword));
			formInputs.add(Pair.createPair("p_t04", newPassword));
			formInputs.add(Pair.createPair("p_t05", verifiedPassword));
		//System.out.println(formInputs + "\n\n\n + ===================");
		for(int i = 0; i < formInputs.size(); i++) {
			//System.out.println(formInputs.get(i).getElement0() +formInputs.get(i).getElement1() );
			CPParamsNames[i] = formInputs.get(i).getElement0() ;
			CPParamsValues[i] = formInputs.get(i).getElement1();
			System.out.println(formInputs.get(i).getElement0()
			+" " + formInputs.get(i).getElement1());
		}
		System.out.println("size is "  + formInputs.size());
		return formInputs;
	}
	public static Map<String,String> getAndSetFormInputs(Document doc, String semesterNum) {
		//if semester value == -1, formInputs are not for changing semester.
		Map<String, String> formInputs = new HashMap<>();
		Elements inputs = doc.select("input");
		for(Element input: inputs) {
			formInputs.put(input.attr("name"), input.attr("value"));
		}
		//p_t01: (8359) First Semester // (8354) Second Semester // (8356) Summer Semester
		//p_request value for changing semester is "P0_TERM_LOV".
		if(!semesterNum.equals("-1")) {
			formInputs.put("p_t01", semesterNum);
			formInputs.put("p_request", "P0_TERM_LOV");
		}
		return formInputs;
	}
	public static void changeSemesterToSelected(String Semester) {
		String URL = "https://sis.yu.edu.jo/pls/yuapps/wwv_flow.accept";
		try {
			Response responseChangeSemesterToSelected = Jsoup.connect(URL)
					.userAgent("Mozilla/5.0")
			        .timeout(10 * 10000)
			        .method(Method.POST)
			        .data(getAndSetFormInputs(doc_Sis1_HomePage,Semester))
			        .cookies(cookies_Sis1_HomePage)
			        .followRedirects(true)
			        .execute();
			Document docSemester = responseChangeSemesterToSelected.parse();
		}
			catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	
	public static void connectToSis0_LoginPage() {
			try {
				Response response_Sis0_LoginPage = Jsoup.connect("https://sis.yu.edu.jo/pls/yuapps/f?p=134:101")
				.userAgent("Mozilla/5.0")
				.timeout(10 * 1000)
				.followRedirects(true)
				.execute();
		            cookies_Sis0_LoginPage = response_Sis0_LoginPage.cookies();
		            doc_Sis0_LoginPage = response_Sis0_LoginPage.parse();
		            Elements inputs = doc_Sis0_LoginPage.select("input");
		            int i = 0; 	//loginParams pointer
		           for(Element input : inputs) {
		        	   //p_t03 = student number
		        	   //p_t04 = password
		        	   //p_request = P101_PASSWORD
		        	  //System.out.println(input.attr("name")+": "+input.attr("value"));
		        	   loginParamsNames[i] = input.attr("name");
		        	   loginParamsValues[i] = input.attr("value");
		        	   i++;
		           }
		         //  loginParamsValues[10] = "2015901115";
		        //  loginParamsValues[12] = "fukdatshit9631";
			} catch (IOException e) {
				System.out.println("Something went wrong while trying to connect to sis0_LoginPage\n");
				e.printStackTrace();
			}

	}
	
	public static void connectToSis1_SisHomePage() {
		
	    String strActionURL = "https://sis.yu.edu.jo/pls/yuapps/wwv_flow.accept";
        try {
        	//only way to do it because you can't send them in a map due to a unique key for each element
        	// try a multi key map later on for fun.
			Response response_Sis1_HomePage = Jsoup.connect(strActionURL)  
			        .userAgent("Mozilla/5.0")
			        .timeout(10 * 10000)
			        .method(Method.POST)
			        .data(loginParamsNames[0], loginParamsValues[0])
			        .data(loginParamsNames[1], loginParamsValues[1])
			        .data(loginParamsNames[2], loginParamsValues[2])
			        .data(loginParamsNames[3], loginParamsValues[3])
			        .data(loginParamsNames[4], loginParamsValues[4])
			        .data(loginParamsNames[5], loginParamsValues[5])
			        .data(loginParamsNames[6], loginParamsValues[6])
			        .data(loginParamsNames[7], loginParamsValues[7])
			        .data(loginParamsNames[8], loginParamsValues[8])
			        .data(loginParamsNames[9], loginParamsValues[9])
			        .data(loginParamsNames[10], loginParamsValues[10])
			        .data(loginParamsNames[11], loginParamsValues[11])
			        .data(loginParamsNames[12], loginParamsValues[12])
			        .data(loginParamsNames[13], loginParamsValues[13])
			        .cookies(cookies_Sis0_LoginPage)
			        .followRedirects(true)
			        .execute();
					 cookies_Sis1_HomePage = response_Sis1_HomePage.cookies();
					 doc_Sis1_HomePage = response_Sis1_HomePage.parse();
					 linkAdder = response_Sis1_HomePage.url().toString().substring(43) + "::NO:::";
				//	 System.out.println(linkAdder);
				 //  System.out.println(response_Sis1_HomePage.url() + "     POST LOGIN URL");
					// System.out.println(doc_Sis1_HomePage);
					// System.out.println(getAndSetFormInputs(doc_Sis1_HomePage, "8356"));
		} catch (IOException e) {
			System.out.println("Something went wrong while attempting to connect Sis1_HomePage");
			e.printStackTrace();
		}
                 
	}
	public static void connectToSis2_MainMenuPage() {
		String mainMenuURL = baseLink + mainMenuLink + linkAdder;
		
		try {
			Response response_Sis2_MainMenuPage = Jsoup.connect(mainMenuURL)
				   .method(Method.GET)
			 	   .userAgent("Mozilla/5.0")
			       .timeout(10 * 10000)
			       .cookies(cookies_Sis1_HomePage)
			       .execute();
			doc_Sis2_MainMenuPage = response_Sis2_MainMenuPage.parse();
			cookies_Sis2_MainMenuPage = response_Sis2_MainMenuPage.cookies();
			// for some reason cookies for Sis2 are empty, try to figure it out later on.
			//System.out.println(doc_Sis2_MainMenuPage);
			//Elements inputs = doc_Sis2_MainMenuPage.select("input")
			
		} catch (IOException e) {
			System.out.println("Something went wrong while attempting to connect Sis2_MainMenuPage");
			e.printStackTrace();
		}
	}

	/**
	 * Connects to student personal information link and assigns values to the arraylist "listStudentInfo"
	 * after you call this function, use a getter to "listStudentInfo" to access personal info.
	 * @return
	 */
	public static void connectToStudentInfoLink() {
		String studentInfoURL = baseLink + studentInfoLink + linkAdder;
		try {
			Response response_Sis3_StudentInfoPage = Jsoup.connect(studentInfoURL)
					.method(Method.GET)
					.userAgent("Mozilla/5.0")
					.timeout(10 * 10000)
					.cookies(cookies_Sis1_HomePage)
					.execute();
			doc_Sis3_StudentInfoPage = response_Sis3_StudentInfoPage.parse();
			Elements infoDatas = doc_Sis3_StudentInfoPage.select("td.t10data");
			Elements infoTitles = doc_Sis3_StudentInfoPage.select("th.t10ReportHeader");
			for (Element infoData : infoDatas) {
				listStudentInfo.add(Pair.createPair(" ", infoData.text()));
			}
			int index = 0;
			for (Element infoTitle : infoTitles) {
				listStudentInfo.get(index).setFirst(infoTitle.text());
				index++;
			}
			/*for(int i = 0 ; i < listStudentInfo.size(); i ++) {
		System.out.println(listStudentInfo.get(i).getElement0() +" : " + listStudentInfo.get(i).getElement1());
			}*/
		//	return listStudentInfo;
		} catch (IOException e) {
			System.out.println("Something went wrong while attempting to connect Sis3_StudentInfoPage");
			e.printStackTrace();
		}
	}
	public static void getSelectedSemesterClassesInfo(String semesterName) {
			String studentClasses = baseLink + linkRegisteredClasses + linkAdder;
			if(semesterName.equals("SummerSemester")) {
			changeSemesterToSelected("8356");
			createClassesAndMarks(studentClasses, "SummerSemester");
		}//if ending
			else if (semesterName.equals("SecondSemester")) {
			changeSemesterToSelected("8354");
			createClassesAndMarks(studentClasses, "SecondSemester");
		} //else if ending
			else if (semesterName.equals("FirstSemester")) {
				changeSemesterToSelected("8353");
				createClassesAndMarks(studentClasses, "FirstSemester");
			}
			else if (semesterName.equals("CurrentSemester")) {
				changeSemesterToSelected("8359");
				createClassesAndMarks(studentClasses, "CurrentSemester");
			}
			else 
				createClassesAndMarks(studentClasses, "CurrentSemester");
	}
}
	

