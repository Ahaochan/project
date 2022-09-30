package moe.ahao.unit.test.support;

import org.assertj.core.api.SoftAssertionsProvider;

public class AssertSupport {

    /**
     * 验证业务异常
     * @param expectMessage 期待的异常message
     * @param expectedThrowable 期待的异常类型
     * @param runnable 业务方法
     */
    public static <T extends Throwable> T assertThrows(String expectMessage, Class<T> expectedThrowable,
                                                       SoftAssertionsProvider.ThrowingRunnable runnable) {
        try {
            runnable.run();
        } catch (Throwable actualThrown) {
            String actualMessage =  actualThrown.getMessage();
            if (expectedThrowable.isInstance(actualThrown) && actualMessage.equals(expectMessage)) {
                @SuppressWarnings("unchecked") T retVal = (T) actualThrown;
                return retVal;
            } else {
                String expected = formatClass(expectedThrowable) + " | expectMessage=["+expectMessage+"]";
                Class<? extends Throwable> actualThrowable = actualThrown.getClass();
                String actual = formatClass(actualThrowable) + " | actualMessage=["+actualMessage+"]";
                if (expected.equals(actual)) {
                    // There must be multiple class loaders. Add the identity hash code so the message
                    // doesn't say "expected: java.lang.String<my.package.MyException> ..."
                    expected += "@" + Integer.toHexString(System.identityHashCode(expectedThrowable));
                    actual += "@" + Integer.toHexString(System.identityHashCode(actualThrowable));
                }
                String mismatchMessage = format("unexpected exception type thrown;", expected, actual);

                // The AssertionError(String, Throwable) ctor is only available on JDK7.
                AssertionError assertionError = new AssertionError(mismatchMessage);
                assertionError.initCause(actualThrown);
                throw assertionError;
            }
        }
        String notThrownMessage = String
                .format("expected %s to be thrown, but nothing was thrown",
                        formatClass(expectedThrowable));
        throw new AssertionError(notThrownMessage);
    }

    private static String buildPrefix(String message) {
        return message != null && message.length() != 0 ? message + ": " : "";
    }

    private static String formatClass(Class<?> value) {
        String className = value.getCanonicalName();
        return className == null ? value.getName() : className;
    }

    static String format(String message, Object expected, Object actual) {
        String formatted = "";
        if (message != null && !"".equals(message)) {
            formatted = message + " ";
        }
        String expectedString = String.valueOf(expected);
        String actualString = String.valueOf(actual);
        if (equalsRegardingNull(expectedString, actualString)) {
            return formatted + "expected: "
                    + formatClassAndValue(expected, expectedString)
                    + " but was: " + formatClassAndValue(actual, actualString);
        } else {
            return formatted + "expected:<" + expectedString + "> but was:<"
                    + actualString + ">";
        }
    }

    private static String formatClassAndValue(Object value, String valueString) {
        String className = value == null ? "null" : value.getClass().getName();
        return className + "<" + valueString + ">";
    }

    private static boolean equalsRegardingNull(Object expected, Object actual) {
        if (expected == null) {
            return actual == null;
        }

        return isEquals(expected, actual);
    }

    private static boolean isEquals(Object expected, Object actual) {
        return expected.equals(actual);
    }

}
