package az.mm.delivery.order.enums;

public enum NotificationMessage {

    ORDER_ACCEPTED("Your order is accepted"),
    ORDER_DELIVERED("Your order is delivered");

    private final String message;

    NotificationMessage(String message) {
        this.message = message;
    }

    public String message() {
        return message;
    }

}
