package de.gdevelop.ibb.model.producer;

import static java.time.Instant.now;
import static org.omnifaces.util.Beans.getManager;
import static org.omnifaces.util.BeansLocal.destroy;

import java.io.Serializable;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.context.SessionScoped;
import javax.enterprise.event.Observes;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.inject.Named;

import de.gdevelop.ibb.business.service.UserService;
import de.gdevelop.ibb.model.User;
import de.gdevelop.ibb.view.ActiveUser;
import org.omnifaces.security.AuthenticatedEvent;
import org.omnifaces.security.LoggedOutEvent;

@SessionScoped
public class ActiveUserProducer implements Serializable {

	private static final long serialVersionUID = 1L;

	private User activeUser;

	@Inject
	private UserService userService;

	@Produces @Named @RequestScoped
	public ActiveUser getActiveUser() {
		return new ActiveUser(activeUser);
	}

	public void onAuthenticated(@Observes AuthenticatedEvent event) {
		if (activeUser == null || !event.getUserPrincipal().getName().equals(activeUser.getEmail())) {
			activeUser = userService.getActiveUser();

			if (activeUser != null) {
				activeUser.setLastLogin(now());
				userService.update(activeUser);
			}

			refreshInjectedActiveUsers();
		}
	}

	public void onLoggedOut(@Observes LoggedOutEvent event) {
		activeUser = null;
		refreshInjectedActiveUsers();
	}

	private void refreshInjectedActiveUsers() {
		destroy(getManager(), ActiveUser.class);
	}

}