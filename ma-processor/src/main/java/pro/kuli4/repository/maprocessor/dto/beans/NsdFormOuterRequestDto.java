package pro.kuli4.repository.maprocessor.dto.beans;

import lombok.*;
import pro.kuli4.repository.maprocessor.entities.forms.NsdFormDirections;
import pro.kuli4.repository.maprocessor.entities.forms.NsdFormStatuses;
import pro.kuli4.repository.maprocessor.entities.forms.NsdFormTypes;

@Builder
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class NsdFormOuterRequestDto {
    private long id;
    private String messageId;
    private NsdFormTypes type;
    private NsdFormDirections direction;
    private NsdFormStatuses status;
}
