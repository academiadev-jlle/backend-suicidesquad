package br.com.academiadev.suicidesquad.controller;

import java.io.File;
import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.academiadev.suicidesquad.service.AmazonService;

@RestController
@CrossOrigin
public class AmazonController {
	
	@Autowired
	private AmazonService amazonService;
	
	@PostMapping("/amazon")
	public void saveImageToAmazon(File imageObj, String imageKey) throws IOException {
		amazonService.saveImageToAmazon(imageObj, imageKey);
	}
	
	@DeleteMapping("/amazon")
	public void deleteImageFromAmazon(String imageKey) {
		amazonService.deleteImageFromAmazon(imageKey);
	}
}
