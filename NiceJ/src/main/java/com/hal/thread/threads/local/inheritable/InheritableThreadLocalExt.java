package com.hal.thread.threads.local.inheritable;

import java.util.Date;

public class InheritableThreadLocalExt extends InheritableThreadLocal {
		@Override
		protected Long initialValue() {
			return new Date().getTime();
		}
		
		@Override
		protected Object childValue(Object parentValue) {
		// TODO Auto-generated method stub
		return parentValue + "childValue";
		}
}
