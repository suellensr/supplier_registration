package edu.challengethree.supplier_registration.controllers;

import edu.challengethree.supplier_registration.DTOs.SupplierCreationDTO;
import edu.challengethree.supplier_registration.DTOs.SupplierDTO;
import edu.challengethree.supplier_registration.DTOs.SupplierSimplifiedDTO;
import edu.challengethree.supplier_registration.exceptions.ResourceNotFoundException;
import edu.challengethree.supplier_registration.model.entities.User;
import edu.challengethree.supplier_registration.services.interfaces.SupplierService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@CrossOrigin("*")
@Validated
public class SupplierController {

    @Autowired
    private SupplierService supplierService;

    @PostMapping("/supplier-register")
    public ResponseEntity<SupplierDTO> registerSupplier(@Valid @RequestBody SupplierCreationDTO supplierCreationDTO, Authentication authentication) {
        String userId = ((User) authentication.getPrincipal()).getId();
        SupplierDTO createdSupplier = supplierService.createSupplier(supplierCreationDTO,userId);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdSupplier);
    }

    @PutMapping("/supplier-update/{id}")
    public ResponseEntity<SupplierDTO> updateSupplier(@PathVariable String id, @Valid @RequestBody SupplierCreationDTO supplierCreationDTO, Authentication authentication) {
        try {
            String userId = ((User) authentication.getPrincipal()).getId();
            SupplierDTO updatedSupplier = supplierService.updateSupplier(id, userId, supplierCreationDTO);
            return ResponseEntity.status(HttpStatus.OK).body(updatedSupplier);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @GetMapping("/get-suppliers")
    public ResponseEntity<List<SupplierSimplifiedDTO>> getAllSuppliers(Authentication authentication) {
        String userId = ((User) authentication.getPrincipal()).getId();
        List<SupplierSimplifiedDTO> allSuppliers = supplierService.getAllSuppliers(userId);
        return ResponseEntity.status(HttpStatus.OK).body(allSuppliers);
    }

    @GetMapping("/get-supplier/{id}")
    public ResponseEntity<SupplierDTO> getSupplierById(@PathVariable String id, Authentication authentication) {
        try {
            String userId = ((User) authentication.getPrincipal()).getId();
            SupplierDTO supplierDTO = supplierService.getSupplierById(id, userId);
            return ResponseEntity.status(HttpStatus.OK).body(supplierDTO);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteSupplier(@PathVariable String id, Authentication authentication) {
        try {
            String userId = ((User) authentication.getPrincipal()).getId();
            String message = supplierService.deleteSupplier(id, userId);
            return ResponseEntity.status(HttpStatus.OK).body(message);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PatchMapping("/add-phone/{id}")
    public ResponseEntity<SupplierDTO> addPhone(@PathVariable String id, Authentication authentication, @RequestBody String phoneNumber) {
        try {
            String userId = ((User) authentication.getPrincipal()).getId();
            SupplierDTO updatedSupplier = supplierService.addPhone(id, userId, phoneNumber);
            return ResponseEntity.status(HttpStatus.OK).body(updatedSupplier);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @PatchMapping("/remove-phone/{id}")
    public ResponseEntity<SupplierDTO> removePhone(@PathVariable String id, Authentication authentication, @RequestBody String phoneNumber) {
        try {
            String userId = ((User) authentication.getPrincipal()).getId();
            SupplierDTO updatedSupplier = supplierService.removePhone(id, userId, phoneNumber);
            return ResponseEntity.status(HttpStatus.OK).body(updatedSupplier);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }
}