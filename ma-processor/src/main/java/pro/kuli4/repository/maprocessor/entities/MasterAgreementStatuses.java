package pro.kuli4.repository.maprocessor.entities;

public enum MasterAgreementStatuses {
    NEW("Новое"),
    COUNTER_MESSAGE_EXPECTED("Ожидает встречной анкеты"),
    USER_APPROVE_EXPECTED("Ожидает подтверждение пользователя"),
    REGISTRATION_REJECTED_BY_USER("Регистрация отклонена пользователем"),
    REGISTRATION_REJECTED_BY_CA("Регистрация отклонена контрагентом"),
    REGISTRATION_REJECTED_BY_REPOSITORY("Регистрация отклонена репозитарием"),
    SENT_TO_REGISTRATION("Отправлено на регистрацию"),
    REGISTERED("Зарегистрировано");


    private final String name;

    MasterAgreementStatuses(String name) {
        this.name = name;
    }

    String getName() {
        return name;
    }
}
