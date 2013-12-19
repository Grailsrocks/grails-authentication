package com.grailsrocks.authentication

import grails.util.GrailsUtil
import org.codehaus.groovy.grails.commons.GrailsApplication

import com.grailsrocks.authentication.AuthenticatedUser

class AuthenticationController {

	def defaultAction = "index"
	
	def authenticationService

    /**
     * Extract success_* and error_* into maps that can be passed to redirect(),
     * but forbidding the use of "url" which could lead to XSS attacks or phishing
     */
    def extractParams() {
        def redirectParams = [success:[:], error:[:]]
        params.keySet().each() { name -> 
            if (name.startsWith("success_") || name.startsWith('error_')) {
                def underscore = name.indexOf('_')
                if (underscore >= name.size()-1) return
                def prefix = name[0..underscore-1]
                def urlParam = name[underscore+1..-1]
                if (urlParam != 'url') {
                    redirectParams[prefix][urlParam] = params[name]
                }
            }
        }
        return redirectParams
    }

    def index = {
        // Display a page offering the appropriate options by default, but only in development!
        if (GrailsUtil.environment != GrailsApplication.ENV_DEVELOPMENT) {
            response.sendError(404)
        }
    }
    
	def login = { LoginForm form ->
	    def urls = extractParams()
		if (!form.hasErrors()) {
			def loginResult = authenticationService.login( form.login, form.password)
			if (loginResult.result == 0) {
				flash.loginForm = form
				if (log.debugEnabled) log.debug("Login succeeded for [${form.login}]")
				redirect(flash.authSuccessURL ? flash.authSuccessURL : urls.success)
			} else {                  
				flash.loginForm = form
				flash.authenticationFailure = loginResult
				if (log.debugEnabled) log.debug("Login failed for [${form.login}] - reason: ${loginResult.result}")
				redirect(flash.authFailureURL ? flash.authFailureURL : urls.error)
			}
		} else {
			flash.loginForm = form
			flash.loginFormErrors = form.errors // Workaround for grails bug 
			if (log.debugEnabled) log.debug("Login failed for [${form.login}] - form invalid: ${form.errors}")
			redirect(flash.authErrorURL ? flash.authErrorURL : urls.error)
		}
	}

	def signup = { SignupForm form ->
	    def urls = extractParams()
		if (!form.hasErrors()) {
			def signupResult = authenticationService.signup( login:form.login, 
				password:form.password, email:form.email, immediate:true, extraParams:params)
			if ((signupResult.result == 0) || (signupResult.result == AuthenticatedUser.AWAITING_CONFIRMATION)) {
				if (log.debugEnabled) {
					if (signupResult == AuthenticatedUser.AWAITING_CONFIRMATION) {
						log.debug("Signup succeeded pending email confirmation for [${form.login}] / [${form.email}]")
					} else {
						log.debug("Signup succeeded for [${form.login}]")
					}
				}
				redirect(flash.authSuccessURL ? flash.authSuccessURL : urls.success)
			} else {
				flash.authenticationFailure = signupResult
				flash.signupForm = form
				if (log.debugEnabled) log.debug("Signup failed for [${form.login}] reason ${signupResult.result}")
				redirect(flash.authErrorURL ? flash.authErrorURL : urls.error)
			}
		} else {
			flash.signupForm = form
			flash.signupFormErrors = form.errors // Workaround for grails bug in 0.5.6
			if (log.debugEnabled) log.debug("Signup failed for [${form.login}] - form invalid: ${form.errors}")
			redirect(flash.authErrorURL ? flash.authErrorURL : urls.error)
		}		
	}

	def logout = { 
	    def urls = extractParams() 
		authenticationService.logout( authenticationService.sessionUser )
		redirect(flash.authSuccessURL ? flash.authSuccessURL : urls.success)
	}
}

class LoginForm {

	String login
	String password
	
	boolean rememberMe
	
	static constraints = {
		login(size:4..40, nullable: false, blank:false)
		password(size:6..40, password:true, nullable: false, blank:false)
	}

}

class SignupForm {

	String login
	String email
	String password
	String passwordConfirm
	
	boolean rememberMe
	
	static constraints = {
		login(size:6..40, blank:false, nullable: false)
		email(email:true, size:6..40, blank:false, nullable: false)
		password(size:6..40, password:true, blank:false, nullable: false)
		passwordConfirm(password:true, validator: { val, obj -> obj.password == val })
	}

}