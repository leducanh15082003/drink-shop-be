package isd.be.htc.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import isd.be.htc.dto.NotificationPayloadDTO;

@Service
public class SupabaseNotificationService {

    private final WebClient webClient;

    public SupabaseNotificationService(
            @Value("${supabase.url}") String supabaseUrl,
            @Value("${supabase.apiKey}") String supabaseApiKey) {
        this.webClient = WebClient.builder()
                .baseUrl(supabaseUrl)
                .defaultHeader("apikey", supabaseApiKey)
                .defaultHeader("Authorization", "Bearer " + supabaseApiKey)
                .defaultHeader("Content-Type", "application/json")
                .build();
    }

    public void sendNotification(NotificationPayloadDTO payload) {
        webClient.post()
                .uri("/rest/v1/notifications")
                .bodyValue(payload)
                .retrieve()
                .bodyToMono(String.class)
                .subscribe(
                        body -> System.out.println("✅ Sent: " + body),
                        error -> System.err.println("❌ Error: " + error.getMessage()));
    }
}
