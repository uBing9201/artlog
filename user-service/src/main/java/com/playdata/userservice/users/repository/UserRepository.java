package com.playdata.userservice.users.repository;

import com.playdata.userservice.users.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

}
