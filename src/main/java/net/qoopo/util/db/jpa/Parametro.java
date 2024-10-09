package net.qoopo.util.db.jpa;

import java.util.ArrayList;
import java.util.List;
import jakarta.persistence.ParameterMode;

/**
 *
 * @author alberto
 */
public class Parametro {

    private List<ParametroJPA> lista;

    public static Parametro get() {
        return new Parametro();
    }

    public Parametro() {
        lista = new ArrayList<>();
    }

    public Parametro agregar(String parametro, Object valor) {
        if (lista == null) {
            lista = new ArrayList<>();
        }
        lista.add(new ParametroJPA(parametro, valor));
        return this;
    }

    public Parametro agregar(String parametro, Class clase, ParameterMode parameterMode) {
        if (lista == null) {
            lista = new ArrayList<>();
        }
        lista.add(new ParametroJPA(parametro, clase, parameterMode));
        return this;
    }

    public Parametro agregar(ParametroJPA parametro) {
        if (lista == null) {
            lista = new ArrayList<>();
        }
        lista.add(parametro);
        return this;
    }

    public List<ParametroJPA> getLista() {
        return lista;
    }

}
