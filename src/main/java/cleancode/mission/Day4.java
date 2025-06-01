//package cleancode.mission;
//
///**
// * ✔️ 사용자가 생성한 '주문'이 유효한지를 검증하는 메서드.
// * ✔️ Order는 주문 객체이고, 필요하다면 Order에 추가적인 메서드를 만들어도 된다. (Order 내부의 구현을 구체적으로 할 필요는 없다.)
// * ✔️ 필요하다면 메서드를 추출할 수 있다.
// */
//public class Day4 {
//    public boolean validateOrder(Order order) {
//        try {
//            if (order.notExistItem()) {
//                throw new IllegalArgumentException("주문 항목이 없습니다.");
//                return false;
//            }
//            if (order.incorrectTotalPrice()) {
//                throw new IllegalArgumentException("올바르지 않은 총 가격입니다.");
//                return false;
//            }
//            if (order.notExistCustomerInfo()) {
//                throw new IllegalArgumentException("사용자 정보가 없습니다.");
//                return false;
//            }
//            return true;
//        } catch (IllegalArgumentException e) {
//            log.error("주문 검증 중 예외 발생: {}", e.getMessage());
//            return false;
//        }
//    }
//
//}
//
//class Order {
//    private List<Item> items;
//    private Customer customer;
//
//    public boolean notExistItem() {
//        // 아이템 있는지 확인한느 로직
//        return true; // 예시로 true 반환
//    }
//
//    public double totalPrice() {
//        // 총 가격 계산 로직
//        return totalPrice;
//    }
//
//    public boolean incorrectTotalPrice() {
//        return totalPrice() < 0;
//    }
//
//    public boolean notExistCustomerInfo() {
//        // 사용자 정보가 있는지 확인하는 로직
//        return true; // 예시로 true 반환
//    }
//}
