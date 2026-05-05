package com.data.salesanalytics.config;

import com.data.salesanalytics.sales.SaleRecord;
import com.data.salesanalytics.sales.SaleRecordRepository;
import com.data.salesanalytics.user.Role;
import com.data.salesanalytics.user.User;
import com.data.salesanalytics.user.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;

@Component
public class DataSeeder implements CommandLineRunner {
    private final UserRepository userRepository;
    private final SaleRecordRepository saleRecordRepository;
    private final PasswordEncoder passwordEncoder;
    private final boolean seedDemoData;

    public DataSeeder(UserRepository userRepository, SaleRecordRepository saleRecordRepository,
                      PasswordEncoder passwordEncoder, @Value("${app.seed-demo-data}") boolean seedDemoData) {
        this.userRepository = userRepository;
        this.saleRecordRepository = saleRecordRepository;
        this.passwordEncoder = passwordEncoder;
        this.seedDemoData = seedDemoData;
    }

    @Override
    @Transactional
    public void run(String... args) {
        if (!seedDemoData || userRepository.existsByEmail("demo@data.com")) {
            return;
        }
        User user = new User();
        user.setName("Usuario Demo");
        user.setEmail("demo@data.com");
        user.setPassword(passwordEncoder.encode("Demo1234"));
        user.setRole(Role.USER);
        userRepository.save(user);

        Object[][] rows = {
                {"2026-01-05", "Laptop Pro 14", "Tecnologia", 8, "3200000", "E-commerce", "Bogota", "Empresarial"},
                {"2026-01-12", "Camiseta Urbana", "Ropa", 45, "65000", "Tienda fisica", "Armenia", "Retail"},
                {"2026-01-18", "Suplemento Vital", "Salud", 30, "85000", "Redes sociales", "Medellin", "Consumidor final"},
                {"2026-02-03", "Cafe Premium", "Alimentos", 120, "18000", "Mayorista", "Pereira", "Distribuidor"},
                {"2026-02-14", "Tablet Edu", "Tecnologia", 12, "1450000", "E-commerce", "Cali", "Educativo"},
                {"2026-02-26", "Silla Ergonomica", "Hogar", 18, "420000", "Tienda fisica", "Bogota", "Empresarial"},
                {"2026-03-06", "Smartphone X", "Tecnologia", 22, "2100000", "E-commerce", "Medellin", "Retail"},
                {"2026-03-11", "Chaqueta Outdoor", "Ropa", 28, "180000", "Redes sociales", "Armenia", "Consumidor final"},
                {"2026-03-20", "Filtro de Agua", "Hogar", 35, "130000", "Mayorista", "Cali", "Distribuidor"},
                {"2026-03-28", "Snacks Saludables", "Alimentos", 160, "12000", "Tienda fisica", "Pereira", "Retail"},
                {"2026-04-02", "Monitor 27", "Tecnologia", 16, "980000", "E-commerce", "Bogota", "Empresarial"},
                {"2026-04-08", "Vitaminas Plus", "Salud", 55, "62000", "Redes sociales", "Medellin", "Consumidor final"},
                {"2026-04-16", "Jeans Clasico", "Ropa", 38, "120000", "Tienda fisica", "Cali", "Retail"},
                {"2026-04-23", "Set Cocina", "Hogar", 24, "250000", "Mayorista", "Armenia", "Distribuidor"},
                {"2026-05-04", "Auriculares Neo", "Tecnologia", 40, "210000", "E-commerce", "Pereira", "Retail"},
                {"2026-05-09", "Cafe Premium", "Alimentos", 180, "18500", "Mayorista", "Bogota", "Distribuidor"},
                {"2026-05-14", "Suplemento Vital", "Salud", 72, "87000", "Redes sociales", "Cali", "Consumidor final"},
                {"2026-05-19", "Mesa Compacta", "Hogar", 30, "310000", "Tienda fisica", "Medellin", "Empresarial"},
                {"2026-05-24", "Smartphone X", "Tecnologia", 35, "2050000", "E-commerce", "Armenia", "Retail"},
                {"2026-05-29", "Camiseta Urbana", "Ropa", 70, "68000", "Redes sociales", "Pereira", "Consumidor final"}
        };
        for (Object[] row : rows) {
            SaleRecord sale = new SaleRecord();
            sale.setSaleDate(LocalDate.parse((String) row[0]));
            sale.setProductName((String) row[1]);
            sale.setCategory((String) row[2]);
            sale.setUnitsSold((Integer) row[3]);
            sale.setUnitPrice(new BigDecimal((String) row[4]));
            sale.setSalesChannel((String) row[5]);
            sale.setRegion((String) row[6]);
            sale.setCustomerSegment((String) row[7]);
            sale.setUser(user);
            saleRecordRepository.save(sale);
        }
    }
}
