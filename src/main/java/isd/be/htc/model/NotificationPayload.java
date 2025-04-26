package isd.be.htc.model;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NotificationPayload {
    private String title;
    private String content;
    private String type;
}
