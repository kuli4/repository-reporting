package pro.kuli4.repository.maprocessor.dto.mappers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import pro.kuli4.repository.maprocessor.dto.OuterRequestMapper;
import pro.kuli4.repository.maprocessor.dto.beans.NsdFormOuterRequestDto;
import pro.kuli4.repository.maprocessor.entities.forms.NsdForm;

@Component
@Slf4j
public class NsdFormOuterRequestMapper implements OuterRequestMapper<NsdForm, NsdFormOuterRequestDto> {

    @Override
    public NsdFormOuterRequestDto map(NsdForm form) {
        log.debug("================ {} ================", form.getId());
        return NsdFormOuterRequestDto.builder()
                .id(form.getId())
                .messageId(form.getMessageId())
                .type(form.getType())
                .direction(form.getDirection())
                .status(form.getStatus())
                .build();
    }

}
