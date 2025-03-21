package kim.ics.generator.birthday;

import lombok.Getter;

@Getter
public enum BirthDateType {
    SOLAR("阳历"),
    LUNAR("农历"),
    ;

    private final String name;

    BirthDateType(String name) {
        this.name = name;
    }

    public static BirthDateType from(String name) {
        for (BirthDateType birthDateType : BirthDateType.values()) {
            if (birthDateType.getName().equals(name)) {
                return birthDateType;
            }
        }
        return null;
    }

}
