package pro.kuli4.repository.maprocessor.services.forms;

import pro.kuli4.repository.maprocessor.entities.forms.Cm10NsdForm;

public interface MasterAgreementNsdFormGenerator {
    String generateCM010(Cm10NsdForm form);
}
