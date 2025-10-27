package com.generation.farmacia.controller;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.math.BigDecimal;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.generation.farmacia.model.Categoria;
import com.generation.farmacia.model.Produto;
import com.generation.farmacia.util.BaseControllerTest;
import com.generation.farmacia.util.JwtHelper;
import com.generation.farmacia.util.TestBuilder;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName("Testes - Produto Controller")
class ProdutoControllerTest extends BaseControllerTest {

	private static final String BASE_URL = "/produtos";
	private Categoria categoria;
	private BigDecimal preco;
	
	/**
	 * Método executado antes dos testes desta classe.
	 * Cria uma categoria para ser usada nos testes de produto.
	 */
	@BeforeAll
	void prepararDados() {
		// Chama o método da classe pai primeiro
		inicializarTestes();
		
		// Cria uma categoria para usar nos testes
		categoria = categoriaRepository.save(
				TestBuilder.criarCategoria(null, "Categoria 01")
		);
		
		// Cria um Objeto BigDecimal com um preço padrão
		preco = new BigDecimal("250.50");
	}
	
	@Test
	@Order(1)
	@DisplayName("01 - Deve cadastrar um novo produto")
	void deveCadastrarProduto() {
		// Given
		Produto produto = TestBuilder.criarProduto(null, "Produto 01", preco, categoria, "Descrição Grande 01", "Marca 01");

		// When
		String token = JwtHelper.obterToken(testRestTemplate, ADMIN, SENHA);
		HttpEntity<Produto> requisicao = JwtHelper.criarRequisicaoComToken(produto, token);
		ResponseEntity<Produto> resposta = testRestTemplate.exchange(
				BASE_URL, HttpMethod.POST, requisicao, Produto.class);

		// Then
		assertEquals(HttpStatus.CREATED, resposta.getStatusCode());
		assertNotNull(resposta.getBody());
		assertEquals("Produto 01", resposta.getBody().getNome());
	}

	@Test
	@Order(2)
	@DisplayName("02 - Deve atualizar um produto")
	void deveAtualizarProduto() {
		// Given
		Produto produto = produtoRepository.save(
				TestBuilder.criarProduto(null, "Produto 02", preco, categoria, "Descrição Grande 02", "Marca 02")
		);
		
		Produto produtoAtualizado = TestBuilder.criarProduto(
				produto.getId(), 
				"Produto 02 - Atualizado", 
				preco, 
				categoria,
				"Descrição Grandona 02",
				"Marca 02"
		);

		// When
		String token = JwtHelper.obterToken(testRestTemplate, ADMIN, SENHA);
		HttpEntity<Produto> requisicao = JwtHelper.criarRequisicaoComToken(produtoAtualizado, token);
		ResponseEntity<Produto> resposta = testRestTemplate.exchange(
				BASE_URL, HttpMethod.PUT, requisicao, Produto.class);

		// Then
		assertEquals(HttpStatus.OK, resposta.getStatusCode());
		assertNotNull(resposta.getBody());
		assertEquals("Produto 02 - Atualizado", resposta.getBody().getNome());
	}

	@Test
	@Order(3)
	@DisplayName("03 - Deve listar todos os produtos")
	void deveListarTodosProdutos() {
		// Given
		produtoRepository.save(TestBuilder.criarProduto(null, "Produto 03", preco, categoria, "Descrição Grande 03", "Marca 03"));
		produtoRepository.save(TestBuilder.criarProduto(null, "Produto 04", preco, categoria, "Descrição Grande 04", "Marca 04"));

		// When
		String token = JwtHelper.obterToken(testRestTemplate, ADMIN, SENHA);
		HttpEntity<Void> requisicao = JwtHelper.criarRequisicaoComToken(token);
		ResponseEntity<Produto[]> resposta = testRestTemplate.exchange(
				BASE_URL, HttpMethod.GET, requisicao, Produto[].class);

		// Then
		assertEquals(HttpStatus.OK, resposta.getStatusCode());
		assertNotNull(resposta.getBody());
	}
	
	@Test
	@Order(4)
	@DisplayName("04 - Deve buscar um produto por ID")
	void deveBuscarProdutoPorId() {
		// Given
		Produto produto = produtoRepository.save(
				TestBuilder.criarProduto(null, "Produto 05", preco, categoria, "Descrição Grande 05", "Marca 05")
		);
		
		// When
		String token = JwtHelper.obterToken(testRestTemplate, ADMIN, SENHA);
		HttpEntity<Void> requisicao = JwtHelper.criarRequisicaoComToken(token);
		ResponseEntity<Produto> resposta = testRestTemplate.exchange(
				BASE_URL + "/" + produto.getId(), HttpMethod.GET, requisicao, Produto.class);

		// Then
		assertEquals(HttpStatus.OK, resposta.getStatusCode());
		assertNotNull(resposta.getBody());
		assertEquals("Produto 05", resposta.getBody().getNome());
	}
	
	@Test
	@Order(5)
	@DisplayName("05 - Deve buscar produtos por nome")
	void deveBuscarProdutosPorNome() {
		// Given
		produtoRepository.save(
				TestBuilder.criarProduto(null, "Produto 06", preco, categoria, "Descrição Grande 06", "Marca 06")
		);
				
		// When
		String token = JwtHelper.obterToken(testRestTemplate, ADMIN, SENHA);
		HttpEntity<Void> requisicao = JwtHelper.criarRequisicaoComToken(token);
		ResponseEntity<Produto[]> resposta = testRestTemplate.exchange(
				BASE_URL + "/nome/Produto 06", HttpMethod.GET, requisicao, Produto[].class);
		
		// Then
		assertEquals(HttpStatus.OK, resposta.getStatusCode());
		assertNotNull(resposta.getBody());
	}
	
	@Test
	@Order(6)
	@DisplayName("06 - Deve deletar um produto")
	void deveDeletarProduto() {
		// Given
		Produto produto = produtoRepository.save(
				TestBuilder.criarProduto(null, "Produto 07", preco, categoria, "Descrição Grande 07", "Marca 07")
		);
				
		// When
		String token = JwtHelper.obterToken(testRestTemplate, ADMIN, SENHA);
		HttpEntity<Void> requisicao = JwtHelper.criarRequisicaoComToken(token);
		ResponseEntity<Void> resposta = testRestTemplate.exchange(
				BASE_URL + "/" + produto.getId(), HttpMethod.DELETE, requisicao, Void.class);

		// Then
		assertEquals(HttpStatus.NO_CONTENT, resposta.getStatusCode());
	}
	
	@Test
	@Order(7)
	@DisplayName("07 - Deve buscar produtos maiores que 200")
	void deveBuscarProdutoMaiorQue200() {
		// Given
		produtoRepository.save(
				TestBuilder.criarProduto(null, "Produto 08", preco, categoria, "Descrição Grande 08", "Marca 08")
		);
				
		// When
		String token = JwtHelper.obterToken(testRestTemplate, ADMIN, SENHA);
		HttpEntity<Void> requisicao = JwtHelper.criarRequisicaoComToken(token);
		ResponseEntity<Produto[]> resposta = testRestTemplate.exchange(
				BASE_URL + "/preco_maior/200", HttpMethod.GET, requisicao, Produto[].class);

		// Then
		assertEquals(HttpStatus.OK, resposta.getStatusCode());
		assertNotNull(resposta.getBody());
	}
	
	@Test
	@Order(8)
	@DisplayName("08 - Deve buscar produtos menores que 300")
	void deveBuscarProdutoMenorQue300() {
		// Given
		produtoRepository.save(
				TestBuilder.criarProduto(null, "Produto 09", preco, categoria, "Descrição Grande 09", "Marca 09")
		);
				
		// When
		String token = JwtHelper.obterToken(testRestTemplate, ADMIN, SENHA);
		HttpEntity<Void> requisicao = JwtHelper.criarRequisicaoComToken(token);
		ResponseEntity<Produto[]> resposta = testRestTemplate.exchange(
				BASE_URL + "/preco_menor/300", HttpMethod.GET, requisicao, Produto[].class);

		// Then
		assertEquals(HttpStatus.OK, resposta.getStatusCode());
		assertNotNull(resposta.getBody());
	}
}