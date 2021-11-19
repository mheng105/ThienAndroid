package com.example.ad_btl.Characters;

import java.io.Serializable;
import java.util.List;

public class Kinds implements Serializable {
    String nameKind;

    public Kinds() {
    }

    public String getNamekind() {
        return nameKind;
    }

    public void setNamekind(String namekind) {
        this.nameKind = namekind;
    }


    public Kinds(String namekind) {
        this.nameKind = namekind;
    }
}
