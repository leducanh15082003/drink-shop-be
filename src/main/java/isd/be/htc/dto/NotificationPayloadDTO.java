package isd.be.htc.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NotificationPayloadDTO {
    private String title;
    private String content;
    private String type;
    private String link; // 👉 Link tới order, ví dụ: /admin/orders/123
    private String dataid; // orderId, hoặc productId tuỳ trường hợp
}
