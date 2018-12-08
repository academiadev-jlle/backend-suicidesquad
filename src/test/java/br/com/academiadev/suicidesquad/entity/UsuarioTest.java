package br.com.academiadev.suicidesquad.entity;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.hamcrest.Matchers.equalTo;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UsuarioTest {
	
	@Test
	public void setarPets() {
		Usuario usuario = new Usuario();
		Pet pet = new Pet();
		List<Pet> pets = new ArrayList<>();
		
		pets.add(pet);
		usuario.setPets(pets);
		assertThat(usuario.getPets(), hasSize(1));
	}
	
	@Test
	public void setarTelefones() {
		Usuario usuario = new Usuario();
		Telefone telefone1 = new Telefone();
		Telefone telefone2 = new Telefone();
		
		List<Telefone> lista1 = new ArrayList<>();
		List<Telefone> lista2 = new ArrayList<>();
		
		lista1.add(telefone1);
		lista2.add(telefone1);
		lista2.add(telefone2);
		
		assertThat(usuario.getTelefones(), hasSize(0));
		
		usuario.setTelefones(lista1);
		assertThat(usuario.getTelefones(), hasSize(1));
		
		usuario.setTelefones(lista2);
		assertThat(usuario.getTelefones(), hasSize(2));
	}
	
	@Test
	public void adicionarTelefone() {
		Usuario usuario = new Usuario();
		Telefone telefone = new Telefone();
		
		int tamanho = usuario.getTelefones().size();
		usuario.addTelefone(telefone);
		assertThat(usuario.getTelefones(), hasSize(tamanho + 1));
		assertThat(telefone.getUsuario(), equalTo(usuario));
	}
	
	@Test
	public void verificarEmailPublico() {
		Usuario usuario = new Usuario();
		assertTrue(usuario.isEmailPublico());
	}
}
