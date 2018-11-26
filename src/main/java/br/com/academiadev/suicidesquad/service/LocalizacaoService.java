package br.com.academiadev.suicidesquad.service;

import br.com.academiadev.suicidesquad.entity.Localizacao;
import br.com.academiadev.suicidesquad.repository.LocalizacaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LocalizacaoService {

    private final LocalizacaoRepository localizacaoRepository;

    @Autowired
    public LocalizacaoService(LocalizacaoRepository localizacaoRepository) {
        this.localizacaoRepository = localizacaoRepository;
    }

    public Localizacao findOrCreate(String bairro, String cidade, String uf) {
        return localizacaoRepository.findByBairroAndCidadeAndUf(bairro, cidade, uf)
                .orElseGet(() -> localizacaoRepository.save(Localizacao.builder()
                        .bairro(bairro)
                        .cidade(cidade)
                        .uf(uf)
                        .build()));
    }
}
