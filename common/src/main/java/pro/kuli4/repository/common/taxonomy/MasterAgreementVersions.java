package pro.kuli4.repository.common.taxonomy;

import java.util.Arrays;

// Source - https://repository.nsd.ru/versioned/current/taxonomy/master-agreement-version(nsdrus)
public enum MasterAgreementVersions {

    V_2011("2011", "FpML", "Стандартная документация для срочных сделок на финансовых рынках (версия 2011 г.). Генеральное соглашение НФА об общих условиях заключения договоров РЕПО на рынке ценных бумаг и Примерные условия договоров РЕПО на российском финансовом рынке (редакция 2011г.). Генеральное соглашение ICMA для сделок репо (редакция 2011 г.)"),
    V_2002("2002", "FpML", "Генеральное соглашение ISDA (редакция 2002 г.).");

    private final String value;
    private final String source;
    private final String description;

    MasterAgreementVersions(String value, String source, String description) {
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

    public static MasterAgreementVersions findByValue(String value) {
        return Arrays.stream(MasterAgreementVersions.values())
                .filter(item -> item.getValue().equals(value))
                .findFirst().orElse(null);
    }
}
