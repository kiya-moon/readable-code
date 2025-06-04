package cleancode.minesweeper.tobe;

import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;

/**
 * 이름 변경 : shift + F6(macOS), F6 (Windows/Linux)
 * 메서드 추출 : option + command + M (macOS), ctrl + alt + M (Windows/Linux)
 * 상수로 추출 : option + command + C (macOS), ctrl + alt + C (Windows/Linux)
 * try ~ catch 블록 추가 : option + command + T (macOS), ctrl + alt + T (Windows/Linux)
 */
public class MinesweeperGame {

    public static final int BOARD_ROW_SIZE = 8;
    public static final int BOARD_COL_SIZE = 10;
    public static final int LAND_MINE_COUNT = 10;
    public static final Scanner SCANNER = new Scanner(System.in);
    private static final Cell[][] BOARD = new Cell[BOARD_ROW_SIZE][BOARD_COL_SIZE];

    private static int gameStatus = 0; // 0: 게임 중, 1: 승리, -1: 패배

    public static void main(String[] args) {
        showGameStartComments();
        initializeGame();

        while (true) {
            try {
                System.out.println("   a b c d e f g h i j");
                showBoard();

                if (doesUserWinTheGame()) { // 구체적이었던 조건문을 추상화하여 추상레벨을 맞춰주기
                    System.out.println("지뢰를 모두 찾았습니다. GAME CLEAR!");
                    break;
                }
                if (doesUserLoseTheGame()) {
                    System.out.println("지뢰를 밟았습니다. GAME OVER!");
                    break;
                }

                System.out.println();

                String cellInput = getCellInputFromUser();
                String userActionInput = getUserActionInputFromUser();
                actOnCell(cellInput, userActionInput);
            } catch (AppException e) {
                System.out.println(e.getMessage());
            } catch (Exception e) {
                System.out.println("프로그램에 문제가 생겼습니다.");
            }
        }
    }

    private static void actOnCell(String cellInput, String userActionInput) {
        int selectedColIndex = getSelectedColIndex(cellInput);
        int selectedRowIndex = getSelectedRowIndex(cellInput);

        if (doesUserChooseToPlantFlag(userActionInput)) {
            BOARD[selectedRowIndex][selectedColIndex].flag();
            checkIfGameIsOver();
            return;
        }

        if (doesUserChooseToOpenCell(userActionInput)) {
            if (isLandMineCell(selectedRowIndex, selectedColIndex)) {    // 사용자가 지뢰 cell을 밟았다면 game over
                BOARD[selectedRowIndex][selectedColIndex].open();
                changeGameStatusToLose();
                return;
            }

            open(selectedRowIndex, selectedColIndex);
            checkIfGameIsOver();
            return;
        }
        throw new AppException("잘못된 번호를 선택하셨습니다.");
    }

    private static void changeGameStatusToLose() {
        gameStatus = -1;
    }

    private static boolean isLandMineCell(int selectedRowIndex, int selectedColIndex) {
        return BOARD[selectedRowIndex][selectedColIndex].isLandMine();
    }

    private static boolean doesUserChooseToOpenCell(String userActionInput) {
        return userActionInput.equals("1");
    }

    private static boolean doesUserChooseToPlantFlag(String userActionInput) {
        return userActionInput.equals("2");
    }

    private static int getSelectedRowIndex(String cellInput) {
        char cellInputRow = cellInput.charAt(1);
        int selectedRowIndex = convertRowFrom(cellInputRow);
        return selectedRowIndex;
    }

    private static int getSelectedColIndex(String cellInput) {
        char cellInputCol = cellInput.charAt(0);
        int selectedColIndex = convertColFrom(cellInputCol);
        return selectedColIndex;
    }

    private static String getUserActionInputFromUser() {
        System.out.println("선택한 셀에 대한 행위를 선택하세요. (1: 오픈, 2: 깃발 꽂기)");
        return SCANNER.nextLine();
    }

    private static String getCellInputFromUser() {
        System.out.println("선택할 좌표를 입력하세요. (예: a1)");
        return SCANNER.nextLine();
    }

    private static boolean doesUserLoseTheGame() {
        return gameStatus == -1;
    }

    private static boolean doesUserWinTheGame() {
        return gameStatus == 1;
    }

    private static void checkIfGameIsOver() {
        boolean isAllChecked = isAllCellChecked();
        if (isAllChecked) {
            changeGameStatusToWin();
        }
    }   // >>> 역할에 맞도록 이름 변경

    private static void changeGameStatusToWin() {
        gameStatus = 1;
    }

    // 메서드 리팩토링 Tip!
    // 리팩토링 할 메서드가 여러 곳에서 사용하고 있다면 바꾸었을 때 사이드 이펙트가 발생할 수 도 있다.
    // 따라서, 리팩토링 할 메서드를 복제해서 수정 후, 수정한 메서드로 변경 + 테스트를 반복해서 최종적으로 적용해야 한다.
    private static boolean isAllCellChecked() {  // 중첩 반복문을 사용하지 않고, 스트림을 사용하여 가독성을 높임
        return Arrays.stream(BOARD)
                .flatMap(Arrays::stream)
                .noneMatch(Cell::isChecked);
    }

    private static int convertRowFrom(char cellInputRow) {
        int rowIndex = Character.getNumericValue(cellInputRow) - 1;
        if (rowIndex >= BOARD_ROW_SIZE) {
            throw new AppException("잘못된 입력입니다.");
        }
        return rowIndex;
    }

    private static int convertColFrom(char cellInputCol) {
        switch (cellInputCol) {
            case 'a':
//                selectedColIndex = 0;
//                break;
                return 0;
            case 'b':
                return 1;
            case 'c':
                 return 2;
            case 'd':
                return 3;
            case 'e':
                return 4;
            case 'f':
                return 5;
            case 'g':
                return 6;
            case 'h':
                return 7;
            case 'i':
                return 8;
            case 'j':
                return 9;
            default:
                throw new AppException("잘못된 입력입니다.");
        }
    }

    private static void showBoard() {
        for (int row = 0; row < BOARD_ROW_SIZE; row++) {
            System.out.printf("%d  ", row + 1);
            for (int col = 0; col < BOARD_COL_SIZE; col++) {
                System.out.print(BOARD[row][col].getSign() + " "); // Cell 객체 내부에서 보드를 그리고 System.out.print() 하는 것은 이상하므로,
                                                                    // 여기서는 Cell 객체에게 데이터 줘! 내가 그려줄게! 하는 것이 맞다.
            }
            System.out.println();
        }
    }

    private static void initializeGame() {
        for (int row = 0; row < BOARD_ROW_SIZE; row++) {
            for (int col = 0; col < BOARD_COL_SIZE; col++) {
                BOARD[row][col] = Cell.create();
            }
        }

        for (int i = 0; i < LAND_MINE_COUNT; i++) {
            int col = new Random().nextInt(BOARD_COL_SIZE);
            int row = new Random().nextInt(BOARD_ROW_SIZE);
            BOARD[row][col].turnOnLandMine();
        }

        for (int row = 0; row < BOARD_ROW_SIZE; row++) {
            for (int col = 0; col < BOARD_COL_SIZE; col++) {
                if (isLandMineCell(row, col)) {
                    continue;
                }
                int count = countNearbyLandMines(row, col);
                BOARD[row][col].updateNearbyLandMineCount(count);
            }
        }
    }

    private static int countNearbyLandMines(int row, int col) {
        int count = 0;

            if (row - 1 >= 0 && col - 1 >= 0 && isLandMineCell(row - 1, col - 1)) {
                count++;
            }
            if (row - 1 >= 0 && isLandMineCell(row - 1, col)) {
                count++;
            }
            if (row - 1 >= 0 && col + 1 < BOARD_COL_SIZE && isLandMineCell(row - 1, col + 1)) {
                count++;
            }
            if (col - 1 >= 0 && isLandMineCell(row, col - 1)) {
                count++;
            }
            if (col + 1 < BOARD_COL_SIZE && isLandMineCell(row, col + 1)) {
                count++;
            }
            if (row + 1 < BOARD_ROW_SIZE && col - 1 >= 0 && isLandMineCell(row + 1, col - 1)) {
                count++;
            }
            if (row + 1 < BOARD_ROW_SIZE && isLandMineCell(row + 1, col)) {
                count++;
            }
            if (row + 1 < BOARD_ROW_SIZE && col + 1 < BOARD_COL_SIZE && isLandMineCell(row + 1, col + 1)) {
                count++;
            }
        return count;
    }

    private static void showGameStartComments() {
        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
        System.out.println("지뢰찾기 게임 시작!");
        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
    }

    // 재귀함수 - 종단 조건 설정을 잘 해놓지 않는다면 스택 오버 플로우 등이 발생할 수 있다
    // 재귀적으로 돌면서 주변에 인접한 열 수 있는 모든 cell을 오픈하는 함수
    private static void open(int row, int col) {
        if (row < 0 || row >= BOARD_ROW_SIZE || col < 0 || col >= BOARD_COL_SIZE) {  // 보드 범위를 벗어나면 정상적이지 않으므로 return
            return;
        }
        if (BOARD[row][col].isOpened()) { // 이미 오픈된 cell이라면 return
            return;
        }
        if (isLandMineCell(row, col)) {  // 지뢰 cell이라면 return
            return;
        }

        BOARD[row][col].open();

        if (BOARD[row][col].hasLandMineCount()) {
            return;
        }

        open(row - 1, col - 1);
        open(row - 1, col);
        open(row - 1, col + 1);
        open(row, col - 1);
        open(row, col + 1);
        open(row + 1, col - 1);
        open(row + 1, col);
        open(row + 1, col + 1);
    }
}
