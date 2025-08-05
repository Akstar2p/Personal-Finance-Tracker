package com.financetracker.services;

import com.financetracker.dto.BudgetRequestDto;
import com.financetracker.dto.BudgetResponseDto;
import com.financetracker.customexceptions.ResourceNotFoundException;
import com.financetracker.entities.Budget;
import com.financetracker.entities.Category;
import com.financetracker.entities.User;

import lombok.AllArgsConstructor;

import com.financetracker.dao.BudgetRepository;
import com.financetracker.dao.CategoryRepository;
import com.financetracker.dao.UserRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
@AllArgsConstructor
public class BudgetServiceImpl implements BudgetService{

    private final BudgetRepository budgetRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;

   
    @Override
    public BudgetResponseDto createOrUpdateBudget(String email, BudgetRequestDto dto) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Category category = categoryRepository.findByNameAndType(dto.getCategoryName(), null) // adjust type if needed
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));

        // Fetch existing or create
        Budget budget = budgetRepository.findByUserAndCategoryAndMonth(user, category, dto.getMonth())
                .orElseGet(() -> {
                    Budget b = new Budget();
                    b.setUser(user);
                    b.setCategory(category);
                    b.setMonth(dto.getMonth());
                    return b;
                });

        budget.setMonthlyLimit(dto.getMonthlyLimit());
        Budget saved = budgetRepository.save(budget);

        return new BudgetResponseDto(
                saved.getId(),
                saved.getCategory().getName(),
                saved.getMonth(),
                saved.getMonthlyLimit()
        );
    }

    @Override
    public Optional<Budget> getBudgetEntity(User user, Category category, java.time.YearMonth month) {
        return budgetRepository.findByUserAndCategoryAndMonth(user, category, month);
    }

    @Override
    public BudgetResponseDto getBudget(String email, String categoryName, java.time.YearMonth month) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Category category = categoryRepository.findByNameAndType(categoryName, null)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));

        Budget budget = budgetRepository.findByUserAndCategoryAndMonth(user, category, month)
                .orElseThrow(() -> new ResourceNotFoundException("Budget not set for given category/month"));

        return new BudgetResponseDto(
                budget.getId(),
                category.getName(),
                budget.getMonth(),
                budget.getMonthlyLimit()
        );
    }
}
