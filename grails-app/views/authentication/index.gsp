<html>
<body>
	<h1>Authentication</h1>
	<auth:ifLoggedIn>
		You are currently logged in as: <auth:user/>
		<h2>Log out</h2>
		<auth:form authAction="logout" success="[controller:'authentication', action:'index']" error="[controller:'authentication', action:'index']">
		    <g:actionSubmit value="Log out"/> 
		</auth:form>
	</auth:ifLoggedIn>
	<auth:ifUnconfirmed>
		You've registered but we're still waiting to confirm your account. <g:link action="reconfirm">Click here to send a new confirmation request</g:link> if you missed it the first time.
	</auth:ifUnconfirmed>
	<auth:ifNotLoggedIn>
	<g:if test="${flash.authenticationFailure}">
		Sorry but your login/signup failed - reason: <g:message code="authentication.failure.${flash.authenticationFailure.result}"/><br/>
	</g:if>

	<p>You are not logged in. Please log in or sign up:</p>
		<h2>Log in</h2>
		<auth:form authAction="login" success="[controller:'authentication', action:'index']" error="[controller:'authentication', action:'index']">
			    User ID: <g:textField name="login" value="${flash.loginForm?.login?.encodeAsHTML()}"/><br/>
				<g:hasErrors bean="${flash.loginFormErrors}" field="login"><g:renderErrors bean="${flash.loginFormErrors}" as="list" field="login"/></g:hasErrors>
			    Password: <input name="password" value="" type="password"/><br/>
				<g:hasErrors bean="${flash.loginFormErrors}" field="password"><g:renderErrors bean="${flash.loginFormErrors}" as="list" field="password"/></g:hasErrors>
		    <g:actionSubmit value="Log in"/> 
		</auth:form>
	
		<h2>Sign up</h2>
		<auth:form authAction="signup"
			success="[controller:'authentication', action:'index']"
			error="[controller:'authentication', action:'index']">
		    User ID: <g:textField name="login" value="${flash.signupForm?.login?.encodeAsHTML()}"/><br/>
			<g:hasErrors bean="${flash.signupFormErrors}" field="login"><g:renderErrors bean="${flash.signupFormErrors}" as="list" field="login"/></g:hasErrors>
		    Email: <g:textField name="email" value="${flash.signupForm?.email?.encodeAsHTML()}"/><br/>
			<g:hasErrors bean="${flash.signupFormErrors}" field="email"><g:renderErrors bean="${flash.signupFormErrors}" as="list" field="email"/></g:hasErrors>
		    Password: <input name="password" value="" type="password"/><br/>
			<g:hasErrors bean="${flash.signupFormErrors}" field="password"><g:renderErrors bean="${flash.signupFormErrors}" as="list" field="password"/></g:hasErrors>
		    Confirm password: <input name="passwordConfirm" value="" type="password"/><br/>
			<g:hasErrors bean="${flash.signupFormErrors}" field="passwordConfirm"><g:renderErrors bean="${flash.signupFormErrors}" as="list" field="passwordConfirm"/></g:hasErrors>
		    <g:actionSubmit value="Sign up"/>
		</auth:form>
	</auth:ifNotLoggedIn>
</body>
</html>