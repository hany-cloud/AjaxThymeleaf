package net.hka.examples.thymeleaf.web.controller;

import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import net.hka.common.web.servlet.mvc.support.MessageHelper;
import net.hka.common.web.servlet.util.AjaxUtils;
import net.hka.examples.thymeleaf.business.domain.UserRole;
import net.hka.examples.thymeleaf.business.service.AccountService;
import net.hka.examples.thymeleaf.web.dto.AccountDto;

@Controller
class SignupController {

	private static final String SIGNUP_VIEW_NAME = "signup/signup";
	
	// link signup messages by message resource
	private static final String SIGNUP_SUCCESS_MESSAGE = "signup.success";
	
	private static final String SIGNUP_EMAIL_EXIST_MESSAGE = "signup.email.in.use";
	

	//@Autowired
	private AccountService accountService;
	
	public SignupController(AccountService accountService) {
		super();
		this.accountService = accountService;
	}

	@GetMapping("signup")
	String signup(Model model, @RequestHeader(value = "X-Requested-With", required = false) String requestedWith) {
		
		model.addAttribute(new AccountDto());
		if (AjaxUtils.isAjaxRequest(requestedWith)) {
			return SIGNUP_VIEW_NAME.concat(" :: signupForm");
		}
		return SIGNUP_VIEW_NAME;
	}

	@PostMapping("signup")
	String signup(@Valid @ModelAttribute AccountDto accountDto, Errors errors, RedirectAttributes ra) {
		
		if (errors.hasErrors()) {
			return SIGNUP_VIEW_NAME;
		}
		
		// save account with role user into database
		AccountDto savedAccountDto = accountService.save(accountDto, UserRole.USER.getValue());
		
		// data is saved
		if(savedAccountDto.getId() != null) {
			accountService.signin(accountDto);
	        // see messages.properties and homeSignedIn.html
	        MessageHelper.addSuccessAttribute(ra, SIGNUP_SUCCESS_MESSAGE);
			return "redirect:/";
		} 
		
		// account is already exist
		errors.rejectValue("email", SIGNUP_EMAIL_EXIST_MESSAGE, "Sorry! the email is already used");
		return SIGNUP_VIEW_NAME;
	}
}
