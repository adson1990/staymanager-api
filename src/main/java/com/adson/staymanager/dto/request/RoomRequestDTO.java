package com.adson.staymanager.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;

public record RoomRequestDTO(
              @NotBlank(message = "O número do quarto é obrigatório")  
              String number, 
              
              Integer floor, 
              
              @NotNull(message = "A diária do quarto é obrigatória")
              @Positive(message = "A diária do quarto deve ser um valor positivo")
              BigDecimal dailyRate) 
{}
