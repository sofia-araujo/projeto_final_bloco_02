package com.generation.farmacia.util;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.generation.farmacia.model.Categoria;
import com.generation.farmacia.model.Produto;
import com.generation.farmacia.model.Usuario;
import com.generation.farmacia.model.UsuarioLogin;

public class TestBuilder {
	public static Usuario criarUsuario(Long id, String nome, String usuario, String senha, LocalDate dataNascimento) {
		Usuario novoUsuario = new Usuario();
		novoUsuario.setId(id);
		novoUsuario.setNome(nome);
		novoUsuario.setUsuario(usuario);
		novoUsuario.setSenha(senha);
		novoUsuario.setFoto("-");
		return novoUsuario;
	}

	public static UsuarioLogin criarUsuarioLogin(String usuario, String senha) {
		UsuarioLogin usuarioLogin = new UsuarioLogin();
		usuarioLogin.setUsuario(usuario);
		usuarioLogin.setSenha(senha);
		return usuarioLogin;
	}
	
	public static Categoria criarCategoria(Long id, String tipo) {
		Categoria novaCategoria = new Categoria();
		novaCategoria.setId(id);
		novaCategoria.setTipo(tipo);
		return novaCategoria;
	}
	
	public static Produto criarProduto(Long id, String nome, BigDecimal preco, Categoria categoria, String descricao, String marca) {
        Produto produto = new Produto();
        produto.setId(id);
        produto.setNome(nome);
        produto.setPreco(preco);
        produto.setFoto("-");
        produto.setCategoria(categoria);
        produto.setDescricao(descricao);
        produto.setMarca(marca);
        return produto;
    }
}