package tdd.minesweeper.tobe.minesweeper;

import cleancode.minesweeper.tobe.Minesweeper;
import cleancode.minesweeper.tobe.minesweeper.board.GameBoard;
import cleancode.minesweeper.tobe.minesweeper.board.position.CellPosition;
import cleancode.minesweeper.tobe.minesweeper.config.GameConfig;
import cleancode.minesweeper.tobe.minesweeper.exception.GameException;
import cleancode.minesweeper.tobe.minesweeper.io.InputHandler;
import cleancode.minesweeper.tobe.minesweeper.io.OutputHandler;
import cleancode.minesweeper.tobe.minesweeper.user.UserAction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

class MinesweeperTest {

    private GameConfig mockConfig;
    private InputHandler mockInputHandler;
    private OutputHandler mockOutputHandler;
    private Minesweeper minesweeper;

    @BeforeEach
    void setUp() {
        mockInputHandler = mock(InputHandler.class);
        mockOutputHandler = mock(OutputHandler.class);
        mockConfig = mock(GameConfig.class);
        when(mockConfig.getInputHandler()).thenReturn(mockInputHandler);
        when(mockConfig.getOutputHandler()).thenReturn(mockOutputHandler);
        when(mockConfig.getGameLevel()).thenReturn(1); // 적절한 레벨 값 설정

        minesweeper = new Minesweeper(mockConfig);
    }

    @Test
    @DisplayName("잘못된 셀 좌표를 입력했을 때 예외를 던진다")
    void shouldThrowExceptionWhenInvalidCellPosition() {
        // given
        CellPosition invalidPosition = new CellPosition(99, 99); // 유효하지 않은 좌표
        GameBoard gameBoard = mock(GameBoard.class);
        when(mockInputHandler.getCellPositionFromUser()).thenReturn(invalidPosition);
        when(gameBoard.isInvalidCellPosition(invalidPosition)).thenReturn(true);

        // Minesweeper 내부 필드 gameBoard를 주입하려면 리플렉션 필요
        setPrivateField(minesweeper, "gameBoard", gameBoard);

        // when & then
        assertThatThrownBy(() -> invokePrivateMethod(minesweeper, "getCellInputFromUser"))
                .isInstanceOf(GameException.class)
                .hasMessageContaining("잘못된 좌표를 선택하셨습니다.");
    }

    @Test
    @DisplayName("플래그 액션을 입력받으면 해당 셀에 플래그를 표시한다")
    void shouldFlagCellWhenUserChoosesFlag() {
        // given
        CellPosition position = new CellPosition(1, 1);
        GameBoard gameBoard = mock(GameBoard.class);
        setPrivateField(minesweeper, "gameBoard", gameBoard);

        // when
        invokePrivateMethod(minesweeper, "actOnCell", position, UserAction.FLAG);

        // then
        verify(gameBoard, times(1)).flagAt(position);
    }

    @Test
    @DisplayName("오픈 액션을 입력받으면 해당 셀을 연다")
    void shouldOpenCellWhenUserChoosesOpen() {
        // given
        CellPosition position = new CellPosition(1, 1);
        GameBoard gameBoard = mock(GameBoard.class);
        setPrivateField(minesweeper, "gameBoard", gameBoard);

        // when
        invokePrivateMethod(minesweeper, "actOnCell", position, UserAction.OPEN);

        // then
        verify(gameBoard, times(1)).openAt(position);
    }

    @Test
    @DisplayName("잘못된 유저 액션을 입력받으면 예외를 던진다")
    void shouldThrowExceptionWhenInvalidUserAction() {
        // given
        CellPosition position = new CellPosition(1, 1);
        UserAction invalidAction = null;
        GameBoard gameBoard = mock(GameBoard.class);
        setPrivateField(minesweeper, "gameBoard", gameBoard);

        // when & then
        assertThatThrownBy(() -> invokePrivateMethod(minesweeper, "actOnCell", position, invalidAction))
                .isInstanceOf(GameException.class)
                .hasMessageContaining("잘못된 번호를 선택하셨습니다.");
    }

    // 리플렉션 유틸
    private void setPrivateField(Object target, String fieldName, Object value) {
        try {
            var field = target.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(target, value);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private Object invokePrivateMethod(Object target, String methodName, Object... args) {
        try {
            Class<?>[] argTypes = new Class[args.length];
            for (int i = 0; i < args.length; i++) {
                argTypes[i] = args[i] != null ? args[i].getClass() : Object.class;
            }

            var method = target.getClass().getDeclaredMethod(methodName, argTypes);
            method.setAccessible(true);
            return method.invoke(target, args);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
