package com.minho.ownit.resale;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ResaleCategoryRepo extends CrudRepository<ResaleCategory, Integer>{

}
