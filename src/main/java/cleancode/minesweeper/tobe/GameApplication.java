package cleancode.minesweeper.tobe;

import cleancode.minesweeper.tobe.gamelevel.Advanced;
import cleancode.minesweeper.tobe.gamelevel.Beginner;
import cleancode.minesweeper.tobe.gamelevel.GameLevel;
import cleancode.minesweeper.tobe.gamelevel.Middle;
import cleancode.minesweeper.tobe.io.ConsoleInputHandler;
import cleancode.minesweeper.tobe.io.ConsoleOutputHandler;
import cleancode.minesweeper.tobe.io.InputHandler;
import cleancode.minesweeper.tobe.io.OutputHandler;

/**
 * 이름 변경 : shift + F6(macOS), F6 (Windows/Linux)
 * 메서드 추출 : option + command + M (macOS), ctrl + alt + M (Windows/Linux)
 * 상수로 추출 : option + command + C (macOS), ctrl + alt + C (Windows/Linux)
 * try ~ catch 블록 추가 : option + command + T (macOS), ctrl + alt + T (Windows/Linux)
 */
public class GameApplication {   // 게임의 진입점으로 리팩토링 > 지뢰찾기 뿐만 아니라 다른 게임의 시작점으로도 사용 가능해졌다
    public static void main(String[] args) {
        GameLevel gameLevel = new Advanced();
        InputHandler inputHandler = new ConsoleInputHandler();
        OutputHandler outputHandler = new ConsoleOutputHandler();

        // 구체 클래스가 아닌 인터페이스에 의존하면서, 의존 객체를 외부에서 생성자 주입으로 전달받고 있으므로, DIP와 DI가 적용되었다.
        Minesweeper minesweeper = new Minesweeper(gameLevel, inputHandler, outputHandler);
        minesweeper.initialize();
        minesweeper.run();
    }
}
