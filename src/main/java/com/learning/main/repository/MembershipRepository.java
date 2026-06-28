package com.learning.main.repository;
import org.springframework.data.jpa.repository.JpaRepository;

import com.learning.main.enm.MembershipType;
import com.learning.main.model.Customer;
import com.learning.main.model.Membership;

import java.time.LocalDate;

import java.util.List;
import java.util.Optional;

public interface MembershipRepository extends JpaRepository<Membership, Long> {
    List<Membership> findByCustomer(Customer customer);
    List<Membership> findByMembershipType(MembershipType type);
    List<Membership> findByEndDateAfter(LocalDate date);
    
    
    Optional<Membership> findByCustomerAndEndDateAfter(Customer customer, LocalDate date);

}