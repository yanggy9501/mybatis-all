package com.mybatis.demo.mapper.testGeneric;

public class TestInterface {

	/**
	 * 方式1：通过中间类做类型转换，通过定义有泛型方法的接口，实现接口并重新泛型方法，此时将泛型写成具体类型。通过泛型接口去调用泛型方法。
	 *
	 * @return
	 * @param <T>
	 */
	public <T> T listGeneric() {
		TObject tObject = new ObjectImpl();
		ObjectImpl tObjectImpl = new ObjectImpl();
		return tObject.getDefault();

//		return tObjectImpl.getDefault();
	}

	/**
	 * 方式2：通过强制类型转换将 泛型   ------- 转换到 -------> 具体类型
	 *
	 * 泛型 T 到具体类型，调用方调用 getGeneric 返回值可以认为是任意类型。
	 * List<User> list = testInterface.listGeneric();
	 * User user = testInterface.listGeneric();
	 * Order order = testInterface.listGeneric();
	 *
	 * 在编译器都不会报错，但是运行时存在类型转换错误的风险。
	 *
	 * @return
	 * @param <T>
	 */
	@SuppressWarnings("unchecked")
	public <T> T getGeneric() {
		return (T) new Object();
	}

	/**
	 * 传入的参数类型是任意一种类型，由调用方决定。save(user), save(student), save(order) 随意。
	 *
	 * @param param 参数是一个泛型，具体类型在运行时才知道
	 * @param <T>
	 */
	public <T> void save(T param) {
		System.out.println(param);
	}

	public <K> void save2(K param) {
		save(param);
	}

}
