package isd.be.htc.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StatisticDTO {
    private String label;
    private Object value;
    private String change;
    private boolean positive;
}
