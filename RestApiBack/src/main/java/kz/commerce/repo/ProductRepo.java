package kz.commerce.repo;

import jakarta.transaction.Transactional;
import kz.commerce.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepo extends JpaRepository<Product, Integer> {

    @Modifying(clearAutomatically = true)
    @Transactional
    @Query("update Product product set product.stockQuantity=:remainQuantity where product.id=:productId")
    void pursache(@Param("productId") int productId, @Param("remainQuantity") int remainQuantity);

    @Modifying(clearAutomatically = true)
    @Transactional
    @Query("update Product product set product.stockQuantity=0, product.productAvailable=false where product.id=:productId")
    void pursacheAndDisable(@Param("productId") int productId);

    @Query("select product from Product product where product.name ilike concat('%',:keyword,'%') or product.description ilike concat('%',:keyword,'%') or product.brand ilike concat('%',:keyword,'%') or product.category ilike concat('%', :keyword, '%')")
    List<Product> search(@Param("keyword") String keyword);
}
