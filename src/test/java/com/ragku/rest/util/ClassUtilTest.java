package com.ragku.rest.util;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Set;

import com.ragku.rest.WebContext;
import com.ragku.rest.annotation.Controller;
import com.ragku.rest.annotation.DeleteMapping;
import com.ragku.rest.annotation.GetMapping;
import com.ragku.rest.annotation.PostMapping;
import com.ragku.rest.annotation.PutMapping;

public class ClassUtilTest {

	public static void main(String[] args) throws ClassNotFoundException, IOException, InstantiationException, IllegalAccessException {
		WebContext wc = new WebContext();
		Set<Class<?>> classes = ClassScanUtil.listClasses("com.ragku");
		for(Class<?> a : classes) {
			if(null == a.getAnnotation(Controller.class)) {
				continue;
			}
			wc.addClass(a.getName(), a);
			for(Method m : a.getMethods()) {
				String[] paramNames = MethodUtil.getMethodParamNames(m);
				PostMapping pm = m.getAnnotation(PostMapping.class);
				if(null != pm) {
					wc.addMethod(pm.value(), a.newInstance(), m, paramNames);
				}
				PutMapping putm = m.getAnnotation(PutMapping.class);
				if(null != putm) {
					wc.addMethod(putm.value(), a.newInstance(), m, paramNames);
				}
				DeleteMapping dm = m.getAnnotation(DeleteMapping.class);
				if(null != dm) {
					wc.addMethod(dm.value(), a.newInstance(), m, paramNames);
				}
				GetMapping gm = m.getAnnotation(GetMapping.class);
				if(null != gm) {
					wc.addMethod(gm.value(), a.newInstance(), m, paramNames);
				}
			}
		}
	}
}
