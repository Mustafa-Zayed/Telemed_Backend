package com.mustafaz.telemed.role.repo;

import com.mustafaz.telemed.role.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepo extends JpaRepository<Role, Long> {

}
