package org.rail.spring2024.service;

import lombok.RequiredArgsConstructor;
import org.rail.spring2024.dto.ProductDTO;
import org.rail.spring2024.exception.ProductNotFoundException;
import org.rail.spring2024.mapper.ProductMapper;
import org.rail.spring2024.model.Product;
import org.rail.spring2024.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Class separating presentation layer from persistent layer of product
 */
@Service
@RequiredArgsConstructor
@Transactional
public class ProductService {
    /**
     * property for working with persistent layer
     */
    private final ProductRepository productRepository;

    /**
     * property for mapping {@link Product} and {@link ProductDTO} class
     */
    private final ProductMapper mapper;

    /**
     * searching for all products in the database
     * @return list of {@link ProductDTO}
     */
    @Transactional(readOnly = true)
    public List<ProductDTO> getAllProducts() {
        return productRepository.findAll().stream()
                .map(mapper::mapToDto)
                .toList();
    }

    /**
     * saves passed {@link ProductDTO} object to the database
     * @param productDTO {@link ProductDTO} object that was passed by the user
     * @return status of method work
     */
    public String saveProduct(ProductDTO productDTO) {
        productRepository.findByName(productDTO.getName()).ifPresent(product -> {
            throw new RuntimeException("Product with that name already exists");
        });

        Product product = mapper.mapToEntity(productDTO, null);
        product.setDateCreated(LocalDate.now());
        product.setDateQuantityUpdated(LocalDateTime.now());

        productRepository.save(product);
        return "product saved";
    }

    /**
     * changes existing {@link Product} object to new one
     * @param productName name of the {@link Product} to be changed
     * @param productDTO {@link ProductDTO} object that was passed by the user
     * @return status of method work
     * @throws ProductNotFoundException if {@link Product} with provided name is not found in database
     */
    public String putProduct(String productName, ProductDTO productDTO) throws ProductNotFoundException {
        Product product = productRepository.findByName(productName)
                .orElseThrow(() -> new ProductNotFoundException("Product not found with name: " + productName));
        if (!productName.equals(productDTO.getName())) {
            productRepository.findByName(productDTO.getName()).ifPresent(p -> {
                throw new RuntimeException("Product with that name already exists");
            });
        }
        Product mappedToEntity = mapper.mapToEntity(productDTO, product.getUuid());
        mappedToEntity.setDateQuantityUpdated(LocalDateTime.now());
        mappedToEntity.setDateCreated(product.getDateCreated());
        productRepository.save(mappedToEntity);
        return "product updated";
    }

    /**
     * deletes {@link Product} from the database
     * @param productName name of the {@link Product} that user wants to delete
     * @return string with operation work status
     * @throws ProductNotFoundException if {@link Product} with provided name is not found in database
     */
    public String deleteProduct(String productName) throws ProductNotFoundException {
        Product product = productRepository.findByName(productName)
                .orElseThrow(() -> new ProductNotFoundException("Product not found with name: " + productName));
        productRepository.delete(product);
        return "product deleted";
    }
}
