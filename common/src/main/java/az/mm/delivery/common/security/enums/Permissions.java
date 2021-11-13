package az.mm.delivery.common.security.enums;

public enum Permissions {

    VIEW_USER,
    VIEW_USER_LIST,
    ADD_USER,
    UPDATE_USER,
    DELETE_USER,

    VIEW_ROLE,
    ADD_ROLE,
    UPDATE_ROLE,
    DELETE_ROLE,

    VIEW_PERMISSION,
    ADD_PERMISSION,
    UPDATE_PERMISSION,
    DELETE_PERMISSION,

    CREATE_COURIER,
    VIEW_COURIER,

    VIEW_PARCEL,
    UPDATE_PARCEL,
    CHANGE_PARCEL_STATUS,
    RECEIVE_PARCEL,
    TRACK_PARCEL,
    COMPLETE_PARCEL_ORDER,

    VIEW_ORDER,
    CREATE_ORDER,
    UPDATE_ORDER,
    CHANGE_ORDER_DESTINATION,
    CANCEL_ORDER,
    ACCEPT_ORDER,
    CHANGE_ORDER_STATUS,
    ASSIGN_COURIER;

    public String get() {
        return this.name().toLowerCase();
    }

}