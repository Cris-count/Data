package com.data.salesanalytics.sales;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.time.LocalDate;

public record SaleRecordRequest(
        @NotNull(message = "La fecha de venta es obligatoria")
        LocalDate saleDate,
        @NotBlank(message = "El producto es obligatorio")
        @Size(max = 140, message = "El producto no debe superar 140 caracteres")
        String productName,
        @NotBlank(message = "La categoria es obligatoria")
        String category,
        @NotNull(message = "Las unidades son obligatorias")
        @PositiveOrZero(message = "Las unidades no pueden ser negativas")
        Integer unitsSold,
        @NotNull(message = "El precio unitario es obligatorio")
        @DecimalMin(value = "0.0", inclusive = true, message = "El precio no puede ser negativo")
        BigDecimal unitPrice,
        @NotBlank(message = "El canal de venta es obligatorio")
        String salesChannel,
        @NotBlank(message = "La region es obligatoria")
        String region,
        @NotBlank(message = "El segmento de cliente es obligatorio")
        String customerSegment
) {}
