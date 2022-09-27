package moe.ahao.spring.boot.powermock;

public class StringService {
    public String toLowerCase(String str) {
        return str.toLowerCase();
    }

    public final String toLowerCaseFinal(String str) {
        return str.toLowerCase();
    }

    private String toLowerCasePrivate(String str) {
        return str.toLowerCase();
    }

    public String toLowerCasePrivateFacade(String str) {
        return toLowerCasePrivate(str);
    }

    public static String toLowerCaseStatic(String str) {
        return str.toLowerCase();
    }
}
