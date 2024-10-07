package org.tinkoff.userservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.tinkoff.userservice.entity.User;

public interface UserRepository extends JpaRepository<User, Integer> {

}