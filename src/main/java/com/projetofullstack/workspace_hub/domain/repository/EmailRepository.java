package com.projetofullstack.workspace_hub.domain.repository;

import com.projetofullstack.workspace_hub.domain.entities.Email;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmailRepository extends JpaRepository<Email, Long> {
}
