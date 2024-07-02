package edu.challengethree.supplier_registration.services.utils;

import edu.challengethree.supplier_registration.DTOs.AddressDTO;
import edu.challengethree.supplier_registration.model.entities.Address;
import org.springframework.stereotype.Component;

@Component
public class AddressMapper {

    public static Address addressDTOToAddress(AddressDTO addressDTO) {
        Address address = new Address();
        address.setZipCode(addressDTO.getCep());
        address.setStreet(addressDTO.getLogradouro());
        address.setNumber(addressDTO.getNumero());
        address.setComplement(addressDTO.getComplemento());
        address.setNeighborhood(addressDTO.getBairro());
        address.setCity(addressDTO.getLocalidade());
        address.setState(addressDTO.getUf());
        address.setCountry(addressDTO.getPais());

        return address;
    }
}
