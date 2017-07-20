package com.ahao.exception;

/**
 * ApplicationException<br>
 * 系统出错对象。
 *
 * @author Ahaochan
 */
public class ApplicationException extends Exception {
    public ApplicationException(Throwable root) {
        super(root);
    }

    public ApplicationException(String string, Throwable root) {
        super(string, root);
    }

    public ApplicationException(String s) {
        super(s);
    }
}
