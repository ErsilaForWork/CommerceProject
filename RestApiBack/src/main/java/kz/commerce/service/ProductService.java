package kz.commerce.service;

import kz.commerce.exceptions.NoEnuoghProduct;
import kz.commerce.model.Product;
import kz.commerce.repo.ProductRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public class ProductService {
    private final ProductRepo productRepo;

    @Autowired
    public ProductService(ProductRepo productRepo){
        this.productRepo = productRepo;
    }

    public void save(Product product){
        productRepo.save(product);
    }

    public List<Product> getAll() {
        return productRepo.findAll();
    }

    public Product getById(int id) {
        return productRepo.findById(id).orElse(new Product(-1));
    }

    public void delete(int id) {
        Product product = new Product(id);
        productRepo.delete(product);
    }

    public void save(Product product, MultipartFile imageFile) throws IOException {
        product.setImageName(imageFile.getOriginalFilename());
        product.setImageData(imageFile.getBytes());
        product.setImageType(imageFile.getContentType());

        productRepo.save(product);
    }

    public void pursache(int productId, int quantity) throws NoEnuoghProduct {
        Product product = getById(productId);
        if(product.getId() > 0 && product.getStockQuantity() > quantity)
            productRepo.pursache(productId, product.getStockQuantity() - quantity);
        else if(product.getStockQuantity() == quantity){
            productRepo.pursacheAndDisable(productId);
        }
        else if(product.getStockQuantity() < quantity){
            throw new NoEnuoghProduct("No Enough Product To Buy!");
        }
    }

    public List<Product> searchForProducts(String keyword) {
        return productRepo.search(keyword);
    }
}
