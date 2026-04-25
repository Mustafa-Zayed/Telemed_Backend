package com.mustafaz.telemed.role.service;

import com.mustafaz.telemed.exceptions.NotFoundException;
import com.mustafaz.telemed.res.Response;
import com.mustafaz.telemed.role.entity.Role;
import com.mustafaz.telemed.role.repo.RoleRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {
    private final RoleRepo roleRepo;

    @Override
    public Response<Role> createRole(Role roleRequest) {
        Role savedRole = roleRepo.save(roleRequest);

        return Response.<Role>builder()
                .statusCode(HttpStatus.CREATED.value())
                .message("Role Saved Successfully")
                .data(savedRole)
                .build();
    }

    @Override
    public Response<Role> updateRole(Role roleRequest) {
        Role role = roleRepo.findById(roleRequest.getId())
                .orElseThrow(() -> new NotFoundException("Role not found"));

        role.setName(roleRequest.getName());

        Role updatedRole = roleRepo.save(role);

        return Response.<Role>builder()
                .statusCode(200)
                .message("Role updated Successfully")
                .data(updatedRole)
                .build();

    }

    @Override
    public Response<List<Role>> getAllRoles() {
        List<Role> roles = roleRepo.findAll();

        return Response.<List<Role>>builder()
                .statusCode(200)
                .message("Roles retrieved successfully")
                .data(roles)
                .build();
    }

    @Override
    public Response<?> deleteRole(Long id) {

        if (!roleRepo.existsById(id)) {
            throw new NotFoundException("Role Not Found");
        }
        roleRepo.deleteById(id);

        return Response.builder()
                .statusCode(HttpStatus.OK.value())
                .message("Role deleted successfully")
                .build();
    }
}