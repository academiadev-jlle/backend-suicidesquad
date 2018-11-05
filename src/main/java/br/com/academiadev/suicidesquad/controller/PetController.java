package br.com.academiadev.suicidesquad.controller;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.academiadev.suicidesquad.entity.Pet;
import br.com.academiadev.suicidesquad.enums.Tipo;
import br.com.academiadev.suicidesquad.exception.ResourceNotFoundException;
import br.com.academiadev.suicidesquad.service.PetService;

@RestController
@RequestMapping("/v1/pet")
public class PetController {
    private final PetService petService;

    @Autowired
    public PetController(PetService petService) {
        this.petService = petService;
    }
    
    private String queryBuilder(String key, String value) {
    	String query = "";
    	try {
    		String atributeName;
    		if (key.equals("sexopet")) {
    			atributeName = key.substring(0, 1).toUpperCase()
    				.concat(key.substring(1,3).toLowerCase())
    				.concat(key.substring(3,4).toUpperCase())
    				.concat(key.substring(4).toLowerCase());
    		} else {
    			atributeName = key.substring(0, 1).toUpperCase()
    				.concat(key.substring(1).toLowerCase());
    		}
			
			String enumPath = "br.com.academiadev.suicidesquad.enums.".concat(atributeName);
			Class<?> enumClass = Class.forName(enumPath);
			Method enumMethod = enumClass
					.getDeclaredMethod("valueOf", String.class);

			String converterPath = "br.com.academiadev.suicidesquad.converter.".concat(atributeName).concat("Converter");
			Class<?> converterClass = Class.forName(converterPath);
			Method converterMethod = converterClass
					.getDeclaredMethod("convertToDatabaseColumn", enumClass);
			
			Integer databaseValue = (Integer) converterMethod
					.invoke(converterClass.newInstance(),
							enumMethod.invoke(null, value.toUpperCase())
							);
			
			query = query.concat(" AND ")
				.concat(key)
				.concat("=")
				.concat(databaseValue.toString());
		} catch (ClassNotFoundException | NoSuchMethodException | SecurityException
				| IllegalAccessException | IllegalArgumentException
				| InvocationTargetException | InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    	return query;
    }
    
    @GetMapping(value = "/find")
    public Page<Pet> getPets(
    		@RequestParam Map<String, String> allParams,
    		Pageable pageable) {
    	
    	StringBuilder customRequest = new StringBuilder("SELECT pet FROM Pet pet WHERE 1=1");
    	allParams.forEach((String key, String value) -> customRequest.append(queryBuilder(key, value)));
    	return petService.findAll(customRequest.toString(), pageable);
    }

    @PostMapping("/add")
    Pet createPet(@Valid @RequestBody Pet pet) {
        return petService.save(pet);
    }

    @GetMapping("/find/{idPet}")
    public Pet getPet(@PathVariable Long idPet) {
        return petService
                .findById(idPet)
                .orElseThrow(() -> new ResourceNotFoundException("Pet com o id " + idPet + " não foi encontrado"));
    }
}
