package de.gdevelop.ibb.view.admin;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;

import de.gdevelop.ibb.business.service.UserService;
import de.gdevelop.ibb.model.User;
import org.omnifaces.cdi.ViewScoped;
import org.omnifaces.optimusfaces.model.PagedDataModel;

@Named
@ViewScoped
public class UsersBacking implements Serializable {

	private static final long serialVersionUID = 1L;

	private PagedDataModel<User> users;

	@Inject
	private UserService userService;

	@PostConstruct
	public void init() {
		users = PagedDataModel.forClass(User.class).withDefaultSortField("created").build();
	}

	public void delete(User user) {
		userService.delete(user);
	}

	public PagedDataModel<User> getUsers() {
		return users;
	}

}