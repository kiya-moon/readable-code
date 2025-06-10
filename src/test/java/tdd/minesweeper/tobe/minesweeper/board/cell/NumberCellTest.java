package tdd.minesweeper.tobe.minesweeper.board.cell;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

@DisplayName("NumberCell 클래스 단위 테스트")
class NumberCellTest {

    @Test
    @DisplayName("NumberCell 생성 시 주변 지뢰 개수가 올바르게 저장된다")
    void testNearbyLandMineCountIsSet() {
        // given
        int nearbyMines = 3;

        // when
        NumberCell numberCell = new NumberCell(nearbyMines);

        // then
        assertThat(numberCell.hasLandMineCount()).isTrue();
    }

    @Test
    @DisplayName("NumberCell은 지뢰가 아니다")
    void testIsLandMineReturnsFalse() {
        // given
        NumberCell numberCell = new NumberCell(1);

        // when
        boolean result = numberCell.isLandMine();

        // then
        assertThat(result).isFalse();
    }

    @Test
    @DisplayName("아직 열리지 않은 NumberCell의 스냅샷은 Unchecked 상태이다")
    void testSnapshotBeforeOpenOrFlag() {
        // given
        NumberCell numberCell = new NumberCell(2);

        // when
        var snapshot = numberCell.getSnapshot();

        // then
        assertThat(snapshot.isUnchecked()).isTrue();
        assertThat(snapshot.isFlagged()).isFalse();
        assertThat(snapshot.isOpened()).isFalse();
    }

    @Test
    @DisplayName("NumberCell을 열면 스냅샷에 주변 지뢰 개수가 포함된다")
    void testSnapshotAfterOpen() {
        // given
        NumberCell numberCell = new NumberCell(4);

        // when
        numberCell.open();
        var snapshot = numberCell.getSnapshot();

        // then
        assertThat(snapshot.isOpened()).isTrue();
        assertThat(snapshot.getNumber()).isEqualTo(4);
    }

    @Test
    @DisplayName("NumberCell에 플래그를 꽂으면 플래그 상태가 된다")
    void testFlagSetsFlaggedSnapshot() {
        // given
        NumberCell numberCell = new NumberCell(5);

        // when
        numberCell.flag();
        var snapshot = numberCell.getSnapshot();

        // then
        assertThat(snapshot.isFlagged()).isTrue();
        assertThat(snapshot.isOpened()).isFalse();
    }

    @Test
    @DisplayName("NumberCell의 isChecked는 열린 상태일 때 true를 반환한다")
    void testIsCheckedReflectsOpenState() {
        // given
        NumberCell numberCell = new NumberCell(1);

        // when / then
        assertThat(numberCell.isChecked()).isFalse();

        // when
        numberCell.open();

        // then
        assertThat(numberCell.isChecked()).isTrue();
    }

    @Test
    @DisplayName("NumberCell의 isOpened는 열린 상태일 때 true를 반환한다")
    void testIsOpenedReflectsOpenState() {
        // given
        NumberCell numberCell = new NumberCell(1);

        // when / then
        assertThat(numberCell.isOpened()).isFalse();

        // when
        numberCell.open();

        // then
        assertThat(numberCell.isOpened()).isTrue();
    }
}
