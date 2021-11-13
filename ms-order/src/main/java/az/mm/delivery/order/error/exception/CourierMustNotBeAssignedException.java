package az.mm.delivery.order.error.exception;

import az.mm.delivery.common.error.exception.CommonBadRequestException;

public class CourierMustNotBeAssignedException extends CommonBadRequestException {

    public CourierMustNotBeAssignedException() {
        super(400, "Courier must not be assigned to unaccepted order");
    }

}
