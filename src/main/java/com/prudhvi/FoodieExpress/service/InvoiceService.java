package com.prudhvi.FoodieExpress.service;

import com.prudhvi.FoodieExpress.entity.Order;
import com.prudhvi.FoodieExpress.entity.OrderItem;
import com.prudhvi.FoodieExpress.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.xhtmlrenderer.pdf.ITextRenderer;

import java.io.ByteArrayOutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@Service
public class InvoiceService {

    @Autowired
    private OrderRepository orderRepository;

    public ResponseEntity<Resource> generateInvoice(Long orderId) {
        Optional<Order> orderOptional = orderRepository.findById(orderId);
        if (orderOptional.isEmpty()) {
            throw new RuntimeException("Order not found!");
        }

        Order order = orderOptional.get();
        String htmlContent = loadHtmlTemplate("src/main/resources/templates/invoice-template.html");

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        DecimalFormat df = new DecimalFormat("0.00");

        double totalAmount = 0.0;
        StringBuilder itemsHtml = new StringBuilder();

        for (OrderItem item : order.getOrderItems()) {
            double itemTotal = item.getQuantity() * item.getRestaurantItem().getCost();
            totalAmount += itemTotal;

            itemsHtml.append("<tr>")
                     .append("<td>").append(item.getRestaurantItem().getItem().getName()).append("</td>")
                     .append("<td>").append(item.getRestaurantItem().getRestaurant().getName()).append("</td>")
                     .append("<td>").append(item.getQuantity()).append("</td>")
                     .append("<td>").append(df.format(item.getRestaurantItem().getCost())).append("</td>")
                     .append("<td>").append(df.format(itemTotal)).append("</td>")
                     .append("</tr>");
        }

        htmlContent = htmlContent.replace("${orderId}", String.valueOf(order.getId()))
                                 .replace("${orderDate}", order.getOrderDate().format(dtf))
                                 .replace("${customerName}", order.getCustomer().getFirstName() + order.getCustomer().getLastname())
                                 .replace("${totalAmount}", df.format(totalAmount))
                                 .replace("${orderItems}", itemsHtml.toString());

        byte[] pdfBytes = convertHtmlToPdf(htmlContent);
        ByteArrayResource resource = new ByteArrayResource(pdfBytes);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=invoice_" + orderId + ".pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(resource);
    }

    private String loadHtmlTemplate(String filePath) {
        try {
            return new String(Files.readAllBytes(Paths.get(filePath)));
        } catch (Exception e) {
            throw new RuntimeException("Error loading invoice template: " + e.getMessage());
        }
    }

    private byte[] convertHtmlToPdf(String html) {
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            ITextRenderer renderer = new ITextRenderer();
            renderer.setDocumentFromString(html);
            renderer.layout();
            renderer.createPDF(outputStream);
            renderer.finishPDF();
            return outputStream.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("Error generating PDF: " + e.getMessage());
        }
    }
}
