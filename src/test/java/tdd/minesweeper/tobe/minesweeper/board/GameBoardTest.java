package tdd.minesweeper.tobe.minesweeper.board;

import tdd.minesweeper.tobe.minesweeper.board.cell.CellSnapshot;
import tdd.minesweeper.tobe.minesweeper.board.position.CellPosition;
import tdd.minesweeper.tobe.minesweeper.gamelevel.GameLevel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class GameBoardTest {
    private GameBoard gameBoard;

    @BeforeEach
    void setUp() {
        // given: 초급 난이도의 게임 레벨 설정
        GameLevel gameLevel = GameLevel.EASY;
        gameBoard = new GameBoard(gameLevel);
        gameBoard.initializeGame();
    }

    @Test
    @DisplayName("유효하지 않은 셀 좌표는 true를 반환한다")
    void testInvalidCellPosition() {
        // given
        int invalidRow = gameBoard.getRowSize();
        int invalidCol = gameBoard.getColSize();
        cleancode.minesweeper.tobe.minesweeper.board.position.CellPosition invalidPosition = new cleancode.minesweeper.tobe.minesweeper.board.position.CellPosition(invalidRow, invalidCol);

        // when
        boolean isInvalid = gameBoard.isInvalidCellPosition(invalidPosition);

        // then
        assertThat(isInvalid).isTrue();
    }

    @Test
    @DisplayName("지뢰를 선택한 경우 게임 상태가 LOSE로 바뀐다")
    void testOpenLandMineChangesStatusToLose() {
        // given
        cleancode.minesweeper.tobe.minesweeper.board.position.CellPosition landMinePosition = findLandMineCell();

        // when
        gameBoard.openAt(landMinePosition);

        // then
        assertThat(gameBoard.isLoseStatus()).isTrue();
    }

    @Test
    @DisplayName("빈 셀을 열면 연쇄적으로 주변 셀이 열린다")
    void testOpenEmptyCellOpensSurroundings() {
        // given
        cleancode.minesweeper.tobe.minesweeper.board.position.CellPosition emptyCell = findSafeEmptyCell();

        // when
        gameBoard.openAt(emptyCell);

        // then
        cleancode.minesweeper.tobe.minesweeper.board.cell.CellSnapshot snapshot = gameBoard.getSnapshot(emptyCell);
        assertThat(snapshot.isOpened()).isTrue();
    }

    @Test
    @DisplayName("플래그를 지정한 셀은 열리지 않는다")
    void testFlaggedCellDoesNotOpen() {
        // given
        cleancode.minesweeper.tobe.minesweeper.board.position.CellPosition target = new cleancode.minesweeper.tobe.minesweeper.board.position.CellPosition(0, 0);

        // when
        gameBoard.flagAt(target);
        cleancode.minesweeper.tobe.minesweeper.board.cell.CellSnapshot snapshot = gameBoard.getSnapshot(target);

        // then
        assertThat(snapshot.isFlagged()).isTrue();
        assertThat(snapshot.isOpened()).isFalse();
    }

    // 유틸: 지뢰가 있는 셀 위치 찾기
    private cleancode.minesweeper.tobe.minesweeper.board.position.CellPosition findLandMineCell() {
        for (int row = 0; row < gameBoard.getRowSize(); row++) {
            for (int col = 0; col < gameBoard.getColSize(); col++) {
                cleancode.minesweeper.tobe.minesweeper.board.position.CellPosition position = new cleancode.minesweeper.tobe.minesweeper.board.position.CellPosition(row, col);
                if (gameBoard.getSnapshot(position).isLandMine()) {
                    return position;
                }
            }
        }
        throw new IllegalStateException("지뢰가 있는 셀을 찾을 수 없습니다.");
    }

    // 유틸: 지뢰가 없는 빈 셀 위치 찾기
    private cleancode.minesweeper.tobe.minesweeper.board.position.CellPosition findSafeEmptyCell() {
        for (int row = 0; row < gameBoard.getRowSize(); row++) {
            for (int col = 0; col < gameBoard.getColSize(); col++) {
                cleancode.minesweeper.tobe.minesweeper.board.position.CellPosition position = new cleancode.minesweeper.tobe.minesweeper.board.position.CellPosition(row, col);
                cleancode.minesweeper.tobe.minesweeper.board.cell.CellSnapshot snapshot = gameBoard.getSnapshot(position);
                if (!snapshot.isLandMine() && snapshot.getLandMineCount() == 0) {
                    return position;
                }
            }
        }
        throw new IllegalStateException("빈 셀을 찾을 수 없습니다.");
    }
}