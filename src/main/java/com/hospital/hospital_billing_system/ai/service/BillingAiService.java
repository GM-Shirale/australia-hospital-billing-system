package com.hospital.hospital_billing_system.ai.service;

import com.hospital.hospital_billing_system.ai.dto.MbsSuggestionRequest;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

@Service
public class BillingAiService {

    private final ChatClient chatClient;

    public BillingAiService(ChatClient.Builder chatClientBuilder) {
        this.chatClient = chatClientBuilder.build();
    }

    public String suggestMbsCodes(MbsSuggestionRequest request) {
        String systemPrompt = """
                You are an Australian Medicare Benefits Schedule (MBS) billing expert.
                Suggest appropriate MBS item codes based on the doctor's consultation notes.
                For each suggestion, provide:
                1. MBS Item Code (e.g., 23, 36)
                2. Schedule Fee (AUD)
                3. Compliance rationale according to Medicare guidelines.
                """;

        String userPrompt = String.format(
                "Specialty: %s, Duration: %s minutes, Clinical Notes: %s",
                request.getDoctorSpecialty() != null ? request.getDoctorSpecialty() : "General Practice",
                request.getConsultationDurationMinutes() != null ? request.getConsultationDurationMinutes() : "Not specified",
                request.getClinicalSummary()
        );

        return chatClient.prompt()
                .system(systemPrompt)
                .user(userPrompt)
                .call()
                .content();
    }
}