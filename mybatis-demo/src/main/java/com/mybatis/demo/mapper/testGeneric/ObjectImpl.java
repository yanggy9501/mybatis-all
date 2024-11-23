package com.mybatis.demo.mapper.testGeneric;

import java.util.Arrays;
import java.util.List;

public class ObjectImpl implements TObject {
//	@SuppressWarnings("unchecked")
	@Override
	public Object getDefault() {
		return new Object();
	}
}
