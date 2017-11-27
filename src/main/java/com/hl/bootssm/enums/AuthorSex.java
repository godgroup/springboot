package com.hl.bootssm.enums;

/**
 * @author Static
 */
public enum AuthorSex implements EnumType {
    /**
     * 男
     */
    Male("male"),

    /**
     * 女
     */
    Female("female");

    private String ligeral;

    AuthorSex(String ligeral) {
        this.ligeral = ligeral;
    }

    @Override
    public String getLiteral() {
        return this.ligeral;
    }
}