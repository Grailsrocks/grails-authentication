import org.codehaus.groovy.grails.web.servlet.GrailsApplicationAttributes
import grails.util.GrailsWebUtil
import com.grailsrocks.authentication.*

class AuthenticationTagLibTests extends GroovyTestCase {

	void testAuthForm() {
		def tag = new AuthenticationTagLib()
		
		def result = tag.form([authAction:"login", success:[controller:'good', action:'loggedIn'],
		    error:[controller:'bad', action:'failed']]) {
		        //textField([name:"test", value:"dummy"])
		}
		    
		def output = result
		
		println output

		assertTrue output.contains("<form")
		assertTrue output.contains("action=\"/authentication/login\"")

		assertTrue output.contains("name=\"success_controller\" value=\"good\"")
		assertTrue output.contains("name=\"success_action\" value=\"loggedIn\"")
		assertTrue output.contains("name=\"error_controller\" value=\"bad\"")
		assertTrue output.contains("name=\"error_action\" value=\"failed\"")

//		assertTrue result.contains("name=\"test\" value=\"dummy\"")
	}

    void testAuthFormDefaultingSuccessErrorParams() {
		def tag = new AuthenticationTagLib()

        def webreq = GrailsWebUtil.bindMockWebRequest()
        webreq.request[GrailsApplicationAttributes.CONTROLLER_NAME_ATTRIBUTE] = "test"
        webreq.request[GrailsApplicationAttributes.ACTION_NAME_ATTRIBUTE] = "hello"
    
		def result = tag.form([authAction:"login"]) {
		}

		def output = result

		println output

		assertTrue output.contains("<form")
		assertTrue output.contains("action=\"/authentication/login\"")

		assertTrue output.contains("name=\"success_controller\" value=\"test\"")
		assertTrue output.contains("name=\"success_action\" value=\"hello\"")
		assertTrue output.contains("name=\"error_controller\" value=\"test\"")
		assertTrue output.contains("name=\"error_action\" value=\"hello\"")

//		assertTrue result.contains("name=\"test\" value=\"dummy\"")
	}


    void testAuthFormDefaultingActionOnlyIfDefaultingController() {
		def tag = new AuthenticationTagLib()

        def webreq = GrailsWebUtil.bindMockWebRequest()
        webreq.request[GrailsApplicationAttributes.CONTROLLER_NAME_ATTRIBUTE] = "test"
        webreq.request[GrailsApplicationAttributes.ACTION_NAME_ATTRIBUTE] = "hello"

		def result = tag.form([authAction:"login", success:[controller:'xxx']]) {
		}

		def output = result

		println output

		assertTrue output.contains("<form")
		assertTrue output.contains("action=\"/authentication/login\"")

		assertTrue output.contains("name=\"success_controller\" value=\"xxx\"")
		assertFalse output.contains("name=\"success_action\" value=\"hello\"")
		assertTrue output.contains("name=\"error_controller\" value=\"test\"")
		assertTrue output.contains("name=\"error_action\" value=\"hello\"")

//		assertTrue result.contains("name=\"test\" value=\"dummy\"")
	}

	void testLogoutLink() {
		def tag = new AuthenticationTagLib()

		def result = tag.logoutLink([success:[controller:'good', action:'loggedIn']]) {
		}
		    
		def output = result
		
		assertTrue output.contains('/authentication/logout?')
		assertTrue output.contains('success_controller=good')
		assertTrue output.contains('success_action=loggedIn')
    }

	void testLogoutLinkPartialAttribs() {
		def tag = new AuthenticationTagLib()
		
		
        def webreq = GrailsWebUtil.bindMockWebRequest()
        webreq.request[GrailsApplicationAttributes.CONTROLLER_NAME_ATTRIBUTE] = "test"
        webreq.request[GrailsApplicationAttributes.ACTION_NAME_ATTRIBUTE] = "hello"

		def result = tag.logoutLink([success:[action:'loggedIn']]) {
		}
		    
		def output = result
		
		assertTrue output.contains('/authentication/logout?')
		assertTrue output.contains('success_controller=test')
		assertTrue output.contains('success_action=loggedIn')
    }
    
	void testLogoutLinkPartialAttribsControllerOnly() {
		def tag = new AuthenticationTagLib()
		
		
        def webreq = GrailsWebUtil.bindMockWebRequest()
        webreq.request[GrailsApplicationAttributes.CONTROLLER_NAME_ATTRIBUTE] = "test"
        webreq.request[GrailsApplicationAttributes.ACTION_NAME_ATTRIBUTE] = "hello"

		def result = tag.logoutLink([success:[controller:'xxxx']]) {
		}
		    
		def output = result
		
		assertTrue output.contains('/authentication/logout?')
		assertTrue output.contains('success_controller=xxxx')
		assertFalse output.contains('success_action=hello')
    }

}
