package kz.commerce.controller;

import kz.commerce.model.Product;
import kz.commerce.service.CartService;
import kz.commerce.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;


@RestController
@RequestMapping("/api")
@CrossOrigin("*")
public class ProductController {

    private final ProductService service;
    private final CartService cartService;

    @Autowired
    public ProductController(ProductService service, CartService cartService){
        this.service = service;
        this.cartService = cartService;
    }

    @GetMapping("/products")
    public ResponseEntity<List<Product>> getAll(){
        return new ResponseEntity<>(service.getAll(), HttpStatus.OK);
    }

    @GetMapping("product/{id}")
    public ResponseEntity<Product> getById(@PathVariable("id") int id){
        Product product = service.getById(id);
        if(product.getId() > 0)
            return new ResponseEntity<>(product, HttpStatus.OK);
        else
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/products/search")
    public ResponseEntity<List<Product>> search(@RequestParam String keyword){
        List<Product> products = service.searchForProducts(keyword);
        return new ResponseEntity<>(products, HttpStatus.OK);
    }

    @DeleteMapping("product/{id}")
    public void delete(@PathVariable("id") int id){
        service.delete(id);
        cartService.delete(id);
    }

    @PostMapping("/product")
    public ResponseEntity<?> create(@RequestPart Product product, @RequestPart MultipartFile imageFile){
        try{
            service.save(product, imageFile);
            return new ResponseEntity<>(HttpStatus.CREATED);
        }catch (IOException e){
            return  new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/product/{productId}/image")
    public ResponseEntity<byte[]> getImage(@PathVariable("productId") int id){
        System.out.println("Image Getting");
        Product product = service.getById(id);
        if(product.getId() > 0)
            return new ResponseEntity<>(product.getImageData(), HttpStatus.OK);
        else
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }


    @PutMapping("product/{id}")
    public ResponseEntity<?> update(@RequestPart Product product, @RequestPart MultipartFile imageFile){
        try{
            service.save(product, imageFile);
            return new ResponseEntity<>(HttpStatus.CREATED);
        }catch (IOException e){
            return  new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
