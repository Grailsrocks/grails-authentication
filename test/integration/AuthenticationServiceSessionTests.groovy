import groovy.mock.interceptor.*
import com.grailsrocks.authentication.*

// Tests that test the session data is correct for core service metho
class AuthenticationServiceSessionTests extends GroovyTestCase {

    // Copy this here so we can use it without making the mocks spew
    static USERKEY = AuthenticationService.SESSION_KEY_AUTH_USER

	void testSignupWithData() {
/*
		def service = new AuthenticationService()
		
	    service.signup(login:'marcpalmer', email:'spamtrap@anyware.co.uk', password:'secret' )

		assertTrue controller.session[USERKEY].loggedIn
		assertEquals "marc", controller.session[USERKEY].login
*/
	}
    /*

	void testSignupWithLoginAlreadyTaken() {
		def service = new AuthenticationServic()
		
	    service.signup(login:'marcpalmer', email:'spamtrap@anyware.co.uk', password:'secret' )

		def controller = new AuthenticationController()
	
		def authUser = new AuthenticatedUser(login:'marc', loggedIn:true, 
			result:AuthenticatedUser.ERROR_LOGIN_NAME_NOT_AVAILABLE )
		def mockAuth = new MockFor( AuthenticationService)
		mockAuth.demand.signup(1) { params ->
			return authUser
		}
		
		controller.params.login = 'marcpalmer'
		controller.params.email = 'spamtrap@anyware.co.uk'
		controller.params.password = 'secret'
		controller.params.passwordConfirm = 'secret'
			
		controller.flash.authErrorURL = [url:"/failure"]
		controller.flash.next()
		
		mockAuth.use {
			controller.authenticationService = new AuthenticationService()
			controller.signup()
		}

		assertEquals AuthenticatedUser.ERROR_LOGIN_NAME_NOT_AVAILABLE, controller.flash.authenticationFailure.result
		assertNotNull controller.flash.signupForm
		assertTrue controller.flash.signupForm instanceof SignupForm
		assert controller.response.redirectedUrl == "/failure"
	}
	
	void testSignupWithEmailConfirmationRequired() {
		def controller = new AuthenticationController()
	
		def authUser = new AuthenticatedUser(login:'marc', loggedIn:true, 
			result:AuthenticatedUser.AWAITING_CONFIRMATION )
		def mockAuth = new MockFor( AuthenticationService)
		mockAuth.demand.signup(1) { params ->
			return authUser
		}
		
		def mockForm = new StubFor( SignupForm)
		controller.params.login = 'marcpalmer'
		controller.params.password = 'secret'
		controller.params.passwordConfirm = 'secret'
		controller.params.email = "spamtrap@anyware.co.uk"

		controller.flash.authSuccessURL = [url:"/awaitConf"]
		controller.flash.next()

		mockAuth.use {
			controller.authenticationService = new AuthenticationService()
			controller.signup()
		}

		assertEquals AuthenticatedUser.AWAITING_CONFIRMATION, controller.session[USERKEY].result
		assert controller.response.redirectedUrl == "/awaitConf"
		assertEquals "marc", controller.session[USERKEY].login
	}

	void testLogin() {
		def controller = new AuthenticationController()
		
		def authUser = new AuthenticatedUser(login:'marcpalmer', loggedIn:false, result:0 )

		def mockAuth = new MockFor( AuthenticationService)
		mockAuth.demand.login(1) { login, password ->
		    authUser.loggedIn = true
			return authUser
		}
		
        controller.params.login = "marcpalmer"
        controller.params.password = "secret"
		
		controller.params.success_controller = 'portal'
		controller.params.success_action = 'dashboard'
		controller.params.error_controller = 'invalid'
		controller.params.error_action = 'error'
		
		mockAuth.use {
			controller.authenticationService = new AuthenticationService()
			controller.login()
			assertEquals 0, controller.session[USERKEY].result
			assertTrue controller.session[USERKEY].loggedIn
			assertEquals "marcpalmer", controller.session[USERKEY].login
		}

		assert controller.response.redirectedUrl == "/portal/dashboard"
		assertEquals "marcpalmer", controller.session[USERKEY].login
		assertEquals "marcpalmer", controller.flash.loginForm.login
	}

	void testLoginLogout() {
		def params = [:]
        params.login = "marcpalmer"
        params.password = "secret"
		
		params.success_controller = 'portal'
		params.success_action = 'dashboard'
		params.error_controller = 'invalid'
		params.error_action = 'error'

		def authUser = new AuthenticatedUser(login:'marcpalmer', loggedIn:true, result:0 )

        // Do the logout
		def controller = new AuthenticationController()
		controller.sessio[USERKEY] = authUser
		
		def mockAuth = new MockFor( AuthenticationService)
		mockAuth.demand.logout(1) { user ->
		    println "user is: $user"
		    user.loggedIn = false
		}
	
		controller.params.putAll(params)
		
		mockAuth.use {
			controller.authenticationService = new AuthenticationService()
			controller.logout()
			assertNull controller.session[USERKEY]
			assertFalse authUser.loggedIn
		}
		assert controller.response.redirectedUrl == "/portal/dashboard"
	}
*/
}
