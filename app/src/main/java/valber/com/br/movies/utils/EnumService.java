package valber.com.br.movies.utils;

import valber.com.br.movies.domain.LancamentosMetodo;
import valber.com.br.movies.domain.MaisVotadosMetodo;
import valber.com.br.movies.domain.PopularMetodo;
import valber.com.br.movies.utilsInteface.ServiceIntaface;

public enum EnumService {

    POPULAR{
        @Override
        public ServiceIntaface getMetodo() {
            return new PopularMetodo();
        }
    }, LANCAMENTO {
        @Override
        public ServiceIntaface getMetodo() {
            return new LancamentosMetodo();
        }
    }, MAIS_VOTADOS {
        @Override
        public ServiceIntaface getMetodo() {
            return new MaisVotadosMetodo();
        }
    };

    public abstract ServiceIntaface getMetodo();


}
