package edu.challengethree.supplier_registration.services.interfaces;

import edu.challengethree.supplier_registration.DTOs.SupplierCreationDTO;
import edu.challengethree.supplier_registration.DTOs.SupplierDTO;
import edu.challengethree.supplier_registration.DTOs.SupplierSimplifiedDTO;
import edu.challengethree.supplier_registration.exceptions.ResourceNotFoundException;

import java.util.List;

public interface SupplierService {
    SupplierDTO createSupplier(SupplierCreationDTO supplierCreationDTO, String userId);
    SupplierDTO updateSupplier(String id, String userId, SupplierCreationDTO supplierCreationDTO) throws ResourceNotFoundException;
    List<SupplierSimplifiedDTO> getAllSuppliers(String userId);
    SupplierDTO getSupplierById(String id, String userId) throws ResourceNotFoundException;
    String deleteSupplier(String id, String userId) throws ResourceNotFoundException;
    SupplierDTO addPhone(String id,String userId, String phoneNumber) throws ResourceNotFoundException;
    SupplierDTO removePhone(String id, String userId, String phoneNumber) throws ResourceNotFoundException;
}
