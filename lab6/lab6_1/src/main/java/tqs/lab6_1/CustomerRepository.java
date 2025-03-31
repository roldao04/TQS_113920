package tqs.lab6_1;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
    // You can define custom query methods if needed
    Customer findByName(String name);
}
