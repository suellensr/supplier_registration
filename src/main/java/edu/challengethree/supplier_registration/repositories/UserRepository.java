package edu.challengethree.supplier_registration.repositories;

import edu.challengethree.supplier_registration.model.entities.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends MongoRepository<User, String> {
    User findByEmail(String email);
}
