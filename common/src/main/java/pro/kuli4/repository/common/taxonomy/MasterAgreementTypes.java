package pro.kuli4.repository.common.taxonomy;

import java.util.Arrays;

// Source - https://repository.nsd.ru/versioned/current/taxonomy/master-agreement-type(nsdrus)
public enum MasterAgreementTypes {

    RUSSIAN_DERIVATIVES("RussianDerivatives", "FpML", "Стандартная документация для срочных сделок на финансовых рынках."),
    ISDA("ISDA", "FpML", "Генеральное соглашение ISDA."),
    GMRA("GMRA", "FpML", "Генеральное соглашение ICMA для сделок репо.");

    private final String value;
    private final String source;
    private final String description;

    MasterAgreementTypes(String value, String source, String description) {
        this.value = value;
        this.source = source;
        this.description = description;
    }

    public String getValue() {
        return value;
    }

    public String getSource() {
        return source;
    }

    public String getDescription() {
        return description;
    }

    public static MasterAgreementTypes findByValue(String value) {
        return Arrays.stream(MasterAgreementTypes.values())
                .filter(item -> item.getValue().equals(value))
                .findFirst().orElse(null);
    }
}
