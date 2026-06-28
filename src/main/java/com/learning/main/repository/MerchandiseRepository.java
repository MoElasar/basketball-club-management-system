package com.learning.main.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import com.learning.main.enm.MerchandiseCategory;
import com.learning.main.model.Merchandise;
import java.util.List;

public interface MerchandiseRepository extends JpaRepository<Merchandise, Long> {
    List<Merchandise> findByCategory(MerchandiseCategory category);
    List<Merchandise> findByPriceBetween(double minPrice, double maxPrice);
    List<Merchandise> findByStockQuantityGreaterThan(int quantity);
}
