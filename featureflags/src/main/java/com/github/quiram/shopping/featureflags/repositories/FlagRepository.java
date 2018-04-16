package com.github.quiram.shopping.featureflags.repositories;

import com.github.quiram.shopping.featureflags.model.Flag;
import org.springframework.data.repository.CrudRepository;

public interface FlagRepository extends CrudRepository<Flag, Long> {
    Flag findByName(String name);
}
