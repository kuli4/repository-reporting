package pro.kuli4.repository.apireciever.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pro.kuli4.repository.apireciever.services.MasterAgreementProcessService;

@RestController
@RequestMapping("/ma/")
@Slf4j
public class MasterAgreementProcessController {

    private final MasterAgreementProcessService masterAgreementProcessService;

    public MasterAgreementProcessController(MasterAgreementProcessService masterAgreementProcessService) {
        this.masterAgreementProcessService = masterAgreementProcessService;
    }

    /*
      Json example
      {
          "header": { 								// Техническая информация запроса
              "sourceSystem": "SMART",				// M - Идентификатор системы источника
              "messageId": "ABCD1234"				// M - Идентификатор запроса в системе источнике
          },
          "body": {									// Тело запроса
              "ma": { 								// Информация о заключенном ГС
                  "id": "FX-52",						// M - Идентификатор ГС в Альфа-банке
                  "regDate": "2020-11-16",			// M - Дата заключения ГС
                  "isFirstParty": true, 				// M - Если true, то Альфа-банк первая сторона по ГС, иначе вторая [true, false]
                  "type": "RussianDerivatives",		// M - Тип ГС [из справочника НРД]
                  "version": "2011",					// M - Версия формы ГС, в соответствие с которой сторонами заключено ГС [из справочника НРД]
                  "isMaUtiGenerator": true,			// M - Если true, то Альфа-банк UTI-генерирующая сторона по ГС [true, false]
                  "isDealUtiGenerator": true,			// M - Если true, то Альфа-банк UTI-генерирующая сторона по сделкам [true, false]
                  "uti": "LEI12345TRADEID12344", 		// O - UTI ГС
                  "confirmationMethod": "MXME"		// M - Метод подтверждения сделок в рамках регистрируемого ГС ["MXME", "MATH"]
              },
              "ca": {									//Информация о контрагенте
                  "idType": "RPZR",					// M - Тип идентификатора контрагента ["RPZR", "INN", "SWIFT", "BIC", "ABS", "PASS"]
                  "id": "NSD00000435J",				// M - Идентификатор контрагента
              },
              "tech": {
                  "sendNow": true 					// M - Если true, то генерация анкеты с соответствующим изменением в НРД отправляется сразу, если false, то требуется подтверждение пользователя [true, false]
              }
          }
      }
     */
    @RequestMapping(method = RequestMethod.POST, value = "/new")
    public ResponseEntity<String> newMasterAgreement(@RequestBody String body) throws Exception {
        return masterAgreementProcessService.handle(body);
    }

}
