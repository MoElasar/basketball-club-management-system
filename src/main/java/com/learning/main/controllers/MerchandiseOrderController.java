package com.learning.main.controllers;

import com.learning.main.enm.OrderStatus;
import com.learning.main.model.*;
import com.learning.main.repository.services.*;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping("/admin/orders")
public class MerchandiseOrderController {
    private final MerchandiseOrderService orderService;
    private final CustomerService customerService;
    private final MerchandiseService merchandiseService;
    private final MerchandiseOrderItemService orderItemService;

    public MerchandiseOrderController(MerchandiseOrderService orderService, 
                                    CustomerService customerService,
                                    MerchandiseService merchandiseService,
                                    MerchandiseOrderItemService orderItemService) {
        this.orderService = orderService;
        this.customerService = customerService;
        this.merchandiseService = merchandiseService;
        this.orderItemService = orderItemService;
    }

    private boolean isAuthenticated(HttpSession session) {
        return session.getAttribute("user") != null;
    }

    @GetMapping
    public String listOrders(Model model, HttpSession session) {
        if (!isAuthenticated(session)) {
            return "redirect:/login";
        }
        
        User currentUser = (User) session.getAttribute("user");
        model.addAttribute("orders", orderService.getAllMerchandiseOrders());
        model.addAttribute("pageTitle", "Order Management");
        model.addAttribute("currentUser", currentUser);
        return "admin/orders/list";
    }

    @GetMapping("/create")
    public String showCreateForm(Model model, HttpSession session) {
        if (!isAuthenticated(session)) {
            return "redirect:/login";
        }
        
        User currentUser = (User) session.getAttribute("user");
        model.addAttribute("order", new MerchandiseOrder());
        model.addAttribute("customers", customerService.getAllCustomers());
        model.addAttribute("merchandiseList", merchandiseService.getAllMerchandise());
        model.addAttribute("statuses", OrderStatus.values());
        model.addAttribute("pageTitle", "Create Order");
        model.addAttribute("currentUser", currentUser);
        return "admin/orders/create";
    }

    @PostMapping("/save")
    public String saveOrder(@ModelAttribute MerchandiseOrder order, 
                          @RequestParam List<Long> merchandiseIds,
                          @RequestParam List<Integer> quantities,
                          HttpSession session,
                          RedirectAttributes redirectAttributes) {
        if (!isAuthenticated(session)) {
            return "redirect:/login";
        }
        
        try {
            order.setOrderDateTime(LocalDateTime.now());
            MerchandiseOrder savedOrder = orderService.saveMerchandiseOrder(order);

            double totalAmount = 0.0;

            for (int i = 0; i < merchandiseIds.size(); i++) {
                final Long currentMerchandiseId = merchandiseIds.get(i); // This variable is effectively final for each iteration
                Merchandise merchandise = merchandiseService.getMerchandiseById(currentMerchandiseId)
                        .orElseThrow(() -> new IllegalArgumentException("Merchandise not found with ID: " + currentMerchandiseId));

                
                
                if (quantities.get(i) > 0) {
                    if (quantities.get(i) > merchandise.getStockQuantity()) {
                        throw new IllegalArgumentException("Not enough stock for " + merchandise.getItemName() + 
                                ". Available: " + merchandise.getStockQuantity());
                    }

                    MerchandiseOrderItem item = new MerchandiseOrderItem();
                    item.setOrder(savedOrder);
                    item.setMerchandise(merchandise);
                    item.setQuantity(quantities.get(i));
                    item.setUnitPrice(merchandise.getPrice());
                    orderItemService.saveMerchandiseOrderItem(item);

                    // Update merchandise stock
                    merchandise.setStockQuantity(merchandise.getStockQuantity() - quantities.get(i));
                    merchandiseService.saveMerchandise(merchandise);

                    totalAmount += merchandise.getPrice() * quantities.get(i);
                }
            }

            savedOrder.setTotalAmount(totalAmount);
            orderService.saveMerchandiseOrder(savedOrder);

            redirectAttributes.addFlashAttribute("successMessage", "Order created successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error creating order: " + e.getMessage());
        }
        return "redirect:/admin/orders";
    }

    @GetMapping("/view/{id}")
    public String viewOrder(@PathVariable Long id, 
                          Model model, 
                          HttpSession session,
                          RedirectAttributes redirectAttributes) {
        if (!isAuthenticated(session)) {
            return "redirect:/login";
        }
        
        User currentUser = (User) session.getAttribute("user");
        return orderService.getMerchandiseOrderById(id)
                .map(order -> {
                    model.addAttribute("order", order);
                    model.addAttribute("items", orderItemService.findMerchandiseOrderItemsByOrder(order));
                    model.addAttribute("pageTitle", "Order Details");
                    model.addAttribute("currentUser", currentUser);
                    return "admin/orders/view";
                })
                .orElseGet(() -> {
                    redirectAttributes.addFlashAttribute("errorMessage", "Order not found with ID: " + id);
                    return "redirect:/admin/orders";
                });
    }

    @PostMapping("/update-status/{id}")
    public String updateStatus(@PathVariable Long id, 
                             @RequestParam OrderStatus status,
                             HttpSession session,
                             RedirectAttributes redirectAttributes) {
        if (!isAuthenticated(session)) {
            return "redirect:/login";
        }
        
        try {
            MerchandiseOrder order = orderService.getMerchandiseOrderById(id)
                    .orElseThrow(() -> new IllegalArgumentException("Order not found with ID: " + id));
            
            order.setStatus(status);
            orderService.saveMerchandiseOrder(order);
            
            redirectAttributes.addFlashAttribute("successMessage", "Order status updated successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error updating order status: " + e.getMessage());
        }
        return "redirect:/admin/orders/view/" + id;
    }
}