package br.com.fiap.patiovison.helper;

/**
 * Constantes utilizadas na aplicação para evitar magic numbers
 * e centralizar valores importantes.
 */
public final class AppConstants {
    
    // Mensagens de sucesso
    public static final String MSG_PATIO_SALVO = "Pátio salvo com sucesso!";
    public static final String MSG_PATIO_ATUALIZADO = "Pátio atualizado com sucesso!";
    public static final String MSG_PATIO_REMOVIDO = "Pátio removido com sucesso!";
    
    public static final String MSG_SETOR_SALVO = "Setor salvo com sucesso!";
    public static final String MSG_SETOR_ATUALIZADO = "Setor atualizado com sucesso!";
    public static final String MSG_SETOR_REMOVIDO = "Setor removido com sucesso!";
    
    public static final String MSG_MOTO_SALVA = "Moto salva com sucesso!";
    public static final String MSG_MOTO_REMOVIDA = "Moto removida com sucesso!";
    
    // Mensagens de erro
    public static final String ERR_PATIO_NAO_ENCONTRADO = "Pátio não encontrado";
    public static final String ERR_SETOR_NAO_ENCONTRADO = "Setor não encontrado";
    public static final String ERR_MOTO_NAO_ENCONTRADA = "Moto não encontrada";
    
    // Redirects
    public static final String REDIRECT_PATIO = "redirect:/patio";
    public static final String REDIRECT_SETOR = "redirect:/setor";
    public static final String REDIRECT_MOTO = "redirect:/moto";
    public static final String REDIRECT_INDEX = "redirect:/index";
    
    // Views
    public static final String VIEW_PATIO_INDEX = "patio/index";
    public static final String VIEW_PATIO_FORM = "patio/form";
    public static final String VIEW_SETOR_INDEX = "setor/index";
    public static final String VIEW_SETOR_FORM = "setor/form";
    public static final String VIEW_MOTO_INDEX = "moto/index";
    public static final String VIEW_MOTO_FORM = "moto/form";
    public static final String VIEW_INDEX = "index";
    
    // Atributos do modelo
    public static final String ATTR_MESSAGE = "message";
    public static final String ATTR_PATIOS = "patios";
    public static final String ATTR_PATIO = "patio";
    public static final String ATTR_SETORES = "setores";
    public static final String ATTR_SETOR = "setor";
    public static final String ATTR_MOTOS = "motos";
    public static final String ATTR_MOTO = "moto";
    
    // Ocupação - limites
    public static final int OCUPACAO_MAXIMA_PERCENTUAL = 100;
    public static final double PERCENTUAL_MULTIPLICADOR = 100.0;
    
    private AppConstants() {
        // Classe utilitária - construtor privado
    }
}