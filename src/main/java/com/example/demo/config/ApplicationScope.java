package com.example.demo.config;

import javax.servlet.ServletContext;

import org.springframework.stereotype.Component;
import org.springframework.web.context.ServletContextAware;

@Component
//アプリケーションスコープの情報を扱う為にServletContextAwareを実装
public class ApplicationScope implements ServletContextAware {
	
	private static ServletContext servletContext;

//	getServletContextをオーバーライドすることでServletContextオブジェクトにアクセスできる
	@Override
	public void setServletContext(ServletContext servletContext) {
		ApplicationScope.servletContext = servletContext;
	}
	
	public static void setAttribute(String name, Object value) {
		servletContext.setAttribute(name, value);
	}

	public static Object getAttribute(String name) {
		return servletContext.getAttribute(name);
	}
	
	public static void removeAttribute(String name) {
		servletContext.removeAttribute(name);
	}
}
