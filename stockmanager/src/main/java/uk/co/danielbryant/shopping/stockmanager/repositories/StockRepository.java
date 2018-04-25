package uk.co.danielbryant.shopping.stockmanager.repositories;

import org.springframework.data.repository.CrudRepository;
import uk.co.danielbryant.shopping.stockmanager.model.v1.Stock;

public interface StockRepository extends CrudRepository<Stock, String> {
}
