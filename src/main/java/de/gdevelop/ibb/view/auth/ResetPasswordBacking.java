package de.gdevelop.ibb.view.auth;

import static de.gdevelop.ibb.model.LoginToken.TokenType.RESET_PASSWORD;
import static org.omnifaces.util.Faces.getRemoteAddr;
import static org.omnifaces.util.Faces.getRequestURL;
import static org.omnifaces.util.Faces.redirect;
import static org.omnifaces.util.Messages.addFlashGlobalInfo;
import static org.omnifaces.util.Messages.addFlashGlobalWarn;
import static org.omnifaces.util.Messages.addGlobalInfo;

import java.io.IOException;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

import de.gdevelop.ibb.business.service.UserService;
import org.omnifaces.cdi.Param;

@Named
@RequestScoped
public class ResetPasswordBacking extends AuthBacking {

	@Inject @Param
	private String token;

	@Inject
	private UserService userService;

	@Inject
	private Logger logger;

	@Override
	@PostConstruct
	public void init() throws IOException {
		super.init();

		if (token != null && userService.getByLoginToken(token, RESET_PASSWORD) == null) {
			addFlashGlobalWarn("reset_password.message.warn.invalid_token");
			redirect("reset-password");
		}
	}

	public void requestResetPassword() {
		String email = user.getEmail();
		String ipAddress = getRemoteAddr();

		try {
			userService.requestResetPassword(email, ipAddress, getRequestURL() + "?token=%s");
		}
		catch (Exception e) {
			logger.warning(ipAddress + " made a failed attempt to reset password for email " + email + ": " + e);
		}

		addGlobalInfo("reset_password.message.info.email_sent"); // For security, show success message regardless of outcome.
	}

	public void saveNewPassword() throws IOException {
		userService.updatePassword(token, password);
		addFlashGlobalInfo("reset_password.message.info.password_changed");
		redirect("user/profile");
	}

	public String getToken() {
		return token;
	}

}