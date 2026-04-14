package com.example.bookstore.catalog.domain;

import com.example.bookstore.catalog.ApplicationProperties;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ProductService {

    private final ProductRepository productRepository;
    private final ApplicationProperties properties;

    ProductService(ProductRepository productRepository, ApplicationProperties properties) {
        this.productRepository = productRepository;
        this.properties = properties;
    }

    public PagedResult<Product> getAllProducts(int page) {
        Sort sort = Sort.by("name").ascending();
        page = page > 0 ? page - 1 : 0;
        Pageable pageable = PageRequest.of(page, properties.pageSize(), sort);
        Page<Product> productPages = productRepository.findAll(pageable).map(ProductMapper::toProduct);

        return new PagedResult<>(
                productPages.getContent(),
                productPages.getNumber() + 1,
                productPages.getTotalPages(),
                productPages.getTotalElements(),
                productPages.isFirst(),
                productPages.isLast(),
                productPages.hasNext(),
                productPages.hasPrevious());
    }

    public Product getProductByCode(String code) {
        return productRepository
                .findByCode(code)
                .map(ProductMapper::toProduct)
                .orElseThrow(() -> ProductNotFoundException.forCode(code));
    }
}
