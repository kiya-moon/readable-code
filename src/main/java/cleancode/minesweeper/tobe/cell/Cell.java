package cleancode.minesweeper.tobe.cell;

/**
 * Cell 객체 < 지뢰 Cell, 숫자 Cell, 빈 Cell
 */
public abstract class Cell {
    protected static final String FLAG_SIGN = "⚑";
    protected static final String UNCHECKED_SIGN = "□";

    protected boolean isFlagged;
    protected boolean isOpened;

    public abstract boolean isLandMine();
    public abstract boolean hasLandMineCount();
    public abstract String getSign();

    public void flag() {
        this.isFlagged = true;
    }
    public void open() {
        this.isOpened = true;
    }
    public boolean isChecked() {
        return isFlagged || isOpened;
    }
    public boolean isOpened() {
        return isOpened;
    }
}
