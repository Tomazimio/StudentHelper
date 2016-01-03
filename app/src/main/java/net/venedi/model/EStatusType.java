package net.venedi.model;

public enum EStatusType {

    PENDING(0),
    SUCCESS(1),
    FAIL(2);

    private int typeId;

    private EStatusType(int taken){
        this.typeId = taken;
    }

    public int getTypeId() {
        return typeId;
    }

    public static EStatusType getById(int id) {
        if(id == 0) {
            return PENDING;
        } else if(id == 1) {
            return SUCCESS;
        } else {
            return FAIL;
        }
    }

}
