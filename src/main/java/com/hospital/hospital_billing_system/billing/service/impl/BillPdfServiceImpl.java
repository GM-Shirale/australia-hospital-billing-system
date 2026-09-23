package com.hospital.hospital_billing_system.billing.service.impl;

import com.hospital.hospital_billing_system.billing.entity.Bill;
import com.hospital.hospital_billing_system.billing.entity.BillItem;
import com.hospital.hospital_billing_system.billing.repository.BillRepository;
import com.hospital.hospital_billing_system.billing.service.BillPdfService;
import com.hospital.hospital_billing_system.common.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.font.Standard14Fonts;
import org.apache.pdfbox.pdmodel.graphics.color.PDColor;
import org.apache.pdfbox.pdmodel.graphics.color.PDDeviceRGB;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;

@Service
@RequiredArgsConstructor
@Slf4j
public class BillPdfServiceImpl implements BillPdfService {

    private final BillRepository billRepository;

    @Override
    @Transactional(readOnly = true)
    public byte[] generateBillPdf(Long billId) {

        log.info("Generating final Australian bill PDF for bill id: {}", billId);

        Bill bill = billRepository.findById(billId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Bill not found with id: " + billId));

        try (PDDocument document = new PDDocument();
             ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {

            PDPage page = new PDPage(PDRectangle.A4);
            document.addPage(page);

            try (PDPageContentStream contentStream =
                         new PDPageContentStream(document, page)) {

                float margin = 50;
                float pageWidth = page.getMediaBox().getWidth();
                float y = 790;

                DateTimeFormatter dateFormatter =
                        DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");

                PDType1Font boldFont =
                        new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD);

                PDType1Font normalFont =
                        new PDType1Font(Standard14Fonts.FontName.HELVETICA);

                // PDF colors
                PDColor darkBlue = new PDColor(
                        new float[]{0.10f, 0.20f, 0.40f},
                        PDDeviceRGB.INSTANCE
                );

                PDColor lightBlue = new PDColor(
                        new float[]{0.88f, 0.93f, 0.98f},
                        PDDeviceRGB.INSTANCE
                );

                PDColor black = new PDColor(
                        new float[]{0f, 0f, 0f},
                        PDDeviceRGB.INSTANCE
                );

                PDColor white = new PDColor(
                        new float[]{1f, 1f, 1f},
                        PDDeviceRGB.INSTANCE
                );

                PDColor lightGray = new PDColor(
                        new float[]{0.95f, 0.95f, 0.95f},
                        PDDeviceRGB.INSTANCE
                );

                // =========================
                // HOSPITAL HEADER
                // =========================

                contentStream.setNonStrokingColor(darkBlue);

                contentStream.addRect(
                        0,
                        745,
                        pageWidth,
                        70
                );

                contentStream.fill();

                contentStream.setNonStrokingColor(white);

                contentStream.setFont(boldFont, 20);

                writeCenteredText(
                        contentStream,
                        "HOSPITAL BILLING SYSTEM",
                        pageWidth,
                        790,
                        boldFont,
                        20
                );

                contentStream.setFont(normalFont, 10);

                writeCenteredText(
                        contentStream,
                        "FINAL PATIENT BILL",
                        pageWidth,
                        770,
                        normalFont,
                        10
                );

                writeCenteredText(
                        contentStream,
                        "Australia | Currency: AUD",
                        pageWidth,
                        755,
                        normalFont,
                        9
                );

                y = 715;

                // =========================
                // BILL INFORMATION
                // =========================

                drawSectionHeader(
                        contentStream,
                        "BILL INFORMATION",
                        margin,
                        y,
                        pageWidth - (2 * margin),
                        darkBlue,
                        white,
                        boldFont
                );

                y -= 30;

                contentStream.setNonStrokingColor(black);
                contentStream.setFont(normalFont, 10);

                writeText(
                        contentStream,
                        "Bill Number: " + safeText(bill.getBillNumber()),
                        margin,
                        y
                );

                writeRightText(
                        contentStream,
                        "Bill Date: " +
                                bill.getBillDate().format(dateFormatter),
                        pageWidth - margin,
                        y,
                        normalFont,
                        10
                );

                y -= 35;

                // =========================
                // PATIENT DETAILS
                // =========================

                drawSectionHeader(
                        contentStream,
                        "PATIENT DETAILS",
                        margin,
                        y,
                        pageWidth - (2 * margin),
                        darkBlue,
                        white,
                        boldFont
                );

                y -= 30;

                contentStream.setNonStrokingColor(black);
                contentStream.setFont(normalFont, 10);

                writeText(
                        contentStream,
                        "Patient Number: " +
                                safeText(
                                        bill.getPatient().getPatientNumber()),
                        margin,
                        y
                );

                y -= 18;

                writeText(
                        contentStream,
                        "Patient Name: " +
                                getPatientName(bill),
                        margin,
                        y
                );

                y -= 18;

                writeText(
                        contentStream,
                        "Date of Birth: " +
                                safeText(
                                        String.valueOf(
                                                bill.getPatient().getDateOfBirth())),
                        margin,
                        y
                );

                y -= 18;

                writeText(
                        contentStream,
                        "Gender: " +
                                safeText(
                                        String.valueOf(
                                                bill.getPatient().getGender())),
                        margin,
                        y
                );

                y -= 35;

                // =========================
                // CHARGES
                // =========================

                drawSectionHeader(
                        contentStream,
                        "CHARGES",
                        margin,
                        y,
                        pageWidth - (2 * margin),
                        darkBlue,
                        white,
                        boldFont
                );

                y -= 25;

                // Table header background
                contentStream.setNonStrokingColor(lightBlue);

                contentStream.addRect(
                        margin,
                        y - 7,
                        pageWidth - (2 * margin),
                        22
                );

                contentStream.fill();

                contentStream.setNonStrokingColor(black);
                contentStream.setFont(boldFont, 9);

                writeText(
                        contentStream,
                        "Item",
                        margin + 5,
                        y
                );

                writeText(
                        contentStream,
                        "Type",
                        250,
                        y
                );

                writeText(
                        contentStream,
                        "Qty",
                        330,
                        y
                );

                writeText(
                        contentStream,
                        "Unit Price",
                        375,
                        y
                );

                writeText(
                        contentStream,
                        "Amount",
                        480,
                        y
                );

                y -= 22;

                contentStream.setFont(normalFont, 9);

                for (BillItem item : bill.getBillItems()) {

                    // Alternate row background
                    contentStream.setNonStrokingColor(lightGray);

                    contentStream.addRect(
                            margin,
                            y - 7,
                            pageWidth - (2 * margin),
                            20
                    );

                    contentStream.fill();

                    contentStream.setNonStrokingColor(black);

                    writeText(
                            contentStream,
                            safeText(item.getItemName()),
                            margin + 5,
                            y
                    );

                    writeText(
                            contentStream,
                            item.getItemType() != null
                                    ? item.getItemType().name()
                                    : "",
                            250,
                            y
                    );

                    writeText(
                            contentStream,
                            String.valueOf(item.getQuantity()),
                            330,
                            y
                    );

                    writeText(
                            contentStream,
                            "$" + formatAmount(item.getUnitPrice()),
                            375,
                            y
                    );

                    writeText(
                            contentStream,
                            "$" + formatAmount(item.getTotalAmount()),
                            480,
                            y
                    );

                    y -= 20;

                    // Avoid content going below the page
                    if (y < 315) {
                        break;
                    }
                }

                y -= 20;

                // =========================
                // BILL SUMMARY
                // =========================

                drawSectionHeader(
                        contentStream,
                        "BILL SUMMARY",
                        margin,
                        y,
                        pageWidth - (2 * margin),
                        darkBlue,
                        white,
                        boldFont
                );

                y -= 28;

                contentStream.setFont(normalFont, 10);

                writeAmountRow(
                        contentStream,
                        "Total Charges",
                        formatAmount(bill.getTotalAmount()),
                        margin,
                        pageWidth - margin,
                        y,
                        black,
                        normalFont,
                        10
                );

                y -= 20;

                writeAmountRow(
                        contentStream,
                        "Patient Responsibility",
                        formatAmount(bill.getPatientAmount()),
                        margin,
                        pageWidth - margin,
                        y,
                        black,
                        normalFont,
                        10
                );

                y -= 20;

                writeAmountRow(
                        contentStream,
                        "Private Insurance",
                        formatAmount(bill.getInsuranceAmount()),
                        margin,
                        pageWidth - margin,
                        y,
                        black,
                        normalFont,
                        10
                );

                y -= 20;

                writeAmountRow(
                        contentStream,
                        "Medicare Contribution",
                        formatAmount(bill.getMedicareAmount()),
                        margin,
                        pageWidth - margin,
                        y,
                        black,
                        normalFont,
                        10
                );

                y -= 20;

                // GST
                BigDecimal gstAmount = BigDecimal.ZERO;

                writeAmountRow(
                        contentStream,
                        "GST",
                        formatAmount(gstAmount),
                        margin,
                        pageWidth - margin,
                        y,
                        black,
                        normalFont,
                        10
                );

                y -= 30;

                // =========================
                // TOTAL PATIENT PAYABLE
                // =========================

                contentStream.setNonStrokingColor(darkBlue);

                contentStream.addRect(
                        margin,
                        y - 12,
                        pageWidth - (2 * margin),
                        38
                );

                contentStream.fill();

                contentStream.setNonStrokingColor(white);

                contentStream.setFont(boldFont, 12);

                writeText(
                        contentStream,
                        "TOTAL PATIENT AMOUNT PAYABLE",
                        margin + 10,
                        y
                );

                writeRightText(
                        contentStream,
                        "$" +
                                formatAmount(
                                        bill.getPatientAmount()),
                        pageWidth - margin - 10,
                        y,
                        boldFont,
                        12
                );

                y -= 55;

                // =========================
                // GST INFORMATION
                // =========================

                contentStream.setNonStrokingColor(black);

                contentStream.setFont(boldFont, 9);

                writeText(
                        contentStream,
                        "GST INFORMATION",
                        margin,
                        y
                );

                y -= 15;

                contentStream.setFont(normalFont, 8);

                writeText(
                        contentStream,
                        "Hospital treatment may be GST-free under Australian GST rules.",
                        margin,
                        y
                );

                y -= 12;

                writeText(
                        contentStream,
                        "GST shown above is $0.00 for this patient medical bill.",
                        margin,
                        y
                );

                y -= 30;

                // =========================
                // FOOTER
                // =========================

                contentStream.setFont(normalFont, 8);

                writeCenteredText(
                        contentStream,
                        "Thank you for using our hospital services.",
                        pageWidth,
                        y,
                        normalFont,
                        8
                );

                y -= 12;

                writeCenteredText(
                        contentStream,
                        "Please retain this document for your records.",
                        pageWidth,
                        y,
                        normalFont,
                        8
                );
            }

            document.save(outputStream);

            log.info(
                    "Australian hospital bill PDF generated successfully for bill id: {}",
                    billId
            );

            return outputStream.toByteArray();

        } catch (IOException exception) {

            log.error(
                    "Error while generating bill PDF for bill id: {}",
                    billId,
                    exception
            );

            throw new IllegalStateException(
                    "Unable to generate bill PDF"
            );
        }
    }

    private String getPatientName(Bill bill) {

        String firstName = safeText(
                bill.getPatient().getFirstName()
        );

        String middleName = safeText(
                bill.getPatient().getMiddleName()
        );

        String lastName = safeText(
                bill.getPatient().getLastName()
        );

        return (firstName + " " + middleName + " " + lastName)
                .replaceAll("\\s+", " ")
                .trim();
    }

    private void drawSectionHeader(
            PDPageContentStream contentStream,
            String title,
            float x,
            float y,
            float width,
            PDColor backgroundColor,
            PDColor textColor,
            PDType1Font boldFont
    ) throws IOException {

        contentStream.setNonStrokingColor(backgroundColor);

        contentStream.addRect(
                x,
                y - 8,
                width,
                20
        );

        contentStream.fill();

        contentStream.setNonStrokingColor(textColor);

        contentStream.setFont(
                boldFont,
                10
        );

        writeText(
                contentStream,
                title,
                x + 8,
                y - 3
        );
    }

    private void writeAmountRow(
            PDPageContentStream contentStream,
            String label,
            String amount,
            float left,
            float right,
            float y,
            PDColor textColor,
            PDType1Font font,
            float fontSize
    ) throws IOException {

        contentStream.setNonStrokingColor(textColor);
        contentStream.setFont(font, fontSize);

        writeText(
                contentStream,
                label,
                left,
                y
        );

        writeRightText(
                contentStream,
                "$" + amount,
                right,
                y,
                font,
                fontSize
        );
    }

    private void writeText(
            PDPageContentStream contentStream,
            String text,
            float x,
            float y
    ) throws IOException {

        contentStream.beginText();
        contentStream.newLineAtOffset(x, y);
        contentStream.showText(safeText(text));
        contentStream.endText();
    }

    private void writeCenteredText(
            PDPageContentStream contentStream,
            String text,
            float pageWidth,
            float y,
            PDType1Font font,
            float fontSize
    ) throws IOException {

        float textWidth =
                font.getStringWidth(
                        safeText(text)
                ) / 1000 * fontSize;

        float x =
                (pageWidth - textWidth) / 2;

        contentStream.beginText();
        contentStream.setFont(font, fontSize);
        contentStream.newLineAtOffset(x, y);
        contentStream.showText(safeText(text));
        contentStream.endText();
    }

    private void writeRightText(
            PDPageContentStream contentStream,
            String text,
            float rightX,
            float y,
            PDType1Font font,
            float fontSize
    ) throws IOException {

        float textWidth =
                font.getStringWidth(
                        safeText(text)
                ) / 1000 * fontSize;

        float x =
                rightX - textWidth;

        contentStream.beginText();
        contentStream.setFont(font, fontSize);
        contentStream.newLineAtOffset(x, y);
        contentStream.showText(safeText(text));
        contentStream.endText();
    }

    private String formatAmount(BigDecimal amount) {

        if (amount == null) {
            return "0.00";
        }

        return amount
                .setScale(2)
                .toPlainString();
    }

    private String safeText(String text) {

        if (text == null || text.isBlank()) {
            return "";
        }

        return text
                .replace("\n", " ")
                .replace("\r", " ");
    }
}