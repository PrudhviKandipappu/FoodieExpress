package com.prudhvi.FoodieExpress.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.prudhvi.FoodieExpress.service.InvoiceService;

@RestController
@RequestMapping("/invoice")
public class InvoiceController {
	
	@Autowired
	private InvoiceService invoiceService;
	
	@GetMapping("/generate/{orderId}")
    public ResponseEntity<org.springframework.core.io.Resource> generateInvoice(@PathVariable Long orderId) {
        return invoiceService.generateInvoice(orderId);
    }
}
