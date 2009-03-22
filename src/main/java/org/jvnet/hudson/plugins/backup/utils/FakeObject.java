package org.jvnet.hudson.plugins.backup.utils;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import javax.servlet.ServletContext;

import org.kohsuke.stapler.StaplerRequest;
import org.kohsuke.stapler.StaplerResponse;

public class FakeObject {

	public static StaplerResponse getStaplerResponseFake() {
		return (StaplerResponse) Proxy.newProxyInstance(Thread.currentThread()
				.getContextClassLoader(),
				new Class[] { StaplerResponse.class }, new InvocationHandler() {
					public Object invoke(Object proxy, Method method,
							Object[] args) throws Throwable {
						return null;
					}
				});
	}

	public static StaplerRequest getStaplerRequestFake(
			ServletContext servletContext) {
		InvocationHandler handler = new StaplerRequestInvocationHandler(servletContext);

		return (StaplerRequest) Proxy.newProxyInstance(Thread.currentThread()
				.getContextClassLoader(), new Class[] { StaplerRequest.class },
				handler);
	}
	
	private static class StaplerRequestInvocationHandler implements InvocationHandler {
			public ServletContext servletContext;

			public StaplerRequestInvocationHandler(ServletContext servletContext) {
				this.servletContext = servletContext;
			}
			
			public Object invoke(Object proxy, Method method, Object[] args)
					throws Throwable {
				if (method.getName().equals("getContextPath")) {
					return "";
				} else if (method.getName().equals("getServletContext")) {
					return servletContext;
				} else {
					return null;
				}
			}
		
	}
	
}
