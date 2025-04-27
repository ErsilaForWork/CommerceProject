package kz.commerce.controller;

import kz.commerce.DTO.CartGetDTO;
import kz.commerce.DTO.UpdateQuantityDTO;
import kz.commerce.exceptions.NoEnuoghProduct;
import kz.commerce.model.Product;
import kz.commerce.service.CartService;
import kz.commerce.service.ProductService;
import org.antlr.v4.runtime.misc.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;


@RestController
@RequestMapping("/api/cart")
@CrossOrigin("*")
public class CartController {

    private final ProductService productService;
    private final CartService cartService;

    @Autowired
    public CartController(ProductService service, CartService cartService){
        this.productService = service;
        this.cartService = cartService;
    }

    @PostMapping("/product")
    public ResponseEntity<?> create(@RequestBody CartGetDTO data) {
        System.out.println("PostMapping for cart");
        Product product1 = productService.getById(data.getProductId());
        if (product1.getId() > 0 && product1.getStockQuantity() >= 1) {
            cartService.save(data.getProductId(), data.getQuantity());
            System.out.println("Saved To Cart Succes");
            return new ResponseEntity<>(HttpStatus.OK);
        } else if(product1.getStockQuantity() < 1){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } else{
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/checkout")
    public ResponseEntity<?> pursache(@RequestBody List<CartGetDTO> datas){
        for (CartGetDTO data : datas){
            try{
                productService.pursache(data.getProductId(), data.getQuantity());
                cartService.delete(data.getProductId());
            }catch (NoEnuoghProduct e) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/product/{productId}")
    public ResponseEntity<?> update(@PathVariable("productId") int prodId, @RequestBody UpdateQuantityDTO data) {
        Product product = productService.getById(prodId);
        if (product.getId() > 0 && data.getQuantity() <= product.getStockQuantity()) {
            cartService.save(prodId, data.getQuantity());
            return new ResponseEntity<>(HttpStatus.OK);
        }else {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/products")
    public ResponseEntity<List<Pair<Product, Integer>>> getAll(){
        System.out.println("Get Mapping");
        return new ResponseEntity<>(new ArrayList<>(cartService.getAllProductPairs()) , HttpStatus.OK);
    }

    @DeleteMapping("/product/{productId}")
    public void delete(@PathVariable("productId") int productId){
        cartService.delete(productId);
    }
}
