package com.learning.main.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import com.learning.main.model.Merchandise;
import com.learning.main.model.MerchandiseOrder;
import com.learning.main.model.MerchandiseOrderItem;

import java.util.List;

public interface MerchandiseOrderItemRepository extends JpaRepository<MerchandiseOrderItem, Long> {
    List<MerchandiseOrderItem> findByOrder(MerchandiseOrder order);
    List<MerchandiseOrderItem> findByMerchandise(Merchandise merchandise);
}