package kz.commerce.service;

import kz.commerce.model.Cart;
import kz.commerce.model.Product;
import kz.commerce.repo.CartRepo;
import kz.commerce.repo.ProductRepo;
import org.antlr.v4.runtime.misc.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CartService {

    private CartRepo repo;
    private ProductRepo productRepo;

    @Autowired
    public void setProductRepo(ProductRepo productRepo){
        this.productRepo = productRepo;
    }

    @Autowired
    public void setRepo(CartRepo repo){
        this.repo = repo;
    }

    public void save(int id, int quantity){
        repo.save(new Cart(id, quantity));
    }

    public List<Cart> getAll() {
        return repo.findAll();
    }

    public void delete(int id) {
        repo.delete(new Cart(id));
    }

    public List<Pair<Product, Integer>> getAllProductPairs() {
        List<Cart> cartProducts = repo.findAll();
        List<Pair<Product, Integer>> result = new ArrayList<>();
        Product product;

        for(Cart el : cartProducts){
            product = productRepo.findById(el.getProdId()).orElse(new Product(-1));
            if(product.getId() > 0)
                result.add(new Pair<>(product,el.getQuantity()));
        }
        return result;
    }
}
