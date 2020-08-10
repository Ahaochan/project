package moe.ahao.spring.boot.jpa.module.enums;

public enum Sex {
    man(1, "男"), woman(2, "女");

    int code;
    String name;

    Sex(int code, String name) {
        this.code = code;
        this.name = name;
    }

    public static Sex parse(int code) {
        for (Sex item : Sex.values()) {
            if (item.code == code) {
                return item;
            }
        }
        return null;
    }

    public int getCode() {
        return code;
    }

    public String getName() {
        return name;
    }
}
