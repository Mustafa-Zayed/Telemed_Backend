package com.mustafaz.telemed.role.service;

import com.mustafaz.telemed.res.Response;
import com.mustafaz.telemed.role.entity.Role;

import java.util.List;

public interface RoleService {
    Response<Role> createRole(Role roleRequest);

    Response<Role> updateRole(Role roleRequest);

    Response<List<Role>> getAllRoles();

    Response<?> deleteRole(Long id);
}