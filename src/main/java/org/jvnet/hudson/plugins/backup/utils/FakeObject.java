/*
 * The MIT License
 *
 * Copyright (c) 2009-2010, Vincent Sellier
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

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
