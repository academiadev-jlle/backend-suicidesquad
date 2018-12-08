package br.com.academiadev.suicidesquad.converter;

import br.com.academiadev.suicidesquad.enums.*;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class ConvertersTest {

    private CastracaoConverter castracaoConverter = new CastracaoConverter();
    private CategoriaConverter categoriaConverter = new CategoriaConverter();
    private ComprimentoPeloConverter comprimentoPeloConverter = new ComprimentoPeloConverter();
    private CorConverter corConverter = new CorConverter();
    private PorteConverter porteConverter = new PorteConverter();
    private RacaConverter racaConverter = new RacaConverter();
    private SexoPetConverter sexoPetConverter = new SexoPetConverter();
    private SexoUsuarioConverter sexoUsuarioConverter = new SexoUsuarioConverter();
    private SituacaoConverter situacaoConverter = new SituacaoConverter();
    private TipoConverter tipoConverter = new TipoConverter();
    private VacinacaoConverter vacinacaoConverter = new VacinacaoConverter();

    @Test
    public void convertToDatabaseColumn() {
        assertThat(castracaoConverter.convertToDatabaseColumn(Castracao.CASTRADO), equalTo(3));
        assertThat(categoriaConverter.convertToDatabaseColumn(Categoria.ACHADO), equalTo(1));
        assertThat(comprimentoPeloConverter.convertToDatabaseColumn(ComprimentoPelo.CURTO), equalTo(2));
        assertThat(corConverter.convertToDatabaseColumn(Cor.BRANCO), equalTo(2));
        assertThat(porteConverter.convertToDatabaseColumn(Porte.PEQUENO), equalTo(1));
        assertThat(racaConverter.convertToDatabaseColumn(Raca.CACHORRO_SRD), equalTo(1));
        assertThat(sexoPetConverter.convertToDatabaseColumn(SexoPet.FEMEA), equalTo(2));
        assertThat(sexoUsuarioConverter.convertToDatabaseColumn(SexoUsuario.NAO_INFORMADO), equalTo(3));
        assertThat(situacaoConverter.convertToDatabaseColumn(Situacao.ENCONTRADO), equalTo(2));
        assertThat(tipoConverter.convertToDatabaseColumn(Tipo.CACHORRO), equalTo(1));
        assertThat(vacinacaoConverter.convertToDatabaseColumn(Vacinacao.PARCIAL), equalTo(2));
    }

    @Test
    public void convertToEntityAttribute() {
        assertThat(castracaoConverter.convertToEntityAttribute(3), equalTo(Castracao.CASTRADO));
        assertThat(categoriaConverter.convertToEntityAttribute(1), equalTo(Categoria.ACHADO));
        assertThat(comprimentoPeloConverter.convertToEntityAttribute(2), equalTo(ComprimentoPelo.CURTO));
        assertThat(corConverter.convertToEntityAttribute(2), equalTo(Cor.BRANCO));
        assertThat(porteConverter.convertToEntityAttribute(1), equalTo(Porte.PEQUENO));
        assertThat(racaConverter.convertToEntityAttribute(1), equalTo(Raca.CACHORRO_SRD));
        assertThat(sexoPetConverter.convertToEntityAttribute(2), equalTo(SexoPet.FEMEA));
        assertThat(sexoUsuarioConverter.convertToEntityAttribute(3), equalTo(SexoUsuario.NAO_INFORMADO));
        assertThat(situacaoConverter.convertToEntityAttribute(2), equalTo(Situacao.ENCONTRADO));
        assertThat(tipoConverter.convertToEntityAttribute(1), equalTo(Tipo.CACHORRO));
        assertThat(vacinacaoConverter.convertToEntityAttribute(2), equalTo(Vacinacao.PARCIAL));
    }
}