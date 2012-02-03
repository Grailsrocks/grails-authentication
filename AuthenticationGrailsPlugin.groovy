
class AuthenticationGrailsPlugin {
	def version = '2.0.1'
    def author = "Marc Palmer"
    def authorEmail = "marc@anyware.co.uk"
    def title = "Simple, extensible authentication services with signup support"
    def description = '''
Simple authentication mechanisms with signup and authorisation hooks and DB abstraction, all handled with
events.
'''
    def grailsVersion = "1.1 > *"
    
    def loadAfter = ['logging']
        
    def documentation = 'http://grails.org/plugin/authentication'
    	
	def doWithSpring = {
		// TODO Implement runtime spring config (optional)
	}   
	def doWithApplicationContext = { applicationContext ->
		// TODO Implement post initialization spring config (optional)		
	}
	def doWithDynamicMethods = {
        applicationContext.authenticationService.configChanged()
	}	                                      
    def onChange = { event ->
    }                                                                               
	def onApplicationChange = { event ->
		// TODO Implement code that is executed when any class in a GrailsApplication changes
		// the event contain: event.source, event.application and event.applicationContext objects
	}
    def onConfigChange = { event ->
        applicationContext.authenticationService.configChanged()
    }
}
