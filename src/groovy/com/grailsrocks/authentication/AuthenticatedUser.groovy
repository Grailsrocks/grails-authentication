package com.grailsrocks.authentication

/**
 * Class encapsulating the result of a login. Encapsulates errors also so that
 * login failures to not use exceptions (very bad for performance and they are not ... exceptional)
 */
class AuthenticatedUser implements Serializable {
	static final ERROR_NO_SUCH_LOGIN = 1
	static final ERROR_INCORRECT_CREDENTIALS = 2
	static final ERROR_LOGIN_NAME_NOT_AVAILABLE = 3
	static final AWAITING_CONFIRMATION = 4
	
	String login
    
    /**
     * Date/time at which this user logged in
     */
	Date loginTime

	/**
	 * Is the user currently logged in
	 */
	boolean loggedIn
	
	/**
	 * True if email address has been confirmed to work
	 */
	boolean confirmed

	/**
	 * The domain object id of the user principal object for this account
	 */
	def userObjectId
	
	/**
	 * Any application-specific attributes you wish to store for this session can go here
	 */
	def attributes = [:]
	
	/**
	 * The result code for the outcome of the last login attempt
	 */
	int result
}