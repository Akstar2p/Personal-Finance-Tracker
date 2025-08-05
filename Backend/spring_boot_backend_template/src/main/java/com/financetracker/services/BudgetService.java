package com.financetracker.services;

import java.util.Optional;

import com.financetracker.dto.BudgetRequestDto;
import com.financetracker.dto.BudgetResponseDto;
import com.financetracker.entities.Budget;
import com.financetracker.entities.Category;
import com.financetracker.entities.User;

public interface BudgetService {

	BudgetResponseDto createOrUpdateBudget(String email, BudgetRequestDto dto);

	Optional<Budget> getBudgetEntity(User user, Category category, java.time.YearMonth month);

	BudgetResponseDto getBudget(String email, String categoryName, java.time.YearMonth month);
}
