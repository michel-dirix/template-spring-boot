package fr.midix.template.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import fr.midix.template.entities.User;

@Repository
public interface UserRepository extends MongoRepository<User, String> {

  User findByEmail(String email);

  boolean existsByEmailIgnoreCase(String email);
}
