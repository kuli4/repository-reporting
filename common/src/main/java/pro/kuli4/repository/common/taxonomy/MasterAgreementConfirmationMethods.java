package pro.kuli4.repository.common.taxonomy;

// Source - https://repository.nsd.ru/versioned/current/reference/types/simpleMasterAgreementConfirmationEnum
public enum MasterAgreementConfirmationMethods {
    MXME("Комбинированный способ подтверждения."),
    MATH("Встречный способ подтверждения.");

    private final String description;

    MasterAgreementConfirmationMethods(String description) {
        this.description = description;
    }

    String getDescription() {
        return description;
    }

}
