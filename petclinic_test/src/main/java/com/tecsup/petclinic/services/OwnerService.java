package com.tecsup.petclinic.services;

import com.tecsup.petclinic.dtos.OwnerDTO;
import com.tecsup.petclinic.exceptions.OwnerNotFoundException;

import java.util.List;

/**
 *
 * @author jsalazar
 *
 */
public interface OwnerService {

	OwnerDTO create(OwnerDTO ownerDTO);

	OwnerDTO update(OwnerDTO ownerDTO);

	void delete(Integer id) throws OwnerNotFoundException;

	OwnerDTO findById(Integer id) throws OwnerNotFoundException;

	List<OwnerDTO> findByFirstName(String firstName);

	List<OwnerDTO> findByLastName(String lastName);

	List<OwnerDTO> findAll();
}
