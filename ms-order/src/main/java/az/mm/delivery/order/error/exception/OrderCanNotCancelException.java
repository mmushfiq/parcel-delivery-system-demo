package az.mm.delivery.order.error.exception;

import az.mm.delivery.common.error.exception.CommonBadRequestException;

public class OrderCanNotCancelException extends CommonBadRequestException {

    public OrderCanNotCancelException() {
        super(400, "Order is in progress so it can not be cancelled");
    }

}
