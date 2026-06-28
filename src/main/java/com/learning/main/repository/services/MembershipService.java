package com.learning.main.repository.services;

import com.learning.main.enm.MembershipType;
import com.learning.main.model.Customer;
import com.learning.main.model.Membership;
import com.learning.main.repository.MembershipRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class MembershipService {

    private final MembershipRepository membershipRepository;

    @Autowired
    public MembershipService(MembershipRepository membershipRepository) {
        this.membershipRepository = membershipRepository;
    }

    public Membership saveMembership(Membership membership) {
        return membershipRepository.save(membership);
    }

    public Optional<Membership> getMembershipById(Long id) {
        return membershipRepository.findById(id);
    }

    public List<Membership> getAllMemberships() {
        return membershipRepository.findAll();
    }

    public void deleteMembership(Long id) {
        membershipRepository.deleteById(id);
    }

    public List<Membership> findMembershipsByCustomer(Customer customer) {
        return membershipRepository.findByCustomer(customer);
    }

    // This method finds an active membership for a given customer
    // Assuming 'active' means the end date is in the future relative to the current date.
    // It returns the first found active membership, assuming one active membership per customer.
    public Optional<Membership> findActiveMembershipByCustomer(Customer customer) {
        return membershipRepository.findByCustomerAndEndDateAfter(customer, LocalDate.now())
                                   .stream()
                                   .findFirst();
    }

    public List<Membership> findMembershipsByMembershipType(MembershipType type) {
        return membershipRepository.findByMembershipType(type);
    }

    public List<Membership> findMembershipsByEndDateAfter(LocalDate date) {
        return membershipRepository.findByEndDateAfter(date);
    }

    // New method to create a membership with predefined details based on type
    public Membership createNewMembership(Customer customer, MembershipType type) {
        Membership membership = new Membership();
        membership.setCustomer(customer);
        membership.setMembershipType(type);
        membership.setStartDate(LocalDate.now());

        // Set end date, fee, and benefits based on the membership type
        switch (type) {
            case SEASON:
                membership.setEndDate(LocalDate.now().plusYears(1)); // 1 year
                membership.setFee(500.00);
                membership.setBenefits("Access to all regular season games, special events, 20% merchandise discount.");
                break;
            case PREMIUM:
                membership.setEndDate(LocalDate.now().plusMonths(6)); // 6 months
                membership.setFee(250.00);
                membership.setBenefits("Priority seating, pre-sale access to playoff tickets, 15% merchandise discount.");
                break;
            case STANDARD:
                membership.setEndDate(LocalDate.now().plusMonths(3)); // 3 months
                membership.setFee(100.00);
                membership.setBenefits("General admission to games, 10% merchandise discount.");
                break;
            case FAMILY:
                membership.setEndDate(LocalDate.now().plusYears(1)); // 1 year
                membership.setFee(800.00);
                membership.setBenefits("Benefits for up to 4 family members, 25% merchandise discount, family event invitations.");
                break;
            case STUDENT:
                membership.setEndDate(LocalDate.now().plusYears(1)); // 1 year
                membership.setFee(80.00);
                membership.setBenefits("Discounted access to games (with valid ID), 5% merchandise discount.");
                break;
            default:
                // Default case or throw an exception if an unsupported type is passed
                membership.setEndDate(LocalDate.now().plusMonths(1));
                membership.setFee(0.0);
                membership.setBenefits("No specific benefits for unknown type.");
                break;
        }
        return membership;
    }
}