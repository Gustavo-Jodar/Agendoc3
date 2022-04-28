package br.ufscar.dc.dsw.util;

import java.util.function.Function;

public class Formata {

    public Formata() {
    }

    public String formataString(String string) {

        // Ã§ = ç; Ã = Ç; Ã£ = ã ; Ã¡ = á ; Ãµ = õ ; Ã³ = ó ; Ã© = é ; Ã = í ; Ãª = ê
        string = string.replaceAll("Ã§", "c");
        string = string.replaceAll("Ã", "c");
        string = string.replaceAll("Ã£", "a");
        string = string.replaceAll("Ã¡", "a");
        string = string.replaceAll("Ãµ", "o");
        string = string.replaceAll("Ã³", "o");
        string = string.replaceAll("Ã©", "e");
        string = string.replaceAll("Ã", "i");
        string = string.replaceAll("Ãª", "e");

        string = string.replaceAll("ç", "c");
        string = string.replaceAll("Ç", "c");
        string = string.replaceAll("ã", "a");
        string = string.replaceAll("á", "a");
        string = string.replaceAll("õ", "o");
        string = string.replaceAll("ó", "o");
        string = string.replaceAll("é", "e");
        string = string.replaceAll("í", "i");
        string = string.replaceAll("ê", "e");

        string = string.toUpperCase();

        return string;
    }
}
