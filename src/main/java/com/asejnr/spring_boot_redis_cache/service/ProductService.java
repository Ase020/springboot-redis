package com.asejnr.spring_boot_redis_cache.service;

import com.asejnr.spring_boot_redis_cache.dto.ProductDto;
import com.asejnr.spring_boot_redis_cache.entity.Product;
import com.asejnr.spring_boot_redis_cache.repository.ProductRepository;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    //    private final CacheManager cacheManager;

    public ProductService(ProductRepository productRepository, CacheManager cacheManager) {
        this.productRepository = productRepository;
        //        this.cacheManager = cacheManager;
    }

    @CachePut(value = "PRODUCT_CACHE", key = "#result.id()")
    public ProductDto createProduct(ProductDto productDto) {
        var product = new Product();
        product.setName(productDto.name());
        product.setPrice(productDto.price());

        Product savedProduct = productRepository.save(product);

        //        OR
        //        Cache productCache = cacheManager.getCache("PRODUCT_CACHE");
        //        assert productCache != null;
        //        productCache.put(savedProduct.getId(), savedProduct);
        return new ProductDto(savedProduct.getId(), savedProduct.getName(), savedProduct.getPrice());
    }

    @Cacheable(value = "PRODUCT_CACHE", key = "#productId")
    public ProductDto getProduct(Long productId) {
        Product product = productRepository
                .findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("Cannot find product with id " + productId));
        return new ProductDto(product.getId(), product.getName(), product.getPrice());
    }

    @CachePut(value = "PRODUCT_CACHE", key = "#result.id()")
    public ProductDto updateProduct(ProductDto productDto) {
        Long productId = productDto.id();
        Product product = productRepository
                .findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("Cannot find product with id " + productId));

        product.setName(productDto.name());
        product.setPrice(productDto.price());

        Product updatedProduct = productRepository.save(product);
        return new ProductDto(updatedProduct.getId(), updatedProduct.getName(), updatedProduct.getPrice());
    }

    @CacheEvict(value = "PRODUCT_CACHE", key = "#productId")
    public void deleteProduct(Long productId) {
        productRepository.deleteById(productId);
    }
}
