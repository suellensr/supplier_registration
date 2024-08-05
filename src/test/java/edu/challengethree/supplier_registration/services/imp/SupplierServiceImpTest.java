package edu.challengethree.supplier_registration.services.imp;


import edu.challengethree.supplier_registration.DTOs.AddressDTO;
import edu.challengethree.supplier_registration.DTOs.SupplierCreationDTO;
import edu.challengethree.supplier_registration.DTOs.SupplierDTO;
import edu.challengethree.supplier_registration.DTOs.SupplierSimplifiedDTO;
import edu.challengethree.supplier_registration.exceptions.ResourceNotFoundException;
import edu.challengethree.supplier_registration.exceptions.SupplierAlreadyRegisteredException;
import edu.challengethree.supplier_registration.model.entities.Supplier;
import edu.challengethree.supplier_registration.model.enums.PersonType;
import edu.challengethree.supplier_registration.repositories.SupplierRepository;
import edu.challengethree.supplier_registration.services.impl.SupplierServiceImpl;
import edu.challengethree.supplier_registration.services.utils.SupplierMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SupplierServiceImpTest {

    @Mock
    private SupplierRepository supplierRepository;

    @InjectMocks
    private SupplierServiceImpl supplierService;

    @Test
    @DisplayName("Should create an supplier successfully")
    public void createSupplierSuccess() {

        // Arrange
        SupplierCreationDTO supplierCreationDTO  = getSupplierCreationDTO();
        String documentNumber = "65.239.655/0001-62";
        String userId = "user01";

        Supplier supplier = SupplierMapper.supplierCreationDTOToSupplier(supplierCreationDTO, userId);
        SupplierDTO supplierDTO = SupplierMapper.supplierToSupplierDTO(supplier);

        when(supplierRepository.findByDocumentNumber(documentNumber)).thenReturn(Optional.empty());

        when(supplierRepository.save(any(Supplier.class))).thenReturn(supplier);

        // Act
        SupplierDTO createdSupplier = supplierService.createSupplier(supplierCreationDTO, userId);

        // Assert
        assertThat(createdSupplier)
                .usingRecursiveComparison()
                .isEqualTo(supplierDTO);
    }

    @Test
    @DisplayName("Should create an supplier should throw SupplierAlreadyRegisteredException")
    public void createSupplierException() {

        SupplierCreationDTO supplierCreationDTO = getSupplierCreationDTO();
        String documentNumber = "65.239.655/0001-62";
        String userId = "user01";
        Optional<Supplier> supplierFound = Optional.of(SupplierMapper.supplierCreationDTOToSupplier(supplierCreationDTO, userId));

        when(supplierRepository.findByDocumentNumber(documentNumber)).thenReturn(supplierFound);

        //Act and Assert
        assertThatThrownBy(() -> supplierService.createSupplier(supplierCreationDTO, userId))
                .isInstanceOf(SupplierAlreadyRegisteredException.class)
                .hasMessageContaining("Este fornecedor já está cadastrado.");
    }

    @Test
    @DisplayName("Should update a supplier successfully")
    public void updateSupplierSuccess() {
        // Arrange
        SupplierCreationDTO supplierCreationDTO = getSupplierCreationDTO();
        String id = "supplier01";
        String userId = "user01";
        Supplier existingSupplier = SupplierMapper.supplierCreationDTOToSupplier(supplierCreationDTO, userId);
        existingSupplier.setId(id);
        supplierCreationDTO.setSupplierName("Teste Update");
        supplierCreationDTO.setContactName("Contact Test Update Name");

        Supplier expectedSupplier = SupplierMapper.supplierCreationDTOToSupplier(supplierCreationDTO, userId);
        expectedSupplier.setId(id);

        when(supplierRepository.findByIdAndUserId(id, userId)).thenReturn(Optional.of(existingSupplier));
        when(supplierRepository.save(any(Supplier.class))).thenReturn(expectedSupplier);

        // Act
        SupplierDTO updatedSupplier = supplierService.updateSupplier(id, userId, supplierCreationDTO);

        // Assert
        assertThat(updatedSupplier)
                .usingRecursiveComparison()
                .isEqualTo(SupplierMapper.supplierToSupplierDTO(expectedSupplier));
    }

    @Test
    @DisplayName("Should throw ResourceNotFoundException when updating a supplier")
    public void updateSupplierNotFound() {
        // Arrange
        SupplierCreationDTO supplierCreationDTO = getSupplierCreationDTO();
        String id = "supplier01";
        String userId = "user01";
        when(supplierRepository.findByIdAndUserId(id, userId)).thenReturn(Optional.empty());

        // Act and Assert
        assertThatThrownBy(() -> supplierService.updateSupplier(id, userId, supplierCreationDTO))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Supplier with id " + id + " not found.");
    }

    @Test
    @DisplayName("Should return all suppliers for a user")
    public void getAllSuppliersSuccess() {
        String userId = "user01";
        Supplier supplier1 = SupplierMapper.supplierCreationDTOToSupplier(getSupplierCreationDTO(), userId);
        Supplier supplier2 = SupplierMapper.supplierCreationDTOToSupplier(getSupplierCreationDTO(), userId);

        List<Supplier> suppliers = Arrays.asList(supplier1, supplier2);
        when(supplierRepository.findAllByUserId(userId)).thenReturn(suppliers);

        // Act
        List<SupplierSimplifiedDTO> allSuppliers = supplierService.getAllSuppliers(userId);

        // Assert
        assertThat(allSuppliers.size()).isEqualTo(2);
        assertEquals(supplier1.getSupplierName(), allSuppliers.get(0).getSupplierName());
        assertEquals(supplier2.getSupplierName(), allSuppliers.get(1).getSupplierName());
    }

    @Test
    @DisplayName("Should return supplier by ID for a user")
    public void getSupplierByIdSuccess() throws ResourceNotFoundException {
        // Arrange
        String userId = "user01";
        String supplierId = "supplier01";
        Supplier existingSupplier = SupplierMapper.supplierCreationDTOToSupplier(getSupplierCreationDTO(), userId);
        existingSupplier.setId(supplierId);

        when(supplierRepository.findByIdAndUserId(supplierId, userId)).thenReturn(Optional.of(existingSupplier));

        // Act
        SupplierDTO supplierDTO = supplierService.getSupplierById(supplierId, userId);

        // Assert
        assertThat(existingSupplier)
                .usingRecursiveComparison()
                .isEqualTo(supplierDTO);
    }

    @Test
    @DisplayName("Should delete supplier by ID for a user")
    public void deleteSupplierSuccess() throws ResourceNotFoundException {
        // Arrange
        String userId = "user01";
        String supplierId = "supplier01";
        Supplier existingSupplier = SupplierMapper.supplierCreationDTOToSupplier(getSupplierCreationDTO(), userId);
        existingSupplier.setId(supplierId);

        when(supplierRepository.findByIdAndUserId(supplierId, userId)).thenReturn(Optional.of(existingSupplier));
        doNothing().when(supplierRepository).deleteById(supplierId);

        // Act
        String result = supplierService.deleteSupplier(supplierId, userId);

        // Assert
        assertEquals("Supplier with id " + supplierId + " has been deleted successfully.", result);
        verify(supplierRepository, times(1)).deleteById(supplierId);
    }

    private SupplierCreationDTO getSupplierCreationDTO() {

        List<String> phoneNumbers = Arrays.asList("+55 (11) 99999-9999", "+55 (11) 2222-2222");

        SupplierCreationDTO supplierCreationDTO = new SupplierCreationDTO();
        supplierCreationDTO.setSupplierName("Supplier Test Name");
        supplierCreationDTO.setContactName("Contact Test Name");
        supplierCreationDTO.setContactEmail("contact@example.com");
        supplierCreationDTO.setPersonType(PersonType.COMPANY);
        supplierCreationDTO.setDocumentNumber("65.239.655/0001-62");
        supplierCreationDTO.setPhoneNumbers(phoneNumbers);
        supplierCreationDTO.setAddressDTO(getAddressDTO());
        supplierCreationDTO.setActivityDescription("Esse é apenas um teste.");

        return supplierCreationDTO;
    }

    private AddressDTO getAddressDTO() {
        AddressDTO addressDTO = new AddressDTO();
        addressDTO.setCep("28941-068");
        addressDTO.setLogradouro("Rua do Teste");
        addressDTO.setNumero("100");
        addressDTO.setComplemento("Complemento");
        addressDTO.setBairro("Centro");
        addressDTO.setLocalidade("Qualquer um");
        addressDTO.setUf("LA");
        addressDTO.setPais("Teste");

        return addressDTO;
    }
}
