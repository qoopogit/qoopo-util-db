package net.qoopo.util.db.jpa;

import java.io.Serializable;

import jakarta.persistence.ParameterMode;

/**
 *
 * @author alberto
 */
public class ParametroJPA implements Serializable {

    private String parametro;
    private transient Object valor;
    // usado para los parametros de los sp
    private int indice = 0;
    private Class parameterClass;
    private ParameterMode parameterMode;

    public ParametroJPA() {
        //
    }

    public ParametroJPA(String parametro, Object valor) {
        this.parametro = parametro;
        this.valor = valor;
    }

    public ParametroJPA(int indice, Object valor) {
        this.indice = indice;
        this.valor = valor;
    }

    public ParametroJPA(String parametro, Class parameterClass, ParameterMode parameterMode) {
        this.parametro = parametro;
        this.parameterClass = parameterClass;
        this.parameterMode = parameterMode;
    }

    public ParametroJPA(int indice, Class parameterClass, ParameterMode parameterMode) {
        this.indice = indice;
        this.parameterClass = parameterClass;
        this.parameterMode = parameterMode;
    }

    public String getParametro() {
        return parametro;
    }

    public void setParametro(String parametro) {
        this.parametro = parametro;
    }

    public Object getValor() {
        return valor;
    }

    public void setValor(Object valor) {
        this.valor = valor;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 61 * hash + (this.parametro != null ? this.parametro.hashCode() : 0);
        hash = 61 * hash + (this.valor != null ? this.valor.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final ParametroJPA other = (ParametroJPA) obj;
        if ((this.parametro == null) ? (other.parametro != null) : !this.parametro.equals(other.parametro)) {
            return false;
        }
        return !(this.valor != other.valor && (this.valor == null || !this.valor.equals(other.valor)));
    }

    @Override
    public String toString() {
        return "ParametroJPA{" + "parametro=" + parametro + ", valor=" + valor + '}';
    }

    public ParameterMode getParameterMode() {
        return parameterMode;
    }

    public void setParameterMode(ParameterMode parameterMode) {
        this.parameterMode = parameterMode;
    }

    public Class getParameterClass() {
        return parameterClass;
    }

    public void setParameterClass(Class parameterClass) {
        this.parameterClass = parameterClass;
    }

    public int getIndice() {
        return indice;
    }

    public void setIndice(int indice) {
        this.indice = indice;
    }

}
