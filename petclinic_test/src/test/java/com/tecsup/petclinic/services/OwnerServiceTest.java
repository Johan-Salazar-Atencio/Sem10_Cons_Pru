package com.tecsup.petclinic.services;

import com.tecsup.petclinic.dtos.OwnerDTO;
import com.tecsup.petclinic.exceptions.OwnerNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Slf4j
public class OwnerServiceTest {

	@Autowired
	private OwnerService ownerService;

	/**
	 *
	 */
	@Test
	public void testFindOwnerById() {

		final String FIRST_NAME_EXPECTED = "George";
		final String LAST_NAME_EXPECTED = "Franklin";

		Integer ID = 1;

		OwnerDTO owner = null;

		try {
			owner = this.ownerService.findById(ID);
		} catch (OwnerNotFoundException e) {
			fail(e.getMessage());
		}
		log.info("" + owner);

		assertEquals(FIRST_NAME_EXPECTED, owner.getFirstName());
		assertEquals(LAST_NAME_EXPECTED, owner.getLastName());
	}

	/**
	 *
	 */
	@Test
	public void testFindOwnerByFirstName() {

		String FIND_FIRST_NAME = "George";
		int SIZE_EXPECTED = 1;

		List<OwnerDTO> owners = this.ownerService.findByFirstName(FIND_FIRST_NAME);

		assertEquals(SIZE_EXPECTED, owners.size());
		assertEquals(FIND_FIRST_NAME, owners.get(0).getFirstName());
	}

	/**
	 *
	 */
	@Test
	public void testCreateOwner() {

		String FIRST_NAME = "Johan";
		String LAST_NAME = "Salazar";
		String ADDRESS = "Av. Cascanueces 2221";
		String CITY = "Lima";
		String TELEPHONE = "987654321";

		OwnerDTO ownerDTO = OwnerDTO.builder()
				.firstName(FIRST_NAME)
				.lastName(LAST_NAME)
				.address(ADDRESS)
				.city(CITY)
				.telephone(TELEPHONE)
				.build();

		OwnerDTO newOwnerDTO = this.ownerService.create(ownerDTO);

		log.info("OWNER CREATED :" + newOwnerDTO);

		assertNotNull(newOwnerDTO.getId());
		assertEquals(FIRST_NAME, newOwnerDTO.getFirstName());
		assertEquals(LAST_NAME, newOwnerDTO.getLastName());
		assertEquals(ADDRESS, newOwnerDTO.getAddress());
		assertEquals(CITY, newOwnerDTO.getCity());
		assertEquals(TELEPHONE, newOwnerDTO.getTelephone());
	}

	/**
	 *
	 */
	@Test
	public void testUpdateOwner() {

		String FIRST_NAME = "Ana";
		String LAST_NAME = "Torres";
		String ADDRESS = "Jr. Lampa 123";
		String CITY = "Lima";
		String TELEPHONE = "911111111";

		String UP_FIRST_NAME = "Ana Maria";
		String UP_LAST_NAME = "Torres Diaz";
		String UP_ADDRESS = "Av. Arequipa 456";
		String UP_CITY = "Arequipa";
		String UP_TELEPHONE = "922222222";

		OwnerDTO ownerDTO = OwnerDTO.builder()
				.firstName(FIRST_NAME)
				.lastName(LAST_NAME)
				.address(ADDRESS)
				.city(CITY)
				.telephone(TELEPHONE)
				.build();

		// ------------ Create ---------------

		log.info(">" + ownerDTO);
		OwnerDTO ownerDTOCreated = this.ownerService.create(ownerDTO);
		log.info(">>" + ownerDTOCreated);

		// ------------ Update ---------------

		// Prepare data for update
		ownerDTOCreated.setFirstName(UP_FIRST_NAME);
		ownerDTOCreated.setLastName(UP_LAST_NAME);
		ownerDTOCreated.setAddress(UP_ADDRESS);
		ownerDTOCreated.setCity(UP_CITY);
		ownerDTOCreated.setTelephone(UP_TELEPHONE);

		// Execute update
		OwnerDTO upgradeOwnerDTO = this.ownerService.update(ownerDTOCreated);
		log.info(">>>>" + upgradeOwnerDTO);

		//            EXPECTED        ACTUAL
		assertEquals(ownerDTOCreated.getId(), upgradeOwnerDTO.getId());
		assertEquals(UP_FIRST_NAME, upgradeOwnerDTO.getFirstName());
		assertEquals(UP_LAST_NAME, upgradeOwnerDTO.getLastName());
		assertEquals(UP_ADDRESS, upgradeOwnerDTO.getAddress());
		assertEquals(UP_CITY, upgradeOwnerDTO.getCity());
		assertEquals(UP_TELEPHONE, upgradeOwnerDTO.getTelephone());
	}

	/**
	 *
	 */
	@Test
	public void testDeleteOwner() {

		String FIRST_NAME = "Luis";
		String LAST_NAME = "Quispe";
		String ADDRESS = "Calle Los Pinos 789";
		String CITY = "Cusco";
		String TELEPHONE = "933333333";

		// ------------ Create ---------------

		OwnerDTO ownerDTO = OwnerDTO.builder()
				.firstName(FIRST_NAME)
				.lastName(LAST_NAME)
				.address(ADDRESS)
				.city(CITY)
				.telephone(TELEPHONE)
				.build();

		OwnerDTO newOwnerDTO = this.ownerService.create(ownerDTO);
		log.info("" + newOwnerDTO);

		// ------------ Delete ---------------

		try {
			this.ownerService.delete(newOwnerDTO.getId());
		} catch (OwnerNotFoundException e) {
			fail(e.getMessage());
		}

		// ------------ Validation ---------------

		assertThrows(OwnerNotFoundException.class,
				() -> this.ownerService.findById(newOwnerDTO.getId()));
	}
}
