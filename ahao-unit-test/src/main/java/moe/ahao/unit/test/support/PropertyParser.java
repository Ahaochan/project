package moe.ahao.unit.test.support;

import java.util.Locale;

/**
 * 属性解析器
 */
public class PropertyParser {
	private PropertyParser() {
	}

	/**
	 * 通过get,set,is方法来获取属性
	 */
	public static String methodToProperty(String name) {
		if (name.startsWith("is")) {
			name = name.substring(2);
		} else {
			if (!name.startsWith("get") && !name.startsWith("set")) {
				throw new UnsupportedOperationException("Error parsing property name '" + name + "'.  Didn't start with 'is', 'get' or 'set'.");
			}

			name = name.substring(3);
		}

		if (name.length() == 1 || name.length() > 1 && !Character.isUpperCase(name.charAt(1))) {
			name = name.substring(0, 1).toLowerCase(Locale.ENGLISH) + name.substring(1);
		}

		return name;
	}

	public static boolean isProperty(String name) {
		return isGetter(name) || isSetter(name);
	}

	public static boolean isGetter(String name) {
		return name.startsWith("get") && name.length() > 3 || name.startsWith("is") && name.length() > 2;
	}

	public static boolean isSetter(String name) {
		return name.startsWith("set") && name.length() > 3;
	}
}
