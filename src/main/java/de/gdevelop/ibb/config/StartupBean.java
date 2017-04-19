package de.gdevelop.ibb.config;

import static java.text.MessageFormat.format;
import static java.util.Arrays.asList;
import static java.util.ResourceBundle.getBundle;
import static de.gdevelop.ibb.model.Group.ADMIN;
import static de.gdevelop.ibb.model.Group.USER;
import static org.omnifaces.util.Faces.getLocale;
import static org.omnifaces.utils.Lang.isEmpty;

import java.util.ResourceBundle;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import de.gdevelop.ibb.business.exception.InvalidUsernameException;
import de.gdevelop.ibb.business.service.UserService;
import de.gdevelop.ibb.model.User;
import org.omnifaces.cdi.Startup;
import org.omnifaces.util.Messages;

@Startup
public class StartupBean {

	@Inject
	private UserService userService;

	@PostConstruct
	public void init() {
		setupTestUsers();
		configureMessageResolver();
	}

	private void setupTestUsers() {
	 	try {
			userService.getByEmailAndPassword("admin@ibb-d.de", "passw0rd");
		}
	 	catch (InvalidUsernameException e) {
			User user = new User();
			user.setFullName("Test Admin");
			user.setEmail("admin@ibb-d.de");
			user.setGroups(asList(ADMIN, USER));
			userService.registerUser(user, "passw0rd");
		}

		try {
			userService.getByEmailAndPassword("user@ibb-d.de", "passw0rd");
		}
		catch (InvalidUsernameException e) {
			User user = new User();
			user.setFullName("Test User");
			user.setEmail("user@ibb-d.de");
			user.setGroups(asList(USER));
			userService.registerUser(user, "passw0rd");
		}
	}

	private void configureMessageResolver() {
		Messages.setResolver(new Messages.Resolver() {
			private static final String BASE_NAME = "de.gdevelop.ibb.i18n.ApplicationBundle";

			@Override
			public String getMessage(String message, Object... params) {
				ResourceBundle bundle = getBundle(BASE_NAME, getLocale());
				if (bundle.containsKey(message)) {
					message = bundle.getString(message);
				}
				return isEmpty(params) ? message : format(message, params);
			}
		});
	}

}