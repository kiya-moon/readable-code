package cleancode.minesweeper.tobe;

// getter, setter는 처음부터 만들지 않는다. 특히 setter는 사용x
public class Cell {
    private static final String FLAG_SIGN = "⚑";
    private static final String LAND_MINE_SIGN = "☼";
    private static final String CLOSED_CELL_SIGN = "□";
    private static final String OPENED_CELL_SIGN = "■";

    private final String sign;
    private int nearbyLandMineCount;    // cell 주변의 지뢰 수와 지뢰 여부도 Cell 객체에서 처리
    private boolean isLandMine;

    public Cell(String sign, int nearbyLandMineCount, boolean isLandMine) {
        this.sign = sign;
        this.nearbyLandMineCount = nearbyLandMineCount;
        this.isLandMine = isLandMine;
    }

    // 정적 팩토리 메서드를 만들어서 밖에서 사용할 수 있게 한다(강사님 취향)
    // 해당 메서드가 없다면 Cell 객체를 생성할 때마다 new Cell()을 호출해야 한다.
    // ex. BOARD2[selectedRowIndex][selectedColIndex] = new Cell(FLAG_SIGN);
    // of를 사용하면?  BOARD2[selectedRowIndex][selectedColIndex] = Cell.of(FLAG_SIGN);
    public static Cell of(String sign, int nearbyLandMineCount, boolean isLandMine) {
        return new Cell(sign, nearbyLandMineCount, isLandMine);
    }

    public static Cell ofFlag() {
        return of(FLAG_SIGN, 0, false);
    }

    public static Cell ofLandMine() {
        return of(LAND_MINE_SIGN, 0, false);
    }

    public static Cell ofClosed() {
        return of(CLOSED_CELL_SIGN, 0, false);
    }

    public static Cell ofOpened() {
        return of(OPENED_CELL_SIGN, 0 , false);
    }

    public static Cell ofNearbyLandMineCount(int count) {
        return of(String.valueOf(count), 0, false);
    }

    public boolean isClosed() {
        return CLOSED_CELL_SIGN.equals(this.sign);
    }

    public boolean doseNotClosed() {
        return !isClosed();
    }

    public String getSign() {
        return sign;
    }
}
