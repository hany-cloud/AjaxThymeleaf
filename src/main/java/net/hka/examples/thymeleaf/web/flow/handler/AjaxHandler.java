package net.hka.examples.thymeleaf.web.flow.handler;

import org.springframework.stereotype.Component;

import net.hka.examples.thymeleaf.web.flow.model.AjaxModel;

@Component
public class AjaxHandler {

	public AjaxModel init() {
		return new AjaxModel();
	}

}
