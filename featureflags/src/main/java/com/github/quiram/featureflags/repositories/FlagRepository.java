package com.github.quiram.featureflags.repositories;

import com.github.quiram.featureflags.model.Flag;
import org.springframework.data.repository.CrudRepository;

public interface FlagRepository extends CrudRepository<Flag, String> {
}
