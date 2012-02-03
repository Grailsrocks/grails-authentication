import groovy.mock.interceptor.*
import com.grailsrocks.authentication.*
import grails.util.GrailsWebUtil
import org.springframework.mock.web.*
import org.codehaus.groovy.grails.web.servlet.GrailsApplicationAttributes

class AuthenticationServiceFilteringTests extends GroovyTestCase {
	

    // Copy this here so we can use it without making the mocks spew
    static USERKEY = AuthenticationService.SESSION_KEY_AUTH_USER

	void testFilterRequestNotLoggedIn() {
		def service = new AuthenticationService()

	    def request = GrailsWebUtil.bindMockWebRequest()
	    println "mock req is: ${request.currentRequest}"
	    println "mock resp is: ${request.currentResponse}"
	    request.currentRequest.setAttribute(GrailsApplicationAttributes.CONTROLLER_NAME_ATTRIBUTE, 'test')
	    request.currentRequest.setAttribute(GrailsApplicationAttributes.ACTION_NAME_ATTRIBUTE, 'index')

        assertFalse service.filterRequest(request.currentRequest, request.currentResponse, '/authentication/login')
        
        assertEquals '/authentication/login', request.currentResponse.redirectedUrl
	}

	void testFilterRequestLoggedInNotAuth() {
		def service = new AuthenticationService()

        def dummyuser = new AuthenticatedUser(login:'marcpalmer', loggedIn: true)
	    def request = GrailsWebUtil.bindMockWebRequest()
	    request.session[USERKEY] = dummyuser
	    request.currentRequest.setAttribute(GrailsApplicationAttributes.CONTROLLER_NAME_ATTRIBUTE, 'test')
	    request.currentRequest.setAttribute(GrailsApplicationAttributes.ACTION_NAME_ATTRIBUTE, 'index')
	    	    
		def mockEvents = new MockFor(DummyEventHandler)
		mockEvents.demand.onCheckAuthorized(1) { params -> 
			assert params.user == dummyuser
			assert params.request == request.currentRequest
			assert params.controllerName == 'test'
			assert params.actionName == 'index'
			return false
		}
		mockEvents.demand.onUnauthorizedAccess(1) { params -> 
			assert params.response == request.currentResponse
			assert params.request == request.currentRequest
			request.currentResponse.sendError(403)
		}

		mockEvents.use {
			service.events = new DummyEventHandler()

			assertFalse service.filterRequest(request.currentRequest, request.currentResponse, '/authentication/login')
            assertEquals 403, request.currentResponse.status
		}
	}
	
	void testFilterRequestLoggedInAuthOK() {
		def service = new AuthenticationService()

        def dummyuser = new AuthenticatedUser(login:'marcpalmer', loggedIn: true)
	    def request = GrailsWebUtil.bindMockWebRequest()

	    request.session[USERKEY] = dummyuser
	    request.currentRequest.setAttribute(GrailsApplicationAttributes.CONTROLLER_NAME_ATTRIBUTE, 'test')
	    request.currentRequest.setAttribute(GrailsApplicationAttributes.ACTION_NAME_ATTRIBUTE, 'index')
	    
		def mockEvents = new MockFor(DummyEventHandler)
		mockEvents.demand.onCheckAuthorized(1) { params -> 
			assert params.user == dummyuser
			assert params.request == request.currentRequest
			assert params.controllerName == 'test'
			assert params.actionName == 'index'
			return true
		}

		mockEvents.use {
			service.events = new DummyEventHandler()

			assertTrue service.filterRequest(request.currentRequest, request.currentResponse, '/authentication/login')
		}
	}    

	void testFilterRequestLoggedInAuthOKThreadLocalRequestResponse() {
		def service = new AuthenticationService()

        def dummyuser = new AuthenticatedUser(login:'marcpalmer', loggedIn: true)
	    def request = GrailsWebUtil.bindMockWebRequest()

	    request.session[USERKEY] = dummyuser
	    request.currentRequest.setAttribute(GrailsApplicationAttributes.CONTROLLER_NAME_ATTRIBUTE, 'test')
	    request.currentRequest.setAttribute(GrailsApplicationAttributes.ACTION_NAME_ATTRIBUTE, 'index')
	    
		def mockEvents = new MockFor(DummyEventHandler)
		mockEvents.demand.onCheckAuthorized(1) { params -> 
			assert params.user == dummyuser
			assert params.request == request.currentRequest
			assert params.controllerName == 'test'
			assert params.actionName == 'index'
			return true
		}

		mockEvents.use {
			service.events = new DummyEventHandler()

			assertTrue service.filterRequest('/authentication/login')
		}
	}	
	
	void testRequireAuthorizationLoggedInAuthOKThreadLocalRequestResponse() {
		def service = new AuthenticationService()

        def dummyuser = new AuthenticatedUser(login:'marcpalmer', loggedIn: true)
	    def request = GrailsWebUtil.bindMockWebRequest()

	    request.session[USERKEY] = dummyuser
	    request.currentRequest.setAttribute(GrailsApplicationAttributes.CONTROLLER_NAME_ATTRIBUTE, 'test')
	    request.currentRequest.setAttribute(GrailsApplicationAttributes.ACTION_NAME_ATTRIBUTE, 'index')
	    
		def mockEvents = new MockFor(DummyEventHandler)
		mockEvents.demand.onHasAuthorization(1) { params -> 
			assert params.user == dummyuser
			assert params.request == request.currentRequest
			assert params.controllerName == 'test'
			assert params.actionName == 'index'
			assert params.requirement == 'superuser'
			return true
		}

		mockEvents.use {
			service.events = new DummyEventHandler()

			assertTrue service.requireAuthorization('superuser', '/authentication/login')
		}
	}	
}