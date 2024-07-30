package edu.challengethree.supplier_registration.repositories;

import edu.challengethree.supplier_registration.model.entities.Supplier;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SupplierRepository extends MongoRepository<Supplier, String> {
    List<Supplier> findAllByUserId(String userId);
    Optional<Supplier> findByIdAndUserId(String id, String userId);
    Optional<Supplier> findByDocumentNumber(String documentNumber);
}

