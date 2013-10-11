package org.mule.modules.salesforce;

import java.net.Authenticator;
import java.net.PasswordAuthentication;

/**
 * Created with IntelliJ IDEA.
 * User: albin
 * Date: 10/1/13
 * Time: 3:02 PM
 * To change this template use File | Settings | File Templates.
 */
public class ProxyAuthenticator extends Authenticator {

    private String user, password;

    public ProxyAuthenticator(String user, String password) {
        this.user = user;
        this.password = password;
    }

    protected PasswordAuthentication getPasswordAuthentication() {
        return new PasswordAuthentication(user, password.toCharArray());
    }
}
