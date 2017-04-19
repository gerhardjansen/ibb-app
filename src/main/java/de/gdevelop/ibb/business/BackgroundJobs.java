package de.gdevelop.ibb.business;

import javax.ejb.Schedule;
import javax.ejb.Singleton;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import de.gdevelop.ibb.business.service.LoginTokenService;

@Singleton
public class BackgroundJobs {

	@PersistenceContext
	private EntityManager entityManager;

	@Inject
	private LoginTokenService loginTokenService;

	@Schedule(dayOfWeek = "*", hour = "*", minute = "0", second = "0", persistent = false)
	public void hourly() {
		loginTokenService.removeExpired();
	}

}