package com.akvasoft.atlenta.repo;

import com.akvasoft.atlenta.modal.Atlanta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface repository extends JpaRepository<Atlanta, Integer> {
    Atlanta findTopByDateEqualsAndRecordNo(String date, String rec_no);
}
