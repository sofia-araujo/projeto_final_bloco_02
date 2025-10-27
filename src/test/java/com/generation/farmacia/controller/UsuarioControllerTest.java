package com.generation.farmacia.controller;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.generation.farmacia.model.Usuario;
import com.generation.farmacia.util.BaseControllerTest;
import com.generation.farmacia.util.JwtHelper;
import com.generation.farmacia.util.TestBuilder;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName("Testes - Usuário Controller")
class UsuarioControllerTest extends BaseControllerTest {

	private static final String BASE_URL = "/usuarios";

	@Test
	@Order(1)
	@DisplayName("01 - Deve cadastrar um novo usuário com sucesso")
	void deveCadastrarUsuario() {
		// Given - Preparação
		Usuario usuario = TestBuilder.criarUsuario(null, "Ana Maria", 
				"anamaria@email.com.br", "12345678", null);
		
		// When - Ação
		HttpEntity<Usuario> requisicao = new HttpEntity<Usuario>(usuario);
		ResponseEntity<Usuario> resposta = testRestTemplate.exchange(
				BASE_URL + "/cadastrar", HttpMethod.POST, requisicao, Usuario.class);
		
		// Then - Verificação
		assertEquals(HttpStatus.CREATED, resposta.getStatusCode());
		assertNotNull(resposta.getBody());
		assertEquals(usuario.getUsuario(), resposta.getBody().getUsuario());
	}

	@Test
	@Order(2)
	@DisplayName("02 - Não deve permitir duplicação de usuário")
	void naoDeveDuplicarUsuario() {
		// Given
		Usuario usuario = TestBuilder.criarUsuario(null, "Mariane Lobo", 
				"marianelobo@email.com.br", "12345678", null);
		usuarioService.cadastrarUsuario(usuario);
		
		// When - Tenta cadastrar o mesmo usuário novamente
		HttpEntity<Usuario> requisicao = new HttpEntity<Usuario>(usuario);
		ResponseEntity<Usuario> resposta = testRestTemplate.exchange(
				BASE_URL + "/cadastrar", HttpMethod.POST, requisicao, Usuario.class);
		
		// Then
		assertEquals(HttpStatus.BAD_REQUEST, resposta.getStatusCode());
	}
	

	@Test
	@Order(3)
	@DisplayName("03 - Deve atualizar um usuário com sucesso")
	void deveAtualizarUsuario() {
		// Given
		Usuario usuario = TestBuilder.criarUsuario(null, "Juliano Reis", 
				"julianoreis@email.com.br", "12345678", null);
		Optional<Usuario> cadastrado = usuarioService.cadastrarUsuario(usuario);
		
		Usuario usuarioAtualizado = TestBuilder.criarUsuario(
				cadastrado.get().getId(), 
				"Juliano Reis", 
				"ju_reis@email.com.br", 
				"12345689",
				null
		);
		
		// When
		String token = JwtHelper.obterToken(testRestTemplate, ADMIN, SENHA);
		HttpEntity<Usuario> requisicao = JwtHelper.criarRequisicaoComToken(usuarioAtualizado, token);
		ResponseEntity<Usuario> resposta = testRestTemplate.exchange(
				BASE_URL + "/atualizar", HttpMethod.PUT, requisicao, Usuario.class);
		
		// Then
		assertEquals(HttpStatus.OK, resposta.getStatusCode());
		assertNotNull(resposta.getBody());
		assertEquals("Juliano Reis", resposta.getBody().getNome());
	}

	@Test
	@Order(4)
	@DisplayName("04 - Deve listar todos os usuários")
	void deveListarTodosUsuarios() {
		// Given
		usuarioService.cadastrarUsuario(TestBuilder.criarUsuario(null, "Ana Marques", 
				"ana_marques@email.com.br", "12345678", null));
		usuarioService.cadastrarUsuario(TestBuilder.criarUsuario(null, "Carlos Moura", 
				"carlos_moura@email.com.br", "12345678", null));
		
		// When
		String token = JwtHelper.obterToken(testRestTemplate, ADMIN, SENHA);
		HttpEntity<Void> requisicao = JwtHelper.criarRequisicaoComToken(token);
		ResponseEntity<Usuario[]> resposta = testRestTemplate.exchange(
				BASE_URL + "/all", HttpMethod.GET, requisicao, Usuario[].class);
		
		// Then
		assertEquals(HttpStatus.OK, resposta.getStatusCode());
		assertNotNull(resposta.getBody());
	}
	
	@Test
	@Order(5)
	@DisplayName("05 - Deve buscar um usuário por ID")
	void deveBuscarUsuarioPorId() {
		// Given
		Optional<Usuario> usuario = usuarioService.cadastrarUsuario(
				TestBuilder.criarUsuario(null, "Fabiana Moraes", 
						"fabi_moraes@email.com.br", "12345678", null)
		);
		
		// When
		String token = JwtHelper.obterToken(testRestTemplate, ADMIN, SENHA);
		HttpEntity<Void> requisicao = JwtHelper.criarRequisicaoComToken(token);
		ResponseEntity<Usuario> resposta = testRestTemplate.exchange(
				BASE_URL + "/" + usuario.get().getId(), HttpMethod.GET, requisicao, Usuario.class);
		
		// Then
		assertEquals(HttpStatus.OK, resposta.getStatusCode());
		assertNotNull(resposta.getBody());
		assertEquals("Fabiana Moraes", resposta.getBody().getNome());
	}
}