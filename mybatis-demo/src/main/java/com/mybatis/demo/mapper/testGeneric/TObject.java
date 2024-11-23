package com.mybatis.demo.mapper.testGeneric;

import java.util.List;

public interface TObject {
	public <T> T getDefault();
}
