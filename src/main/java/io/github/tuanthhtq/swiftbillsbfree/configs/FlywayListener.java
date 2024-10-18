package io.github.tuanthhtq.swiftbillsbfree.configs;

import org.flywaydb.core.Flyway;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

/**
 * @author io.github.tuanthhtq
 */

@Component
public class FlywayListener implements ApplicationListener<ApplicationReadyEvent> {

	@Value("${spring.datasource.url}")
	private String dbUrl;

	@Value("${spring.datasource.username}")
	private String username;

	@Value("${spring.datasource.password}")
	private String password;

	/**
	 * Handle an application event.
	 *
	 * @param event the event to respond to
	 */
	@Override
	public void onApplicationEvent(ApplicationReadyEvent event) {
		Flyway.configure().baselineOnMigrate(true).baselineVersion("0").dataSource(dbUrl, username, password).load().migrate();
	}
}
