package com.enoumanah.pollcreator.poll_api.repository;

import com.enoumanah.pollcreator.poll_api.model.Option;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OptionRepository extends JpaRepository<Option, Long> {
}
