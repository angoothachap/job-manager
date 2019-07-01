package com.frontend.jobmanger.controller;

import javax.annotation.Resource;
import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import com.frontend.jobmanager.service.InMemoryUserService;
import models.User;
import models.UserEmploymentState;
import models.UserSexState;

@Controller
public class UserAccountOfficeController
{
	@Resource
	private InMemoryUserService inMemUserService;
   
	private User actualUserWhichIslogedIn;
	
	@GetMapping(value = { "/memberarea" })
	@ResponseStatus(code = HttpStatus.FORBIDDEN)
	public String notAuthAccessToMemberArea()
	{

		return "restrictedAccess";
	}

	@GetMapping(value = { "/memberarea/userAccountOffice/" })
	public String authUserAccessToOfficeMemberArea(@RequestParam String uName,
			@Valid @ModelAttribute User currentLogedUser, BindingResult bindingResult, Model userModel)
	{
		String pathToUserAccountOffice = "restrictedAccess";

		caseForRunningUnitTestsWhenInMemServiceNotAvailable();	
		actualUserWhichIslogedIn = inMemUserService.findUserByNickname(uName);
		if(actualUserWhichIslogedIn != null) {
			currentLogedUser = actualUserWhichIslogedIn;
			userModel.addAttribute("userLoginName",uName);
		}
		pathToUserAccountOffice = validateThatGivenUserReallyRegistered(uName, pathToUserAccountOffice);

		return pathToUserAccountOffice;
	}

	private String validateThatGivenUserReallyRegistered(String uName, String pathToUserAccountOffice)
	{
		if (actualUserWhichIslogedIn != null)
		{
			if(actualUserWhichIslogedIn.getUserNickName().equals(uName) ) {
			  pathToUserAccountOffice = "memberarea/userAccountOffice";
			}
		}
		return pathToUserAccountOffice;
	}

	private void caseForRunningUnitTestsWhenInMemServiceNotAvailable()
	{
		if (inMemUserService == null)
		{
			inMemUserService = new InMemoryUserService();
			createTestUserAndSaveItInRepoForTestPurposeOnly();
		}
	}
	
	private void createTestUserAndSaveItInRepoForTestPurposeOnly()
	{
		User testUser = new User();
		testUser.setUserFirstName("Thomas");
		testUser.setUserLastName("Jefferson");
		testUser.setUserBirthDate("01.12.1978");
		testUser.setUserEmail("cooldude@io.com");
		testUser.setUserCity("Detroit");
		testUser.setUserStreetName("BerlinerStreet");
		testUser.setUserStreetNumber(123);
		testUser.setUserCountryName("USA");
		testUser.setUserNickName("testdude000");
		testUser.setCurrentUserSexState(UserSexState.MALE);
		testUser.setCurrentEmploymentState(UserEmploymentState.SELFEMPLOYED);
		String pass = "tuxtux123456";
		inMemUserService.saveNewUserWithRandomPass(testUser, pass);
	}

}
