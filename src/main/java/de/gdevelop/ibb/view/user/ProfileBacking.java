package de.gdevelop.ibb.view.user;

import static org.omnifaces.util.Messages.addGlobalInfo;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.validation.constraints.NotNull;

import de.gdevelop.ibb.business.service.UserService;
import de.gdevelop.ibb.model.User;
import de.gdevelop.ibb.model.validator.Password;
import de.gdevelop.ibb.view.ActiveUser;

@Named
@RequestScoped
public class ProfileBacking {

	private User user;
	private @NotNull @Password String currentPassword;
	private @NotNull @Password String newPassword;

	@Inject
	private ActiveUser activeUser;

	@Inject
	private UserService userService;

	@PostConstruct
	public void init() {
		user = userService.getById(activeUser.getId());
	}

	public void save() {
		userService.update(user);
		addGlobalInfo("user_profile.message.info.account_updated");
	}

	public void changePassword() {
		userService.updatePassword(user, newPassword);
		addGlobalInfo("user_profile.message.info.password_changed");
	}

	public User getUser() {
		return user;
	}

	public String getCurrentPassword() {
		return currentPassword;
	}

	public void setCurrentPassword(String currentPassword) {
		this.currentPassword = currentPassword;
	}

	public String getNewPassword() {
		return newPassword;
	}

	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}

}