package cleancode.minesweeper.tobe;

// getter, setter는 처음부터 만들지 않는다. 특히 setter는 사용x
public class Cell {
    private static final String FLAG_SIGN = "⚑";
    private static final String LAND_MINE_SIGN = "☼";
    private static final String UNCHECKED_SIGN = "□";
    private static final String EMPTY_SIGN = "■";

    private int nearbyLandMineCount;
    private boolean isLandMine;
    private boolean isFlagged;
    private boolean isOpened;

    private Cell(int nearbyLandMineCount, boolean isLandMine, boolean isFlagged, boolean isOpened) {
        this.nearbyLandMineCount = nearbyLandMineCount;
        this.isLandMine = isLandMine;
        this.isFlagged = isFlagged;
        this.isOpened = isOpened;
    }

    // 정적 팩토리 메서드를 만들어서 밖에서 사용할 수 있게 한다(강사님 취향)
    // 해당 메서드가 없다면 Cell 객체를 생성할 때마다 new Cell()을 호출해야 한다.
    // ex. BOARD2[selectedRowIndex][selectedColIndex] = new Cell(FLAG_SIGN);
    // of를 사용하면?  BOARD2[selectedRowIndex][selectedColIndex] = Cell.of(FLAG_SIGN);
    public static Cell of(int nearbyLandMineCount, boolean isLandMine, boolean isFlagged, boolean isOpened) {
        return new Cell(nearbyLandMineCount, isLandMine, isFlagged, isOpened);
    }

    public static Cell create() {
        return of(0, false, false, false);
    }

    public void turnOnLandMine() {
        this.isLandMine = true;
    }

    public void updateNearbyLandMineCount(int count) {
        this.nearbyLandMineCount = count;
    }

    public void flag() {
        this.isFlagged = true;
    }

    public void open() {
        this.isOpened = true;
    }

    public boolean isChecked() {
        return isFlagged || isOpened;
    }

    public boolean isLandMine() {
        return isLandMine;
    }

    public boolean isOpened() {
        return isOpened;
    }

    public boolean hasLandMineCount() {
        return this.nearbyLandMineCount != 0;
    }

    public String getSign() {
        if (isOpened) {
            if (isLandMine) {
                return LAND_MINE_SIGN;
            }
            if (hasLandMineCount()) {
                return String.valueOf(nearbyLandMineCount);
            }
            return EMPTY_SIGN;
        }

        if (isFlagged) {
            return FLAG_SIGN;
        }

        return UNCHECKED_SIGN;
    }

//    public static Cell ofFlag() {
//        return of(FLAG_SIGN, 0, false);
//    }
//
//    public static Cell ofLandMine() {
//        return of(LAND_MINE_SIGN, 0, false);
//    }
//
//    public static Cell ofClosed() {
//        return of(CLOSED_CELL_SIGN, 0, false);
//    }
//
//    public static Cell ofOpened() {
//        return of(OPENED_CELL_SIGN, 0 , false);
//    }
//
//    public static Cell ofNearbyLandMineCount(int count) {
//        return of(String.valueOf(count), 0, false);
//    }
    // >>> 리팩토링을 진행하면서 컴파일 오류가 나서 O, false를 넣어주었지만 이상하다
    // 리팩토링을 멈추고 Cell이 가질 수 있는 상태를 정의해보자
    // 깃발 표시 여부
    // cell 열림/닫힘
    // 사용자가 확인/미확인
    // ex. 깃발이 꽂혀있다면, 사용자가 확인한 상태이면서 cell은 닫힌 상태
    // 하지만 게임 종료 조건은 cell이 모두 열려있는 상태면 종료된다.
    // 종료 조건이 이상하다는 것을 깨닫는다.
    // 원인은 기존에는 String 배열 Board에 값을 덮어씌웠지만, 이젠 Cell 객체 Board라서 다르게 처리해야 하기 때문이다.
    // 도메인 정보가 갱신되었으므로 과감히 리팩토링을 진행한다.

//    public boolean isClosed() {
//        return CLOSED_CELL_SIGN.equals(this.sign);
//    }
//
//    public boolean doseNotClosed() {
//        return !isClosed();
//    }
//
//    public String getSign() {
//        return sign;
//    }
}
