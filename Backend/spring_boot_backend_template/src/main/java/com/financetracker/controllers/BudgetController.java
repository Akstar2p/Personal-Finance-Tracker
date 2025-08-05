package com.financetracker.controllers;

import com.financetracker.dto.BudgetRequestDto;
import com.financetracker.dto.BudgetResponseDto;
import com.financetracker.services.BudgetService;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/budgets")
@AllArgsConstructor
public class BudgetController {

    private final BudgetService budgetService;

    
    @PreAuthorize("isAuthenticated()")
    @PostMapping
    public BudgetResponseDto createOrUpdateBudget(
            @AuthenticationPrincipal UserDetails userDetails,
            @Valid @RequestBody BudgetRequestDto dto
    ) {
        return budgetService.createOrUpdateBudget(userDetails.getUsername(), dto);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping
    public BudgetResponseDto getBudget(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam String categoryName,
            @RequestParam String month // e.g., "2025-08"
    ) {
        java.time.YearMonth ym = java.time.YearMonth.parse(month);
        return budgetService.getBudget(userDetails.getUsername(), categoryName, ym);
    }
}
