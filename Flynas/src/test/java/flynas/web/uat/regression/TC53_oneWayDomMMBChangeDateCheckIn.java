package flynas.web.uat.regression;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.ctaf.accelerators.TestEngine;
import com.ctaf.support.ExcelReader;
import com.ctaf.support.HtmlReportSupport;
import com.ctaf.utilities.Reporter;

import flynas.web.testObjects.BookingPageLocators;
import flynas.web.workflows.BookingPageFlow;

public class TC53_oneWayDomMMBChangeDateCheckIn extends BookingPageFlow{
	
	ExcelReader xls = new ExcelReader(configProps.getProperty("TestDataIBEUAT"),"FL_WEB_45");

	@Test(dataProvider = "testData",groups={"Chrome"})
	public  void TC_53_oneWayDomMMBChangeDateCheckIn(String tripType, 
			String origin, String dest,String deptDate, String origin2,String departure2,
			String retdate,String Adult,String Child,String infant, String promo, 
			String strBookingClass, String bundle,String FlightType,String totalpass, String nationality,
			String Doctypr,String docNumber,String naSmiles,String Mobile,
			String email ,String SelectSeat,String paymenttype,String bookingtype, 
			String charity,String Currency, String newDate,String Description
			) throws Throwable {
		try {
			//System.out.println(paymenttype);
			TestEngine.testDescription.put(HtmlReportSupport.tc_name, Description);
			String	deptdate = pickDate(deptDate);
			String	retrndate = pickDate(retdate);
			

			String[] Credentials = pickCredentials("UserCredentials");
			String username =Credentials[0];
			String password =Credentials[1];
			String lastname =Credentials[3];
			
			click(BookingPageLocators.login_lnk, "Login");
			switchtoChildWindow();
			login(username,password);
					
			inputBookingDetails(tripType, origin, dest, deptdate, origin2, departure2, retrndate,Adult, Child, infant,promo,Currency,paymenttype);
			selectClass(strBookingClass, bundle);
			clickContinueBtn();
					
			//Clicking continue button on Passenger details page
			continueOnPassengerDetails();
			
			//Clicking continue button on Baggage details page
			coninueOnBaggage();
			
			selectSeat(SelectSeat, bookingtype);
			payment(paymenttype,"");
			String strPNR = getReferenceNumber();
			System.out.println(strPNR);
			validate_ticketStatus(strPNR);
			
			
			//Verifying PNR numbers
			String	newdate = pickDate(newDate);
			String strPNRChangeDate = changeDate(strPNR, username, "", lastname, newdate, SelectSeat,totalpass,strBookingClass,0);
			
			//Reporting the test case status
			if(strPNRChangeDate.trim().equalsIgnoreCase(strPNR)){
				Reporter.SuccessReport("Change Flight Date", "Flight Date has changed successfully");
			}else{
				Reporter.failureReport("Change Flight Date", "Flight Date has NOT changed successfully");
			}
			
			searchFlightCheckin(strPNR, "", "", lastname);
			performCheckin(SelectSeat,paymenttype,totalpass);
			validateCheckin();
			
			updateStatus("IBE_UAT_Reg","TC53_oneWayDomMMBChangeDateCheckIn","Pass");
			Reporter.SuccessReport("TC53_oneWayDomMMBChangeDateCheckIn", "Pass");
			}
		
	catch (Exception e) {
			e.printStackTrace();
			updateStatus("IBE_UAT_Reg","TC53_oneWayDomMMBChangeDateCheckIn","Fail");
			Reporter.failureReport("TC53_oneWayDomMMBChangeDateCheckIn", "Failed");
		}
	}
	
	@DataProvider(name="testData")
	public Object[][] createdata1() {
	    return (Object[][]) new Object[][] { 
	    		{
	    			
		    		xls.getCellValue("Trip Type", "Value"),
		    		xls.getCellValue("Origin", "Value"),
		    		xls.getCellValue("Destination", "Value"),
		    		xls.getCellValue("Departure Date", "Value9"),
		    		"",
		    		"",
		    		xls.getCellValue("Return Date", "Value9"),
		    		xls.getCellValue("Adults Count", "Value"),
		    		xls.getCellValue("Child Count", "Value"),
		    		xls.getCellValue("Infant Count", "Value"),
		    		xls.getCellValue("Promo", "Value"),
		    		xls.getCellValue("Booking Class", "Value3"),
		    		xls.getCellValue("Bundle", "Value2"),
		    		xls.getCellValue("Flight Type", "Value"),
		    		xls.getCellValue("Total Passenger", "Value"),
		    		xls.getCellValue("Nationality", "Value"),
		    		xls.getCellValue("Document Type", "Value"),
		    		xls.getCellValue("Doc Number", "Value"),
		    		xls.getCellValue("na Smiles", "Value"),
		    		xls.getCellValue("Mobile", "Value"),
		    		xls.getCellValue("Email Address", "Value"),
		    		xls.getCellValue("Select Seat", "Value"),
		    		xls.getCellValue("Payment Type", "Value"),
		    		"",
	    			xls.getCellValue("Charity Donation", "Value"),
	    			xls.getCellValue("Currency", "Value"),
	    			xls.getCellValue("NewDate", "Value"),
		    		"Verify One way business class Domestic  booking, change date and cancellation"
    			}
	    	};
	}

}